package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record EnchantmentHolderItemComponent() implements ItemComponent {
    public static final EnchantmentHolderItemComponent INSTANCE = new EnchantmentHolderItemComponent();
    public static final Codec<EnchantmentHolderItemComponent> CODEC = Codec.unit(INSTANCE);

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.ENCHANTMENT_HOLDER;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        ItemStack.appendEnchantments(tooltip, EnchantedBookItem.getEnchantmentNbt(stack));
    }
}
