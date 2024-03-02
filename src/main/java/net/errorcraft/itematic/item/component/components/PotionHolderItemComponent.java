package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.SharedConstants;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record PotionHolderItemComponent(float durationMultiplier) implements ItemComponent<PotionHolderItemComponent> {
    public static final Codec<PotionHolderItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.FLOAT.fieldOf("duration_multiplier").forGetter(PotionHolderItemComponent::durationMultiplier)
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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        PotionUtil.buildTooltip(stack, tooltip, this.durationMultiplier, world == null ? SharedConstants.TICKS_PER_SECOND : world.getTickManager().getTickRate());
    }

    public String translationKey(ItemStack stack, String baseTranslationKey) {
        return Potion.finishTranslationKey(PotionUtil.getPotion(stack), baseTranslationKey + ".effect.");
    }

    public static PotionHolderItemComponent of(float durationMultiplier) {
        return new PotionHolderItemComponent(durationMultiplier);
    }
}
