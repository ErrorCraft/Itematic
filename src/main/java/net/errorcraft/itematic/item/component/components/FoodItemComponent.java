package net.errorcraft.itematic.item.component.components;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.effect.StatusEffectInstanceUtil;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public record FoodItemComponent(int nutrition, float saturationModifier, boolean alwaysEdible, List<Effect> effects) implements ItemComponent<FoodItemComponent> {
    public static final Codec<FoodItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("nutrition").forGetter(FoodItemComponent::nutrition),
        Codec.FLOAT.fieldOf("saturation_modifier").forGetter(FoodItemComponent::saturationModifier),
        Codec.BOOL.optionalFieldOf("always_edible", false).forGetter(FoodItemComponent::alwaysEdible),
        Effect.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(FoodItemComponent::effects)
    ).apply(instance, FoodItemComponent::new));

    @Override
    public ItemComponentType<FoodItemComponent> type() {
        return ItemComponentTypes.FOOD;
    }

    @Override
    public Codec<FoodItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void finishUsing(World world, LivingEntity user, ItemStack stack, int usedTicks, ItemStackConsumer resultStackConsumer) {
        user.itematic$eatFood(world, stack, resultStackConsumer);
    }

    public boolean mayStartUsing(PlayerEntity user) {
        return user.canConsume(this.alwaysEdible);
    }

    public static ItemComponent<?>[] from(FoodComponent component) {
        return from(component, null);
    }

    public static ItemComponent<?>[] from(FoodComponent component, RegistryEntry<Item> resultItem) {
        return from(component, component.isSnack() ? 16 : 32, UseAction.EAT, resultItem);
    }

    public static FoodItemComponent of(int nutrition, float saturationModifier, boolean alwaysEdible, List<Effect> effects) {
        return new FoodItemComponent(nutrition, saturationModifier, alwaysEdible, effects);
    }

    public static ItemComponent<?>[] from(FoodComponent component, int useDuration, UseAction useAction, RegistryEntry<Item> resultItem) {
        return new ItemComponent<?>[] {
            UseDurationItemComponent.of(useDuration),
            of(component.getHunger(), component.getSaturationModifier(), component.isAlwaysEdible(), getEffects(component.getStatusEffects())),
            UseAnimationItemComponent.of(useAction),
            ConsumableItemComponent.of(resultItem)
        };
    }

    private static List<Effect> getEffects(List<Pair<StatusEffectInstance, Float>> pairs) {
        if (pairs.isEmpty()) {
            return List.of();
        }
        List<Effect> effects = new ArrayList<>();
        for (Pair<StatusEffectInstance, Float> pair : pairs) {
            effects.add(new Effect(pair.getFirst(), pair.getSecond()));
        }
        return effects;
    }

    public record Effect(StatusEffectInstance effect, float chance) {
        public static final Codec<Effect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            StatusEffectInstanceUtil.CODEC.fieldOf("effect").forGetter(Effect::effect),
            Codec.floatRange(0.0f, 1.0f).fieldOf("chance").forGetter(Effect::chance)
        ).apply(instance, Effect::new));

        public void tryApply(LivingEntity target, Random random) {
            if (random.nextFloat() < this.chance) {
                target.addStatusEffect(new StatusEffectInstance(this.effect));
            }
        }
    }
}
