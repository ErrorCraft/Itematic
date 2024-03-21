package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CatEntity.class)
public abstract class CatEntityExtender extends MobEntityExtender {
    @Shadow
    private TemptGoal temptGoal;

    protected CatEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "isBreedingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean testForFoodItemsUseItemTagCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isIn(ItematicItemTags.CAT_FOOD);
    }

    @Inject(
        method = "initGoals",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/passive/CatEntity;temptGoal:Lnet/minecraft/entity/ai/goal/TemptGoal;",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        )
    )
    private void newTemptGoalSetItems(CallbackInfo info) {
        this.temptGoal.itematic$setItems(ItematicItemTags.CAT_TEMPT_ITEMS);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.CAT_SPAWN_EGG;
    }
}
