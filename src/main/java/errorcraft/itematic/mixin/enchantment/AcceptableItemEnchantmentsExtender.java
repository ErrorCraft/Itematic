package errorcraft.itematic.mixin.enchantment;

import errorcraft.itematic.item.component.ItemComponentTypes;
import errorcraft.itematic.item.component.components.ForgeableItemComponent;
import net.minecraft.enchantment.*;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin({ Enchantment.class, BindingCurseEnchantment.class, DamageEnchantment.class, EfficiencyEnchantment.class, ThornsEnchantment.class, UnbreakingEnchantment.class })
public class AcceptableItemEnchantmentsExtender {
    @Inject(
        method = "isAcceptableItem",
        at = @At("HEAD"),
        cancellable = true
    )
    public void isAcceptableItemUseItemComponent(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        Optional<ForgeableItemComponent> component = stack.getComponent(ItemComponentTypes.FORGEABLE);
        if (component.isEmpty()) {
            info.setReturnValue(false);
            return;
        }
        RegistryEntry<Enchantment> entry = Registries.ENCHANTMENT.getEntry((Enchantment) (Object)this);
        info.setReturnValue(component.get().enchantments().map(entry::isIn).orElse(true));
    }
}
