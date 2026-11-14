package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.level.block.entity.SignModifier;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.DyeColor;

import java.util.Optional;

public record SignModifierItemBehavior(SignModifier modifier) implements ItemBehavior<SignModifierItemBehavior> {
    public static final Codec<SignModifierItemBehavior> CODEC = SignModifier.CODEC.xmap(
        SignModifierItemBehavior::new,
        SignModifierItemBehavior::modifier
    );

    public static SignModifierItemBehavior dye(DyeColor color, Holder<SoundEvent> applySound) {
        return new SignModifierItemBehavior(
            new SignModifier(Optional.of(color), Optional.empty(), Optional.empty(), applySound, false)
        );
    }

    public static SignModifierItemBehavior glow(boolean glow, Holder<SoundEvent> applySound) {
        return new SignModifierItemBehavior(
            new SignModifier(Optional.empty(), Optional.of(glow), Optional.empty(), applySound, false)
        );
    }

    public static SignModifierItemBehavior wax(boolean wax, Holder<SoundEvent> applySound) {
        return new SignModifierItemBehavior(
            new SignModifier(Optional.empty(), Optional.empty(), Optional.of(wax), applySound, true)
        );
    }

    @Override
    public ItemBehaviorType<SignModifierItemBehavior> type() {
        return ItemBehaviorType.SIGN_MODIFIER;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.SIGN_MODIFIER, this.modifier);
    }
}
