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
import net.errorcraft.itematic.world.entity.initializer.initializers.ArrowEntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.initializers.EndCrystalEntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.initializers.EyeOfEnderEntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.initializers.FireworkRocketEntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.initializers.HangingEntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.initializers.MinecartEntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.initializers.SimpleEntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.initializers.ThrownBallEntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.initializers.ThrownTridentEntityInitializer;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.arrow.SpectralArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.entity.vehicle.minecart.Minecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartChest;
import net.minecraft.world.entity.vehicle.minecart.MinecartCommandBlock;
import net.minecraft.world.entity.vehicle.minecart.MinecartFurnace;
import net.minecraft.world.entity.vehicle.minecart.MinecartHopper;
import net.minecraft.world.entity.vehicle.minecart.MinecartSpawner;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.function.Consumer;

@Mixin(EntityType.class)
public abstract class EntityTypeExtender<T extends Entity> implements EntityTypeAccess<T> {
    @Shadow
    public static <T extends Entity> Consumer<T> appendDefaultStackConfig(Consumer<T> initialConfig, Level level, ItemStack itemStack, @Nullable LivingEntity user) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    @Nullable
    public abstract T create(ServerLevel level, @Nullable Consumer<T> postSpawnConfig, BlockPos spawnPos, EntitySpawnReason spawnReason, boolean tryMoveDown, boolean movedUp);

    @Unique
    private EntityInitializer<T> initializer;

    @Unique
    @Nullable
    private ActionContext actionContext;

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=minecart"
            )
        )
    )
    private static EntityType.Builder<Minecart> setMinecartInitializer(EntityType.Builder<Minecart> builder) {
        builder.itematic$initializer(MinecartEntityInitializer::new);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=chest_minecart"
            )
        )
    )
    private static EntityType.Builder<MinecartChest> setChestMinecartInitializer(EntityType.Builder<MinecartChest> builder) {
        builder.itematic$initializer(MinecartEntityInitializer::new);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=furnace_minecart"
            )
        )
    )
    private static EntityType.Builder<MinecartFurnace> setFurnaceMinecartInitializer(EntityType.Builder<MinecartFurnace> builder) {
        builder.itematic$initializer(MinecartEntityInitializer::new);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=tnt_minecart"
            )
        )
    )
    private static EntityType.Builder<MinecartTNT> setTntMinecartInitializer(EntityType.Builder<MinecartTNT> builder) {
        builder.itematic$initializer(MinecartEntityInitializer::new);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=spawner_minecart"
            )
        )
    )
    private static EntityType.Builder<MinecartSpawner> setSpawnerMinecartInitializer(EntityType.Builder<MinecartSpawner> builder) {
        builder.itematic$initializer(MinecartEntityInitializer::new);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=hopper_minecart"
            )
        )
    )
    private static EntityType.Builder<MinecartHopper> setHopperMinecartInitializer(EntityType.Builder<MinecartHopper> builder) {
        builder.itematic$initializer(MinecartEntityInitializer::new);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=command_block_minecart"
            )
        )
    )
    private static EntityType.Builder<MinecartCommandBlock> setCommandBlockMinecartInitializer(EntityType.Builder<MinecartCommandBlock> builder) {
        builder.itematic$initializer(MinecartEntityInitializer::new);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=end_crystal"
            )
        )
    )
    private static EntityType.Builder<EndCrystal> setEndCrystalInitializer(EntityType.Builder<EndCrystal> builder) {
        builder.itematic$initializer(EndCrystalEntityInitializer.INSTANCE);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=painting"
            )
        )
    )
    private static EntityType.Builder<Painting> setPaintingInitializer(EntityType.Builder<Painting> builder) {
        builder.itematic$initializer(HangingEntityInitializer.of(
            (level, pos, facing) -> Painting.create(level, pos, facing).orElse(null)
        ));
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=item_frame"
            )
        )
    )
    private static EntityType.Builder<ItemFrame> setItemFrameInitializer(EntityType.Builder<ItemFrame> builder) {
        builder.itematic$initializer(HangingEntityInitializer.of(ItemFrame::new));
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=glow_item_frame"
            )
        )
    )
    private static EntityType.Builder<GlowItemFrame> setGlowItemFrameInitializer(EntityType.Builder<GlowItemFrame> builder) {
        builder.itematic$initializer(HangingEntityInitializer.of(GlowItemFrame::new));
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=arrow"
            )
        )
    )
    private static EntityType.Builder<Arrow> setArrowInitializer(EntityType.Builder<Arrow> builder) {
        builder.itematic$initializer(ArrowEntityInitializer.of(
            Arrow::new,
            Arrow::new
        ));
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=spectral_arrow"
            )
        )
    )
    private static EntityType.Builder<SpectralArrow> setSpectralArrowInitializer(EntityType.Builder<SpectralArrow> builder) {
        builder.itematic$initializer(ArrowEntityInitializer.of(
            SpectralArrow::new,
            SpectralArrow::new
        ));
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=trident"
            )
        )
    )
    private static EntityType.Builder<ThrownTrident> setTridentInitializer(EntityType.Builder<ThrownTrident> builder) {
        builder.itematic$initializer(ThrownTridentEntityInitializer.INSTANCE);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
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
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=eye_of_ender"
            )
        )
    )
    private static EntityType.Builder<EyeOfEnder> setEyeOfEnderInitializer(EntityType.Builder<EyeOfEnder> builder) {
        builder.itematic$initializer(EyeOfEnderEntityInitializer.INSTANCE);
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=small_fireball"
            )
        )
    )
    private static EntityType.Builder<SmallFireball> setSmallFireballInitializer(EntityType.Builder<SmallFireball> builder) {
        builder.itematic$initializer(ThrownBallEntityInitializer.of(
            (player, level, x, y, z) -> new SmallFireball(level, player, new Vec3(x, y, z)),
            SmallFireball::new
        ));
        return builder;
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=wind_charge"
            )
        )
    )
    private static EntityType.Builder<WindCharge> setWindChargeInitializer(EntityType.Builder<WindCharge> builder) {
        builder.itematic$initializer(ThrownBallEntityInitializer.of(
            WindCharge::new,
            WindCharge::new
        ));
        return builder;
    }

    @WrapOperation(
        method = "create(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnReason;)Lnet/minecraft/world/entity/Entity;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType$EntityFactory;create(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/Entity;"
        )
    )
    @Nullable
    private T useEntityInitializer(EntityType.EntityFactory<T> instance, EntityType<T> type, Level level, Operation<T> original, @Local(name = "reason", argsOnly = true) EntitySpawnReason reason) {
        if (this.actionContext == null) {
            return original.call(instance, type, level);
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
    private static <T extends Entity> Consumer<T> copier(ActionContext context, @Nullable EntitySpawnCallback callback, boolean allowItemData) {
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
