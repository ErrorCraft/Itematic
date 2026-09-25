package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record DamageableItemBehavior(int durability, Optional<Holder<SoundEvent>> breakSound, boolean preserveItem) implements ItemBehavior<DamageableItemBehavior> {
    public static final Codec<DamageableItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("durability").forGetter(DamageableItemBehavior::durability),
        SoundEvent.CODEC.optionalFieldOf("break_sound").forGetter(DamageableItemBehavior::breakSound),
        Codec.BOOL.optionalFieldOf("preserve_item", false).forGetter(DamageableItemBehavior::preserveItem)
    ).apply(instance, DamageableItemBehavior::new));

    public static DamageableItemBehavior of(int durability) {
        return new DamageableItemBehavior(durability, Optional.empty(), false);
    }

    public static DamageableItemBehavior of(int durability, Holder<SoundEvent> breakSound) {
        return new DamageableItemBehavior(durability, Optional.of(breakSound), false);
    }

    public static DamageableItemBehavior ofPreserved(int durability) {
        return new DamageableItemBehavior(durability, Optional.empty(), true);
    }

    @Override
    public ItemBehaviorType<DamageableItemBehavior> type() {
        return ItemBehaviorType.DAMAGEABLE;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.MAX_DAMAGE, this.durability);
        builder.set(DataComponents.DAMAGE, 0);
        this.breakSound.ifPresent(breakSound -> builder.set(DataComponents.BREAK_SOUND, breakSound));
    }

    public int maximumDamage(ItemStack stack) {
        return stack.getMaxDamage() - (this.preserveItem ? 1 : 0);
    }

    public boolean isUsable(ItemStack stack) {
        return stack.getDamageValue() < this.maximumDamage(stack);
    }
}
