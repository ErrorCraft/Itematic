package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

public record CooldownItemComponent(int ticks) implements ItemComponent<CooldownItemComponent> {
    public static final Codec<CooldownItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.POSITIVE_INT.fieldOf("ticks").forGetter(CooldownItemComponent::ticks)
    ).apply(instance, CooldownItemComponent::new));

    @Override
    public ItemComponentType<CooldownItemComponent> type() {
        return ItemComponentTypes.COOLDOWN;
    }

    @Override
    public Codec<CooldownItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        user.getItemCooldownManager().set(stack.getItem(), this.ticks);
        return ItemResult.PASS;
    }

    public static CooldownItemComponent of(int ticks) {
        return new CooldownItemComponent(ticks);
    }
}
