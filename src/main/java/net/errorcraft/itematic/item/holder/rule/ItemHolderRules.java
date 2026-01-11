package net.errorcraft.itematic.item.holder.rule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.predicate.item.ItemPredicateUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.predicate.item.ItemPredicate;
import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ItemHolderRules(List<Rule> rules) {
    public static final Codec<ItemHolderRules> CODEC = Rule.CODEC.listOf().xmap(ItemHolderRules::new, ItemHolderRules::rules);
    public static final PacketCodec<RegistryByteBuf, ItemHolderRules> PACKET_CODEC = Rule.PACKET_CODEC.collect(PacketCodecs.toList()).xmap(ItemHolderRules::new, ItemHolderRules::rules);

    public static Builder builder() {
        return new Builder();
    }

    public Fraction occupancy(ItemStack stack) {
        for (Rule rule : this.rules) {
            if (rule.test(stack)) {
                return rule.rule.occupancy(stack);
            }
        }
        return Fraction.getFraction(1, stack.getMaxCount());
    }

    public boolean canOccupy(ItemStack stack) {
        for (Rule rule : this.rules) {
            if (rule.test(stack)) {
                return rule.rule.canOccupy(stack);
            }
        }
        return true;
    }

    public static class Builder {
        private final List<Rule> rules = new ArrayList<>();

        public ItemHolderRules build() {
            return new ItemHolderRules(this.rules);
        }

        public Builder rule(ItemHolderRule rule) {
            this.rules.add(new Rule(Optional.empty(), rule));
            return this;
        }

        public Builder rule(ItemHolderRule rule, ItemPredicate condition) {
            this.rules.add(new Rule(Optional.of(condition), rule));
            return this;
        }
    }

    public record Rule(Optional<ItemPredicate> condition, ItemHolderRule rule) {
        public static final Codec<Rule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemPredicate.CODEC.optionalFieldOf("condition").forGetter(Rule::condition),
            ItemHolderRule.CODEC.forGetter(Rule::rule)
        ).apply(instance, Rule::new));
        public static final PacketCodec<RegistryByteBuf, Rule> PACKET_CODEC = PacketCodec.tuple(
            ItemPredicateUtil.PACKET_CODEC.collect(PacketCodecs::optional), Rule::condition,
            ItemHolderRule.PACKET_CODEC, Rule::rule,
            Rule::new
        );

        public boolean test(ItemStack stack) {
            return this.condition.map(predicate -> predicate.test(stack))
                .orElse(true);
        }
    }
}
