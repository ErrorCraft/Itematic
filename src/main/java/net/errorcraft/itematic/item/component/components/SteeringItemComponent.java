package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemSteerable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Optional;

public record SteeringItemComponent(RegistryEntry<EntityType<?>> target, int damage) implements ItemComponent {
    public static final Codec<SteeringItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ENTITY_TYPE).fieldOf("target").forGetter(SteeringItemComponent::target),
        Codec.INT.fieldOf("damage").forGetter(SteeringItemComponent::damage)
    ).apply(instance, SteeringItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.STEERING;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        if (world.isClient()) {
            return TypedActionResult.pass(stack);
        }
        Optional<ItemStack> result = this.apply(user, hand, stack);
        if (result.isPresent()) {
            return TypedActionResult.success(result.get());
        }
        user.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
        return TypedActionResult.pass(stack);
    }

    private Optional<ItemStack> apply(PlayerEntity user, Hand hand, ItemStack stack) {
        Entity vehicle = user.getControllingVehicle();
        if (!user.hasVehicle() || !(vehicle instanceof ItemSteerable itemSteerable)) {
            return Optional.empty();
        }
        return this.apply(vehicle, itemSteerable, stack, user, hand);
    }

    private Optional<ItemStack> apply(Entity vehicle, ItemSteerable itemSteerable, ItemStack stack, PlayerEntity user, Hand hand) {
        Optional<RegistryKey<EntityType<?>>> key = Registries.ENTITY_TYPE.getKey(vehicle.getType());
        if (key.isEmpty()) {
            return Optional.empty();
        }
        if (!this.target.matchesKey(key.get())) {
            return Optional.empty();
        }
        if (!itemSteerable.consumeOnAStickItem()) {
            return Optional.empty();
        }
        stack.damage(this.damage, user, hand);
        return Optional.of(stack);
    }
}
