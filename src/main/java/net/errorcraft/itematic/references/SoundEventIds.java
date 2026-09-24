package net.errorcraft.itematic.references;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;

public class SoundEventIds {
    public static final ResourceKey<SoundEvent> ARMOR_EQUIP_ELYTRA = of("item.armor.equip_elytra");
    public static final ResourceKey<SoundEvent> ARMOR_EQUIP_GENERIC = of("item.armor.equip_generic");
    public static final ResourceKey<SoundEvent> ARMOR_STAND_PLACE = of("entity.armor_stand.place");
    public static final ResourceKey<SoundEvent> ARMOR_UNEQUIP_WOLF = of("item.armor.unequip_wolf");
    public static final ResourceKey<SoundEvent> ARMOR_EQUIP_NAUTILUS = of("item.armor.equip_nautilus");
    public static final ResourceKey<SoundEvent> ARMOR_UNEQUIP_NAUTILUS = of("item.armor.unequip_nautilus");
    public static final ResourceKey<SoundEvent> BOTTLE_EMPTY = of("item.bottle.empty");
    public static final ResourceKey<SoundEvent> BOTTLE_FILL = of("item.bottle.fill");
    public static final ResourceKey<SoundEvent> BUCKET_EMPTY = of("item.bucket.empty");
    public static final ResourceKey<SoundEvent> BUCKET_EMPTY_AXOLOTL = of("item.bucket.empty_axolotl");
    public static final ResourceKey<SoundEvent> BUCKET_EMPTY_FISH = of("item.bucket.empty_fish");
    public static final ResourceKey<SoundEvent> BUCKET_EMPTY_LAVA = of("item.bucket.empty_lava");
    public static final ResourceKey<SoundEvent> BUCKET_EMPTY_POWDER_SNOW = of("item.bucket.empty_powder_snow");
    public static final ResourceKey<SoundEvent> BUCKET_EMPTY_TADPOLE = of("item.bucket.empty_tadpole");
    public static final ResourceKey<SoundEvent> BUNDLE_DROP_CONTENTS = of("item.bundle.drop_contents");
    public static final ResourceKey<SoundEvent> BUNDLE_INSERT = of("item.bundle.insert");
    public static final ResourceKey<SoundEvent> BUNDLE_INSERT_FAIL = of("item.bundle.insert_fail");
    public static final ResourceKey<SoundEvent> BUNDLE_REMOVE_ONE = of("item.bundle.remove_one");
    public static final ResourceKey<SoundEvent> ENDER_EYE_LAUNCH = of("entity.ender_eye.launch");
    public static final ResourceKey<SoundEvent> END_PORTAL_FRAME_FILL = of("block.end_portal_frame.fill");
    public static final ResourceKey<SoundEvent> FIRE_CHARGE_USE = of("item.firecharge.use");
    public static final ResourceKey<SoundEvent> FIRE_EXTINGUISH = of("block.fire.extinguish");
    public static final ResourceKey<SoundEvent> FLINT_AND_STEEL_USE = of("item.flintandsteel.use");
    public static final ResourceKey<SoundEvent> GENERIC_SPLASH = of("entity.generic.splash");
    public static final ResourceKey<SoundEvent> HAPPY_GHAST_EQUIP = of("entity.happy_ghast.equip");
    public static final ResourceKey<SoundEvent> HAPPY_GHAST_UNEQUIP = of("entity.happy_ghast.unequip");
    public static final ResourceKey<SoundEvent> HOE_TILL = of("item.hoe.till");
    public static final ResourceKey<SoundEvent> HORSE_ARMOR = of("entity.horse.armor");
    public static final ResourceKey<SoundEvent> HORSE_ARMOR_UNEQUIP = of("item.horse_armor.unequip");
    public static final ResourceKey<SoundEvent> HORSE_SADDLE = of("entity.horse.saddle");
    public static final ResourceKey<SoundEvent> LODESTONE_COMPASS_LOCK = of("item.lodestone_compass.lock");
    public static final ResourceKey<SoundEvent> MACE_SMASH_AIR = of("item.mace.smash_air");
    public static final ResourceKey<SoundEvent> MACE_SMASH_GROUND = of("item.mace.smash_ground");
    public static final ResourceKey<SoundEvent> MACE_SMASH_GROUND_HEAVY = of("item.mace.smash_ground_heavy");
    public static final ResourceKey<SoundEvent> OMINOUS_BOTTLE_DISPOSE = of("item.ominous_bottle.dispose");
    public static final ResourceKey<SoundEvent> SADDLE_UNEQUIP = of("item.saddle.unequip");
    public static final ResourceKey<SoundEvent> SHIELD_BLOCK = of("item.shield.block");
    public static final ResourceKey<SoundEvent> SHIELD_BREAK = of("item.shield.break");
    public static final ResourceKey<SoundEvent> SHOVEL_FLATTEN = of("item.shovel.flatten");
    public static final ResourceKey<SoundEvent> SPEAR_USE = of("item.spear.use");
    public static final ResourceKey<SoundEvent> SPEAR_HIT = of("item.spear.hit");
    public static final ResourceKey<SoundEvent> SPEAR_ATTACK = of("item.spear.attack");
    public static final ResourceKey<SoundEvent> SPEAR_WOOD_USE = of("item.spear_wood.use");
    public static final ResourceKey<SoundEvent> SPEAR_WOOD_HIT = of("item.spear_wood.hit");
    public static final ResourceKey<SoundEvent> SPEAR_WOOD_ATTACK = of("item.spear_wood.attack");
    public static final ResourceKey<SoundEvent> SPYGLASS_USE = of("item.spyglass.use");
    public static final ResourceKey<SoundEvent> SPYGLASS_STOP_USING = of("item.spyglass.stop_using");
    public static final ResourceKey<SoundEvent> TNT_PRIMED = of("entity.tnt.primed");
    public static final ResourceKey<SoundEvent> WOLF_ARMOR_BREAK = of("item.wolf_armor.break");

    private SoundEventIds() {}

    private static ResourceKey<SoundEvent> of(String id) {
        return ResourceKey.create(Registries.SOUND_EVENT, Identifier.withDefaultNamespace(id));
    }
}
