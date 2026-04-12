package net.errorcraft.itematic.world.action.context;

import com.mojang.serialization.Codec;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.context.ContextParameter;

public enum ItemStackTarget implements StringIdentifiable {
    TOOL("tool", LootContextParameters.TOOL);

    public static final Codec<ItemStackTarget> CODEC = StringIdentifiable.createCodec(ItemStackTarget::values);

    private final String name;
    private final ContextParameter<ItemStack> parameter;

    ItemStackTarget(String name, ContextParameter<ItemStack> parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public ContextParameter<ItemStack> parameter() {
        return this.parameter;
    }
}
