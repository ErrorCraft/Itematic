package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public record FireworkExplosionHolderItemComponent() implements ItemComponent<FireworkExplosionHolderItemComponent> {
    public static final FireworkExplosionHolderItemComponent INSTANCE = new FireworkExplosionHolderItemComponent();
    public static final Codec<FireworkExplosionHolderItemComponent> CODEC = Codec.unit(INSTANCE);
    private static final FireworksComponent DEFAULT_COMPONENT = new FireworksComponent(1, List.of());

    @Override
    public ItemComponentType<FireworkExplosionHolderItemComponent> type() {
        return ItemComponentTypes.FIREWORK_EXPLOSION_HOLDER;
    }

    @Override
    public Codec<FireworkExplosionHolderItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.FIREWORKS, DEFAULT_COMPONENT);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        FireworkExplosionComponent explosion = stack.get(DataComponentTypes.FIREWORK_EXPLOSION);
        if (explosion != null) {
            explosion.appendTooltip(context, tooltip::add, type);
        }
    }
}
