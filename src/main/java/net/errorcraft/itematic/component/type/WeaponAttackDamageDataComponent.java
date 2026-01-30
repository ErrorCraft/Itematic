package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.predicate.item.ItemPredicateUtil;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;

import java.util.List;
import java.util.Optional;

public record WeaponAttackDamageDataComponent(List<Rule> rules, double defaultDamage) {
    public static final Codec<WeaponAttackDamageDataComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Rule.CODEC.listOf().fieldOf("rules").forGetter(WeaponAttackDamageDataComponent::rules),
        ItematicCodecs.NON_NEGATIVE_DOUBLE.fieldOf("default_damage").forGetter(WeaponAttackDamageDataComponent::defaultDamage)
    ).apply(instance, WeaponAttackDamageDataComponent::new));
    public static final PacketCodec<RegistryByteBuf, WeaponAttackDamageDataComponent> PACKET_CODEC = PacketCodec.tuple(
        Rule.PACKET_CODEC.collect(PacketCodecs.toList()), WeaponAttackDamageDataComponent::rules,
        PacketCodecs.DOUBLE, WeaponAttackDamageDataComponent::defaultDamage,
        WeaponAttackDamageDataComponent::new
    );

    public double getDamage(ItemStack stack, Entity entity) {
        for (Rule rule : this.rules) {
            if (rule.damage.isPresent() && rule.matches(stack, entity)) {
                return rule.damage.get();
            }
        }

        return this.defaultDamage;
    }

    public boolean shouldAddBase(ItemStack stack, Entity entity) {
        for (Rule rule : this.rules) {
            if (rule.addBase.isPresent() && rule.matches(stack, entity)) {
                return rule.addBase.get();
            }
        }

        return true;
    }

    public record Rule(Optional<RegistryEntryList<EntityType<?>>> entities, Optional<ItemPredicate> item, Optional<Double> damage, Optional<Boolean> addBase) {
        public static final Codec<Rule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.entryList(RegistryKeys.ENTITY_TYPE).optionalFieldOf("entities").forGetter(Rule::entities),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(Rule::item),
            ItematicCodecs.NON_NEGATIVE_DOUBLE.optionalFieldOf("damage").forGetter(Rule::damage),
            Codec.BOOL.optionalFieldOf("add_base").forGetter(Rule::addBase)
        ).apply(instance, Rule::new));
        public static final PacketCodec<RegistryByteBuf, Rule> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.registryEntryList(RegistryKeys.ENTITY_TYPE).collect(PacketCodecs::optional), Rule::entities,
            ItemPredicateUtil.PACKET_CODEC.collect(PacketCodecs::optional), Rule::item,
            PacketCodecs.DOUBLE.collect(PacketCodecs::optional), Rule::damage,
            PacketCodecs.BOOL.collect(PacketCodecs::optional), Rule::addBase,
            Rule::new
        );

        @SuppressWarnings("deprecation")
        public boolean matches(ItemStack stack, Entity entity) {
            if (this.entities.isPresent() && !this.entities.get().contains(entity.getType().getRegistryEntry())) {
                return false;
            }
            return this.item.map(item -> item.test(stack))
                .orElse(true);
        }
    }
}
