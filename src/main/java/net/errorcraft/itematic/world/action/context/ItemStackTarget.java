package net.errorcraft.itematic.world.action.context;

import com.mojang.serialization.Codec;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum ItemStackTarget implements StringIdentifiable {
    TOOL("tool", context -> context.get(LootContextParameters.TOOL)),
    RESULT("result", ActionContext::resultStack);

    public static final Codec<ItemStackTarget> CODEC = StringIdentifiable.createCodec(ItemStackTarget::values);

    private final String name;
    private final Function<ActionContext, ItemStack> itemStackSupplier;

    ItemStackTarget(String name, Function<ActionContext, ItemStack> itemStackSupplier) {
        this.name = name;
        this.itemStackSupplier = itemStackSupplier;
    }

    @Override
    public String asString() {
        return this.name;
    }

    @Nullable
    public ItemStack get(ActionContext context) {
        return this.itemStackSupplier.apply(context);
    }
}
