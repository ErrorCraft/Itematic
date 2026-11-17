package net.errorcraft.itematic.mixin.world.entity.projectile;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.component.Castable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Optional;

@Mixin(FishingHook.class)
public class FishingHookExtender {
    @WrapOperation(
        method = "shouldStopFishing",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isFishingRodCheckCastableDataComponent(ItemStack instance, Object object, Operation<Boolean> original) {
        return instance.has(ItematicDataComponents.CASTABLE);
    }

    @WrapOperation(
        method = "retrieve",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/level/storage/loot/BuiltInLootTables;FISHING:Lnet/minecraft/resources/ResourceKey;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private ResourceKey<LootTable> useDataComponent(Operation<ResourceKey<LootTable>> original) {
        return Castable.CASTABLE.get().fishLootTable();
    }

    @ModifyArg(
        method = "retrieve",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ExperienceOrb;<init>(Lnet/minecraft/world/level/Level;DDDI)V"
        )
    )
    private int useDataComponent(int value, @Local(name = "params") LootParams params) {
        return Castable.CASTABLE.get()
            .fishExperience()
            .get(new LootContext.Builder(params).create(Optional.empty()), 0);
    }
}
