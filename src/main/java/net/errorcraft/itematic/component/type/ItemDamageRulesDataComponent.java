package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public record ItemDamageRulesDataComponent(List<Rule> rules, int defaultItemDamage) {
    public static final Codec<ItemDamageRulesDataComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Rule.CODEC.listOf().fieldOf("rules").forGetter(ItemDamageRulesDataComponent::rules),
        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("default_damage").forGetter(ItemDamageRulesDataComponent::defaultItemDamage)
    ).apply(instance, ItemDamageRulesDataComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemDamageRulesDataComponent> PACKET_CODEC = StreamCodec.composite(
        Rule.PACKET_CODEC.apply(ByteBufCodecs.list()), ItemDamageRulesDataComponent::rules,
        ByteBufCodecs.VAR_INT, ItemDamageRulesDataComponent::defaultItemDamage,
        ItemDamageRulesDataComponent::new
    );

    public int damage(ItemStack stack) {
        for (Rule rule : this.rules) {
            if (rule.damage.isPresent() && rule.matches(stack)) {
                return rule.damage.get();
            }
        }

        return this.defaultItemDamage;
    }

    public record Rule(HolderSet<Item> items, Optional<Integer> damage) {
        public static final Codec<Rule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("items").forGetter(Rule::items),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("damage").forGetter(Rule::damage)
        ).apply(instance, Rule::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, Rule> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderSet(Registries.ITEM), Rule::items,
            ByteBufCodecs.VAR_INT.apply(ByteBufCodecs::optional), Rule::damage,
            Rule::new
        );

        public static Rule of(HolderSet<Item> items, int damage) {
            return new Rule(items, Optional.of(damage));
        }

        public boolean matches(ItemStack stack) {
            return stack.is(this.items);
        }
    }
}
