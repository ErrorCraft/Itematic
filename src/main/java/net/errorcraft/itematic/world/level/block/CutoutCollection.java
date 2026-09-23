package net.errorcraft.itematic.world.level.block;

import net.errorcraft.itematic.util.ItematicUtil;
import net.errorcraft.itematic.world.item.Items;
import net.minecraft.references.BlockItemId;
import org.jspecify.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public record CutoutCollection<T>(T block, T stairs, T slab, @Nullable T wall) {
    public static final CutoutCollection<String> WALL_SUFFIXES = new CutoutCollection<>(
        "",
        "_stairs",
        "_slab",
        "_wall"
    );

    public static <T> CutoutCollection<T> create(T value) {
        return new CutoutCollection<>(value, value, value, value);
    }

    public static <T> CutoutCollection<T> create(T value, T block) {
        return new CutoutCollection<>(block, value, value, value);
    }

    public static <T> CutoutCollection<T> createWithoutWall(T value) {
        return new CutoutCollection<T>(value, value, value, null);
    }

    public static CutoutCollection<String> suffixWithTypeWall(CutoutCollection<String> ids) {
        return zipMap(ids, WALL_SUFFIXES, (id, type) -> id + type);
    }

    public static <T, U, R> CutoutCollection<R> zipMap(CutoutCollection<T> first, CutoutCollection<U> second, BiFunction<T, U, R> operation) {
        return new CutoutCollection<>(
            operation.apply(first.block, second.block),
            operation.apply(first.stairs, second.stairs),
            operation.apply(first.slab, second.slab),
            ItematicUtil.applyNullable(first.wall, second.wall, operation)
        );
    }

    public static void registerItems(CutoutCollection<BlockItemId> blockItems, Items.Bootstrapper bootstrapper) {
        blockItems.forEach(bootstrapper::registerBlock);
    }

    public static void registerBurningItems(CutoutCollection<BlockItemId> blockItems, Items.Bootstrapper bootstrapper) {
        bootstrapper.registerBurningWoodBlock(blockItems.block);
        bootstrapper.registerBurningWoodBlock(blockItems.stairs);
        bootstrapper.registerBurningSlab(blockItems.slab);
        if (blockItems.wall != null) {
            bootstrapper.registerBurningWoodBlock(blockItems.wall);
        }
    }

    public <U> CutoutCollection<U> map(Function<T, U> mapper) {
        return new CutoutCollection<>(
            mapper.apply(this.block),
            mapper.apply(this.stairs),
            mapper.apply(this.slab),
            ItematicUtil.applyNullable(this.wall, mapper)
        );
    }

    public void forEach(Consumer<T> consumer) {
        consumer.accept(this.block);
        consumer.accept(this.stairs);
        consumer.accept(this.slab);
        if (this.wall != null) {
            consumer.accept(this.wall);
        }
    }
}
