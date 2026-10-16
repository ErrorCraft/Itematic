package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;

public record PotionHolderItemBehavior(float durationScale) implements ItemBehavior<PotionHolderItemBehavior> {
    public static final Codec<PotionHolderItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("duration_scale").forGetter(PotionHolderItemBehavior::durationScale)
    ).apply(instance, PotionHolderItemBehavior::new));

    public static PotionHolderItemBehavior of(float durationScale) {
        return new PotionHolderItemBehavior(durationScale);
    }

    @Override
    public ItemBehaviorType<PotionHolderItemBehavior> type() {
        return ItemBehaviorType.POTION_HOLDER;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        builder.set(DataComponents.POTION_DURATION_SCALE, this.durationScale);
    }

    public String translationKey(ItemStack stack, String baseTranslationKey) {
        return stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
            .potion()
            .map(potion -> baseTranslationKey + ".effect." + potion.value().name())
            .orElseGet(() -> baseTranslationKey + ".effect.empty");
    }
}
