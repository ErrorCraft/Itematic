package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.placement.BlockPlacer;
import net.errorcraft.itematic.item.placement.block.picker.BlockPicker;
import net.errorcraft.itematic.item.placement.block.picker.pickers.AttachedToSideBlockPicker;
import net.errorcraft.itematic.item.placement.block.picker.pickers.SimpleBlockPicker;
import net.errorcraft.itematic.mixin.item.ItemAccessor;
import net.errorcraft.itematic.serialization.SetCodec;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public record BlockItemComponent(BlockPicker<?> block, boolean operatorOnly, Set<Pass> passes) implements ItemComponent<BlockItemComponent> {
    public static final Codec<BlockItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockPicker.CODEC.fieldOf("block").forGetter(BlockItemComponent::block),
        Codec.BOOL.optionalFieldOf("operator_only", false).forGetter(BlockItemComponent::operatorOnly),
        SetCodec.forEnum(Pass.CODEC).optionalFieldOf("passes", Pass.DEFAULT_PASSES).forGetter(BlockItemComponent::passes)
    ).apply(instance, BlockItemComponent::new));

    public static BlockItemComponent of(BlockPicker<?> block, boolean operatorOnly, Set<Pass> passes) {
        return new BlockItemComponent(block, operatorOnly, passes);
    }

    public static BlockItemComponent of(RegistryEntry<Block> block) {
        return of(new SimpleBlockPicker(block), false, Pass.DEFAULT_PASSES);
    }

    public static BlockItemComponent of(RegistryEntry<Block> block, Pass... passes) {
        return of(new SimpleBlockPicker(block), false, Set.of(passes));
    }

    public static BlockItemComponent operator(RegistryEntry<Block> block) {
        return of(new SimpleBlockPicker(block), true, Pass.DEFAULT_PASSES);
    }

    public static BlockItemComponent attachedToSide(RegistryEntry<Block> attachedBlock, RegistryEntry<Block> otherBlock, Direction attachedSide) {
        return of(new AttachedToSideBlockPicker(attachedBlock, otherBlock, attachedSide), false, Pass.DEFAULT_PASSES);
    }

    @Override
    public ItemComponentType<BlockItemComponent> type() {
        return ItemComponentTypes.BLOCK;
    }

    @Override
    public Codec<BlockItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (this.isUnuseable(Pass.FLUID)) {
            return ItemResult.PASS;
        }

        BlockHitResult blockHitResult = ItemAccessor.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return ItemResult.PASS;
        }

        ItemUsageContext context = new ItemUsageContext(world, user, hand, stack, blockHitResult);
        return this.place(context, stackExchanger);
    }

    @Override
    public ItemResult useOnBlock(ItemUsageContext context, ItemStackExchanger stackExchanger) {
        if (this.isUnuseable(Pass.BLOCK)) {
            return ItemResult.PASS;
        }

        return this.place(context, stackExchanger);
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        this.block.defaultBlock().value().itematic$addComponents(builder);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        this.block.defaultBlock().value().appendTooltip(stack, context, tooltip, type);
    }

    public boolean canBeNested() {
        return !(this.block.defaultBlock().value() instanceof ShulkerBoxBlock);
    }

    public void onDestroyed(ItemEntity item) {
        ContainerComponent container = item.getStack().set(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT);
        if (container != null) {
            ItemUsage.spawnItemContents(item, container.iterateNonEmptyCopy());
        }
    }

    private boolean isUnuseable(Pass pass) {
        return !this.passes.contains(pass);
    }

    private ItemResult place(ItemUsageContext context, ItemStackExchanger stackExchanger) {
        return BlockPlacer.of(context, stackExchanger, this.block, this.operatorOnly, true)
            .place();
    }

    public enum Pass implements StringIdentifiable {
        BLOCK("block"),
        FLUID("fluid");
        public static final Set<Pass> DEFAULT_PASSES = Set.of(BLOCK);
        public static final Codec<Pass> CODEC = StringIdentifiable.createCodec(Pass::values);

        private final String name;

        Pass(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}
