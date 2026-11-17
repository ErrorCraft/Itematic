package net.errorcraft.itematic.world.item.component;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BookContent {
    private BookContent() {}

    public static void open(Player user, ItemStack stack, InteractionHand hand) {
        user.openItemGui(stack, hand);
        user.awardStat(Stats.ITEM_USED.itematic$get(stack.typeHolder()));
    }
}
