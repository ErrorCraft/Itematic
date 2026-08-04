package net.errorcraft.itematic.mixin.loot;

import net.errorcraft.itematic.access.loot.ContainerComponentModifierAccess;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;

import java.util.stream.Stream;

public interface ContainerComponentModifiersExtender {
    @Mixin(targets = "net/minecraft/world/level/storage/loot/ContainerComponentManipulators$2")
    class BundleContentsExtender implements ContainerComponentModifierAccess<BundleContents> {
        @Override
        public BundleContents itematic$apply(ItemStack stack, BundleContents component, Stream<ItemStack> newContents) {
            return stack.itematic$getBehavior(ItemComponentTypes.ITEM_HOLDER)
                .map(c -> c.createBuilder(stack, component))
                .map(BundleContents.Mutable::clearItems)
                .map(builder -> {
                    newContents.forEach(builder::tryInsert);
                    return builder;
                })
                .map(BundleContents.Mutable::toImmutable)
                .orElse(component);
        }
    }
}
