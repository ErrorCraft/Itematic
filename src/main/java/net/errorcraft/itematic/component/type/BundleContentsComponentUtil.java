package net.errorcraft.itematic.component.type;

import net.errorcraft.itematic.item.holder.rule.ItemHolderRules;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BundleContentsComponentUtil {
    private BundleContentsComponentUtil() {}

    public static BundleContentsComponent create(BundleContentsComponentExtraFields extraFields, List<ItemStack> stacks) {
        BundleContentsComponent component = new BundleContentsComponent(stacks);
        component.itematic$setExtraFields(extraFields);
        component.itematic$calculateOccupancy();
        return component;
    }

    public static BundleContentsComponent create(ItemHolderRules rules) {
        return create(new BundleContentsComponentExtraFields(rules), List.of());
    }
}
