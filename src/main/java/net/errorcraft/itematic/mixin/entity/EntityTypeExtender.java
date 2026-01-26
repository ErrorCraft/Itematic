package net.errorcraft.itematic.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.access.entity.EntityTypeAccess;
import net.errorcraft.itematic.access.entity.EntityTypeBuilderAccess;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.entity.initializer.EntityInitializerCodecCreator;
import net.errorcraft.itematic.entity.initializer.initializers.*;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.vehicle.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
@SuppressWarnings("unchecked")
public abstract class EntityTypeExtender<T extends Entity> implements EntityTypeAccess {
    @Unique
    private MapCodec<? extends EntityInitializer<?>> initializerCodec;

    @Unique
    private EntityInitializer<?> initializer;

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
                args = "stringValue=boat"
            )
        )
    )
    private static EntityType.Builder<BoatEntity> setBoatEntityInitializerCodec(EntityType.Builder<BoatEntity> type) {
        type.itematic$initializerCodec(BoatEntityInitializer.CODEC);
        return type;
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
                args = "stringValue=chest_boat"
            )
        )
    )
    private static EntityType.Builder<ChestBoatEntity> setChestBoatEntityInitializerCodec(EntityType.Builder<ChestBoatEntity> type) {
        type.itematic$initializerCodec(ChestBoatEntityInitializer.CODEC);
        return type;
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
                args = "stringValue=minecart"
            )
        )
    )
    private static EntityType.Builder<MinecartEntity> setMinecartInitializerCodec(EntityType.Builder<MinecartEntity> type) {
        type.itematic$initializerCodec(minecartInitializerCodecCreator(MinecartEntity::new));
        return type;
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
    private static EntityType.Builder<ChestMinecartEntity> setChestMinecartInitializerCodec(EntityType.Builder<ChestMinecartEntity> type) {
        type.itematic$initializerCodec(minecartInitializerCodecCreator(ChestMinecartEntity::new));
        return type;
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
    private static EntityType.Builder<FurnaceMinecartEntity> setFurnaceMinecartInitializerCodec(EntityType.Builder<FurnaceMinecartEntity> type) {
        type.itematic$initializerCodec(minecartInitializerCodecCreator(FurnaceMinecartEntity::new));
        return type;
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
    private static EntityType.Builder<TntMinecartEntity> setTntMinecartInitializerCodec(EntityType.Builder<TntMinecartEntity> type) {
        type.itematic$initializerCodec(minecartInitializerCodecCreator(TntMinecartEntity::new));
        return type;
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
    private static EntityType.Builder<SpawnerMinecartEntity> setSpawnerMinecartInitializerCodec(EntityType.Builder<SpawnerMinecartEntity> type) {
        type.itematic$initializerCodec(minecartInitializerCodecCreator(SpawnerMinecartEntity::new));
        return type;
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
    private static EntityType.Builder<HopperMinecartEntity> setHopperMinecartInitializerCodec(EntityType.Builder<HopperMinecartEntity> type) {
        type.itematic$initializerCodec(minecartInitializerCodecCreator(HopperMinecartEntity::new));
        return type;
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
    private static EntityType.Builder<CommandBlockMinecartEntity> setCommandBlockMinecartInitializerCodec(EntityType.Builder<CommandBlockMinecartEntity> type) {
        type.itematic$initializerCodec(minecartInitializerCodecCreator(CommandBlockMinecartEntity::new));
        return type;
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
    private static EntityType.Builder<EndCrystalEntity> setEndCrystalInitializerCodec(EntityType.Builder<EndCrystalEntity> type) {
        type.itematic$initializerCodec(EndCrystalEntityInitializer.CODEC);
        return type;
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
    private static EntityType.Builder<ArmorStandEntity> setArmorStandInitializerCodec(EntityType.Builder<ArmorStandEntity> type) {
        type.itematic$initializerCodec(ArmorStandEntityInitializer.CODEC);
        return type;
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
    private static EntityType.Builder<PaintingEntity> setPaintingInitializerCodec(EntityType.Builder<PaintingEntity> type) {
        type.itematic$initializerCodec(entityType -> MapCodec.unit(DecorationEntityInitializer.createPainting(entityType)));
        return type;
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
    private static EntityType.Builder<ItemFrameEntity> setItemFrameInitializerCodec(EntityType.Builder<ItemFrameEntity> type) {
        type.itematic$initializerCodec(decorationInitializerCodecCreator(ItemFrameEntity::new));
        return type;
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
    private static EntityType.Builder<GlowItemFrameEntity> setGlowItemFrameInitializerCodec(EntityType.Builder<GlowItemFrameEntity> type) {
        type.itematic$initializerCodec(decorationInitializerCodecCreator(GlowItemFrameEntity::new));
        return type;
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
    private static EntityType.Builder<ArrowEntity> setArrowInitializerCodec(EntityType.Builder<ArrowEntity> type) {
        type.itematic$initializerCodec(entityType -> PersistentProjectileEntityInitializer.createCodec(entityType, ArrowEntity::new, ArrowEntity::new));
        return type;
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
    private static EntityType.Builder<SpectralArrowEntity> setSpectralArrowInitializerCodec(EntityType.Builder<SpectralArrowEntity> type) {
        type.itematic$initializerCodec(entityType -> PersistentProjectileEntityInitializer.createCodec(entityType, SpectralArrowEntity::new, SpectralArrowEntity::new));
        return type;
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
    private static EntityType.Builder<TridentEntity> setTridentInitializerCodec(EntityType.Builder<TridentEntity> type) {
        type.itematic$initializerCodec(TridentEntityInitializer.CODEC);
        return type;
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
    private static EntityType.Builder<FireworkRocketEntity> setFireworkRocketInitializerCodec(EntityType.Builder<FireworkRocketEntity> type) {
        type.itematic$initializerCodec(FireworkRocketEntityInitializer.CODEC);
        return type;
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
    private static EntityType.Builder<EyeOfEnderEntity> setEyeOfEnderInitializerCodec(EntityType.Builder<EyeOfEnderEntity> type) {
        type.itematic$initializerCodec(EyeOfEnderEntityInitializer.CODEC);
        return type;
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
    private static EntityType.Builder<SmallFireballEntity> setSmallFireballInitializerCodec(EntityType.Builder<SmallFireballEntity> type) {
        type.itematic$initializerCodec(SmallFireballEntityInitializer.CODEC);
        return type;
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
    private static EntityType.Builder<WindChargeEntity> setWindChargeInitializerCodec(EntityType.Builder<WindChargeEntity> type) {
        type.itematic$initializerCodec(WindChargeEntityInitializer.CODEC);
        return type;
    }

    @Inject(
        method = "create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType$EntityFactory;create(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;"
        ),
        cancellable = true
    )
    private void createUseEntityInitializerIfPresent(World world, SpawnReason reason, CallbackInfoReturnable<T> info) {
        if (this.initializer == null) {
            return;
        }

        EntityInitializer<?> initializer = this.initializer; // Copy to a local and set the field to null, so we don't get a StackOverflowError
        this.initializer = null;
        info.setReturnValue((T) initializer.create(this.actionContext));
        this.actionContext = null;
    }

    @Override
    public MapCodec<? extends EntityInitializer<?>> itematic$initializerCodec() {
        return this.initializerCodec;
    }

    @Override
    public void itematic$setInitializerCodec(MapCodec<? extends EntityInitializer<?>> initializerCodec) {
        this.initializerCodec = initializerCodec;
    }

    @Override
    public void itematic$setInitializer(EntityInitializer<?> initializer, ActionContext actionContext) {
        this.initializer = initializer;
        this.actionContext = actionContext;
    }

    @Unique
    private static <T extends AbstractMinecartEntity> EntityInitializerCodecCreator<T> minecartInitializerCodecCreator(MinecartEntityInitializer.Creator<T> creator) {
        return type -> MinecartEntityInitializer.createCodec(type, creator);
    }

    @Unique
    private static <T extends ItemFrameEntity> EntityInitializerCodecCreator<T> decorationInitializerCodecCreator(DecorationEntityInitializer.Creator<T> creator) {
        return type -> MapCodec.unit(DecorationEntityInitializer.createItemFrame(type, creator));
    }

    @Mixin(EntityType.Builder.class)
    public static class BuilderExtender<T extends Entity> implements EntityTypeBuilderAccess<T> {
        @Unique
        private EntityInitializerCodecCreator<T> creator;

        @ModifyReturnValue(
            method = "build",
            at = @At("TAIL")
        )
        private EntityType<T> buildSetInitializerCodec(EntityType<T> original) {
            original.itematic$setInitializerCodec(this.creator == null ? SimpleEntityInitializer.createCodec(original) : this.creator.create(original));
            return original;
        }

        @Override
        public void itematic$initializerCodec(MapCodec<? extends EntityInitializer<T>> codec) {
            this.creator = type -> codec;
        }

        @Override
        public void itematic$initializerCodec(EntityInitializerCodecCreator<T> creator) {
            this.creator = creator;
        }
    }
}
