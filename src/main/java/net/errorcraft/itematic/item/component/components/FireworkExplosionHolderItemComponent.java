package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.FireworkStarItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record FireworkExplosionHolderItemComponent() implements ItemComponent {
    public static final Codec<FireworkExplosionHolderItemComponent> CODEC = Codec.unit(new FireworkExplosionHolderItemComponent());

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.FIREWORK_EXPLOSION_HOLDER;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound explosion = stack.getSubNbt(FireworkRocketItem.EXPLOSION_KEY);
        if (explosion != null) {
            FireworkStarItem.appendFireworkTooltip(explosion, tooltip);
        }
    }
}
