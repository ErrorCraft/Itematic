package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TextHolderItemComponent implements ItemComponent<TextHolderItemComponent> {
    public static final TextHolderItemComponent INSTANCE = new TextHolderItemComponent();
    public static final Codec<TextHolderItemComponent> CODEC = MapCodec.unitCodec(INSTANCE);

    private TextHolderItemComponent() {}

    @Override
    public ItemComponentType<TextHolderItemComponent> type() {
        return ItemComponentTypes.TEXT_HOLDER;
    }

    @Override
    public Codec<TextHolderItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(Level world, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        user.openItemGui(stack, hand);
        user.awardStat(Stats.ITEM_USED.itematic$getOrCreateStat(stack.getItemHolder()));
        return ItemResult.SUCCEED;
    }
}
