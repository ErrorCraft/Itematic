package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public record ToolItemBehavior(Tool tool) implements ItemBehavior<ToolItemBehavior> {
    public static final Codec<ToolItemBehavior> CODEC = Tool.CODEC.xmap(
        ToolItemBehavior::new,
        ToolItemBehavior::tool
    );

    public static ToolItemBehavior of(HolderGetter<Block> blocks, ToolMaterial material, TagKey<Block> mineableBlocks) {
        return new ToolItemBehavior(
            new Tool(
                List.of(
                    Tool.Rule.deniesDrops(blocks.getOrThrow(material.incorrectBlocksForDrops())),
                    Tool.Rule.minesAndDrops(blocks.getOrThrow(mineableBlocks), material.speed())
                ),
                1.0f,
                1,
                true
            )
        );
    }

    public static Builder builder(int damage) {
        return new Builder(damage);
    }

    @Override
    public ItemBehaviorType<ToolItemBehavior> type() {
        return ItemBehaviorType.TOOL;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.TOOL, this.tool);
    }

    public static class Builder {
        private final int damage;
        private final List<Tool.Rule> rules = new ArrayList<>();
        private boolean canDestroyBlocksInCreative = true;

        public Builder(int damage) {
            this.damage = damage;
        }

        public ToolItemBehavior build() {
            return new ToolItemBehavior(
                new Tool(
                    this.rules,
                    1.0f,
                    this.damage,
                    this.canDestroyBlocksInCreative
                )
            );
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
