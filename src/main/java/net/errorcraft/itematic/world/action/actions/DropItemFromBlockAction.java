package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

public record DropItemFromBlockAction(ActionContextParameter position, RegistryEntry<Item> item) implements Action {
    public static final Codec<DropItemFromBlockAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(DropItemFromBlockAction::position),
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(DropItemFromBlockAction::item)
    ).apply(instance, DropItemFromBlockAction::new));

    @Override
    public ActionType<?> type() {
        return ActionTypes.DROP_ITEM_FROM_BLOCK;
    }

    @Override
    public boolean execute(ActionContext context) {
        Block.dropStack(context.world(), context.blockPos(this.position), context.side(), new ItemStack(this.item));
        return true;
    }

    public static DropItemFromBlockAction of(ActionContextParameter position, RegistryEntry<Item> item) {
        return new DropItemFromBlockAction(position, item);
    }
}
