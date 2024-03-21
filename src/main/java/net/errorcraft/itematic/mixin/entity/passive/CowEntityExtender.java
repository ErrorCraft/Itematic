package net.errorcraft.itematic.mixin.entity.passive;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CowEntity.class)
public abstract class CowEntityExtender extends MobEntityExtender {
    protected CowEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(
        method = "initGoals",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/entity/mob/PathAwareEntity;DLnet/minecraft/recipe/Ingredient;Z)Lnet/minecraft/entity/ai/goal/TemptGoal;"
        )
    )
    private TemptGoal newTemptGoalSetItems(TemptGoal original) {
        original.itematic$setItems(ItematicItemTags.COW_TEMPT_ITEMS);
        return original;
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.COW_SPAWN_EGG;
    }
}
