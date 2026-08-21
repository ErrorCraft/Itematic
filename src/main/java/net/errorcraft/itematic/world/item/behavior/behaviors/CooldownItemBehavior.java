package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
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

public record CooldownItemBehavior(Optional<Identifier> group, int ticks) implements ItemBehavior<CooldownItemBehavior> {
    public static final Codec<CooldownItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Identifier.CODEC.optionalFieldOf("group").forGetter(CooldownItemBehavior::group),
        ExtraCodecs.POSITIVE_INT.fieldOf("ticks").forGetter(CooldownItemBehavior::ticks)
    ).apply(instance, CooldownItemBehavior::new));

    public static CooldownItemBehavior of(int ticks) {
        return new CooldownItemBehavior(Optional.empty(), ticks);
    }

    @Override
    public ItemBehaviorType<CooldownItemBehavior> type() {
        return ItemBehaviorType.COOLDOWN;
    }

    @Override
    public ItemResult use(Level level, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
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
