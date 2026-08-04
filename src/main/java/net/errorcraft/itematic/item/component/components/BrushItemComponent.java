package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BrushItemComponent implements ItemComponent<BrushItemComponent> {
    public static final BrushItemComponent INSTANCE = new BrushItemComponent();
    public static final Codec<BrushItemComponent> CODEC = MapCodec.unitCodec(INSTANCE);
    private static final BrushItem DUMMY = new BrushItem(new Item.Properties());

    private BrushItemComponent() {}

    @Override
    public ItemComponentType<BrushItemComponent> type() {
        return ItemComponentTypes.BRUSH;
    }

    @Override
    public Codec<BrushItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void using(ItemStack stack, Level world, LivingEntity user, int usedTicks, int remainingUseTicks) {
        DUMMY.itematic$setUsedTicks(usedTicks);
        DUMMY.onUseTick(world, user, stack, remainingUseTicks);
    }
}
