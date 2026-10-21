package net.errorcraft.itematic.world.item.weapon.shooter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.ItematicCodecs;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public record ChargedPowerRules(List<Rule> rules, float defaultPower) {
    public static final Codec<ChargedPowerRules> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Rule.CODEC.listOf().fieldOf("rules").forGetter(ChargedPowerRules::rules),
        ItematicCodecs.NON_NEGATIVE_FLOAT.fieldOf("default_power").forGetter(ChargedPowerRules::defaultPower)
    ).apply(instance, ChargedPowerRules::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ChargedPowerRules> STREAM_CODEC = StreamCodec.composite(
        Rule.STREAM_CODEC.apply(ByteBufCodecs.list()), ChargedPowerRules::rules,
        ByteBufCodecs.FLOAT, ChargedPowerRules::defaultPower,
        ChargedPowerRules::new
    );

    public float power(ItemStack stack) {
        for (Rule rule : this.rules) {
            if (rule.power.isPresent() && rule.matches(stack)) {
                return rule.power.get();
            }
        }

        return this.defaultPower;
    }

    public record Rule(HolderSet<Item> items, Optional<Float> power) {
        public static final Codec<Rule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.holderSet(Registries.ITEM).fieldOf("items").forGetter(Rule::items),
            ItematicCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("power").forGetter(Rule::power)
        ).apply(instance, Rule::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, Rule> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderSet(Registries.ITEM), Rule::items,
            ByteBufCodecs.FLOAT.apply(ByteBufCodecs::optional), Rule::power,
            Rule::new
        );

        public static Rule of(HolderSet<Item> items, float power) {
            return new Rule(items, Optional.of(power));
        }

        public boolean matches(ItemStack stack) {
            return stack.is(this.items);
        }
    }
}
