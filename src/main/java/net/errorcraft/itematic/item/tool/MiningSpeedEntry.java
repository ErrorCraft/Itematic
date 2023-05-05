package net.errorcraft.itematic.item.tool;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.predicate.TagPredicateUtil;
import net.minecraft.block.Block;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.registry.RegistryKeys;

public record MiningSpeedEntry(TagPredicate<Block> tag, float miningSpeed) {
    public static final Codec<MiningSpeedEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagPredicateUtil.codec(RegistryKeys.BLOCK).fieldOf("tag").forGetter(MiningSpeedEntry::tag),
        Codec.FLOAT.fieldOf("mining_speed").forGetter(MiningSpeedEntry::miningSpeed)
    ).apply(instance, MiningSpeedEntry::new));
}
