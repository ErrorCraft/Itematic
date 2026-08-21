package net.errorcraft.itematic.world.item.holder.rule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.advancements.criterion.ItemPredicates;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ItemHolderRules(List<Rule> rules) {
    public static final Codec<ItemHolderRules> CODEC = Rule.CODEC.listOf().xmap(ItemHolderRules::new, ItemHolderRules::rules);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemHolderRules> STREAM_CODEC = Rule.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ItemHolderRules::new, ItemHolderRules::rules);

    public static Builder builder() {
        return new Builder();
    }

    public Fraction occupancy(ItemStack stack) {
        for (Rule rule : this.rules) {
            if (rule.test(stack)) {
                return rule.rule.occupancy(stack);
            }
        }

        return Fraction.getFraction(1, stack.getMaxStackSize());
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
        public static final StreamCodec<RegistryFriendlyByteBuf, Rule> STREAM_CODEC = StreamCodec.composite(
            ItemPredicates.STREAM_CODEC.apply(ByteBufCodecs::optional), Rule::condition,
            ItemHolderRule.STREAM_CODEC, Rule::rule,
            Rule::new
        );

        public boolean test(ItemStack stack) {
            return this.condition.map(predicate -> predicate.test(stack))
                .orElse(true);
        }
    }
}
