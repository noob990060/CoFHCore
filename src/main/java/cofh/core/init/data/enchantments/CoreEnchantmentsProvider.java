package cofh.core.init.data.enchantments;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;
import static cofh.core.util.references.CoreIDs.ID_HOLDING;

public class CoreEnchantmentsProvider extends DatapackBuiltinEntriesProvider {

    public CoreEnchantmentsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, createRegistrySetBuilder(), Set.of("minecraft", ID_COFH_CORE));
    }

    @NotNull
    @Override
    public String getName() {
        return "CoFH Core Enchantments";
    }

    private static RegistrySetBuilder createRegistrySetBuilder() {
        RegistrySetBuilder builder = new RegistrySetBuilder();
        builder.add(Registries.ENCHANTMENT, bootstrap -> {
            bootstrap.register(
                ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, ID_HOLDING)),
                new Enchantment(
                    Component.translatable("enchantment.cofh_core.holding"),
                    new Enchantment.EnchantmentDefinition(
                        // TODO: Define proper item set for container items
                        // For now, use empty sets - this will be refined later
                        HolderSet.empty(), // supportedItems
                        
                        // Optional<HolderSet> of items that the enchantment considers "primary"
                        // For enchanting table visibility
                        Optional.empty(), // primaryItems
                        
                        // Weight of the enchantment in enchanting table
                        5,
                        
                        // Maximum level this enchantment can reach
                        4,
                        
                        // Minimum cost: base cost + cost per level
                        Enchantment.dynamicCost(1, 5),
                        
                        // Maximum cost: base cost + cost per level  
                        Enchantment.dynamicCost(51, 5),
                        
                        // Anvil cost multiplier
                        1,
                        
                        // Equipment slot groups this enchantment affects
                        List.of(EquipmentSlotGroup.ANY)
                    ),
                    // Exclusive set (enchantments incompatible with this one)
                    HolderSet.empty(),
                    // DataComponentMap for effects
                    // TODO: Add holding effect components here
                    net.minecraft.core.component.DataComponentMap.builder().build()
                )
            );
        });
        return builder;
    }
}
