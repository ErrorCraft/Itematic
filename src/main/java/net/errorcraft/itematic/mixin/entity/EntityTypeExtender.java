package net.errorcraft.itematic.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.access.entity.EntityTypeAccess;
import net.errorcraft.itematic.access.entity.EntityTypeBuilderAccess;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.entity.initializer.EntityInitializerCodecCreator;
import net.errorcraft.itematic.entity.initializer.initializers.*;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.vehicle.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
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
    private Codec<? extends EntityInitializer<?>> initializerCodec;
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
    private static EntityType.Builder<?> setBoatEntityInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setChestBoatEntityInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setMinecartInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setChestMinecartInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setFurnaceMinecartInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setTntMinecartInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setSpawnerMinecartInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setHopperMinecartInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setCommandBlockMinecartInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setEndCrystalInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setArmorStandInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setPaintingInitializerCodec(EntityType.Builder<?> type) {
        type.itematic$initializerCodec(entityType -> Codec.unit(DecorationEntityInitializer.createPainting(entityType)));
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
    private static EntityType.Builder<?> setItemFrameInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setGlowItemFrameInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setArrowInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setSpectralArrowInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setTridentInitializerCodec(EntityType.Builder<?> type) {
        type.itematic$initializerCodec(entityType -> PersistentProjectileEntityInitializer.createCodec(entityType, TridentEntity::new, (world, x, y, z, stack) -> null));
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
    private static EntityType.Builder<?> setFireworkRocketInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setEyeOfEnderInitializerCodec(EntityType.Builder<?> type) {
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
    private static EntityType.Builder<?> setSmallFireballInitializerCodec(EntityType.Builder<?> type) {
        type.itematic$initializerCodec(SmallFireballEntityInitializer.CODEC);
        return type;
    }

    @Inject(
        method = "create(Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType$EntityFactory;create(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;"
        ),
        cancellable = true
    )
    private void createUseEntityInitializerIfPresent(World world, CallbackInfoReturnable<@Nullable T> info) {
        if (this.initializer == null) {
            return;
        }
        EntityInitializer<?> initializer = this.initializer; // Copy to a local and set the field to null, so we don't get a StackOverflowError
        this.initializer = null;
        info.setReturnValue((T) initializer.create(this.actionContext));
        this.actionContext = null;
    }

    @Override
    public Codec<? extends EntityInitializer<?>> itematic$initializerCodec() {
        return this.initializerCodec;
    }

    @Override
    public void itematic$setInitializerCodec(Codec<? extends EntityInitializer<?>> initializerCodec) {
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
        return type -> Codec.unit(DecorationEntityInitializer.createItemFrame(type, creator));
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
        public void itematic$initializerCodec(Codec<EntityInitializer<T>> codec) {
            this.creator = type -> codec;
        }

        @Override
        public void itematic$initializerCodec(EntityInitializerCodecCreator<T> creator) {
            this.creator = creator;
        }
    }
}
