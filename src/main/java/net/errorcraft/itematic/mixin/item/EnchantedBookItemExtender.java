package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EnchantmentHolderItemComponent;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookItemExtender {
    @Inject(
        method = "addEnchantment",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void addEnchantmentStoreItemComponentVariable(ItemStack stack, EnchantmentLevelEntry entry, CallbackInfo info, @Share("enchantmentHolder") LocalRef<EnchantmentHolderItemComponent> enchantmentHolderReference) {
        Optional<EnchantmentHolderItemComponent> enchantmentHolder = stack.getComponent(ItemComponentTypes.ENCHANTMENT_HOLDER);
        if (enchantmentHolder.isEmpty()) {
            info.cancel();
            return;
        }
        enchantmentHolderReference.set(enchantmentHolder.get());
    }

    @Redirect(
        method = "addEnchantment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/EnchantedBookItem;getEnchantmentNbt(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/nbt/NbtList;"
        )
    )
    private static NbtList addEnchantmentGetEnchantmentNbtUseItemComponent(ItemStack stack, @Share("enchantmentHolder") LocalRef<EnchantmentHolderItemComponent> enchantmentHolderReference) {
        return enchantmentHolderReference.get().getEnchantments(stack);
    }

    @ModifyArg(
        method = "addEnchantment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/nbt/NbtCompound;put(Ljava/lang/String;Lnet/minecraft/nbt/NbtElement;)Lnet/minecraft/nbt/NbtElement;"
        )
    )
    private static String addEnchantmentPutUseItemComponent(String key, @Share("enchantmentHolder") LocalRef<EnchantmentHolderItemComponent> enchantmentHolderReference) {
        return enchantmentHolderReference.get().enchantmentsKey();
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public static NbtList getEnchantmentNbt(ItemStack stack) {
        return stack.getComponent(ItemComponentTypes.ENCHANTMENT_HOLDER)
            .map(c -> c.getEnchantments(stack))
            .orElse(new NbtList());
    }
}
