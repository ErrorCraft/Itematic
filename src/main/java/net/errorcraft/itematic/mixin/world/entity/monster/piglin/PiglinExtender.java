package net.errorcraft.itematic.mixin.world.entity.monster.piglin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.MeleeWeaponComponents;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Piglin.class)
public abstract class PiglinExtender extends MobExtender {
    public PiglinExtender(EntityType<? extends AbstractPiglin> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "createSpawnWeapon",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForCrossbowUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.CROSSBOW);
    }

    @ModifyExpressionValue(
        method = "createSpawnWeapon",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"
        )
    )
    private int storeSpearChance(int original, @Share("spearChance") LocalIntRef spearChance) {
        spearChance.set(original);
        return original;
    }

    @Redirect(
        method = "createSpawnWeapon",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;CROSSBOW:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForGoldenWeaponUseCreateStack(ItemLike item, @Share("spearChance") LocalIntRef spearChance) {
        return this.level().itematic$createStack(
            spearChance.get() == 0
                ? ItemIds.GOLDEN_SPEAR
                : ItemIds.GOLDEN_SWORD
        );
    }

    @Redirect(
        method = "getArmPose",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/piglin/Piglin;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingCrossbowCheckId(Piglin instance, Item item) {
        return instance.itematic$isHolding(ItemIds.CROSSBOW);
    }

    @WrapMethod(
        method = "canUseNonMeleeWeapon"
    )
    private boolean useItemBehaviorComponent(ItemStack item, Operation<Boolean> original) {
        if (item.itematic$getBehavior(ItemBehaviorType.SHOOTER)
            .map(shooter -> shooter.usesMethod(ShooterMethodType.CHARGEABLE))
            .orElse(false)) {
            return true;
        }

        return item.itematic$getBehavior(ItemBehaviorType.WEAPON)
            .map(weapon -> weapon.has(MeleeWeaponComponents.KINETIC))
            .orElse(false);
    }

    @Redirect(
        method = "holdInOffHand",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isGoldIngotCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.GOLD_INGOT);
    }

    @Redirect(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EquipmentSlot;HEAD:Lnet/minecraft/world/entity/EquipmentSlot;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForGoldenHelmetUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.GOLDEN_HELMET);
    }

    @Redirect(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EquipmentSlot;CHEST:Lnet/minecraft/world/entity/EquipmentSlot;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForGoldenChestplateUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.GOLDEN_CHESTPLATE);
    }

    @Redirect(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EquipmentSlot;LEGS:Lnet/minecraft/world/entity/EquipmentSlot;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForGoldenLeggingsUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.GOLDEN_LEGGINGS);
    }

    @Redirect(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/EquipmentSlot;FEET:Lnet/minecraft/world/entity/EquipmentSlot;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForGoldenBootsUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.GOLDEN_BOOTS);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.PIGLIN_SPAWN_EGG;
    }
}
