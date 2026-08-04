package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.SharedConstants;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.level.Level;
import java.util.Optional;

public record CooldownItemComponent(Optional<Identifier> group, int ticks) implements ItemComponent<CooldownItemComponent> {
    public static final Codec<CooldownItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Identifier.CODEC.optionalFieldOf("group").forGetter(CooldownItemComponent::group),
        ExtraCodecs.POSITIVE_INT.fieldOf("ticks").forGetter(CooldownItemComponent::ticks)
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
    public ItemResult use(Level world, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        UseCooldown useCooldown = stack.get(DataComponents.USE_COOLDOWN);
        if (useCooldown != null) {
            useCooldown.apply(stack, user);
        }

        return ItemResult.PASS;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.USE_COOLDOWN, new UseCooldown((float) this.ticks / SharedConstants.TICKS_PER_SECOND, this.group));
    }
}
