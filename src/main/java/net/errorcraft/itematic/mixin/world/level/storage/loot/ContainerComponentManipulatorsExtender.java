package net.errorcraft.itematic.mixin.world.level.storage.loot;

import net.errorcraft.itematic.access.world.level.storage.loot.ContainerComponentManipulatorAccess;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;

import java.util.stream.Stream;

public interface ContainerComponentManipulatorsExtender {
    @Mixin(targets = "net/minecraft/world/level/storage/loot/ContainerComponentManipulators$2")
    abstract class BundleContentsExtender implements ContainerComponentManipulatorAccess<BundleContents> {
        @Override
        public BundleContents itematic$setContents(ItemStack stack, BundleContents component, Stream<ItemStack> newContents) {
            return stack.itematic$getBehavior(ItemBehaviorType.ITEM_HOLDER)
                .map(itemHolder -> itemHolder.createBuilder(stack, component))
                .map(BundleContents.Mutable::clearItems)
                .map(mutable -> {
                    newContents.forEach(mutable::tryInsert);
                    return mutable;
                })
                .map(BundleContents.Mutable::toImmutable)
                .orElse(component);
        }
    }
}
