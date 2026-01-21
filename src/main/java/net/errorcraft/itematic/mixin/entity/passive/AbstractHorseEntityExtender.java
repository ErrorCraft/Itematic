package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityExtender extends AnimalEntity {
    @Shadow
    public abstract boolean isTame();

    protected AbstractHorseEntityExtender(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "receiveFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isOfForWheatUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.WHEAT);
    }

    @Redirect(
        method = "receiveFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;SUGAR:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForSugarUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.SUGAR);
    }

    @Redirect(
        method = "receiveFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/block/Blocks;HAY_BLOCK:Lnet/minecraft/block/Block;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForHayBlockUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.HAY_BLOCK);
    }

    @Redirect(
        method = "receiveFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;APPLE:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForAppleUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.APPLE);
    }

    @Redirect(
        method = "receiveFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;GOLDEN_CARROT:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForGoldenCarrotUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.GOLDEN_CARROT);
    }

    @Redirect(
        method = "receiveFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;GOLDEN_APPLE:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForGoldenAppleUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.GOLDEN_APPLE);
    }

    @Redirect(
        method = "receiveFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;ENCHANTED_GOLDEN_APPLE:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForEnchantedGoldenAppleUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.ENCHANTED_GOLDEN_APPLE);
    }

    @Redirect(
        method = "readCustomDataFromNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForSaddleUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.SADDLE);
    }

    @Override
    public boolean canEquip(ItemStack stack) {
        return this.isTame() && this.canUseSlot(EquipmentSlot.BODY) && this.isHorseArmor(stack) && !this.isWearingBodyArmor();
    }

    @Mixin(targets = "net/minecraft/entity/passive/AbstractHorseEntity$2")
    public static class SaddleStackReferenceExtender {
        @Redirect(
            method = "set",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
            )
        )
        private boolean isOfForSaddleUseRegistryKeyCheck(ItemStack instance, Item item) {
            return instance.itematic$isOf(ItemKeys.SADDLE);
        }
    }
}
