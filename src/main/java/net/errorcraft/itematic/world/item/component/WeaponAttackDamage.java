package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.predicate.item.ItemPredicates;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import java.util.Optional;

public record WeaponAttackDamage(List<Rule> rules, double defaultDamage) {
    public static final Codec<WeaponAttackDamage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Rule.CODEC.listOf().fieldOf("rules").forGetter(WeaponAttackDamage::rules),
        ItematicCodecs.NON_NEGATIVE_DOUBLE.fieldOf("default_damage").forGetter(WeaponAttackDamage::defaultDamage)
    ).apply(instance, WeaponAttackDamage::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, WeaponAttackDamage> PACKET_CODEC = StreamCodec.composite(
        Rule.PACKET_CODEC.apply(ByteBufCodecs.list()), WeaponAttackDamage::rules,
        ByteBufCodecs.DOUBLE, WeaponAttackDamage::defaultDamage,
        WeaponAttackDamage::new
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

    public record Rule(Optional<HolderSet<EntityType<?>>> entities, Optional<ItemPredicate> item, Optional<Double> damage, Optional<Boolean> addBase) {
        public static final Codec<Rule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).optionalFieldOf("entities").forGetter(Rule::entities),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(Rule::item),
            ItematicCodecs.NON_NEGATIVE_DOUBLE.optionalFieldOf("damage").forGetter(Rule::damage),
            Codec.BOOL.optionalFieldOf("add_base").forGetter(Rule::addBase)
        ).apply(instance, Rule::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, Rule> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderSet(Registries.ENTITY_TYPE).apply(ByteBufCodecs::optional), Rule::entities,
            ItemPredicates.PACKET_CODEC.apply(ByteBufCodecs::optional), Rule::item,
            ByteBufCodecs.DOUBLE.apply(ByteBufCodecs::optional), Rule::damage,
            ByteBufCodecs.BOOL.apply(ByteBufCodecs::optional), Rule::addBase,
            Rule::new
        );

        @SuppressWarnings("deprecation")
        public boolean matches(ItemStack stack, Entity entity) {
            if (this.entities.isPresent() && !this.entities.get().contains(entity.getType().builtInRegistryHolder())) {
                return false;
            }
            return this.item.map(item -> item.test(stack))
                .orElse(true);
        }
    }
}
