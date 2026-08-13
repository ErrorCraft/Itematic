package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BrushItemBehavior implements ItemBehavior<BrushItemBehavior> {
    public static final BrushItemBehavior INSTANCE = new BrushItemBehavior();
    public static final Codec<BrushItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);
    private static final BrushItem DUMMY = new BrushItem(new Item.Properties());

    private BrushItemBehavior() {}

    @Override
    public ItemBehaviorType<BrushItemBehavior> type() {
        return ItemBehaviorType.BRUSH;
    }

    @Override
    public Codec<BrushItemBehavior> codec() {
        return CODEC;
    }

    @Override
    public void using(ItemStack stack, Level world, LivingEntity user, int usedTicks, int remainingUseTicks) {
        DUMMY.itematic$setUsedTicks(usedTicks);
        DUMMY.onUseTick(world, user, stack, remainingUseTicks);
    }
}
