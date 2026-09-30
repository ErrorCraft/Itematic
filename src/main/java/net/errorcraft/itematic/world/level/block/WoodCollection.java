package net.errorcraft.itematic.world.level.block;

import net.errorcraft.itematic.world.item.Items;
import net.errorcraft.itematic.world.item.behavior.behaviors.CookingFuelItemBehavior;
import net.minecraft.core.Direction;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import org.jspecify.annotations.Nullable;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public record WoodCollection<T>(CutoutCollection<T> planks, Strippable<T> log, @Nullable Strippable<T> wood, T sapling, MultiStateCollection<T> leaves, T fenceGate, T sign, T hangingSign, T door, T trapdoor, T button, T pressurePlate, T shelf) {
    public static final WoodCollection<String> PREFIXES = new WoodCollection<>(
        CutoutCollection.create(""),
        Strippable.PREFIXES,
        Strippable.PREFIXES,
        "",
        MultiStateCollection.create(""),
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    );
    public static final WoodCollection<String> SUFFIXES = suffixes("_log", "_wood", "_sapling");
    public static final WoodCollection<String> SUFFIXES_MANGROVE = suffixes("_log", "_wood", "_propagule");
    public static final WoodCollection<String> SUFFIXES_BAMBOO = suffixes("_block", null, "");
    public static final WoodCollection<String> SUFFIXES_NETHER = suffixes("_stem", "_hyphae", "_fungus");

    private static WoodCollection<String> suffixes(String log, @Nullable String wood, String sapling) {
        return new WoodCollection<>(
            CutoutCollection.create("_planks", "", "", ""),
            Strippable.create(log),
            Strippable.create(wood),
            sapling,
            MultiStateCollection.create("_leaves"),
            "_fence_gate",
            "_sign",
            "_hanging_sign",
            "_door",
            "_trapdoor",
            "_button",
            "_pressure_plate",
            "_shelf"
        );
    }

    public static Builder builder(String material) {
        return new Builder(material);
    }

    public static WoodCollection<String> create(String value, Set<String> leaves) {
        return new WoodCollection<>(
            CutoutCollection.builder(value).fence().build(),
            Strippable.create(value),
            Strippable.create(value),
            value,
            MultiStateCollection.create(leaves),
            value,
            value,
            value,
            value,
            value,
            value,
            value,
            value
        );
    }

    public static <T, U, R> WoodCollection<R> zipMap(WoodCollection<T> first, WoodCollection<U> second, BiFunction<T, U, R> operation) {
        return new WoodCollection<>(
            CutoutCollection.zipMap(first.planks, second.planks, operation),
            Strippable.zipMap(first.log, second.log, operation),
            Strippable.zipMap(first.wood, second.wood, operation),
            operation.apply(first.sapling, second.sapling),
            MultiStateCollection.zipMap(first.leaves, second.leaves, operation),
            operation.apply(first.fenceGate, second.fenceGate),
            operation.apply(first.sign, second.sign),
            operation.apply(first.hangingSign, second.hangingSign),
            operation.apply(first.door, second.door),
            operation.apply(first.trapdoor, second.trapdoor),
            operation.apply(first.button, second.button),
            operation.apply(first.pressurePlate, second.pressurePlate),
            operation.apply(first.shelf, second.shelf)
        );
    }

    public static WoodCollection<String> affixWithType(WoodCollection<String> ids, WoodCollection<String> suffixes) {
        return zipMap(
            zipMap(PREFIXES, ids, (prefix, id) -> prefix + id),
            suffixes,
            (prefixedId, suffix) -> prefixedId + suffix
        );
    }

    public static void registerItems(WoodCollection<BlockItemId> blockItems, Items.Bootstrapper bootstrapper, ResourceKey<Block> wallSign, ResourceKey<Block> hangingWallSign, ResourceKey<Block> pottedSapling) {
        CutoutCollection.registerItems(blockItems.planks, bootstrapper);
        blockItems.log.forEach(bootstrapper::registerBlock);
        if (blockItems.wood != null) {
            blockItems.wood.forEach(bootstrapper::registerBlock);
        }

        bootstrapper.registerPottableSapling(
            blockItems.sapling,
            pottedSapling,
            null,
            NumberProviders.COMPOSTABLE_MEDIUM
        );
        blockItems.leaves.forEach(bootstrapper::registerCompostableLeaves);
        bootstrapper.registerBlock(blockItems.fenceGate);
        bootstrapper.builderForBlockAttachedToSide(blockItems.sign, wallSign, Direction.DOWN, 16)
            .register();
        bootstrapper.builderForBlockAttachedToSide(blockItems.hangingSign, hangingWallSign, Direction.UP, 16)
            .register();
        bootstrapper.registerBlock(blockItems.door);
        bootstrapper.registerBlock(blockItems.trapdoor);
        bootstrapper.registerBlock(blockItems.button);
        bootstrapper.registerBlock(blockItems.pressurePlate);
        bootstrapper.registerBlock(blockItems.shelf);
    }

    public static void registerItems(WoodCollection<BlockItemId> blockItems, Items.Bootstrapper bootstrapper, ResourceKey<Block> wallSign, ResourceKey<Block> hangingWallSign, ResourceKey<Block> pottedSapling, ResourceKey<NumberProvider> saplingBurnTime) {
        CutoutCollection.registerBurningItems(blockItems.planks, bootstrapper);
        blockItems.log.forEach(log -> bootstrapper.registerBurningBlock(log, NumberProviders.COOKING_TIME_WOOD_BLOCKS));
        if (blockItems.wood != null) {
            blockItems.wood.forEach(wood -> bootstrapper.registerBurningBlock(wood, NumberProviders.COOKING_TIME_WOOD_BLOCKS));
        }

        bootstrapper.registerPottableSapling(
            blockItems.sapling,
            pottedSapling,
            saplingBurnTime,
            saplingBurnTime == NumberProviders.COOKING_TIME_DRY_PLANTS ? NumberProviders.COMPOSTABLE_LOW : null
        );
        blockItems.leaves.forEach(bootstrapper::registerCompostableLeaves);
        bootstrapper.registerBurningBlock(blockItems.fenceGate, NumberProviders.COOKING_TIME_WOOD_BLOCKS);
        bootstrapper.builderForBlockAttachedToSide(blockItems.sign, wallSign, Direction.DOWN, 16)
            .behavior(CookingFuelItemBehavior.of(NumberProviders.COOKING_TIME_WOOD_ITEMS_LARGE))
            .register();
        bootstrapper.builderForBlockAttachedToSide(blockItems.hangingSign, hangingWallSign, Direction.UP, 16)
            .behavior(CookingFuelItemBehavior.of(NumberProviders.COOKING_TIME_HANGING_SIGNS))
            .register();
        bootstrapper.registerBurningBlock(blockItems.door, NumberProviders.COOKING_TIME_WOOD_ITEMS_LARGE);
        bootstrapper.registerBurningBlock(blockItems.trapdoor, NumberProviders.COOKING_TIME_WOOD_BLOCKS);
        bootstrapper.registerBurningBlock(blockItems.button, NumberProviders.COOKING_TIME_WOOD_ITEMS_EXTRA_SMALL);
        bootstrapper.registerBurningBlock(blockItems.pressurePlate, NumberProviders.COOKING_TIME_WOOD_BLOCKS);
        bootstrapper.registerBurningBlock(blockItems.shelf, NumberProviders.COOKING_TIME_WOOD_BLOCKS);
    }

    public <U> WoodCollection<U> map(Function<T, U> mapper) {
        return new WoodCollection<>(
            this.planks.map(mapper),
            this.log.map(mapper),
            Strippable.map(this.wood, mapper),
            mapper.apply(this.sapling),
            this.leaves.map(mapper),
            mapper.apply(this.fenceGate),
            mapper.apply(this.sign),
            mapper.apply(this.hangingSign),
            mapper.apply(this.door),
            mapper.apply(this.trapdoor),
            mapper.apply(this.button),
            mapper.apply(this.pressurePlate),
            mapper.apply(this.shelf)
        );
    }

    public record Strippable<T>(T unstripped, T stripped) {
        public static final Strippable<String> PREFIXES = new Strippable<>(
            "",
            "stripped_"
        );

        @Nullable
        public static <T> Strippable<T> create(@Nullable T value) {
            if (value == null) {
                return null;
            }

            return new Strippable<T>(value, value);
        }

        @Nullable
        public static <T, U, R> Strippable<R> zipMap(@Nullable Strippable<T> first, @Nullable Strippable<U> second, BiFunction<T, U, R> operation) {
            if (first == null || second == null) {
                return null;
            }

            return new Strippable<>(
                operation.apply(first.unstripped, second.unstripped),
                operation.apply(first.stripped, second.stripped)
            );
        }

        @Nullable
        public static <T, U> Strippable<U> map(WoodCollection.@Nullable Strippable<T> value, Function<T, U> mapper) {
            if (value == null) {
                return null;
            }

            return value.map(mapper);
        }

        public <U> Strippable<U> map(Function<T, U> mapper) {
            return new Strippable<>(
                mapper.apply(this.unstripped),
                mapper.apply(this.stripped)
            );
        }

        public void forEach(Consumer<T> consumer) {
            consumer.accept(this.unstripped);
            consumer.accept(this.stripped);
        }
    }

    public static class Builder {
        private final String material;
        private Set<String> leaves;
        private WoodCollection<String> suffixes = SUFFIXES;

        private Builder(String material) {
            this.material = material;
            this.leaves = Set.of(material);
        }

        public WoodCollection<String> build() {
            return WoodCollection.affixWithType(WoodCollection.create(this.material, this.leaves), this.suffixes);
        }

        public Builder noLeaves() {
            this.leaves = Set.of();
            return this;
        }

        public Builder leaves(String... leaves) {
            this.leaves = Set.of(leaves);
            return this;
        }

        public Builder suffixes(WoodCollection<String> suffixes) {
            this.suffixes = suffixes;
            return this;
        }
    }
}
