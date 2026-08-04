package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

public record PotionHolderItemComponent(float durationMultiplier) implements ItemComponent<PotionHolderItemComponent> {
    public static final Codec<PotionHolderItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.POSITIVE_FLOAT.fieldOf("duration_multiplier").forGetter(PotionHolderItemComponent::durationMultiplier)
    ).apply(instance, PotionHolderItemComponent::new));

    public static PotionHolderItemComponent of(float durationMultiplier) {
        return new PotionHolderItemComponent(durationMultiplier);
    }

    @Override
    public ItemComponentType<PotionHolderItemComponent> type() {
        return ItemComponentTypes.POTION_HOLDER;
    }

    @Override
    public Codec<PotionHolderItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void finishUsing(Level world, LivingEntity user, ItemStack stack, int usedTicks, ItemStackExchanger stackExchanger) {
        PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
        if (potionContents != null) {
            potionContents.applyToLivingEntity(user, stack.getOrDefault(DataComponents.POTION_DURATION_SCALE, 1.0f));
        }
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        builder.set(DataComponents.POTION_DURATION_SCALE, this.durationMultiplier);
    }

    public String translationKey(ItemStack stack, String baseTranslationKey) {
        return stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
            .potion()
            .map(potion -> baseTranslationKey + ".effect." + potion.value().name())
            .orElseGet(() -> baseTranslationKey + ".effect.empty");
    }
}
