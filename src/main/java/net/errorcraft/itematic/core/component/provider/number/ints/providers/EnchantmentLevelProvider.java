package net.errorcraft.itematic.core.component.provider.number.ints.providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.provider.number.ints.IntProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.function.Function;

public abstract class EnchantmentLevelProvider implements IntProvider<EnchantmentLevelProvider, ItemEnchantments> {
    protected final Holder<Enchantment> enchantment;

    public EnchantmentLevelProvider(Holder<Enchantment> enchantment) {
        this.enchantment = enchantment;
    }

    protected static <T extends EnchantmentLevelProvider> MapCodec<T> codec(Function<Holder<Enchantment>, T> constructor) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            Enchantment.CODEC.fieldOf("enchantment").forGetter(t -> t.enchantment)
        ).apply(instance, constructor));
    }

    @Override
    public int get(ItemEnchantments component, DataComponentGetter components) throws ArithmeticException {
        return component.getLevel(this.enchantment);
    }

    public static class Enchantments extends EnchantmentLevelProvider {
        public static final MapCodec<Enchantments> CODEC = codec(Enchantments::new);
        public static final Type<EnchantmentLevelProvider, ItemEnchantments> TYPE = Type.simple(CODEC);

        public Enchantments(Holder<Enchantment> enchantment) {
            super(enchantment);
        }

        @Override
        public Type<EnchantmentLevelProvider, ItemEnchantments> type() {
            return TYPE;
        }

        @Override
        public DataComponentType<ItemEnchantments> component() {
            return DataComponents.ENCHANTMENTS;
        }
    }

    public static class StoredEnchantments extends EnchantmentLevelProvider {
        public static final MapCodec<StoredEnchantments> CODEC = codec(StoredEnchantments::new);
        public static final Type<EnchantmentLevelProvider, ItemEnchantments> TYPE = Type.simple(CODEC);

        public StoredEnchantments(Holder<Enchantment> enchantment) {
            super(enchantment);
        }

        @Override
        public Type<EnchantmentLevelProvider, ItemEnchantments> type() {
            return TYPE;
        }

        @Override
        public DataComponentType<ItemEnchantments> component() {
            return DataComponents.STORED_ENCHANTMENTS;
        }
    }
}
