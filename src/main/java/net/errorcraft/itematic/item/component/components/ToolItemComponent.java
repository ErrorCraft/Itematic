package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.item.tool.MiningSpeedEntry;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public record ToolItemComponent(int damage, List<MiningSpeedEntry> miningSpeeds) implements ItemComponent<ToolItemComponent> {
    public static final Codec<ToolItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("damage").forGetter(ToolItemComponent::damage),
        Codecs.createStrictOptionalFieldCodec(MiningSpeedEntry.CODEC.listOf(), "mining_speeds", List.of()).forGetter(ToolItemComponent::miningSpeeds)
    ).apply(instance, ToolItemComponent::new));

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

    public float getMiningSpeed(BlockState blockState) {
        for (MiningSpeedEntry entry : this.miningSpeeds) {
            if (entry.tag().test(blockState.getRegistryEntry())) {
                return entry.miningSpeed();
            }
        }
        return 1.0f;
    }

    private void useTool(ItemStack stack, World world, BlockPos pos, LivingEntity miner, ItemStackConsumer resultStackConsumer) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }
        ActionContext context = ActionContext.builder(serverWorld, stack, resultStackConsumer, EquipmentSlot.MAINHAND)
            .entityPosition(ActionContextParameter.THIS, miner)
            .position(ActionContextParameter.TARGET, pos.toCenterPos())
            .build();
        stack.itematic$invokeEvent(ItemEvents.USE_TOOL, context);
        stack.itematic$damage(this.damage, context);
    }

    public static Builder builder(int damage) {
        return new Builder(damage);
    }

    public static class Builder {
        private final int damage;
        private final List<MiningSpeedEntry> miningSpeeds = new ArrayList<>();

        public Builder(int damage) {
            this.damage = damage;
        }

        public ToolItemComponent build() {
            return new ToolItemComponent(this.damage, this.miningSpeeds);
        }

        public Builder miningSpeed(float miningSpeed, TagKey<Block> tag, boolean expected) {
            if (tag != null) {
                this.miningSpeeds.add(new MiningSpeedEntry(new TagPredicate<>(tag, expected), miningSpeed));
            }
            return this;
        }
    }
}
