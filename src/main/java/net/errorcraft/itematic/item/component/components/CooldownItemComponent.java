package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.SharedConstants;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UseCooldownComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.Optional;

public record CooldownItemComponent(Optional<Identifier> group, int ticks) implements ItemComponent<CooldownItemComponent> {
    public static final Codec<CooldownItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Identifier.CODEC.optionalFieldOf("group").forGetter(CooldownItemComponent::group),
        Codecs.POSITIVE_INT.fieldOf("ticks").forGetter(CooldownItemComponent::ticks)
    ).apply(instance, CooldownItemComponent::new));

    public static CooldownItemComponent of(int ticks) {
        return new CooldownItemComponent(Optional.empty(), ticks);
    }

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
        UseCooldownComponent useCooldown = stack.get(DataComponentTypes.USE_COOLDOWN);
        if (useCooldown != null) {
            useCooldown.set(stack, user);
        }

        return ItemResult.PASS;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.USE_COOLDOWN, new UseCooldownComponent((float) this.ticks / SharedConstants.TICKS_PER_SECOND, this.group));
    }
}
