package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public record PotionItemComponent() implements ItemComponent<PotionItemComponent> {
    public static final PotionItemComponent INSTANCE = new PotionItemComponent();
    public static final Codec<PotionItemComponent> CODEC = Codec.unit(INSTANCE);

    @Override
    public ItemComponentType<PotionItemComponent> type() {
        return ItemComponentTypes.POTION;
    }

    @Override
    public Codec<PotionItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void finishUsing(World world, LivingEntity user, ItemStack stack, int usedTicks, ItemStackExchanger stackExchanger) {
        PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContents != null) {
            potionContents.apply(user);
        }
    }
}
