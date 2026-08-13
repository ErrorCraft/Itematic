package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

public record PotionHolderItemBehavior(float durationMultiplier) implements ItemBehavior<PotionHolderItemBehavior> {
    public static final Codec<PotionHolderItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.POSITIVE_FLOAT.fieldOf("duration_multiplier").forGetter(PotionHolderItemBehavior::durationMultiplier)
    ).apply(instance, PotionHolderItemBehavior::new));

    public static PotionHolderItemBehavior of(float durationMultiplier) {
        return new PotionHolderItemBehavior(durationMultiplier);
    }

    @Override
    public ItemBehaviorType<PotionHolderItemBehavior> type() {
        return ItemBehaviorType.POTION_HOLDER;
    }

    @Override
    public Codec<PotionHolderItemBehavior> codec() {
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
