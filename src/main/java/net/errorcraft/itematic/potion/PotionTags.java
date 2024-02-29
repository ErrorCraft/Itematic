package net.errorcraft.itematic.potion;

import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class PotionTags {
    public static final TagKey<Potion> TRADEABLE = of("tradeable");

    private PotionTags() {}

    private static TagKey<Potion> of(String id) {
        return TagKey.of(RegistryKeys.POTION, new Identifier(id));
    }
}
