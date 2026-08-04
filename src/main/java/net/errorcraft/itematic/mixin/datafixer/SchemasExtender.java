package net.errorcraft.itematic.mixin.datafixer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import net.errorcraft.itematic.util.datafix.fixes.ImmuneToDamageToDamageResistantComponentFix;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.FireResistantToDamageResistantComponentFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DataFixers.class)
public class SchemasExtender {
    @WrapOperation(
        method = "addFixers",
        at = @At(
            value = "NEW",
            target = "(Lcom/mojang/datafixers/schemas/Schema;)Lnet/minecraft/util/datafix/fixes/FireResistantToDamageResistantComponentFix;"
        )
    )
    private static FireResistantToDamageResistantComponentFix immuneToDamageToDamageResistantComponentFix(Schema outputSchema, Operation<FireResistantToDamageResistantComponentFix> original, DataFixerBuilder builder) {
        builder.addFixer(new ImmuneToDamageToDamageResistantComponentFix(outputSchema));
        return original.call(outputSchema);
    }
}
