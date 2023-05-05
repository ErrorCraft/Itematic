package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.placement.EntityPlacer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.TypedActionResult;

public record EntityItemComponent(EntityType<?> type) implements ItemComponent {
    public static final Codec<EntityItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Registries.ENTITY_TYPE.getCodec().fieldOf("type").forGetter(EntityItemComponent::type)
    ).apply(instance, EntityItemComponent::new));
    private static final String ENTITY_TAG_KEY = "EntityTag";
    private static final String ID_KEY = "id";

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
        EntityType<?> entityType = this.getEntityType(stack.getSubNbt(ENTITY_TAG_KEY));
        EntityPlacer placer = EntityPlacer.of(context, entityType);
        return placer.place();
    }

    private EntityType<?> getEntityType(NbtCompound nbt) {
        if (nbt == null) {
            return this.type;
        }
        if (!nbt.contains(ID_KEY)) {
            return this.type;
        }
        return EntityType.get(nbt.getString(ID_KEY)).orElse(this.type);
    }
}
