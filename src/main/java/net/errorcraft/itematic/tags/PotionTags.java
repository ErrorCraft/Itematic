package net.errorcraft.itematic.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.alchemy.Potion;

public class PotionTags {
    public static final TagKey<Potion> TRADEABLE = of("tradeable");

    private PotionTags() {}

    private static TagKey<Potion> of(String id) {
        return TagKey.create(Registries.POTION, Identifier.withDefaultNamespace(id));
    }
}
