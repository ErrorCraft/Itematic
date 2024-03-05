package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.ItemUsageUtil;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.mixin.item.FilledMapItemAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public record MappableItemComponent(RegistryEntry<Item> transformsInto) implements ItemComponent<MappableItemComponent> {
    public static final Codec<MappableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("transforms_into").forGetter(MappableItemComponent::transformsInto)
    ).apply(instance, MappableItemComponent::new));


    @Override
    public ItemComponentType<MappableItemComponent> type() {
        return ItemComponentTypes.MAPPABLE;
    }

    @Override
    public Codec<MappableItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }
        user.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
        user.getWorld().playSoundFromEntity(null, user, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, user.getSoundCategory(), 1.0f, 1.0f);
        ItemStack resultStack = this.createStack(world, user.getBlockX(), user.getBlockZ(), 0, true, false);
        resultStackConsumer.set(ItemUsageUtil.exchangeStack(stack, user, resultStack, true, true));
        return ActionResult.CONSUME;
    }

    public ItemStack createStack(World world, int x, int z, int scale, boolean showIcons, boolean unlimitedTracking) {
        ItemStack resultStack = new ItemStack(this.transformsInto);
        FilledMapItemAccessor.createMapState(resultStack, world, x, z, scale, showIcons, unlimitedTracking, world.getRegistryKey());
        return resultStack;
    }

    public static MappableItemComponent of(RegistryEntry<Item> transformsInto) {
        return new MappableItemComponent(transformsInto);
    }
}
