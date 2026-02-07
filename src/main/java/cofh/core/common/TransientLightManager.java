package cofh.core.common;

import cofh.core.util.ProxyUtils;
import cofh.lib.util.constants.ModIds;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.BlockLightEngine;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.lighting.LayerLightSectionStorage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Class that allows for the placement of temporary light sources.
 * You should only call the methods in this class from the client side.
 */
@EventBusSubscriber(modid = ModIds.ID_COFH_CORE)
public class TransientLightManager {

    protected static final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
    protected static Long2ByteMap CURRENT = new Long2ByteOpenHashMap();
    protected static Long2ByteMap PREVIOUS = new Long2ByteOpenHashMap();
    private static final Field BLOCK_ENGINE_FIELD = getField(LevelLightEngine.class, "blockEngine");
    private static final Field STORAGE_FIELD = getField(LightEngine.class, "storage");
    private static final Method STORAGE_STORING_LIGHT_FOR_SECTION = getMethod(LayerLightSectionStorage.class, "storingLightForSection", long.class);
    private static final Method STORAGE_GET_STORED_LEVEL = getMethod(LayerLightSectionStorage.class, "getStoredLevel", long.class);
    private static final Method STORAGE_SET_STORED_LEVEL = getMethod(LayerLightSectionStorage.class, "setStoredLevel", long.class, int.class);
    private static final Method ENGINE_GET_STATE = getMethod(LightEngine.class, "getState", BlockPos.class);
    private static final Method ENGINE_GET_EMISSION = getMethod(BlockLightEngine.class, "getEmission", long.class, BlockState.class);
    private static final Method ENGINE_ENQUEUE_DECREASE = getMethod(LightEngine.class, "enqueueDecrease", long.class, long.class);
    private static final Method ENGINE_ENQUEUE_INCREASE = getMethod(LightEngine.class, "enqueueIncrease", long.class, long.class);
    private static final Method ENGINE_CHECK_BLOCK = getMethod(LightEngine.class, "checkBlock", BlockPos.class);
    private static final Method LIGHTENGINE_IS_EMPTY_SHAPE = getMethod(LightEngine.class, "isEmptyShape", BlockState.class);

    // region TRANSIENT
    public static void addLight(BlockPos pos, int level) {

        addLight(pos.asLong(), level);
    }

    /**
     * Places a light source lasting one tick in the client level.
     *
     * @param pos   The position to place the light source at.
     * @param level The light level of the light source.
     */
    public static void addLight(long pos, int level) {

        if (0 < level && level < 16 && level > CURRENT.get(pos)) {
            CURRENT.put(pos, (byte) level);
        }
    }

    @SubscribeEvent
    protected static void tick(ClientTickEvent.Post event) {

        if (CURRENT.isEmpty() && PREVIOUS.isEmpty()) {
            return;
        }
        Level level = ProxyUtils.getClientWorld();
        Object engine = getBlockEngine(level);
        if (level == null || !(engine instanceof BlockLightEngine)) {
            CURRENT.clear();
            return;
        }
        Object storage = getStorage(engine);
        if (storage == null) {
            CURRENT.clear();
            return;
        }
        for (Long2ByteMap.Entry entry : CURRENT.long2ByteEntrySet()) {
            long pos = entry.getLongKey();
            if (!storageStoringLightForSection(storage, SectionPos.blockToSection(pos))) {
                return;
            }
            int light = entry.getByteValue();
            int previous = PREVIOUS.remove(pos);
            if (previous == light) {
                continue;
            }
            int stored = storageGetStoredLevel(storage, pos);
            if (stored == light) {
                continue;
            }
            boolean empty = true;
            if (stored == previous) {
                BlockState state = engineGetState(engine, cursor.set(pos));
                int emitted = engineGetEmission(engine, pos, state);
                if (emitted > light) {
                    if (emitted == stored) {
                        continue;
                    }
                    light = emitted;
                    empty = lightEngineIsEmptyShape(state);
                }
            } else if (stored > light) {
                continue;
            }
            if (stored > 0) {
                storageSetStoredLevel(storage, pos, 0);
                engineEnqueueDecrease(engine, pos, LightEngine.QueueEntry.decreaseAllDirections(stored));
            }
            if (light > 0) {
                engineEnqueueIncrease(engine, pos, LightEngine.QueueEntry.increaseLightFromEmission(light, empty));
            }
        }
        for (Long2ByteMap.Entry entry : PREVIOUS.long2ByteEntrySet()) {
            long pos = entry.getLongKey();
            if (storageGetStoredLevel(storage, pos) == entry.getByteValue()) {
                engineCheckBlock(engine, cursor.set(pos));
            }
        }
        PREVIOUS = CURRENT;
        CURRENT = new Long2ByteOpenHashMap(CURRENT.size() + 10);
    }

