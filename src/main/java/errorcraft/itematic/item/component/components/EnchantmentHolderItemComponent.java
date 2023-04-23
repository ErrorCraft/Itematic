package errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import errorcraft.itematic.item.component.ItemComponent;
import errorcraft.itematic.item.component.ItemComponentType;
import errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record EnchantmentHolderItemComponent(String enchantmentsKey) implements ItemComponent {
    public static final Codec<EnchantmentHolderItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("enchantments_key").forGetter(EnchantmentHolderItemComponent::enchantmentsKey)
    ).apply(instance, EnchantmentHolderItemComponent::new));

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
        ItemStack.appendEnchantments(tooltip, this.getEnchantments(stack));
    }

    public NbtList getEnchantments(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null) {
            return nbt.getList(this.enchantmentsKey, NbtElement.COMPOUND_TYPE);
        }
        return new NbtList();
    }
}
