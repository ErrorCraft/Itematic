package net.errorcraft.itematic.item.tool;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.TagPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

public record MiningSpeedEntry(TagPredicate<Block> tag, float miningSpeed) {
    public static final Codec<MiningSpeedEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagPredicate.codec(Registries.BLOCK).fieldOf("tag").forGetter(MiningSpeedEntry::tag),
        Codec.FLOAT.fieldOf("mining_speed").forGetter(MiningSpeedEntry::miningSpeed)
    ).apply(instance, MiningSpeedEntry::new));
}
