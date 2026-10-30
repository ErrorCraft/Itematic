package net.errorcraft.itematic.world.action.actions;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.CommandStorage;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public abstract class StoreAction<T extends Action<T>, N> implements Action<T> {
    private final ResourceKey<N> provider;
    private final Identifier storage;
    private final NbtPathArgument.NbtPath path;

    protected StoreAction(ResourceKey<N> provider, Identifier storage, NbtPathArgument.NbtPath path) {
        this.provider = provider;
        this.storage = storage;
        this.path = path;
    }

    protected static <T extends StoreAction<T, N>, N> MapCodec<T> codec(ResourceKey<Registry<N>> registry, Function3<ResourceKey<N>, Identifier, NbtPathArgument.NbtPath, T> creator) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(registry).fieldOf("provider").forGetter(T::provider),
            Identifier.CODEC.fieldOf("storage").forGetter(T::storage),
            NbtPathArgument.NbtPath.CODEC.fieldOf("path").forGetter(T::path)
        ).apply(instance, creator));
    }

    public ResourceKey<N> provider() {
        return this.provider;
    }

    public Identifier storage() {
        return this.storage;
    }

    public NbtPathArgument.NbtPath path() {
        return this.path;
    }

    protected abstract ResourceKey<Registry<N>> registry();
    protected abstract Tag create(N value, LootContext context) throws ArithmeticException;

    @Override
    public boolean execute(ActionContext context) {
        LootContext lootContext = context.lootContext();
        if (lootContext == null) {
            return false;
        }

        Optional<N> element = lootContext.getResolver()
            .lookupOrThrow(this.registry())
            .get(this.provider)
            .map(Holder.Reference::value);
        if (element.isEmpty()) {
            return false;
        }

        try {
            CommandStorage storage = lootContext.getLevel().getServer().getCommandStorage();
            CompoundTag data = storage.get(this.storage);
            this.path.set(data, this.create(element.get(), lootContext));
            storage.set(this.storage, data);
            return true;
        } catch (ArithmeticException | CommandSyntaxException _) {
            return false;
        }
    }
}
