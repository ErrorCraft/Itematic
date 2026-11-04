package net.errorcraft.itematic.core.component.provider.number.floats.providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.provider.number.floats.FloatProvider;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.component.CustomModelData;

public record CustomModelDataProvider(int index) implements FloatProvider<CustomModelDataProvider, CustomModelData> {
    public static final MapCodec<CustomModelDataProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("index").forGetter(CustomModelDataProvider::index)
    ).apply(instance, CustomModelDataProvider::new));
    public static final Type<CustomModelDataProvider, CustomModelData> TYPE = Type.simple(CODEC);

    @Override
    public Type<CustomModelDataProvider, CustomModelData> type() {
        return TYPE;
    }

    @Override
    public DataComponentType<CustomModelData> component() {
        return DataComponents.CUSTOM_MODEL_DATA;
    }

    @Override
    public float get(CustomModelData component, DataComponentGetter components) throws ArithmeticException {
        Float value = component.getFloat(this.index);
        if (value == null) {
            throw new ArithmeticException("No float at index " + this.index);
        }

        return value;
    }
}
