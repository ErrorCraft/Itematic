package net.errorcraft.itematic.mixin.world.entity.animal.cow;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(MushroomCow.class)
public abstract class MushroomCowExtender extends MobExtender {
    public MushroomCowExtender(EntityType<? extends MushroomCow> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        )
    )
    private boolean isBowlCheckId(ItemStack instance, Object object, Operation<Boolean> original) {
        return instance.is(ItemIds.BOWL);
    }

    @WrapOperation(
        method = "mobInteract",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForSuspiciousStewUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.SUSPICIOUS_STEW);
    }

    @WrapOperation(
        method = "mobInteract",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
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
    private ItemStack newItemStackForMushroomStewUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.MUSHROOM_STEW);
    }

    @WrapOperation(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
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
    private boolean isShearsCheckId(ItemStack instance, Object object, Operation<Boolean> original) {
        return instance.is(ItemIds.SHEARS);
    }

    @WrapOperation(
        method = "getEffectsFromItemStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/SuspiciousEffectHolder;tryGet(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/block/SuspiciousEffectHolder;"
        )
    )
    @Nullable
    private SuspiciousEffectHolder getSuspiciousStewEffectsUseDataComponent(ItemLike item, Operation<SuspiciousEffectHolder> original, ItemStack itemStack) {
        return itemStack.get(ItematicDataComponents.PROVIDES_SUSPICIOUS_STEW_EFFECTS);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.MOOSHROOM_SPAWN_EGG;
    }
}
