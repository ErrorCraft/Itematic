package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.mixin.world.item.MapItemAccessor;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;

public record MappableItemBehavior(Holder<Item> transformsInto) implements ItemBehavior<MappableItemBehavior> {
    public static final Codec<MappableItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Item.CODEC.fieldOf("transforms_into").forGetter(MappableItemBehavior::transformsInto)
    ).apply(instance, MappableItemBehavior::new));

    public static MappableItemBehavior of(Holder<Item> transformsInto) {
        return new MappableItemBehavior(transformsInto);
    }

    @Override
    public ItemBehaviorType<MappableItemBehavior> type() {
        return ItemBehaviorType.MAPPABLE;
    }

    @Override
    public ItemResult use(Level level, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return ItemResult.SUCCEED;
        }

        user.awardStat(Stats.ITEM_USED.itematic$get(stack.typeHolder()));
        level.playSound(null, user, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, user.getSoundSource(), 1.0f, 1.0f);
        ItemStack resultStack = this.createStack(serverLevel, user.getBlockX(), user.getBlockZ(), 0, true, false);
        stack.consume(1, user);
        stackExchanger.exchange(resultStack);
        return ItemResult.CONSUME;
    }

    public ItemStack createStack(ServerLevel level, int x, int z, int scale, boolean showIcons, boolean unlimitedTracking) {
        ItemStack resultStack = new ItemStack(this.transformsInto);
        MapId mapId = MapItemAccessor.createNewSavedData(level, x, z, scale, showIcons, unlimitedTracking, level.dimension());
        resultStack.set(DataComponents.MAP_ID, mapId);
        return resultStack;
    }
}
