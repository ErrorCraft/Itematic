package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.SharedConstants;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.UseAction;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.List;

public record FoodItemComponent(int nutrition, float saturationModifier, boolean alwaysEdible, List<FoodComponent.StatusEffectEntry> effects) implements ItemComponent<FoodItemComponent> {
    public static final Codec<FoodItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.NONNEGATIVE_INT.fieldOf("nutrition").forGetter(FoodItemComponent::nutrition),
        Codec.FLOAT.fieldOf("saturation_modifier").forGetter(FoodItemComponent::saturationModifier),
        Codec.BOOL.optionalFieldOf("always_edible", false).forGetter(FoodItemComponent::alwaysEdible),
        FoodComponent.StatusEffectEntry.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(FoodItemComponent::effects)
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

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.FOOD, new FoodComponent(this.nutrition, this.saturationModifier, false, 1.0f, this.effects));
    }

    public boolean mayStartUsing(PlayerEntity user) {
        return user.canConsume(this.alwaysEdible);
    }

    public static ItemComponent<?>[] from(FoodComponent component) {
        return from(component, null);
    }

    public static ItemComponent<?>[] from(FoodComponent component, RegistryEntry<Item> resultItem) {
        return from(component, (int)(component.eatSeconds() * SharedConstants.TICKS_PER_SECOND), UseAction.EAT, resultItem);
    }

    public static FoodItemComponent of(int nutrition, float saturationModifier, boolean alwaysEdible, List<FoodComponent.StatusEffectEntry> effects) {
        return new FoodItemComponent(nutrition, saturationModifier, alwaysEdible, effects);
    }

    public static ItemComponent<?>[] from(FoodComponent component, int useDuration, UseAction useAction, RegistryEntry<Item> resultItem) {
        return new ItemComponent<?>[] {
            UseDurationItemComponent.of(useDuration),
            of(component.nutrition(), component.saturationModifier(), component.canAlwaysEat(), component.effects()),
            UseAnimationItemComponent.of(useAction),
            ConsumableItemComponent.of(resultItem)
        };
    }
}
