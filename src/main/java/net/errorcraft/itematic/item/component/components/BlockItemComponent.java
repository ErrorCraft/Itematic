package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.minecraft.client.item.TooltipContext;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
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
    public void addComponents(ComponentMap.Builder builder) {
        this.block.defaultBlock().value().itematic$addComponents(builder);
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

    public boolean canBeNested() {
        return !(this.block.defaultBlock().value() instanceof ShulkerBoxBlock);
    }

    public void onDestroyed(ItemEntity item) {
        ContainerComponent container = item.getStack().set(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT);
        if (container != null) {
            ItemUsage.spawnItemContents(item, container.stream());
        }
    }
}
