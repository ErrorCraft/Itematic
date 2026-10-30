package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.action.ActionType;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

public class StoreIntAction extends StoreAction<StoreIntAction, ContextIntProvider> {
    public static final MapCodec<StoreIntAction> CODEC = codec(Registries.CONTEXT_INT_PROVIDER, StoreIntAction::new);

    protected StoreIntAction(ResourceKey<ContextIntProvider> provider, Identifier storage, NbtPathArgument.NbtPath path) {
        super(provider, storage, path);
    }

    @Override
    public ActionType<StoreIntAction> type() {
        return ActionType.STORE_INT;
    }

    @Override
    protected ResourceKey<Registry<ContextIntProvider>> registry() {
        return Registries.CONTEXT_INT_PROVIDER;
    }

    @Override
    protected Tag create(ContextIntProvider value, LootContext context) throws ArithmeticException {
        return IntTag.valueOf(value.getIntUnsafe(context));
    }
}
