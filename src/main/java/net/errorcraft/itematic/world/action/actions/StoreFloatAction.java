package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.world.action.ActionType;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;

public class StoreFloatAction extends StoreAction<StoreFloatAction, ContextFloatProvider> {
    public static final MapCodec<StoreFloatAction> CODEC = codec(Registries.CONTEXT_FLOAT_PROVIDER, StoreFloatAction::new);

    protected StoreFloatAction(ResourceKey<ContextFloatProvider> provider, Identifier storage, NbtPathArgument.NbtPath path) {
        super(provider, storage, path);
    }

    @Override
    public ActionType<StoreFloatAction> type() {
        return ActionType.STORE_FLOAT;
    }

    @Override
    protected ResourceKey<Registry<ContextFloatProvider>> registry() {
        return Registries.CONTEXT_FLOAT_PROVIDER;
    }

    @Override
    protected Tag create(ContextFloatProvider value, LootContext context) throws ArithmeticException {
        return FloatTag.valueOf(value.getFloatOrThrow(context));
    }
}
