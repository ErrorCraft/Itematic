package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TextHolderItemBehavior implements ItemBehavior<TextHolderItemBehavior> {
    public static final TextHolderItemBehavior INSTANCE = new TextHolderItemBehavior();
    public static final Codec<TextHolderItemBehavior> CODEC = MapCodec.unitCodec(INSTANCE);

    private TextHolderItemBehavior() {}

    @Override
    public ItemBehaviorType<TextHolderItemBehavior> type() {
        return ItemBehaviorType.TEXT_HOLDER;
    }

    @Override
    public ItemResult use(Level world, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        user.openItemGui(stack, hand);
        user.awardStat(Stats.ITEM_USED.itematic$get(stack.getItemHolder()));
        return ItemResult.SUCCEED;
    }
}
