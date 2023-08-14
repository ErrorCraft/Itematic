package net.errorcraft.itematic.mixin.loot;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import net.errorcraft.itematic.loot.LootManagerUtil;
import net.minecraft.loot.LootDataType;
import net.minecraft.registry.RegistryOps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LootDataType.class)
public class LootDataTypeExtender {
    @ModifyArg(
        method = "parse",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            remap = false
        )
    )
    private DynamicOps<JsonElement> useRegistryOps(DynamicOps<JsonElement> ops) {
        return RegistryOps.of(ops, LootManagerUtil.getRegistryManager());
    }
}
