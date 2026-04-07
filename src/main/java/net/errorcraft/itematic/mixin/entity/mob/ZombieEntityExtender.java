package net.errorcraft.itematic.mixin.entity.mob;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityExtender extends MobEntityExtender {
    protected ZombieEntityExtender(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(
        method = "initialize",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/math/random/Random;nextFloat()F",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.25"
            )
        )
    )
    private float storeItemChance(float original, @Share("jackOLanternChance") LocalFloatRef jackOLanternChance) {
        jackOLanternChance.set(original);
        return original;
    }

    @Redirect(
        method = "initialize",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemConvertible item, ServerWorldAccess world, @Share("jackOLanternChance") LocalFloatRef jackOLanternChance) {
        if (jackOLanternChance.get() < 0.1f) {
            return world.itematic$createStack(ItemKeys.JACK_O_LANTERN);
        }
        return world.itematic$createStack(ItemKeys.CARVED_PUMPKIN);
    }

    @Redirect(
        method = "getSkull",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForZombieHeadUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.ZOMBIE_HEAD);
    }

    @Redirect(
        method = "canGather",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForGlowInkSacUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.GLOW_INK_SAC);
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForIronSwordUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.IRON_SWORD);
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;IRON_SWORD:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForIronShovelUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.IRON_SHOVEL);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.ZOMBIE_SPAWN_EGG;
    }
}
