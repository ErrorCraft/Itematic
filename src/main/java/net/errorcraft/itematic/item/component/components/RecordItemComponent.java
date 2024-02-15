package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.util.IdentifierUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record RecordItemComponent(RegistryEntry<SoundEvent> soundEvent, String descriptionKey, int duration, int outputSignal) implements ItemComponent<RecordItemComponent> {
    public static final Codec<RecordItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SoundEvent.ENTRY_CODEC.fieldOf("sound_event").forGetter(RecordItemComponent::soundEvent),
        Codec.STRING.fieldOf("description_key").forGetter(RecordItemComponent::descriptionKey),
        Codecs.NONNEGATIVE_INT.fieldOf("duration").forGetter(RecordItemComponent::duration),
        Codec.intRange(0, 15).fieldOf("output_signal").forGetter(RecordItemComponent::outputSignal)
    ).apply(instance, RecordItemComponent::new));

    @Override
    public ItemComponentType<RecordItemComponent> type() {
        return ItemComponentTypes.RECORD;
    }

    @Override
    public Codec<RecordItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context, ItemStackConsumer resultStackConsumer) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState blockState = world.getBlockState(pos);
        ItemStack stack = context.getStack();
        if (!blockState.isOf(Blocks.JUKEBOX) || blockState.get(JukeboxBlock.HAS_RECORD)) {
            return ActionResult.PASS;
        }

        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        PlayerEntity player = context.getPlayer();
        if (world.getBlockEntity(pos) instanceof JukeboxBlockEntity jukeboxBlockEntity) {
            jukeboxBlockEntity.setStack(stack.split(1));
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, blockState));
        }

        if (player != null) {
            player.incrementStat(Stats.PLAY_RECORD);
        }
        return ActionResult.CONSUME;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(this.getDescription().formatted(Formatting.GRAY));
    }

    public MutableText getDescription() {
        return Text.translatable(this.descriptionKey);
    }

    public static RecordItemComponent of(RegistryEntry<SoundEvent> soundEvent, RegistryKey<Item> key, int duration, int outputSignal) {
        return new RecordItemComponent(soundEvent, IdentifierUtil.createTranslationKey(key, "item", "desc"), duration, outputSignal);
    }
}
