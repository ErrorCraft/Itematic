package net.errorcraft.itematic.sound;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundEventKeys {
    public static final RegistryKey<SoundEvent> ARMOR_EQUIP_ELYTRA = of("item.armor.equip_elytra");
    public static final RegistryKey<SoundEvent> ARMOR_EQUIP_GENERIC = of("item.armor.equip_generic");
    public static final RegistryKey<SoundEvent> BOTTLE_EMPTY = of("item.bottle.empty");
    public static final RegistryKey<SoundEvent> BOTTLE_FILL = of("item.bottle.fill");
    public static final RegistryKey<SoundEvent> BUCKET_EMPTY = of("item.bucket.empty");
    public static final RegistryKey<SoundEvent> BUCKET_EMPTY_AXOLOTL = of("item.bucket.empty_axolotl");
    public static final RegistryKey<SoundEvent> BUCKET_EMPTY_FISH = of("item.bucket.empty_fish");
    public static final RegistryKey<SoundEvent> BUCKET_EMPTY_LAVA = of("item.bucket.empty_lava");
    public static final RegistryKey<SoundEvent> BUCKET_EMPTY_POWDER_SNOW = of("item.bucket.empty_powder_snow");
    public static final RegistryKey<SoundEvent> BUCKET_EMPTY_TADPOLE = of("item.bucket.empty_tadpole");
    public static final RegistryKey<SoundEvent> BUNDLE_DROP_CONTENTS = of("item.bundle.drop_contents");
    public static final RegistryKey<SoundEvent> BUNDLE_INSERT = of("item.bundle.insert");
    public static final RegistryKey<SoundEvent> BUNDLE_REMOVE_ONE = of("item.bundle.remove_one");
    public static final RegistryKey<SoundEvent> ENDER_EYE_LAUNCH = of("entity.ender_eye.launch");
    public static final RegistryKey<SoundEvent> END_PORTAL_FRAME_FILL = of("block.end_portal_frame.fill");
    public static final RegistryKey<SoundEvent> FIRE_CHARGE_USE = of("item.firecharge.use");
    public static final RegistryKey<SoundEvent> FIRE_EXTINGUISH = of("block.fire.extinguish");
    public static final RegistryKey<SoundEvent> FLINT_AND_STEEL_USE = of("item.flintandsteel.use");
    public static final RegistryKey<SoundEvent> GENERIC_SPLASH = of("entity.generic.splash");
    public static final RegistryKey<SoundEvent> HOE_TILL = of("item.hoe.till");
    public static final RegistryKey<SoundEvent> HONEY_BOTTLE_DRINK = of("item.honey_bottle.drink");
    public static final RegistryKey<SoundEvent> HORSE_ARMOR = of("entity.horse.armor");
    public static final RegistryKey<SoundEvent> LODESTONE_COMPASS_LOCK = of("item.lodestone_compass.lock");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_5 = of("music_disc.5");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_11 = of("music_disc.11");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_13 = of("music_disc.13");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_BLOCKS = of("music_disc.blocks");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_CAT = of("music_disc.cat");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_CHIRP = of("music_disc.chirp");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_CREATOR = of("music_disc.creator");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_CREATOR_MUSIC_BOX = of("music_disc.creator_music_box");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_FAR = of("music_disc.far");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_MALL = of("music_disc.mall");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_MELLOHI = of("music_disc.mellohi");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_OTHERSIDE = of("music_disc.otherside");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_PIGSTEP = of("music_disc.pigstep");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_PRECIPICE = of("music_disc.precipice");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_RELIC = of("music_disc.relic");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_STAL = of("music_disc.stal");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_STRAD = of("music_disc.strad");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_WAIT = of("music_disc.wait");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_WARD = of("music_disc.ward");
    public static final RegistryKey<SoundEvent> OMINOUS_BOTTLE_DISPOSE = of("item.ominous_bottle.dispose");
    public static final RegistryKey<SoundEvent> SHIELD_BLOCK = of("item.shield.block");
    public static final RegistryKey<SoundEvent> SHIELD_BREAK = of("item.shield.break");
    public static final RegistryKey<SoundEvent> SHOVEL_FLATTEN = of("item.shovel.flatten");
    public static final RegistryKey<SoundEvent> SPYGLASS_USE = of("item.spyglass.use");
    public static final RegistryKey<SoundEvent> SPYGLASS_STOP_USING = of("item.spyglass.stop_using");
    public static final RegistryKey<SoundEvent> TNT_PRIMED = of("entity.tnt.primed");
    public static final RegistryKey<SoundEvent> WOLF_ARMOR_BREAK = of("item.wolf_armor.break");

    private SoundEventKeys() {}

    private static RegistryKey<SoundEvent> of(String id) {
        return RegistryKey.of(RegistryKeys.SOUND_EVENT, Identifier.ofVanilla(id));
    }
}
