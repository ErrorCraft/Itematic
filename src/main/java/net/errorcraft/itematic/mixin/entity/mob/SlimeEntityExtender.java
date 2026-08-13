package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Slime.class)
public abstract class SlimeEntityExtender extends MobEntityExtender {
    protected SlimeEntityExtender(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getAttackDamage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/Slime;getAttributeValue(Lnet/minecraft/core/Holder;)D"
        )
    )
    private double useCustomAttackDamage(Slime instance, Holder<Attribute> attribute) {
        return this.itematic$getAttackDamage();
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.SLIME_SPAWN_EGG;
    }
}
