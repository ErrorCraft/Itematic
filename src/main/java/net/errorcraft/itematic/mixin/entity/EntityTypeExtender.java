package net.errorcraft.itematic.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.access.entity.EntityTypeAccess;
import net.errorcraft.itematic.access.entity.EntityTypeBuilderAccess;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.entity.initializer.EntityInitializerCodecCreator;
import net.errorcraft.itematic.entity.initializer.initializers.*;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.MutableActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.*;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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
    private Codec<? extends EntityInitializer<?>> initializerCodec;
    private EntityInitializer<?> initializer;
    private Direction initializerSide;
    private ActionContext initializerContext;

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
        type.initializerCodec(BoatEntityInitializer.CODEC);
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
        type.initializerCodec(ChestBoatEntityInitializer.CODEC);
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
        type.initializerCodec(minecartInitializerCodecCreator(MinecartEntity::new));
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
        type.initializerCodec(minecartInitializerCodecCreator(ChestMinecartEntity::new));
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
        type.initializerCodec(minecartInitializerCodecCreator(FurnaceMinecartEntity::new));
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
        type.initializerCodec(minecartInitializerCodecCreator(TntMinecartEntity::new));
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
        type.initializerCodec(minecartInitializerCodecCreator(SpawnerMinecartEntity::new));
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
        type.initializerCodec(minecartInitializerCodecCreator(HopperMinecartEntity::new));
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
        type.initializerCodec(minecartInitializerCodecCreator(CommandBlockMinecartEntity::new));
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
        type.initializerCodec(EndCrystalEntityInitializer.CODEC);
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
        type.initializerCodec(ArmorStandEntityInitializer.CODEC);
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
        type.initializerCodec(entityType -> Codec.unit(DecorationEntityInitializer.createPainting(entityType)));
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
        type.initializerCodec(decorationInitializerCodecCreator(ItemFrameEntity::new));
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
        type.initializerCodec(decorationInitializerCodecCreator(GlowItemFrameEntity::new));
        return type;
    }

    @Inject(
        method = "spawnFromItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getNbt()Lnet/minecraft/nbt/NbtCompound;"
        )
    )
    private void createEntityInitializerContext(ServerWorld world, ItemStack stack, @Nullable PlayerEntity player, BlockPos pos, SpawnReason spawnReason, boolean alignPosition, boolean invertY, CallbackInfoReturnable<@Nullable T> info) {
        if (this.initializer != null) {
            this.initializerContext = MutableActionContext.stackUsage(world, stack)
                .entityPosition(ActionContextParameter.THIS, player)
                .position(ActionContextParameter.TARGET, pos)
                .side(this.initializerSide);
        }
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
        info.setReturnValue((T) initializer.create(this.initializerContext));
        this.initializerContext = null;
    }

    @Override
    public Codec<? extends EntityInitializer<?>> initializerCodec() {
        return this.initializerCodec;
    }

    @Override
    public void setInitializerCodec(Codec<? extends EntityInitializer<?>> initializerCodec) {
        this.initializerCodec = initializerCodec;
    }

    @Override
    public void setInitializer(EntityInitializer<?> initializer, Direction initializerSide) {
        this.initializer = initializer;
        this.initializerSide = initializerSide;
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
        private EntityInitializerCodecCreator<T> creator;

        @ModifyReturnValue(
            method = "build",
            at = @At("TAIL")
        )
        private EntityType<T> buildSetInitializerCodec(EntityType<T> original) {
            original.setInitializerCodec(this.creator == null ? SimpleEntityInitializer.createCodec(original) : this.creator.create(original));
            return original;
        }

        @Override
        public void initializerCodec(Codec<EntityInitializer<T>> codec) {
            this.creator = type -> codec;
        }

        @Override
        public void initializerCodec(EntityInitializerCodecCreator<T> creator) {
            this.creator = creator;
        }
    }
}
