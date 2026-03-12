package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.access.entity.mob.MobEntityAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PiglinEntity.class)
public abstract class PiglinEntityExtender extends MobEntityExtender implements MobEntityAccess {
    public PiglinEntityExtender(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "dropEquipment",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForPiglinHeadUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.PIGLIN_HEAD);
    }

    @Redirect(
        method = "makeInitialWeapon",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForCrossbowUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.CROSSBOW);
    }

    @Redirect(
        method = "makeInitialWeapon",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;CROSSBOW:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForGoldenSwordUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.GOLDEN_SWORD);
    }

    @Redirect(
        method = "getActivity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/mob/PiglinEntity;isHolding(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isHoldingForCrossbowUseRegistryKeyCheck(PiglinEntity instance, Item item) {
        return instance.itematic$isHolding(ItemKeys.CROSSBOW);
    }

    @Redirect(
        method = "equipToOffHand",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForGoldIngotUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.GOLD_INGOT);
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
                target = "Lnet/minecraft/entity/EquipmentSlot;HEAD:Lnet/minecraft/entity/EquipmentSlot;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForGoldenHelmetUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.GOLDEN_HELMET);
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
                target = "Lnet/minecraft/entity/EquipmentSlot;CHEST:Lnet/minecraft/entity/EquipmentSlot;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForGoldenChestplateUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.GOLDEN_CHESTPLATE);
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
                target = "Lnet/minecraft/entity/EquipmentSlot;LEGS:Lnet/minecraft/entity/EquipmentSlot;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForGoldenLeggingsUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.GOLDEN_LEGGINGS);
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
                target = "Lnet/minecraft/entity/EquipmentSlot;FEET:Lnet/minecraft/entity/EquipmentSlot;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForGoldenBootsUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.GOLDEN_BOOTS);
    }

    @Override
    public boolean itematic$canUseShooter(ItemStack stack, ShooterItemComponent component) {
        return stack.itematic$isOf(ItemKeys.CROSSBOW);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.PIGLIN_SPAWN_EGG;
    }
}
