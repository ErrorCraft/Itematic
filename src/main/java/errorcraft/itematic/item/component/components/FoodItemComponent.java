package errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import errorcraft.itematic.item.component.ItemComponent;
import errorcraft.itematic.item.component.ItemComponentType;
import errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public record FoodItemComponent(int nutrition, float saturationModifier, boolean alwaysEdible) implements ItemComponent {
    public static final Codec<FoodItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("nutrition").forGetter(FoodItemComponent::nutrition),
        Codec.FLOAT.fieldOf("saturation_modifier").forGetter(FoodItemComponent::saturationModifier),
        Codec.BOOL.optionalFieldOf("always_edible", false).forGetter(FoodItemComponent::alwaysEdible)
    ).apply(instance, FoodItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.FOOD;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        if (user.canConsume(this.alwaysEdible)) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.pass(stack);
    }

    @Override
    public ItemStack finishUsing(World world, LivingEntity user, ItemStack stack) {
        return user.eatFood(world, stack);
    }

    public static ItemComponent[] from(FoodComponent component) {
        return new ItemComponent[] {
            new UseDurationItemComponent(component.isSnack() ? 16 : 32),
            new FoodItemComponent(component.getHunger(), component.getSaturationModifier(), component.isAlwaysEdible())
        };
    }
}
