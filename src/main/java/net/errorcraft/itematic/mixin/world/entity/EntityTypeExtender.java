package net.errorcraft.itematic.mixin.world.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.world.entity.EntityTypeAccess;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.entity.EntitySpawnCallback;
import net.errorcraft.itematic.world.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.EntityInitializerSupplier;
import net.errorcraft.itematic.world.entity.initializer.initializers.SimpleEntityInitializer;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PostSpawnProcessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityType.class)
public abstract class EntityTypeExtender<T extends Entity> implements EntityTypeAccess<T> {
    @Shadow
    public static <T extends Entity> PostSpawnProcessor<T> appendDefaultStackConfig(PostSpawnProcessor<T> initialConfig, Level level, ItemStack itemStack, @Nullable LivingEntity user) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    public abstract @Nullable T create(ServerLevel level, @Nullable PostSpawnProcessor<T> postSpawnConfig, BlockPos spawnPos, EntitySpawnReason spawnReason, boolean tryMoveDown, boolean movedUp);

    @Unique
    private EntityInitializer<T> initializer;

    @Unique
    @Nullable
    private ActionContext actionContext;

    @WrapOperation(
        method = "create(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnRequest;)Lnet/minecraft/world/entity/Entity;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType$EntityFactory;create(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/Entity;"
        )
    )
    @Nullable
    private T useEntityInitializer(EntityType.EntityFactory<T> instance, EntityType<T> type, Level level, Operation<T> original, @Local(name = "request", argsOnly = true) EntitySpawnRequest request) {
        if (this.actionContext == null) {
            return original.call(instance, type, level);
        }

        // Copy to a local and set the field to null so we don't get a StackOverflowError
        ActionContext context = this.actionContext;
        this.actionContext = null;
        return this.initializer.create(context, request.reason());
    }

    @Override
    public void itematic$setInitializer(EntityInitializer<T> initializer) {
        this.initializer = initializer;
    }

    @Override
    public @Nullable T itematic$create(ActionContext context, EntitySpawnReason reason, BlockPos pos, @Nullable EntitySpawnCallback callback, boolean allowItemData, boolean invertY) {
        if (!(context.level() instanceof ServerLevel level)) {
            return null;
        }

        this.actionContext = context;
        return this.create(
            level,
            copier(context, callback, allowItemData),
            pos,
            reason,
            true,
            invertY
        );
    }

    @Unique
    @Nullable
    private static <T extends Entity> PostSpawnProcessor<T> copier(ActionContext context, @Nullable EntitySpawnCallback callback, boolean allowItemData) {
        ItemStack stack = context.getOrDefault(LootContextParams.TOOL, ItemStacks::fromItemInstance, ItemStack.EMPTY);
        if (!allowItemData || stack.isEmpty()) {
            return callback == null ? null : entity -> callback.accept(entity, stack);
        }

        return appendDefaultStackConfig(
            callback == null ? _ -> {} : entity -> callback.accept(entity, stack),
            context.level(),
            stack,
            context.get(LootContextParams.THIS_ENTITY, LivingEntity.class)
        );
    }

    @Mixin(EntityType.Builder.class)
    public static class BuilderExtender<T extends Entity> implements BuilderAccess<T> {
        @Unique
        private EntityInitializerSupplier<T> initializer = SimpleEntityInitializer::new;

        @ModifyReturnValue(
            method = "build",
            at = @At("TAIL")
        )
        private EntityType<T> setInitializer(EntityType<T> original) {
            original.itematic$setInitializer(this.initializer.create(original));
            return original;
        }

        @Override
        public void itematic$initializer(EntityInitializer<T> initializer) {
            this.initializer = _ -> initializer;
        }

        @Override
        public void itematic$initializer(EntityInitializerSupplier<T> initializer) {
            this.initializer = initializer;
        }
    }
}
