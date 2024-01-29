package net.errorcraft.itematic.block.entity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.Util;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DecoratedPotBlockEntityUtil {
    private static final Logger LOGGER = LogUtils.getLogger();

    private DecoratedPotBlockEntityUtil() {}

    public static Sherds fromNbt(DynamicRegistryManager registryManager, NbtCompound nbt) {
        if (nbt == null || !nbt.contains(DecoratedPotBlockEntity.SHERDS_NBT_KEY, NbtElement.LIST_TYPE)) {
            return null;
        }
        RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, registryManager);
        return DecoratedPotBlockEntityUtil.Sherds.CODEC.parse(ops, nbt.getList(DecoratedPotBlockEntity.SHERDS_NBT_KEY, NbtElement.STRING_TYPE))
            .resultOrPartial(LOGGER::error)
            .orElse(null);
    }

    public static ItemStack createStack(DynamicRegistryManager registryManager, Sherds sherds) {
        Optional<RegistryEntry.Reference<Item>> entry = registryManager.get(RegistryKeys.ITEM)
            .getEntry(ItemKeys.DECORATED_POT);
        ItemStack stack = entry.map(ItemStack::new).orElse(ItemStack.EMPTY);
        if (stack.isEmpty()) {
            return stack;
        }
        if (sherds == null || sherds.isEmpty()) {
            return stack;
        }
        NbtCompound nbt = new NbtCompound();
        RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, registryManager);
        DecoratedPotBlockEntityUtil.Sherds.CODEC.encodeStart(ops, sherds)
            .resultOrPartial(LOGGER::error)
            .ifPresent(data -> nbt.put(DecoratedPotBlockEntity.SHERDS_NBT_KEY, data));
        BlockItem.setBlockEntityNbt(stack, BlockEntityType.DECORATED_POT, nbt);
        return stack;
    }

    public record Sherds(RegistryEntry<Item> back, RegistryEntry<Item> left, RegistryEntry<Item> right, RegistryEntry<Item> front) {
        public static final Codec<Sherds> CODEC = RegistryFixedCodec.of(RegistryKeys.ITEM).listOf()
            .comapFlatMap(entries -> Util.decodeFixedLengthList(entries, 4).map(Sherds::new), Sherds::entries);

        private Sherds(List<RegistryEntry<Item>> items) {
            this(items.get(0), items.get(1), items.get(2), items.get(3));
        }

        public boolean isEmpty() {
            return this.back.matchesKey(ItemKeys.BRICK)
                && this.left.matchesKey(ItemKeys.BRICK)
                && this.right.matchesKey(ItemKeys.BRICK)
                && this.front.matchesKey(ItemKeys.BRICK);
        }

        public Stream<RegistryEntry<Item>> stream() {
            return Stream.of(this.back, this.left, this.right, this.front);
        }

        public Stream<RegistryEntry<Item>> streamForwards() {
            return Stream.of(this.front, this.left, this.right, this.back);
        }

        private List<RegistryEntry<Item>> entries() {
            return List.of(this.back, this.left, this.right, this.front);
        }
    }
}
