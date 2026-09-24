package net.errorcraft.itematic.world.level.block;

import net.errorcraft.itematic.util.ItematicUtil;
import net.errorcraft.itematic.world.item.Items;
import net.minecraft.references.BlockItemId;
import org.jspecify.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public record CutoutCollection<T>(T block, @Nullable T stairs, T slab, @Nullable T wall) {
    private static final CutoutCollection<String> WALL_SUFFIXES = new CutoutCollection<>(
        "",
        "_stairs",
        "_slab",
        "_wall"
    );
    private static final CutoutCollection<String> FENCE_SUFFIXES = new CutoutCollection<>(
        "",
        "_stairs",
        "_slab",
        "_fence"
    );

    public static Builder builder(String material) {
        return new Builder(material);
    }

    public static <T> CutoutCollection<T> create(T value) {
        return new CutoutCollection<>(value, value, value, value);
    }

    public static <T> CutoutCollection<T> create(T block, T stairs, T slab, T wall) {
        return new CutoutCollection<>(block, stairs, slab, wall);
    }

    public static CutoutCollection<String> suffixWithType(CutoutCollection<String> ids, CutoutCollection<String> suffixes) {
        return zipMap(ids, suffixes, (id, type) -> id + type);
    }

    public static <T, U, R> CutoutCollection<R> zipMap(CutoutCollection<T> first, CutoutCollection<U> second, BiFunction<T, U, R> operation) {
        return new CutoutCollection<>(
            operation.apply(first.block, second.block),
            ItematicUtil.applyNullable(first.stairs, second.stairs, operation),
            operation.apply(first.slab, second.slab),
            ItematicUtil.applyNullable(first.wall, second.wall, operation)
        );
    }

    public static void registerItems(CutoutCollection<BlockItemId> blockItems, Items.Bootstrapper bootstrapper) {
        blockItems.forEach(bootstrapper::registerBlock);
    }

    public static void registerBurningItems(CutoutCollection<BlockItemId> blockItems, Items.Bootstrapper bootstrapper) {
        bootstrapper.registerBurningWoodBlock(blockItems.block);
        if (blockItems.stairs != null) {
            bootstrapper.registerBurningWoodBlock(blockItems.stairs);
        }

        bootstrapper.registerBurningSlab(blockItems.slab);
        if (blockItems.wall != null) {
            bootstrapper.registerBurningWoodBlock(blockItems.wall);
        }
    }

    public <U> CutoutCollection<U> map(Function<T, U> mapper) {
        return new CutoutCollection<>(
            mapper.apply(this.block),
            ItematicUtil.applyNullable(this.stairs, mapper),
            mapper.apply(this.slab),
            ItematicUtil.applyNullable(this.wall, mapper)
        );
    }

    public void forEach(Consumer<T> consumer) {
        consumer.accept(this.block);
        if (this.stairs != null) {
            consumer.accept(this.stairs);
        }

        consumer.accept(this.slab);
        if (this.wall != null) {
            consumer.accept(this.wall);
        }
    }

    public static class Builder {
        private final String name;
        private String blockName;
        private boolean stairs = true;
        private boolean wall = true;
        private CutoutCollection<String> suffixes = WALL_SUFFIXES;

        private Builder(String name) {
            this.name = name;
            this.blockName = name;
        }

        public CutoutCollection<String> build() {
            return suffixWithType(
                new CutoutCollection<String>(
                    this.blockName,
                    this.stairs ? this.name : null,
                    this.name,
                    this.wall ? this.name : null
                ),
                this.suffixes
            );
        }

        public Builder block(String name) {
            this.blockName = name;
            return this;
        }

        public Builder noStairs() {
            this.stairs = false;
            return this;
        }

        public Builder noWall() {
            this.wall = false;
            return this;
        }

        public Builder fence() {
            if (!this.wall) {
                throw new IllegalStateException("Cutout does not have a fence");
            }

            this.suffixes = FENCE_SUFFIXES;
            return this;
        }
    }
}
