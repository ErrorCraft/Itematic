package net.errorcraft.itematic.predicate.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.network.codec.PacketCodecUtil;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.component.DataComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;

import java.util.Optional;
import java.util.Set;

public record ItemPredicateExtraFields(Optional<Set<ItemComponentType<?>>> behavior, Optional<Set<DataComponentType<?>>> dataComponents) {
    public static final MapCodec<ItemPredicateExtraFields> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItematicCodecs.setCodec(ItematicRegistries.ITEM_COMPONENT_TYPE.getCodec()).optionalFieldOf("behavior").forGetter(ItemPredicateExtraFields::behavior),
        ItematicCodecs.setCodec(Registries.DATA_COMPONENT_TYPE.getCodec()).optionalFieldOf("data_components").forGetter(ItemPredicateExtraFields::dataComponents)
    ).apply(instance, ItemPredicateExtraFields::new));
    public static final PacketCodec<RegistryByteBuf, ItemPredicateExtraFields> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.registryValue(ItematicRegistryKeys.ITEM_COMPONENT_TYPE).collect(PacketCodecUtil::set).collect(PacketCodecs::optional), ItemPredicateExtraFields::behavior,
        PacketCodecs.registryValue(RegistryKeys.DATA_COMPONENT_TYPE).collect(PacketCodecUtil::set).collect(PacketCodecs::optional), ItemPredicateExtraFields::dataComponents,
        ItemPredicateExtraFields::new
    );

    public static ItemPredicateExtraFields of(Set<ItemComponentType<?>> behavior, Set<DataComponentType<?>> dataComponents) {
        return new ItemPredicateExtraFields(
            behavior.isEmpty() ? Optional.empty() : Optional.of(behavior),
            dataComponents.isEmpty() ? Optional.empty() : Optional.of(dataComponents)
        );
    }

    public boolean testExtraFields(ItemStack stack) {
        if (!this.testBehavior(stack)) {
            return false;
        }
        return this.testDataComponents(stack);
    }

    private boolean testBehavior(ItemStack stack) {
        if (this.behavior.isEmpty()) {
            return true;
        }
        for (ItemComponentType<?> type : this.behavior.get()) {
            if (!stack.itematic$hasComponent(type)) {
                return false;
            }
        }
        return true;
    }

    private boolean testDataComponents(ItemStack stack) {
        if (this.dataComponents.isEmpty()) {
            return true;
        }
        for (DataComponentType<?> type : this.dataComponents.get()) {
            if (!stack.contains(type)) {
                return false;
            }
        }
        return true;
    }
}
