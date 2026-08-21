package net.errorcraft.itematic.mixin.world.entity.monster.zombie;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Zombie.class)
public abstract class ZombieExtender extends MobExtender {
    protected ZombieExtender(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @ModifyExpressionValue(
        method = "finalizeSpawn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/RandomSource;nextFloat()F",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.25"
            )
        )
    )
    private float storeItemChance(float original, @Share("jackOLanternChance") LocalFloatRef jackOLanternChance) {
        jackOLanternChance.set(original);
        return original;
    }

    @Redirect(
        method = "finalizeSpawn",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemLike item, ServerLevelAccessor level, @Share("jackOLanternChance") LocalFloatRef jackOLanternChance) {
        if (jackOLanternChance.get() < 0.1f) {
            return level.itematic$createStack(ItemIds.JACK_O_LANTERN);
        }

        return level.itematic$createStack(ItemIds.CARVED_PUMPKIN);
    }

    @Redirect(
        method = "wantsToPickUp",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isGlowInkSacCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.GLOW_INK_SAC);
    }

    @Redirect(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForIronSwordUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.IRON_SWORD);
    }

    @Redirect(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;IRON_SWORD:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForIronSpearUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.IRON_SPEAR);
    }

    @Redirect(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;IRON_SPEAR:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForIronShovelUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.IRON_SHOVEL);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.ZOMBIE_SPAWN_EGG;
    }
}
