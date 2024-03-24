package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PandaEntity.class)
public abstract class PandaEntityExtender extends MobEntityExtender {
    protected PandaEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "method_6504",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private static boolean isOfForBambooUseItemTagCheck(ItemStack instance, Item item) {
        return instance.isIn(ItemTags.PANDA_FOOD);
    }

    @Redirect(
        method = "canEat",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForCakeUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.CAKE);
    }

    @Redirect(
        method = { "method_6504" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 1
        )
    )
    private static boolean isOfForCakeUseRegistryKeyCheckStatic(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.CAKE);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.PANDA_SPAWN_EGG;
    }
}
