package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.block.SuspiciousStewIngredient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(MooshroomEntity.class)
public abstract class MooshroomEntityExtender extends AnimalEntity {
    public MooshroomEntityExtender(EntityType<? extends MooshroomEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isOfForBowlUseRegistryEntryCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BOWL);
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForSuspiciousStewUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.SUSPICIOUS_STEW);
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;SUSPICIOUS_STEW:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForMushroomStewUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.MUSHROOM_STEW);
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;SHEARS:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForShearsUseRegistryEntryCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.SHEARS);
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"
        )
    )
    private boolean isInForSmallFlowersUseItemComponentCheck(ItemStack instance, TagKey<Item> tag) {
        return instance.itematic$hasComponent(ItemComponentTypes.SUSPICIOUS_EFFECT_INGREDIENT);
    }

    @Redirect(
        method = "getStewEffectFrom",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/SuspiciousStewIngredient;of(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/block/SuspiciousStewIngredient;"
        )
    )
    private SuspiciousStewIngredient suspiciousStewEffectsUseItemComponent(ItemConvertible item, @Local(argsOnly = true) ItemStack stack) {
        return stack.itematic$getComponent(ItemComponentTypes.SUSPICIOUS_EFFECT_INGREDIENT)
            .orElse(null);
    }
}
