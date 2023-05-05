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

public record CooldownItemComponent(int ticks) implements ItemComponent {
    public static final Codec<CooldownItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("ticks").forGetter(CooldownItemComponent::ticks)
    ).apply(instance, CooldownItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.COOLDOWN;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        user.getItemCooldownManager().set(stack.getItem(), this.ticks);
        return TypedActionResult.pass(stack);
    }
}
