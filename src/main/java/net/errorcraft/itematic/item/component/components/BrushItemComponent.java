package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BrushItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public record BrushItemComponent() implements ItemComponent<BrushItemComponent> {
    public static final BrushItemComponent INSTANCE = new BrushItemComponent();
    public static final Codec<BrushItemComponent> CODEC = Codec.unit(INSTANCE);
    private static final BrushItem DUMMY = new BrushItem(new Item.Settings());

    public static ItemComponent<?>[] of(int brushTicks) {
        return new ItemComponent<?>[] {
            UseableItemComponent.of(brushTicks, UseableItemComponent.Pass.BLOCK),
            INSTANCE
        };
    }

    @Override
    public ItemComponentType<BrushItemComponent> type() {
        return ItemComponentTypes.BRUSH;
    }

    @Override
    public Codec<BrushItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void using(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks) {
        DUMMY.itematic$setUsedTicks(usedTicks);
        DUMMY.usageTick(world, user, stack, remainingUseTicks);
    }
}
