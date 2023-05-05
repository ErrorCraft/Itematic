package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public record ThrowableItemComponent(float speed, float ySpeedModifierAngle) implements ItemComponent {
    public static final Codec<ThrowableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.FLOAT.fieldOf("speed").forGetter(ThrowableItemComponent::speed),
        Codec.FLOAT.fieldOf("y_speed_modifier_angle").forGetter(ThrowableItemComponent::ySpeedModifierAngle)
    ).apply(instance, ThrowableItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.THROWABLE;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        if (!world.isClient()) {
            stack.getComponent(ItemComponentTypes.PROJECTILE)
                .ifPresent(c -> c.createEntity(world, user, stack, this.ySpeedModifierAngle, this.speed));
        }
        return TypedActionResult.success(stack, world.isClient());
    }
}
