package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.block.BlockKeys;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.placement.BlockPlacer;
import net.errorcraft.itematic.item.placement.block.modifier.BlockStateModifier;
import net.errorcraft.itematic.item.placement.block.modifier.modifiers.AttachedToSideBlockStateModifier;
import net.errorcraft.itematic.item.placement.block.modifier.modifiers.SimpleBlockStateModifier;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record BlockItemComponent(BlockStateModifier<?> block, boolean operatorOnly) implements ItemComponent<BlockItemComponent> {
    public static final Codec<BlockItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockStateModifier.CODEC.fieldOf("block").forGetter(BlockItemComponent::block),
        Codec.BOOL.optionalFieldOf("operator_only", false).forGetter(BlockItemComponent::operatorOnly)
    ).apply(instance, BlockItemComponent::new));

    @Override
    public ItemComponentType<BlockItemComponent> type() {
        return ItemComponentTypes.BLOCK;
    }

    @Override
    public Codec<BlockItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context, ItemStackConsumer resultStackConsumer) {
        BlockPlacer placer = BlockPlacer.of(context, resultStackConsumer, this.block, this.operatorOnly, true);
        return placer.place();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        this.block.defaultBlock().value().appendTooltip(stack, world, tooltip, context, world != null ? world.getRegistryManager() : null);
    }

    public static BlockItemComponent attachedToSide(RegistryEntry<Block> attachedBlock, RegistryEntry<Block> otherBlock, Direction attachedSide) {
        return new BlockItemComponent(new AttachedToSideBlockStateModifier(attachedBlock, otherBlock, attachedSide), false);
    }

    public static BlockItemComponent of(RegistryEntry<Block> block) {
        return new BlockItemComponent(new SimpleBlockStateModifier(block), false);
    }

    public static BlockItemComponent operator(RegistryEntry<Block> block) {
        return new BlockItemComponent(new SimpleBlockStateModifier(block), true);
    }

    public int modifiedMaxCountForOccupancy(ItemStack stack) {
        RegistryEntry<Block> block = this.block.defaultBlock();
        if (!block.matchesKey(BlockKeys.BEEHIVE) && !block.matchesKey(BlockKeys.BEE_NEST)) {
            return stack.getMaxCount();
        }
        NbtCompound blockEntityNbt = BlockItem.getBlockEntityNbt(stack);
        if (blockEntityNbt == null) {
            return stack.getMaxCount();
        }
        if (blockEntityNbt.getList(BeehiveBlockEntity.BEES_KEY, NbtElement.COMPOUND_TYPE).isEmpty()) {
            return stack.getMaxCount();
        }
        return 1;
    }

    public boolean canBeNested() {
        return !(this.block.defaultBlock().value() instanceof ShulkerBoxBlock);
    }

    public void onDestroyed(ItemEntity item) {
        if (!(this.block.defaultBlock().value() instanceof ShulkerBoxBlock)) {
            return;
        }
        NbtCompound blockEntityNbt = BlockItem.getBlockEntityNbt(item.getStack());
        if (blockEntityNbt == null) {
            return;
        }
        if (!blockEntityNbt.contains(ShulkerBoxBlockEntity.ITEMS_KEY, NbtElement.LIST_TYPE)) {
            return;
        }
        NbtList itemsNbt = blockEntityNbt.getList(ShulkerBoxBlockEntity.ITEMS_KEY, NbtElement.COMPOUND_TYPE);
        RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, item.getWorld().getRegistryManager());
        ItemUsage.spawnItemContents(item, itemsNbt.stream().map(itemNbt -> ItemStack.CODEC.parse(ops, itemNbt).result().orElse(ItemStack.EMPTY)));
    }
}
