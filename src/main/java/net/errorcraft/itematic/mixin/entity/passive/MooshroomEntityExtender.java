package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(MushroomCow.class)
public abstract class MooshroomEntityExtender extends MobEntityExtender {
    public MooshroomEntityExtender(EntityType<? extends MushroomCow> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isOfForBowlUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BOWL);
    }

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForSuspiciousStewUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemKeys.SUSPICIOUS_STEW);
    }

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;SUSPICIOUS_STEW:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForMushroomStewUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemKeys.MUSHROOM_STEW);
    }

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;SHEARS:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForShearsUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.SHEARS);
    }

    @Redirect(
        method = "getEffectsFromItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/SuspiciousEffectHolder;tryGet(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/block/SuspiciousEffectHolder;"
        )
    )
    private SuspiciousEffectHolder suspiciousStewEffectsUseItemComponent(ItemLike item, @Local(argsOnly = true) ItemStack stack) {
        return stack.itematic$getBehavior(ItemComponentTypes.SUSPICIOUS_EFFECT_INGREDIENT)
            .orElse(null);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemKeys.MOOSHROOM_SPAWN_EGG;
    }
}
