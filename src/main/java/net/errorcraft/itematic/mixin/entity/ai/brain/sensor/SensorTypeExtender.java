package net.errorcraft.itematic.mixin.entity.ai.brain.sensor;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.sensor.TemptationsSensor;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SensorType.class)
public class SensorTypeExtender {
    @ModifyReturnValue(
        method = "method_33212",
        at = @At("TAIL")
    )
    private static TemptationsSensor temptationsSensorForAxolotlSetTemptations(TemptationsSensor original) {
        original.itematic$setTemptations(ItemTags.AXOLOTL_TEMPT_ITEMS);
        return original;
    }

    @ModifyReturnValue(
        method = "method_35153",
        at = @At("TAIL")
    )
    private static TemptationsSensor temptationsSensorForGoatSetTemptations(TemptationsSensor original) {
        original.itematic$setTemptations(ItematicItemTags.GOAT_TEMPT_ITEMS);
        return original;
    }

    @ModifyReturnValue(
        method = "method_41351",
        at = @At("TAIL")
    )
    private static TemptationsSensor temptationsSensorForFrogSetTemptations(TemptationsSensor original) {
        original.itematic$setTemptations(ItematicItemTags.FROG_TEMPT_ITEMS);
        return original;
    }

    @ModifyReturnValue(
        method = "method_45338",
        at = @At("TAIL")
    )
    private static TemptationsSensor temptationsSensorForCamelSetTemptations(TemptationsSensor original) {
        original.itematic$setTemptations(ItematicItemTags.CAMEL_TEMPT_ITEMS);
        return original;
    }

    @ModifyReturnValue(
        method = "method_55706",
        at = @At("TAIL")
    )
    private static TemptationsSensor temptationsSensorForArmadilloSetTemptations(TemptationsSensor original) {
        original.itematic$setTemptations(ItematicItemTags.ARMADILLO_TEMPT_ITEMS);
        return original;
    }

    @ModifyReturnValue(
        method = "method_51153",
        at = @At("TAIL")
    )
    private static TemptationsSensor temptationsSensorForSnifferSetTemptations(TemptationsSensor original) {
        original.itematic$setTemptations(ItematicItemTags.SNIFFER_TEMPT_ITEMS);
        return original;
    }
}
