package net.errorcraft.itematic.sound;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundEventKeys {
    public static final RegistryKey<SoundEvent> ITEM_ARMOR_EQUIP_CHAIN = of("item.armor.equip_chain");
    public static final RegistryKey<SoundEvent> ITEM_ARMOR_EQUIP_DIAMOND = of("item.armor.equip_diamond");
    public static final RegistryKey<SoundEvent> ITEM_ARMOR_EQUIP_GENERIC = of("item.armor.equip_generic");
    public static final RegistryKey<SoundEvent> ITEM_ARMOR_EQUIP_GOLD = of("item.armor.equip_gold");
    public static final RegistryKey<SoundEvent> ITEM_ARMOR_EQUIP_IRON = of("item.armor.equip_iron");
    public static final RegistryKey<SoundEvent> ITEM_ARMOR_EQUIP_LEATHER = of("item.armor.equip_leather");
    public static final RegistryKey<SoundEvent> ITEM_ARMOR_EQUIP_NETHERITE = of("item.armor.equip_netherite");
    public static final RegistryKey<SoundEvent> ITEM_ARMOR_EQUIP_TURTLE = of("item.armor.equip_turtle");
    public static final RegistryKey<SoundEvent> ITEM_BUCKET_EMPTY = of("item.bucket.empty");
    public static final RegistryKey<SoundEvent> ITEM_BUCKET_EMPTY_AXOLOTL = of("item.bucket.empty_axolotl");
    public static final RegistryKey<SoundEvent> ITEM_BUCKET_EMPTY_FISH = of("item.bucket.empty_fish");
    public static final RegistryKey<SoundEvent> ITEM_BUCKET_EMPTY_LAVA = of("item.bucket.empty_lava");
    public static final RegistryKey<SoundEvent> ITEM_BUCKET_EMPTY_POWDER_SNOW = of("item.bucket.empty_powder_snow");
    public static final RegistryKey<SoundEvent> ITEM_BUCKET_EMPTY_TADPOLE = of("item.bucket.empty_tadpole");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_5 = of("music_disc.5");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_11 = of("music_disc.11");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_13 = of("music_disc.13");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_BLOCKS = of("music_disc.blocks");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_CAT = of("music_disc.cat");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_CHIRP = of("music_disc.chirp");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_FAR = of("music_disc.far");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_MALL = of("music_disc.mall");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_MELLOHI = of("music_disc.mellohi");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_OTHERSIDE = of("music_disc.otherside");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_PIGSTEP = of("music_disc.pigstep");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_RELIC = of("music_disc.relic");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_STAL = of("music_disc.stal");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_STRAD = of("music_disc.strad");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_WAIT = of("music_disc.wait");
    public static final RegistryKey<SoundEvent> MUSIC_DISC_WARD = of("music_disc.ward");

    private SoundEventKeys() {}

    private static RegistryKey<SoundEvent> of(String id) {
        return RegistryKey.of(RegistryKeys.SOUND_EVENT, new Identifier(id));
    }
}
