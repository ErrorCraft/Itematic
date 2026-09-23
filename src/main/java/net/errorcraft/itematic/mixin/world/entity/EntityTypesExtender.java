package net.errorcraft.itematic.mixin.world.entity;

import net.errorcraft.itematic.world.entity.initializer.initializers.ArrowEntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.initializers.EndCrystalEntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.initializers.EyeOfEnderEntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.initializers.FireworkRocketEntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.initializers.HangingEntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.initializers.MinecartEntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.initializers.ThrownBallEntityInitializer;
import net.errorcraft.itematic.world.entity.initializer.initializers.ThrownTridentEntityInitializer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
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
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(EntityTypes.class)
public class EntityTypesExtender {
    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;MINECART:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;CHEST_MINECART:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;FURNACE_MINECART:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;TNT_MINECART:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;SPAWNER_MINECART:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;HOPPER_MINECART:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;COMMAND_BLOCK_MINECART:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;END_CRYSTAL:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;PAINTING:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;ITEM_FRAME:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;GLOW_ITEM_FRAME:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;ARROW:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;SPECTRAL_ARROW:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;TRIDENT:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;FIREWORK_ROCKET:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;EYE_OF_ENDER:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;SMALL_FIREBALL:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
            target = "Lnet/minecraft/world/entity/EntityTypes;register(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EntityTypeIds;WIND_CHARGE:Lnet/minecraft/resources/ResourceKey;",
                opcode = Opcodes.GETSTATIC
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
}
