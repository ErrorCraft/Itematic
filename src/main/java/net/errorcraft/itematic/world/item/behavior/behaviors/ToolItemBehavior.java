package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.ArrayList;
import java.util.List;

public record ToolItemBehavior(Tool tool) implements ItemBehavior<ToolItemBehavior> {
    public static final Codec<ToolItemBehavior> CODEC = Tool.CODEC.xmap(ToolItemBehavior::new, ToolItemBehavior::tool);

    public static ToolItemBehavior of(HolderGetter<Block> blocks, ToolMaterial material, TagKey<Block> mineableBlocks) {
        return new ToolItemBehavior(new Tool(
            List.of(
                Tool.Rule.deniesDrops(blocks.getOrThrow(material.incorrectBlocksForDrops())),
                Tool.Rule.minesAndDrops(blocks.getOrThrow(mineableBlocks), material.speed())
            ),
            1.0f,
            1,
            true
        ));
    }

    public static Builder builder(int damage) {
        return new Builder(damage);
    }

    @Override
    public ItemBehaviorType<ToolItemBehavior> type() {
        return ItemBehaviorType.TOOL;
    }

    @Override
    public boolean postMine(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miner, ItemStackExchanger stackExchanger) {
        if (!level.isClientSide() && state.getDestroySpeed(level, pos) != 0.0f) {
            this.useTool(stack, level, pos, miner, stackExchanger);
        }

        return true;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.TOOL, this.tool);
    }

    private void useTool(ItemStack stack, Level level, BlockPos pos, LivingEntity miner, ItemStackExchanger stackExchanger) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        Tool tool = stack.get(DataComponents.TOOL);
        if (tool == null) {
            return;
        }

        ActionContext context = ActionContext.builder(serverLevel)
            .stackExchanger(stackExchanger)
            .add(LootContextParams.THIS_ENTITY, miner)
            .add(LootContextParams.ORIGIN, miner.position())
            .add(ItematicContextKeys.INTERACTED_POSITION, pos.getCenter())
            .add(LootContextParams.TOOL, stack)
            .add(ItematicContextKeys.EQUIPMENT_SLOT, EquipmentSlot.MAINHAND)
            .build();
        stack.itematic$invokeEvent(ItemEvent.USE_TOOL, context);
        stack.itematic$damage(tool.damagePerBlock(), context);
    }

    public static class Builder {
        private final int damage;
        private final List<Tool.Rule> rules = new ArrayList<>();
        private boolean canDestroyBlocksInCreative = true;

        public Builder(int damage) {
            this.damage = damage;
        }

        public ToolItemBehavior build() {
            return new ToolItemBehavior(new Tool(
                this.rules,
                1.0f,
                this.damage,
                this.canDestroyBlocksInCreative
            ));
        }

        public Builder preventCreativeDestruction() {
            this.canDestroyBlocksInCreative = false;
            return this;
        }

        public Builder rule(Tool.Rule rule) {
            this.rules.add(rule);
            return this;
        }
    }
}
