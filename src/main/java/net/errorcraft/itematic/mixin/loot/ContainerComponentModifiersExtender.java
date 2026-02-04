package net.errorcraft.itematic.mixin.loot;

import net.errorcraft.itematic.access.loot.ContainerComponentModifierAccess;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import java.util.stream.Stream;

public interface ContainerComponentModifiersExtender {
    @Mixin(targets = "net/minecraft/loot/ContainerComponentModifiers$2")
    class BundleContentsExtender implements ContainerComponentModifierAccess<BundleContentsComponent> {
        @Override
        public BundleContentsComponent itematic$create(ItemStack stack, BundleContentsComponent component, Stream<ItemStack> newContents) {
            return stack.itematic$getBehavior(ItemComponentTypes.ITEM_HOLDER)
                .map(c -> c.createBuilder(stack, component))
                .map(BundleContentsComponent.Builder::clear)
                .map(builder -> {
                    newContents.forEach(builder::add);
                    return builder;
                })
                .map(BundleContentsComponent.Builder::build)
                .orElse(component);
        }
    }
}
