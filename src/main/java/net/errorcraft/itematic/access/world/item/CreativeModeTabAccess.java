package net.errorcraft.itematic.access.world.item;

import net.errorcraft.itematic.world.item.group.ItemGroup;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.CreativeModeTab;

public interface CreativeModeTabAccess {
    interface BuilderAccess {
        default CreativeModeTab.Builder itematic$page(int page) {
            throw new AssertionError("Implemented via mixin");
        }
        default CreativeModeTab.Builder itematic$displayItems(Holder<ItemGroup> itemGroup) {
            throw new AssertionError("Implemented via mixin");
        }
        default CreativeModeTab.Builder itematic$searchDisplayItems(HolderSet<ItemGroup> itemGroups) {
            throw new AssertionError("Implemented via mixin");
        }
    }
}
