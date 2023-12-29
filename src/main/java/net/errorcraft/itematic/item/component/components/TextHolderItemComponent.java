package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record TextHolderItemComponent() implements ItemComponent {
    public static final TextHolderItemComponent INSTANCE = new TextHolderItemComponent();
    public static final Codec<TextHolderItemComponent> CODEC = Codec.unit(INSTANCE);

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.TEXT_HOLDER;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        stack.itematic$nbt().ifPresent(nbt -> {
            String author = nbt.getString(WrittenBookItem.AUTHOR_KEY);
            if (!StringHelper.isEmpty(author)) {
                tooltip.add(Text.translatable("book.byAuthor", author).formatted(Formatting.GRAY));
            }
            tooltip.add(Text.translatable("book.generation." + nbt.getInt(WrittenBookItem.GENERATION_KEY)).formatted(Formatting.GRAY));
        });
    }
}
