package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.placement.EntityPlacer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.TypedActionResult;

public record EntityItemComponent(RegistryEntry<EntityType<?>> type) implements ItemComponent {
    public static final Codec<EntityItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ENTITY_TYPE).fieldOf("type").forGetter(EntityItemComponent::type)
    ).apply(instance, EntityItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.ENTITY;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public TypedActionResult<ItemStack> useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        if (context.getWorld().isClient()) {
            return TypedActionResult.success(stack);
        }
        EntityType<?> entityType = this.getEntityType(stack);
        EntityPlacer placer = EntityPlacer.spawned(context, entityType);
        return placer.place();
    }

    public EntityType<?> getEntityType(ItemStack stack) {
        NbtCompound nbt = stack.getSubNbt(EntityType.ENTITY_TAG_KEY);
        if (nbt == null) {
            return this.type.value();
        }
        if (!nbt.contains(Entity.ID_KEY)) {
            return this.type.value();
        }
        return EntityType.get(nbt.getString(Entity.ID_KEY)).orElse(this.type.value());
    }
}
