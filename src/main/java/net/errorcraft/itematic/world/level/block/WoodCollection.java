package net.errorcraft.itematic.world.level.block;

import net.errorcraft.itematic.util.ItematicUtil;
import net.errorcraft.itematic.world.item.Items;
import net.errorcraft.itematic.world.item.behavior.behaviors.FuelItemBehavior;
import net.minecraft.core.Direction;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public record WoodCollection<T>(CutoutCollection<T> planks, Strippable<T> log, @Nullable Strippable<T> wood, T sapling, @Nullable T leaves, T fenceGate, T sign, T hangingSign, T door, T trapdoor, T button, T pressurePlate, T shelf) {
    public static final WoodCollection<String> PREFIXES = new WoodCollection<>(
        CutoutCollection.create(""),
        Strippable.PREFIXES,
        Strippable.PREFIXES,
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    );
    public static final WoodCollection<String> SUFFIXES = suffixes("_log", "_wood", "_sapling", "_leaves");
    public static final WoodCollection<String> SUFFIXES_MANGROVE = suffixes("_log", "_wood", "_propagule", "_leaves");
    public static final WoodCollection<String> SUFFIXES_BAMBOO = suffixes("_block", null, "", null);
    public static final WoodCollection<String> SUFFIXES_NETHER = suffixes("_stem", "_hyphae", "_fungus", null);

    private static WoodCollection<String> suffixes(String log, @Nullable String wood, String sapling, @Nullable String leaves) {
        return new WoodCollection<>(
            CutoutCollection.create("_planks", "", "", ""),
            Strippable.create(log),
            Strippable.create(wood),
            sapling,
            leaves,
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

    public static WoodCollection<String> create(String value) {
        return new WoodCollection<>(
            CutoutCollection.builder(value).fence().build(),
            Strippable.create(value),
            Strippable.create(value),
            value,
            value,
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
            ItematicUtil.applyNullable(first.leaves, second.leaves, operation),
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
            0,
            CompostChances.BIG
        );
        if (blockItems.leaves != null) {
            bootstrapper.registerCompostableLeaves(blockItems.leaves);
        }

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

    public static void registerItems(WoodCollection<BlockItemId> blockItems, Items.Bootstrapper bootstrapper, ResourceKey<Block> wallSign, ResourceKey<Block> hangingWallSign, ResourceKey<Block> pottedSapling, int saplingFuelTicks) {
        CutoutCollection.registerBurningItems(blockItems.planks, bootstrapper);
        blockItems.log.forEach(bootstrapper.registerBurningBlock(FuelTimes.WOOD));
        if (blockItems.wood != null) {
            blockItems.wood.forEach(bootstrapper.registerBurningBlock(FuelTimes.WOOD));
        }

        bootstrapper.registerPottableSapling(
            blockItems.sapling,
            pottedSapling,
            saplingFuelTicks,
            saplingFuelTicks == FuelTimes.PLANT ? CompostChances.SMALL : 0.0f
        );
        if (blockItems.leaves != null) {
            bootstrapper.registerCompostableLeaves(blockItems.leaves);
        }

        bootstrapper.registerBurningBlock(FuelTimes.WOOD).accept(blockItems.fenceGate);
        bootstrapper.builderForBlockAttachedToSide(blockItems.sign, wallSign, Direction.DOWN, 16)
            .behavior(FuelItemBehavior.of(FuelTimes.SIGN))
            .register();
        bootstrapper.builderForBlockAttachedToSide(blockItems.hangingSign, hangingWallSign, Direction.UP, 16)
            .behavior(FuelItemBehavior.of(FuelTimes.HANGING_SIGN))
            .register();
        bootstrapper.registerBurningBlock(FuelTimes.DOOR).accept(blockItems.door);
        bootstrapper.registerBurningBlock(FuelTimes.WOOD).accept(blockItems.trapdoor);
        bootstrapper.registerBurningBlock(FuelTimes.BUTTON).accept(blockItems.button);
        bootstrapper.registerBurningBlock(FuelTimes.WOOD).accept(blockItems.pressurePlate);
        bootstrapper.registerBurningBlock(FuelTimes.WOOD).accept(blockItems.shelf);
    }

    public <U> WoodCollection<U> map(Function<T, U> mapper) {
        return new WoodCollection<>(
            this.planks.map(mapper),
            this.log.map(mapper),
            Strippable.map(this.wood, mapper),
            mapper.apply(this.sapling),
            ItematicUtil.applyNullable(this.leaves, mapper),
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
}
