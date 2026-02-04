package net.errorcraft.itematic.mixin.entity.mob;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.errorcraft.itematic.access.entity.mob.MobEntityAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.SpawnEggItemComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Map;
import java.util.Optional;

@Mixin(MobEntity.class)
public abstract class MobEntityExtender extends LivingEntity implements MobEntityAccess {
    @Shadow
    public abstract void setBaby(boolean baby);

    @Unique
    private static final Int2ObjectMap<Map<EquipmentSlot, RegistryKey<Item>>> LEVEL_TO_EQUIPMENT = Util.make(new Int2ObjectOpenHashMap<>(), map -> {
        map.defaultReturnValue(Map.of());
        map.put(0, Map.of(
            EquipmentSlot.HEAD, ItemKeys.LEATHER_HELMET,
            EquipmentSlot.CHEST, ItemKeys.LEATHER_CHESTPLATE,
            EquipmentSlot.LEGS, ItemKeys.LEATHER_LEGGINGS,
            EquipmentSlot.FEET, ItemKeys.LEATHER_BOOTS
        ));
        map.put(1, Map.of(
            EquipmentSlot.HEAD, ItemKeys.GOLDEN_HELMET,
            EquipmentSlot.CHEST, ItemKeys.GOLDEN_CHESTPLATE,
            EquipmentSlot.LEGS, ItemKeys.GOLDEN_LEGGINGS,
            EquipmentSlot.FEET, ItemKeys.GOLDEN_BOOTS
        ));
        map.put(2, Map.of(
            EquipmentSlot.HEAD, ItemKeys.CHAINMAIL_HELMET,
            EquipmentSlot.CHEST, ItemKeys.CHAINMAIL_CHESTPLATE,
            EquipmentSlot.LEGS, ItemKeys.CHAINMAIL_LEGGINGS,
            EquipmentSlot.FEET, ItemKeys.CHAINMAIL_BOOTS
        ));
        map.put(3, Map.of(
            EquipmentSlot.HEAD, ItemKeys.IRON_HELMET,
            EquipmentSlot.CHEST, ItemKeys.IRON_CHESTPLATE,
            EquipmentSlot.LEGS, ItemKeys.IRON_LEGGINGS,
            EquipmentSlot.FEET, ItemKeys.IRON_BOOTS
        ));
        map.put(4, Map.of(
            EquipmentSlot.HEAD, ItemKeys.DIAMOND_HELMET,
            EquipmentSlot.CHEST, ItemKeys.DIAMOND_CHESTPLATE,
            EquipmentSlot.LEGS, ItemKeys.DIAMOND_LEGGINGS,
            EquipmentSlot.FEET, ItemKeys.DIAMOND_BOOTS
        ));
    });

    protected MobEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyConstant(
        method = "interactWithItem",
        constant = @Constant(
            classValue = SpawnEggItem.class,
            ordinal = 0
        )
    )
    private boolean instanceOfSpawnEggItemUseItemComponentCheck(Object reference, Class<SpawnEggItem> clazz, @Local ItemStack itemStack, @Share("spawnEggItemComponent") LocalRef<SpawnEggItemComponent> spawnEggItemComponent) {
        Optional<SpawnEggItemComponent> optionalSpawnEggItemComponent = itemStack.itematic$getBehavior(ItemComponentTypes.SPAWN_EGG);
        optionalSpawnEggItemComponent.ifPresent(spawnEggItemComponent::set);
        return optionalSpawnEggItemComponent.isPresent();
    }

    @Redirect(
        method = "interactWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;",
            ordinal = 1
        )
    )
    private Item getItemReturnNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "interactWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/SpawnEggItem;spawnBaby(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/entity/EntityType;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/item/ItemStack;)Ljava/util/Optional;"
        )
    )
    private Optional<MobEntity> spawnBabyUseItemComponent(SpawnEggItem instance, PlayerEntity user, MobEntity entity, EntityType<? extends MobEntity> entityType, ServerWorld world, Vec3d pos, ItemStack stack, @Share("spawnEggItemComponent") LocalRef<SpawnEggItemComponent> spawnEggItemComponent) {
        return spawnEggItemComponent.get().spawnBaby(user, entity, entityType, world, pos, stack);
    }

    @Redirect(
        method = "interactWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isOfForLeadUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.LEAD);
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/mob/MobEntity;getEquipmentForSlot(Lnet/minecraft/entity/EquipmentSlot;I)Lnet/minecraft/item/Item;"
        )
    )
    private Item getEquipmentForSlotUseRegistryKey(EquipmentSlot equipmentSlot, int equipmentLevel, @Share("item") LocalRef<RegistryEntry<Item>> item) {
        RegistryKey<Item> key = LEVEL_TO_EQUIPMENT.get(equipmentLevel).get(equipmentSlot);
        Optional<RegistryEntry.Reference<Item>> optionalEntry = this.getWorld().itematic$getItemAccess().getOptionalEntry(key);
        if (optionalEntry.isEmpty()) {
            return null;
        }

        item.set(optionalEntry.get());
        return optionalEntry.get().value();
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseRegistryEntry(ItemConvertible item, @Share("item") LocalRef<RegistryEntry<Item>> itemEntry) {
        return new ItemStack(itemEntry.get());
    }

    @Redirect(
        method = "tryAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/mob/MobEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/entity/attribute/EntityAttributes;ATTACK_DAMAGE:Lnet/minecraft/registry/entry/RegistryEntry;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private double useCustomAttackDamage(MobEntity instance, RegistryEntry<EntityAttribute> attribute) {
        return this.itematic$getAttackDamage();
    }

    /**
     * @author ErrorCraft
     * @reason Uses item keys instead of direct items.
     */
    @Overwrite
    @Nullable
    public ItemStack getPickBlockStack() {
        RegistryKey<Item> key = this.pickBlockKey();
        if (key == null) {
            return null;
        }

        return this.getWorld().itematic$createStack(key);
    }

    @Unique
    @Nullable
    protected RegistryKey<Item> pickBlockKey() {
        return null;
    }

    @Override
    public boolean itematic$trySetBaby(boolean baby) {
        this.setBaby(baby);
        return this.isBaby() == baby;
    }
}
