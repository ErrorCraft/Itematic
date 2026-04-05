package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class TextHolderItemComponent implements ItemComponent<TextHolderItemComponent> {
    public static final TextHolderItemComponent INSTANCE = new TextHolderItemComponent();
    public static final Codec<TextHolderItemComponent> CODEC = Codec.unit(INSTANCE);

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
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        user.useBook(stack, hand);
        user.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
        return ItemResult.SUCCEED;
    }
}
