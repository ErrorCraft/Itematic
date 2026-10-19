package net.errorcraft.itematic.core.component.provider.number.ints.providers;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.provider.number.ints.IntProvider;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

public record CustomDataProvider(NbtPathArgument.NbtPath path) implements IntProvider<CustomDataProvider, CustomData> {
    public static final MapCodec<CustomDataProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        NbtPathArgument.NbtPath.CODEC.fieldOf("path").forGetter(CustomDataProvider::path)
    ).apply(instance, CustomDataProvider::new));
    public static final Type<CustomDataProvider, CustomData> TYPE = Type.simple(CODEC);

    @Override
    public Type<CustomDataProvider, CustomData> type() {
        return TYPE;
    }

    @Override
    public DataComponentType<CustomData> component() {
        return DataComponents.CUSTOM_DATA;
    }

    @Override
    public int get(CustomData component, DataComponentGetter components) throws ArithmeticException {
        try {
            List<Tag> foundTags = this.path.get(component.copyTag());
            if (foundTags.isEmpty()) {
                throw new ArithmeticException("No tags matched");
            }

            if (foundTags.size() > 1) {
                throw new ArithmeticException("Multiple tags matched, expected only one");
            }

            Tag tag = foundTags.getFirst();
            if (tag instanceof NumericTag numeric) {
                return numeric.intValue();
            }

            throw new ArithmeticException("Not a number: " + tag);
        } catch (CommandSyntaxException e) {
            throw new ArithmeticException(e.getMessage());
        }
    }
}
