package net.errorcraft.itematic.client.item.bar;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.client.item.bar.color.ColorProvider;
import net.errorcraft.itematic.client.item.bar.progress.ProgressProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;

public record ItemBarStyle(ProgressProvider progress, ColorProvider color, List<Identifier> textures) {
    public static final Codec<ItemBarStyle> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ProgressProvider.CODEC.fieldOf("progress").forGetter(ItemBarStyle::progress),
        ColorProvider.CODEC.fieldOf("color").forGetter(ItemBarStyle::color),
        Codecs.nonEmptyList(Identifier.CODEC.listOf()).fieldOf("textures").forGetter(ItemBarStyle::textures)
    ).apply(instance, ItemBarStyle::new));

    public boolean isVisible(ItemStack stack) {
        return this.progress.isVisible(stack);
    }

    public Identifier progressTexture(ItemStack stack) {
        float progress = this.progress.get(stack);
        if (progress <= 0.0f) {
            return this.textures.getFirst();
        }
        if (progress >= 1.0f) {
            return this.textures.getLast();
        }
        int index = (int) (progress * (this.textures.size() - 1));
        return this.textures.get(index);
    }

    public int color(ItemStack stack) {
        float progress = this.progress.get(stack);
        return this.color.get(progress);
    }
}
