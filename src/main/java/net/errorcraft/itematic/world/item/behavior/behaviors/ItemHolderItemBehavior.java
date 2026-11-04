package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.holder.ItemHolderSounds;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;

public record ItemHolderItemBehavior(Fraction capacity, ItemHolderRules rules, ItemHolderSounds sounds) implements ItemBehavior<ItemHolderItemBehavior> {
    public static final Codec<Fraction> CAPACITY_CODEC = ItematicCodecs.positiveFraction(100);
    public static final Codec<ItemHolderItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        CAPACITY_CODEC.fieldOf("capacity").forGetter(ItemHolderItemBehavior::capacity),
        ItemHolderRules.CODEC.fieldOf("rules").forGetter(ItemHolderItemBehavior::rules),
        ItemHolderSounds.CODEC.fieldOf("sounds").forGetter(ItemHolderItemBehavior::sounds)
    ).apply(instance, ItemHolderItemBehavior::new));

    public static ItemHolderItemBehavior of(Fraction capacity, ItemHolderRules rules, Holder<SoundEvent> insertItemSound, Holder<SoundEvent> insertFailItemSound, Holder<SoundEvent> removeItemSound, Holder<SoundEvent> emptySound) {
        return new ItemHolderItemBehavior(
            capacity,
            rules,
            new ItemHolderSounds(insertItemSound, insertFailItemSound, removeItemSound, emptySound)
        );
    }

    @Override
    public ItemBehaviorType<ItemHolderItemBehavior> type() {
        return ItemBehaviorType.ITEM_HOLDER;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        builder.set(ItematicDataComponents.ITEM_HOLDER_CAPACITY, this.capacity);
        builder.set(ItematicDataComponents.ITEM_HOLDER_RULES, this.rules);
        builder.set(ItematicDataComponents.ITEM_HOLDER_SOUNDS, this.sounds);
    }
}
