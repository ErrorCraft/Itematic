package net.errorcraft.itematic.mixin.world.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AgeableMob.class)
public class AgeableMobExtender extends PathfinderMob {
    protected AgeableMobExtender(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = "mobInteract",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/Items;GOLDEN_DANDELION:Lnet/minecraft/world/item/Item;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private Item getGoldenDandelionUseDynamicRegistry(Operation<Item> original) {
        return this.level().itematic$getItem(ItemIds.GOLDEN_DANDELION).value();
    }
}
