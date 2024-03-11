package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.WrittenBookContentComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record TextHolderItemComponent() implements ItemComponent<TextHolderItemComponent> {
    public static final TextHolderItemComponent INSTANCE = new TextHolderItemComponent();
    public static final Codec<TextHolderItemComponent> CODEC = Codec.unit(INSTANCE);

    @Override
    public ItemComponentType<TextHolderItemComponent> type() {
        return ItemComponentTypes.TEXT_HOLDER;
    }

    @Override
    public Codec<TextHolderItemComponent> codec() {
        return CODEC;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        WrittenBookContentComponent writtenBookContent = stack.get(DataComponentTypes.WRITTEN_BOOK_CONTENT);
        if (writtenBookContent == null) {
            return;
        }
        if (!StringHelper.isBlank(writtenBookContent.author())) {
            tooltip.add(Text.translatable("book.byAuthor", writtenBookContent.author()).formatted(Formatting.GRAY));
        }
        tooltip.add(Text.translatable("book.generation." + writtenBookContent.generation()).formatted(Formatting.GRAY));
    }
}