    private static Field getField(Class<?> owner, String name) {

        try {
            Field field = owner.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }

    private static Method getMethod(Class<?> owner, String name, Class<?>... params) {

        try {
            Method method = owner.getDeclaredMethod(name, params);
            method.setAccessible(true);
            return method;
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }

    private static Object getBlockEngine(Level level) {

        if (level == null || BLOCK_ENGINE_FIELD == null) {
            return null;
        }
        try {
            return BLOCK_ENGINE_FIELD.get(level.getLightEngine());
        } catch (IllegalAccessException ex) {
            return null;
        }
    }

    private static Object getStorage(Object engine) {

        if (engine == null || STORAGE_FIELD == null) {
            return null;
        }
        try {
            return STORAGE_FIELD.get(engine);
        } catch (IllegalAccessException ex) {
            return null;
        }
    }

    private static boolean storageStoringLightForSection(Object storage, long section) {

        if (storage == null || STORAGE_STORING_LIGHT_FOR_SECTION == null) {
            return false;
        }
        try {
            return (boolean) STORAGE_STORING_LIGHT_FOR_SECTION.invoke(storage, section);
        } catch (ReflectiveOperationException ex) {
            return false;
        }
    }

    private static int storageGetStoredLevel(Object storage, long pos) {

        if (storage == null || STORAGE_GET_STORED_LEVEL == null) {
            return 0;
        }
        try {
            return (int) STORAGE_GET_STORED_LEVEL.invoke(storage, pos);
        } catch (ReflectiveOperationException ex) {
            return 0;
        }
    }

    private static void storageSetStoredLevel(Object storage, long pos, int level) {

        if (storage == null || STORAGE_SET_STORED_LEVEL == null) {
            return;
        }
        try {
            STORAGE_SET_STORED_LEVEL.invoke(storage, pos, level);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private static BlockState engineGetState(Object engine, BlockPos pos) {

        if (engine == null || ENGINE_GET_STATE == null) {
            return null;
        }
        try {
            return (BlockState) ENGINE_GET_STATE.invoke(engine, pos);
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }

    private static int engineGetEmission(Object engine, long pos, BlockState state) {

        if (engine == null || ENGINE_GET_EMISSION == null || state == null) {
            return 0;
        }
        try {
            return (int) ENGINE_GET_EMISSION.invoke(engine, pos, state);
        } catch (ReflectiveOperationException ex) {
            return 0;
        }
    }

    private static boolean lightEngineIsEmptyShape(BlockState state) {

        if (state == null || LIGHTENGINE_IS_EMPTY_SHAPE == null) {
            return true;
        }
        try {
            return (boolean) LIGHTENGINE_IS_EMPTY_SHAPE.invoke(null, state);
        } catch (ReflectiveOperationException ex) {
            return true;
        }
    }

    private static void engineEnqueueDecrease(Object engine, long pos, long entry) {

        if (engine == null || ENGINE_ENQUEUE_DECREASE == null) {
            return;
        }
        try {
            ENGINE_ENQUEUE_DECREASE.invoke(engine, pos, entry);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private static void engineEnqueueIncrease(Object engine, long pos, long entry) {

        if (engine == null || ENGINE_ENQUEUE_INCREASE == null) {
            return;
        }
        try {
            ENGINE_ENQUEUE_INCREASE.invoke(engine, pos, entry);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private static void engineCheckBlock(Object engine, BlockPos pos) {

        if (engine == null || ENGINE_CHECK_BLOCK == null) {
            return;
        }
        try {
            ENGINE_CHECK_BLOCK.invoke(engine, pos);
        } catch (ReflectiveOperationException ignored) {
        }
    }

}
