package net.errorcraft.itematic.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.entity.EntityTypeAccess;
import net.errorcraft.itematic.access.entity.EntityTypeBuilderAccess;
import net.errorcraft.itematic.entity.EntitySpawnCallback;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.entity.initializer.EntityInitializerSupplier;
import net.errorcraft.itematic.entity.initializer.initializers.*;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.vehicle.*;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Objects;
import java.util.function.Consumer;

@Mixin(EntityType.class)
public abstract class EntityTypeExtender<T extends Entity> implements EntityTypeAccess<T> {
    @Shadow
    public static <T extends Entity> Consumer<T> copier(Consumer<T> chained, World world, ItemStack stack, @Nullable LivingEntity spawner) {
        return null;
    }

    @Shadow
    @Nullable
    public abstract T create(ServerWorld world, @Nullable Consumer<T> afterConsumer, BlockPos pos, SpawnReason reason, boolean alignPosition, boolean invertY);

    @Unique
    private EntityInitializer<T> initializer;

    @Unique
    private ActionContext actionContext;

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=minecart"
            )
        )
    )
    private static EntityType.Builder<MinecartEntity> setMinecartInitializer(EntityType.Builder<MinecartEntity> builder) {
        builder.itematic$initializer(MinecartEntityInitializer::new);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=chest_minecart"
            )
        )
    )
    private static EntityType.Builder<ChestMinecartEntity> setChestMinecartInitializer(EntityType.Builder<ChestMinecartEntity> builder) {
        builder.itematic$initializer(MinecartEntityInitializer::new);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=furnace_minecart"
            )
        )
    )
    private static EntityType.Builder<FurnaceMinecartEntity> setFurnaceMinecartInitializer(EntityType.Builder<FurnaceMinecartEntity> builder) {
        builder.itematic$initializer(MinecartEntityInitializer::new);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=tnt_minecart"
            )
        )
    )
    private static EntityType.Builder<TntMinecartEntity> setTntMinecartInitializer(EntityType.Builder<TntMinecartEntity> builder) {
        builder.itematic$initializer(MinecartEntityInitializer::new);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=spawner_minecart"
            )
        )
    )
    private static EntityType.Builder<SpawnerMinecartEntity> setSpawnerMinecartInitializer(EntityType.Builder<SpawnerMinecartEntity> builder) {
        builder.itematic$initializer(MinecartEntityInitializer::new);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=hopper_minecart"
            )
        )
    )
    private static EntityType.Builder<HopperMinecartEntity> setHopperMinecartInitializer(EntityType.Builder<HopperMinecartEntity> builder) {
        builder.itematic$initializer(MinecartEntityInitializer::new);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=command_block_minecart"
            )
        )
    )
    private static EntityType.Builder<CommandBlockMinecartEntity> setCommandBlockMinecartInitializer(EntityType.Builder<CommandBlockMinecartEntity> builder) {
        builder.itematic$initializer(MinecartEntityInitializer::new);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=end_crystal"
            )
        )
    )
    private static EntityType.Builder<EndCrystalEntity> setEndCrystalInitializer(EntityType.Builder<EndCrystalEntity> builder) {
        builder.itematic$initializer(EndCrystalEntityInitializer.INSTANCE);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=armor_stand"
            )
        )
    )
    private static EntityType.Builder<ArmorStandEntity> setArmorStandInitializer(EntityType.Builder<ArmorStandEntity> builder) {
        builder.itematic$initializer(ArmorStandEntityInitializer.INSTANCE);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=painting"
            )
        )
    )
    private static EntityType.Builder<PaintingEntity> setPaintingInitializer(EntityType.Builder<PaintingEntity> builder) {
        builder.itematic$initializer(DecorationEntityInitializer.ofPainting());
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=item_frame"
            )
        )
    )
    private static EntityType.Builder<ItemFrameEntity> setItemFrameInitializer(EntityType.Builder<ItemFrameEntity> builder) {
        builder.itematic$initializer(DecorationEntityInitializer.ofItemFrame(ItemFrameEntity::new));
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=glow_item_frame"
            )
        )
    )
    private static EntityType.Builder<GlowItemFrameEntity> setGlowItemFrameInitializer(EntityType.Builder<GlowItemFrameEntity> builder) {
        builder.itematic$initializer(DecorationEntityInitializer.ofItemFrame(GlowItemFrameEntity::new));
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=arrow"
            )
        )
    )
    private static EntityType.Builder<ArrowEntity> setArrowInitializer(EntityType.Builder<ArrowEntity> builder) {
        builder.itematic$initializer(PersistentProjectileEntityInitializer.of(
            ArrowEntity::new,
            ArrowEntity::new
        ));
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=spectral_arrow"
            )
        )
    )
    private static EntityType.Builder<SpectralArrowEntity> setSpectralArrowInitializer(EntityType.Builder<SpectralArrowEntity> builder) {
        builder.itematic$initializer(PersistentProjectileEntityInitializer.of(
            SpectralArrowEntity::new,
            SpectralArrowEntity::new
        ));
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=trident"
            )
        )
    )
    private static EntityType.Builder<TridentEntity> setTridentInitializer(EntityType.Builder<TridentEntity> builder) {
        builder.itematic$initializer(TridentEntityInitializer.INSTANCE);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=firework_rocket"
            )
        )
    )
    private static EntityType.Builder<FireworkRocketEntity> setFireworkRocketInitializer(EntityType.Builder<FireworkRocketEntity> builder) {
        builder.itematic$initializer(FireworkRocketEntityInitializer.INSTANCE);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=eye_of_ender"
            )
        )
    )
    private static EntityType.Builder<EyeOfEnderEntity> setEyeOfEnderInitializer(EntityType.Builder<EyeOfEnderEntity> builder) {
        builder.itematic$initializer(EyeOfEnderEntityInitializer.INSTANCE);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=small_fireball"
            )
        )
    )
    private static EntityType.Builder<SmallFireballEntity> setSmallFireballInitializer(EntityType.Builder<SmallFireballEntity> builder) {
        builder.itematic$initializer(SmallFireballEntityInitializer.INSTANCE);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/entity/EntityType$Builder;)Lnet/minecraft/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=wind_charge"
            )
        )
    )
    private static EntityType.Builder<WindChargeEntity> setWindChargeInitializer(EntityType.Builder<WindChargeEntity> builder) {
        builder.itematic$initializer(WindChargeEntityInitializer.INSTANCE);
        return builder;
    }

    @WrapOperation(
        method = "create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType$EntityFactory;create(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;"
        )
    )
    private T useEntityInitializer(EntityType.EntityFactory<T> instance, EntityType<T> type, World world, Operation<T> original, @Local(argsOnly = true) SpawnReason reason) {
        if (this.actionContext == null) {
            return original.call(instance, type, world);
        }

        // Copy to a local and set the field to null so we don't get a StackOverflowError
        ActionContext context = this.actionContext;
        this.actionContext = null;
        return this.initializer.create(context, reason);
    }

    @Override
    public void itematic$setInitializer(EntityInitializer<T> initializer) {
        this.initializer = initializer;
    }

    @Override
    public T itematic$create(ActionContext context, SpawnReason reason, BlockPos pos, @Nullable EntitySpawnCallback<T> callback, boolean allowItemData, boolean invertY) {
        this.actionContext = context;
        return this.create(
            context.world(),
            copier(context, callback, allowItemData),
            pos,
            reason,
            true,
            invertY
        );
    }

    @Unique
    @Nullable
    private static <T extends Entity> Consumer<T> copier(ActionContext context, @Nullable EntitySpawnCallback<T> callback, boolean allowItemData) {
        ItemStack stack = context.get(LootContextParameters.TOOL);
        if (!allowItemData || ItemStackUtil.isNullOrEmpty(stack)) {
            return callback == null ? null : entity -> callback.accept(entity, stack);
        }

        return copier(
            callback == null ?entity -> {} : entity -> callback.accept(entity, stack),
            context.world(),
            stack,
            context.get(LootContextParameters.THIS_ENTITY, LivingEntity.class)
        );
    }

    @Mixin(EntityType.Builder.class)
    public static class BuilderExtender<T extends Entity> implements EntityTypeBuilderAccess<T> {
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
            Objects.requireNonNull(initializer);
            this.initializer = type -> initializer;
        }

        @Override
        public void itematic$initializer(EntityInitializerSupplier<T> initializer) {
            this.initializer = Objects.requireNonNull(initializer);
        }
    }
}
