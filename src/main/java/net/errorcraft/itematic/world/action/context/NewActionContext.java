package net.errorcraft.itematic.world.action.context;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.context.ContextParameter;
import net.minecraft.util.context.ContextParameterMap;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class NewActionContext {
    private final ServerWorld world;
    private final ContextParameterMap parameters;
    private final ItemStackExchanger stackExchanger;

    private NewActionContext(ServerWorld world, ContextParameterMap parameters, ItemStackExchanger stackExchanger) {
        this.world = world;
        this.parameters = parameters;
        this.stackExchanger = stackExchanger;
    }

    public static Builder builder(ServerWorld world) {
        return new Builder(world);
    }

    public ServerWorld world() {
        return this.world;
    }

    @Nullable
    public <T> T get(ContextParameter<T> parameter) {
        return this.parameters.getNullable(parameter);
    }

    public ItemStack resultStack() {
        return this.stackExchanger.result();
    }

    public void exchangeStack(ItemStack stack) {
        this.stackExchanger.exchange(stack);
    }

    public LootContext lootContext() {
        LootWorldContext context = new LootWorldContext(
            this.world,
            this.parameters,
            Map.of(),
            0.0f
        );
        return new LootContext.Builder(context).build(Optional.empty());
    }

    public ServerCommandSource commandSource(CommandFunctionManager functionManager) {
        ServerCommandSource source = functionManager.getScheduledCommandSource();
        Entity entity = this.get(LootContextParameters.THIS_ENTITY);
        if (entity != null) {
            source = source.withEntity(entity);
        }

        Vec3d position = this.get(LootContextParameters.ORIGIN);
        if (position != null) {
            source = source.withPosition(position);
        }

        return source;
    }

    public static class Builder {
        private final ServerWorld world;
        private final ContextParameterMap.Builder parameters = new ContextParameterMap.Builder();

        private Builder(ServerWorld world) {
            this.world = world;
        }

        public NewActionContext build() {
            return new NewActionContext(
                this.world,
                this.parameters.build(LootContextTypes.EMPTY),
                ItemStackExchanger.EMPTY
            );
        }

        public <T> Builder add(ContextParameter<T> parameter, T value) {
            this.parameters.add(parameter, value);
            return this;
        }
    }
}
