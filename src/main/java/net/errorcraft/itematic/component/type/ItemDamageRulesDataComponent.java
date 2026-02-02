package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;
import java.util.Optional;

public record ItemDamageRulesDataComponent(List<Rule> rules, int defaultItemDamage) {
    public static final Codec<ItemDamageRulesDataComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Rule.CODEC.listOf().fieldOf("rules").forGetter(ItemDamageRulesDataComponent::rules),
        Codecs.NON_NEGATIVE_INT.fieldOf("default_damage").forGetter(ItemDamageRulesDataComponent::defaultItemDamage)
    ).apply(instance, ItemDamageRulesDataComponent::new));
    public static final PacketCodec<RegistryByteBuf, ItemDamageRulesDataComponent> PACKET_CODEC = PacketCodec.tuple(
        Rule.PACKET_CODEC.collect(PacketCodecs.toList()), ItemDamageRulesDataComponent::rules,
        PacketCodecs.VAR_INT, ItemDamageRulesDataComponent::defaultItemDamage,
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

    public record Rule(RegistryEntryList<Item> items, Optional<Integer> damage) {
        public static final Codec<Rule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.entryList(RegistryKeys.ITEM).fieldOf("items").forGetter(Rule::items),
            Codecs.NON_NEGATIVE_INT.optionalFieldOf("damage").forGetter(Rule::damage)
        ).apply(instance, Rule::new));
        public static final PacketCodec<RegistryByteBuf, Rule> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.registryEntryList(RegistryKeys.ITEM), Rule::items,
            PacketCodecs.VAR_INT.collect(PacketCodecs::optional), Rule::damage,
            Rule::new
        );

        public static Rule of(RegistryEntryList<Item> items, int damage) {
            return new Rule(items, Optional.of(damage));
        }

        public boolean matches(ItemStack stack) {
            return stack.isIn(this.items);
        }
    }
}
