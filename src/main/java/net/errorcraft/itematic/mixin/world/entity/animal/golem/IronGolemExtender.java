package net.errorcraft.itematic.mixin.world.entity.animal.golem;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(IronGolem.class)
public abstract class IronGolemExtender extends MobExtender {
    protected IronGolemExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "getAttackDamage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/golem/IronGolem;getAttributeValue(Lnet/minecraft/core/Holder;)D"
        )
    )
    private double useCustomAttackDamage(IronGolem instance, Holder<Attribute> attribute) {
        return this.itematic$getAttackDamage();
    }

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isIronIngotCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.IRON_INGOT);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.IRON_GOLEM_SPAWN_EGG;
    }
}
