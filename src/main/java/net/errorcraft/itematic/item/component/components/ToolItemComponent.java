package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public record ToolItemComponent(ToolComponent tool) implements ItemComponent<ToolItemComponent> {
    public static final Codec<ToolItemComponent> CODEC = ToolComponent.CODEC.xmap(ToolItemComponent::new, ToolItemComponent::tool);

    public static ToolItemComponent of(RegistryEntryLookup<Block> blocks, ToolMaterial material, TagKey<Block> mineableBlocks) {
        ToolComponent tool = new ToolComponent(
            List.of(
                ToolComponent.Rule.ofNeverDropping(blocks.getOrThrow(material.incorrectBlocksForDrops())),
                ToolComponent.Rule.ofAlwaysDropping(blocks.getOrThrow(mineableBlocks), material.speed())
            ),
            1.0f,
            1
        );
        return new ToolItemComponent(tool);
    }

    public static Builder builder(int damage) {
        return new Builder(damage);
    }

    @Override
    public ItemComponentType<ToolItemComponent> type() {
        return ItemComponentTypes.TOOL;
    }

    @Override
    public Codec<ToolItemComponent> codec() {
        return CODEC;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, ItemStackConsumer resultStackConsumer) {
        if (!world.isClient() && state.getHardness(world, pos) != 0.0f) {
            this.useTool(stack, world, pos, miner, resultStackConsumer);
        }
        return true;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.TOOL, this.tool);
    }

    private void useTool(ItemStack stack, World world, BlockPos pos, LivingEntity miner, ItemStackConsumer resultStackConsumer) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }
        ToolComponent tool = stack.get(DataComponentTypes.TOOL);
        if (tool == null) {
            return;
        }
        ActionContext context = ActionContext.builder(serverWorld, stack, resultStackConsumer, EquipmentSlot.MAINHAND)
            .entityPosition(ActionContextParameter.THIS, miner)
            .position(ActionContextParameter.TARGET, pos.toCenterPos())
            .build();
        stack.itematic$invokeEvent(ItemEvents.USE_TOOL, context);
        stack.itematic$damage(tool.damagePerBlock(), context);
    }

    public static class Builder {
        private final int damage;
        private final List<ToolComponent.Rule> rules = new ArrayList<>();

        public Builder(int damage) {
            this.damage = damage;
        }

        public ToolItemComponent build() {
            return new ToolItemComponent(new ToolComponent(this.rules, 1.0f, this.damage));
        }

        public Builder rule(ToolComponent.Rule rule) {
            this.rules.add(rule);
            return this;
        }
    }
}
