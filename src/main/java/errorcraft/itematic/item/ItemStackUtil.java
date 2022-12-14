package errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ItemStackUtil {
    public static final Codec<ItemStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf(ItemStackUtil.ID_KEY).forGetter(ItemStack::getRegistryEntry),
        Codec.INT.fieldOf(ItemStackUtil.COUNT_KEY).forGetter(ItemStack::getCount),
        NbtCompound.CODEC.optionalFieldOf(ItemStackUtil.NBT_KEY).forGetter(ItemStack::getOptionalNbt)
    ).apply(instance, ItemStackUtil::create));
    private static final String ID_KEY = "id";
    private static final String COUNT_KEY = "Count";
    private static final String NBT_KEY = "tag";
    private static final Identifier AIR = ItemKeys.AIR.getValue();

    public static NbtCompound writeToNbt(NbtCompound nbt, DynamicRegistryManager registryManager, ItemStack itemStack) {
        Registry<Item> registry = registryManager.get(RegistryKeys.ITEM);
        Identifier id = registry.getId(itemStack.getItem());
        itemStack.writeNbt(nbt);
        if (id == null) {
            id = AIR;
        }
        nbt.putString(ID_KEY, id.toString());
        return nbt;
    }

    public static ItemStack readFromNbt(NbtCompound nbt, DynamicRegistryManager registryManager) {
        Registry<Item> registry = registryManager.get(RegistryKeys.ITEM);
        Identifier id = new Identifier(nbt.getString(ID_KEY));
        Optional<RegistryEntry.Reference<Item>> entry = registry.getEntry(RegistryKey.of(RegistryKeys.ITEM, id));
        if (entry.isEmpty()) {
            return ItemStack.EMPTY;
        }
        int count = nbt.getByte(COUNT_KEY);
        ItemStack itemStack = new ItemStack(entry.get(), count);
        if (nbt.contains(NBT_KEY, NbtElement.COMPOUND_TYPE)) {
            itemStack.setNbt(nbt.getCompound(NBT_KEY));
        }
        return itemStack;
    }

    public static Identifier getId(@Nullable RegistryEntry<Item> entry) {
        if (entry == null) {
            return AIR;
        }
        Optional<RegistryKey<Item>> key = entry.getKey();
        if (key.isEmpty()) {
            return AIR;
        }
        return key.get().getValue();
    }

    private static ItemStack create(RegistryEntry<Item> entry, int count, Optional<NbtCompound> nbt) {
        ItemStack itemStack = new ItemStack(entry, count);
        nbt.ifPresent(itemStack::setNbt);
        return itemStack;
    }
}
