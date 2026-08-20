package net.errorcraft.itematic.mixin.world.entity;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.errorcraft.itematic.access.world.entity.MobAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.SpawnEggItemBehavior;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Map;
import java.util.Optional;

@Mixin(Mob.class)
public abstract class MobExtender extends LivingEntity implements MobAccess {
    @Shadow
    public abstract void setBaby(boolean baby);

    @Unique
    private static final Int2ObjectMap<Map<EquipmentSlot, ResourceKey<Item>>> LEVEL_TO_EQUIPMENT = Util.make(new Int2ObjectOpenHashMap<>(), map -> {
        map.defaultReturnValue(Map.of());
        map.put(0, Map.of(
            EquipmentSlot.HEAD, ItemIds.LEATHER_HELMET,
            EquipmentSlot.CHEST, ItemIds.LEATHER_CHESTPLATE,
            EquipmentSlot.LEGS, ItemIds.LEATHER_LEGGINGS,
            EquipmentSlot.FEET, ItemIds.LEATHER_BOOTS
        ));
        map.put(1, Map.of(
            EquipmentSlot.HEAD, ItemIds.COPPER_HELMET,
            EquipmentSlot.CHEST, ItemIds.COPPER_CHESTPLATE,
            EquipmentSlot.LEGS, ItemIds.COPPER_LEGGINGS,
            EquipmentSlot.FEET, ItemIds.COPPER_BOOTS
        ));
        map.put(2, Map.of(
            EquipmentSlot.HEAD, ItemIds.GOLDEN_HELMET,
            EquipmentSlot.CHEST, ItemIds.GOLDEN_CHESTPLATE,
            EquipmentSlot.LEGS, ItemIds.GOLDEN_LEGGINGS,
            EquipmentSlot.FEET, ItemIds.GOLDEN_BOOTS
        ));
        map.put(3, Map.of(
            EquipmentSlot.HEAD, ItemIds.CHAINMAIL_HELMET,
            EquipmentSlot.CHEST, ItemIds.CHAINMAIL_CHESTPLATE,
            EquipmentSlot.LEGS, ItemIds.CHAINMAIL_LEGGINGS,
            EquipmentSlot.FEET, ItemIds.CHAINMAIL_BOOTS
        ));
        map.put(4, Map.of(
            EquipmentSlot.HEAD, ItemIds.IRON_HELMET,
            EquipmentSlot.CHEST, ItemIds.IRON_CHESTPLATE,
            EquipmentSlot.LEGS, ItemIds.IRON_LEGGINGS,
            EquipmentSlot.FEET, ItemIds.IRON_BOOTS
        ));
        map.put(5, Map.of(
            EquipmentSlot.HEAD, ItemIds.DIAMOND_HELMET,
            EquipmentSlot.CHEST, ItemIds.DIAMOND_CHESTPLATE,
            EquipmentSlot.LEGS, ItemIds.DIAMOND_LEGGINGS,
            EquipmentSlot.FEET, ItemIds.DIAMOND_BOOTS
        ));
    });

    protected MobExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "checkAndHandleImportantInteractions",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"
        )
    )
    @Nullable
    private Item getItemUseNull(ItemStack instance) {
        return null;
    }

    @ModifyConstant(
        method = "checkAndHandleImportantInteractions",
        constant = @Constant(
            classValue = SpawnEggItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfSpawnEggItemUseItemBehavior(Object reference, Class<SpawnEggItem> clazz, @Local ItemStack itemStack, @Share("spawnEgg") LocalRef<SpawnEggItemBehavior> spawnEgg) {
        Optional<SpawnEggItemBehavior> optionalSpawnEgg = itemStack.itematic$getBehavior(ItemBehaviorType.SPAWN_EGG);
        optionalSpawnEgg.ifPresent(spawnEgg::set);
        return optionalSpawnEgg.isPresent();
    }

    @Redirect(
        method = "checkAndHandleImportantInteractions",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/SpawnEggItem;spawnOffspringFromSpawnEgg(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Mob;Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/item/ItemStack;)Ljava/util/Optional;"
        )
    )
    private Optional<Mob> spawnOffspringUseItemBehavior(SpawnEggItem instance, Player user, Mob entity, EntityType<? extends Mob> type, ServerLevel level, Vec3 pos, ItemStack stack, @Share("spawnEgg") LocalRef<SpawnEggItemBehavior> spawnEgg) {
        return spawnEgg.get().spawnBaby(user, entity, type, level, pos, stack);
    }

    @Redirect(
        method = "checkAndHandleImportantInteractions",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isLeadCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.LEAD);
    }

    @Redirect(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Mob;getEquipmentForSlot(Lnet/minecraft/world/entity/EquipmentSlot;I)Lnet/minecraft/world/item/Item;"
        )
    )
    @Nullable
    private Item getEquipmentForSlotUseId(EquipmentSlot equipmentSlot, int equipmentLevel, @Share("item") LocalRef<Holder<Item>> itemReference) {
        ResourceKey<Item> itemId = LEVEL_TO_EQUIPMENT.get(equipmentLevel).get(equipmentSlot);
        Optional<Holder.Reference<Item>> item = this.level().itematic$itemAccess().get(itemId);
        if (item.isEmpty()) {
            return null;
        }

        itemReference.set(item.get());
        return item.get().value();
    }

    @Redirect(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseHolder(ItemLike item, @Share("item") LocalRef<Holder<Item>> itemReference) {
        return new ItemStack(itemReference.get());
    }

    @Redirect(
        method = "doHurtTarget",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Mob;getAttributeValue(Lnet/minecraft/core/Holder;)D",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/entity/ai/attributes/Attributes;ATTACK_DAMAGE:Lnet/minecraft/core/Holder;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private double useCustomAttackDamage(Mob instance, Holder<Attribute> attribute) {
        return this.itematic$getAttackDamage();
    }

    /**
     * @author ErrorCraft
     * @reason Uses item keys instead of direct items.
     */
    @Overwrite
    @Nullable
    public ItemStack getPickResult() {
        ResourceKey<Item> itemId = this.pickBlockKey();
        if (itemId == null) {
            return null;
        }

        return this.level().itematic$createStack(itemId);
    }

    @Unique
    @Nullable
    protected ResourceKey<Item> pickBlockKey() {
        return null;
    }

    @Override
    public boolean itematic$trySetBaby(boolean baby) {
        this.setBaby(baby);
        return this.isBaby() == baby;
    }
}
