package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.potion.Potion;
import net.minecraft.text.Text;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;

public record PotionHolderItemComponent(float durationMultiplier) implements ItemComponent<PotionHolderItemComponent> {
    public static final Codec<PotionHolderItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.POSITIVE_FLOAT.fieldOf("duration_multiplier").forGetter(PotionHolderItemComponent::durationMultiplier)
    ).apply(instance, PotionHolderItemComponent::new));

    @Override
    public ItemComponentType<PotionHolderItemComponent> type() {
        return ItemComponentTypes.POTION_HOLDER;
    }

    @Override
    public Codec<PotionHolderItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContents != null) {
            potionContents.buildTooltip(tooltip::add, this.durationMultiplier, context.getUpdateTickRate());
        }
    }

    public String translationKey(ItemStack stack, String baseTranslationKey) {
        return Potion.finishTranslationKey(stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion(), baseTranslationKey + ".effect.");
    }

    public static PotionHolderItemComponent of(float durationMultiplier) {
        return new PotionHolderItemComponent(durationMultiplier);
    }
}
