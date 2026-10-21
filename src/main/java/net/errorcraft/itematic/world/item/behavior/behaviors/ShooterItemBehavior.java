package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.component.ItemDamageRules;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethod;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

import java.util.List;

public record ShooterItemBehavior(ShooterMethod.Properties<?> method, HolderSet<Item> ammunition, HolderSet<Item> heldAmmunition, ItemDamageRules itemDamage, int range) implements ItemBehavior<ShooterItemBehavior> {
    public static final Codec<ShooterItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ShooterMethod.Properties.CODEC.fieldOf("method").forGetter(ShooterItemBehavior::method),
        RegistryCodecs.holderSet(Registries.ITEM).fieldOf("ammunition").forGetter(ShooterItemBehavior::ammunition),
        RegistryCodecs.holderSet(Registries.ITEM).fieldOf("held_ammunition").forGetter(ShooterItemBehavior::heldAmmunition),
        ItemDamageRules.CODEC.fieldOf("item_damage").forGetter(ShooterItemBehavior::itemDamage),
        ExtraCodecs.POSITIVE_INT.fieldOf("range").forGetter(ShooterItemBehavior::range)
    ).apply(instance, ShooterItemBehavior::new));

    public static ShooterItemBehavior of(ShooterMethod.Properties<?> method, HolderSet<Item> ammunition, HolderSet<Item> heldAmmunition, int range, ItemDamageRules.Rule... rules) {
        return new ShooterItemBehavior(
            method,
            ammunition,
            heldAmmunition,
            new ItemDamageRules(List.of(rules), 1),
            range
        );
    }

    @Override
    public ItemBehaviorType<ShooterItemBehavior> type() {
        return ItemBehaviorType.SHOOTER;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(ItematicDataComponents.SHOOTER_METHOD, this.method.method());
        this.method.addComponents(builder);
        builder.set(ItematicDataComponents.SHOOTER_AMMUNITION, this.ammunition);
        builder.set(ItematicDataComponents.SHOOTER_HELD_AMMUNITION, this.heldAmmunition);
        builder.set(ItematicDataComponents.SHOOTER_DAMAGE_RULES, this.itemDamage);
        builder.set(ItematicDataComponents.SHOOTER_RANGE, this.range);
    }
}
