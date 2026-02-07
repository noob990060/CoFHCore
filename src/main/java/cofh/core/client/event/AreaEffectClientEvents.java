package cofh.core.client.event;

import cofh.core.common.capability.CoreCapabilities;
import cofh.core.common.capability.templates.AreaEffectItemWrapper;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

import java.util.List;

import static cofh.core.util.helpers.AreaEffectHelper.validAreaEffectItem;
import static cofh.core.util.helpers.AreaEffectHelper.validAreaEffectMiningItem;
import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

@EventBusSubscriber(modid = ID_COFH_CORE, value = Dist.CLIENT)
public class AreaEffectClientEvents {

    private AreaEffectClientEvents() {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void renderBlockHighlights(RenderHighlightEvent.Block event) {

        if (event.isCanceled()) {
            return;
        }
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        ItemStack stack = player.getMainHandItem();
        if (!validAreaEffectItem(stack)) {
            return;
        }
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        var aeCap = stack.getCapability(CoreCapabilities.AreaEffectHandler.ITEM);
        if (aeCap == null) {
            aeCap = new AreaEffectItemWrapper(stack);
        }
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        ImmutableList<BlockPos> areaBlocks = aeCap.getAreaEffectBlocks(event.getTarget().getBlockPos(), player, level);

        LevelRenderer levelRenderer = event.getLevelRenderer();
        PoseStack matrix = event.getPoseStack();
        VertexConsumer vertexBuilder = event.getMultiBufferSource().getBuffer(RenderType.lines());
        Entity viewEntity = camera.getEntity();
        Level world = player.level();

        Vec3 vec3d = camera.getPosition();
        double d0 = vec3d.x();
        double d1 = vec3d.y();
        double d2 = vec3d.z();

        matrix.pushPose();
        for (BlockPos pos : areaBlocks) {
            if (world.getWorldBorder().isWithinBounds(pos)) {
                VoxelShape shape = world.getBlockState(pos).getShape(world, pos, CollisionContext.of(viewEntity));
                matrix.pushPose();
                matrix.translate(pos.getX() - d0, pos.getY() - d1, pos.getZ() - d2);
                for (AABB box : shape.toAabbs()) {
                    LevelRenderer.renderLineBox(matrix, vertexBuilder, box, 0.0F, 0.0F, 0.0F, 0.4F);
                }
                matrix.popPose();
            }
        }
        matrix.popPose();

        MultiPlayerGameMode gamemode = Minecraft.getInstance().gameMode;
        if (gamemode == null || !gamemode.isDestroying()) {
            return;
        }
        if (!validAreaEffectMiningItem(stack)) {
            return;
        }
        drawBlockDamageTexture(gamemode, event.getLevelRenderer(), event.getPoseStack(),
                Minecraft.getInstance().gameRenderer.getMainCamera(), player.getCommandSenderWorld(), areaBlocks);
    }

    // region HELPERS
    private static void drawBlockDamageTexture(
            MultiPlayerGameMode gameMode,
            LevelRenderer levelRenderer,
            PoseStack posestack,
            Camera camera,
            Level level,
            List<BlockPos> areaBlocks) {

        double d0 = camera.getPosition().x;
        double d1 = camera.getPosition().y;
        double d2 = camera.getPosition().z;

        int progress = gameMode.getDestroyStage();
        if (progress < 0 || progress > 9) {
            return;
        }

        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        VertexConsumer consumer = Minecraft.getInstance().renderBuffers().crumblingBufferSource()
                .getBuffer(ModelBakery.DESTROY_TYPES.get(progress));

        for (BlockPos pos : areaBlocks) {
            posestack.pushPose();
            posestack.translate(
                    pos.getX() - d0,
                    pos.getY() - d1,
                    pos.getZ() - d2);

            dispatcher.renderBreakingTexture(
                    level.getBlockState(pos),
                    pos,
                    level,
                    posestack,
                    consumer);

            posestack.popPose();
        }
    }

    // endregion
}
