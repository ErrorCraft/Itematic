package net.errorcraft.itematic.mixin.core;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MappedRegistry.class)
public class MappedRegistryExtender<T> {
    @Inject(
        method = "register(Lnet/minecraft/resources/ResourceKey;Ljava/lang/Object;Lnet/minecraft/core/RegistrationInfo;)Lnet/minecraft/core/Holder$Reference;",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/ObjectList;add(Ljava/lang/Object;)Z"
        )
    )
    private void setRawId(ResourceKey<T> key, T value, RegistrationInfo registrationInfo, CallbackInfoReturnable<Holder.Reference<T>> info, @Local(name = "holder") Holder.Reference<T> holder, @Local(name = "newId") int newId) {
        holder.itematic$setRawId(newId);
    }
}
