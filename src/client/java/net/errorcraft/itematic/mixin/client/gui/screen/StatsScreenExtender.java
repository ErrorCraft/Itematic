package net.errorcraft.itematic.mixin.client.gui.screen;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.access.client.gui.screen.StatsScreenAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.BlockItemBehavior;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(StatsScreen.class)
public abstract class StatsScreenExtender implements StatsScreenAccess {
    @Shadow
    @Final
    StatsCounter stats;

    @Override
    public StatsCounter itematic$statHandler() {
        return this.stats;
    }

    @Mixin(StatsScreen.ItemStatisticsList.class)
    @SuppressWarnings({
        "rawtypes",
        "unchecked"
    })
    public static class ItemStatsListWidgetExtender extends ContainerObjectSelectionList {
        @Shadow
        @Final
        StatsScreen field_18752;

        @Shadow
        @Final
        protected List<StatType<Item>> itemColumns;

        @Shadow
        @Final
        protected List<StatType<Block>> blockColumns;

        public ItemStatsListWidgetExtender(Minecraft client, int i, int j, int k, int l) {
            super(client, i, j, k, l);
        }

        @Inject(
            method = "<init>",
            at = @At(
                value = "INVOKE",
                target = "Lcom/google/common/collect/Sets;newIdentityHashSet()Ljava/util/Set;",
                remap = false
            )
        )
        private void storeItemsSet(StatsScreen statsScreen, Minecraft client, CallbackInfo info, @Share("items") LocalRef<Set<Holder<Item>>> items) {
            if (client.level == null) {
                items.set(Set.of());
            }

            items.set(this.entries(client.level.registryAccess()));
        }

        @Redirect(
            method = "<init>",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/core/DefaultedRegistry;iterator()Ljava/util/Iterator;"
            )
        )
        private <T> Iterator<T> iteratorReturnEmptyIterator(DefaultedRegistry<T> instance) {
            return Collections.emptyIterator();
        }

        @Redirect(
            method = "<init>",
            at = @At(
                value = "INVOKE",
                target = "Ljava/util/Set;isEmpty()Z"
            )
        )
        private <T> boolean isEmptyUseRegistryEntrySet(Set<T> instance, @Share("items") LocalRef<Set<Holder<Item>>> items) {
            return items.get().isEmpty();
        }

        @Inject(
            method = "<init>",
            at = @At(
                value = "INVOKE",
                target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"
            )
        )
        private void addEntries(StatsScreen statsScreen, Minecraft client, CallbackInfo info, @Share("items") LocalRef<Set<Holder<Item>>> items) {
            for (Holder<Item> entry : items.get()) {
                StatsScreen.ItemStatisticsList.ItemRow itemEntry = StatsScreenAccessor.ItemStatsListWidgetAccessor.StatEntryAccessor.create((StatsScreen.ItemStatisticsList)(Object) this, null);
                itemEntry.itematic$setRegistryEntry(entry);
                this.addEntry(itemEntry);
            }
        }

        @Unique
        private Set<Holder<Item>> entries(RegistryAccess registryManager) {
            StatsCounter statHandler = this.field_18752.itematic$statHandler();
            Set<Holder<Item>> entries = new HashSet<>();
            Registry<Item> items = registryManager.lookupOrThrow(Registries.ITEM);
            Registry<Block> blocks = registryManager.lookupOrThrow(Registries.BLOCK);
            for (Holder<Item> entry : items.asHolderIdMap()) {
                for (StatType<Item> statType : this.itemColumns) {
                    if (this.hasNoStatFor(statType, entry, statHandler)) {
                        continue;
                    }

                    entries.add(entry);
                    break;
                }
            }

            for (Holder<Block> entry : blocks.asHolderIdMap()) {
                for (StatType<Block> statType : this.blockColumns) {
                    if (this.hasNoStatFor(statType, entry, statHandler)) {
                        continue;
                    }

                    ResourceKey<Item> itemId = entry.value().itematic$asItemId();
                    items.get(itemId).ifPresent(entries::add);
                    break;
                }
            }

            entries.removeIf(entry -> entry.is(ItemIds.AIR));
            return entries;
        }

        @Unique
        private <T> boolean hasNoStatFor(StatType<T> statType, Holder<T> entry, StatsCounter statHandler) {
            if (!statType.itematic$contains(entry)) {
                return true;
            }

            return statHandler.getValue(statType.itematic$get(entry)) <= 0;
        }

        @Mixin(targets = "net.minecraft.client.gui.screens.achievement.StatsScreen$ItemStatisticsList$ItemRowComparator")
        public static class ItemComparatorExtender {
            @ModifyConstant(
                method = "compare(Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;)I",
                constant = @Constant(
                    classValue = BlockItem.class,
                    ordinal = 0
                )
            )
            private boolean instanceOfBlockItemForFirstItemUseItemBehaviorCheck(Object reference, Class<BlockItem> clazz, StatsScreen.ItemStatisticsList.ItemRow first, @Share("firstBlock") LocalRef<BlockItemBehavior> firstBlock) {
                Optional<BlockItemBehavior> optionalFirstBlock = first.itematic$registryEntry()
                    .value()
                    .itematic$getBehavior(ItemBehaviorType.BLOCK);
                optionalFirstBlock.ifPresent(firstBlock::set);
                return optionalFirstBlock.isPresent();
            }

            @ModifyConstant(
                method = "compare(Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;)I",
                constant = @Constant(
                    classValue = BlockItem.class,
                    ordinal = 0
                ),
                slice = @Slice(
                    from = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/world/item/BlockItem;getBlock()Lnet/minecraft/world/level/block/Block;"
                    )
                )
            )
            private boolean instanceOfBlockItemForSecondItemUseItemBehaviorCheck(Object reference, Class<BlockItem> clazz, StatsScreen.ItemStatisticsList.ItemRow first, StatsScreen.ItemStatisticsList.ItemRow second, @Share("secondBlock") LocalRef<BlockItemBehavior> secondBlock) {
                Optional<BlockItemBehavior> optionalSecondBlock = second.itematic$registryEntry()
                    .value()
                    .itematic$getBehavior(ItemBehaviorType.BLOCK);
                optionalSecondBlock.ifPresent(secondBlock::set);
                return optionalSecondBlock.isPresent();
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;getItem()Lnet/minecraft/world/item/Item;"
                )
            )
            private Item getItemReturnNull(StatsScreen.ItemStatisticsList.ItemRow instance) {
                return null;
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/BlockItem;getBlock()Lnet/minecraft/world/level/block/Block;"
                )
            )
            private Block getBlockReturnNull(BlockItem instance) {
                return null;
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stats/StatsCounter;getValue(Lnet/minecraft/stats/StatType;Ljava/lang/Object;)I",
                    ordinal = 0
                ),
                slice = @Slice(
                    from = @At(
                        value = "FIELD",
                        target = "Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList;blockColumns:Ljava/util/List;",
                        opcode = Opcodes.GETFIELD
                    ),
                    to = @At(
                        value = "FIELD:LAST",
                        target = "Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList;sortColumn:Lnet/minecraft/stats/StatType;",
                        opcode = Opcodes.GETFIELD
                    )
                )
            )
            private <T> int getStatForFirstBlockUseItemBehavior(StatsCounter instance, StatType<Block> type, T stat, @Share("firstBlock") LocalRef<BlockItemBehavior> firstBlock) {
                return instance.getValue(type.itematic$get(firstBlock.get().block().defaultBlock()));
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stats/StatsCounter;getValue(Lnet/minecraft/stats/StatType;Ljava/lang/Object;)I",
                    ordinal = 1
                ),
                slice = @Slice(
                    from = @At(
                        value = "FIELD",
                        target = "Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList;blockColumns:Ljava/util/List;",
                        opcode = Opcodes.GETFIELD
                    ),
                    to = @At(
                        value = "FIELD:LAST",
                        target = "Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList;sortColumn:Lnet/minecraft/stats/StatType;",
                        opcode = Opcodes.GETFIELD
                    )
                )
            )
            private <T> int getStatForSecondBlockUseItemBehavior(StatsCounter instance, StatType<Block> type, T stat, @Share("secondBlock") LocalRef<BlockItemBehavior> secondBlock) {
                return instance.getValue(type.itematic$get(secondBlock.get().block().defaultBlock()));
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stats/StatsCounter;getValue(Lnet/minecraft/stats/StatType;Ljava/lang/Object;)I",
                    ordinal = 0
                ),
                slice = @Slice(
                    from = @At(
                        value = "FIELD:LAST",
                        target = "Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList;sortColumn:Lnet/minecraft/stats/StatType;",
                        opcode = Opcodes.GETFIELD
                    )
                )
            )
            private <T> int getStatForFirstItemUseRegistryEntry(StatsCounter instance, StatType<Item> type, T stat, StatsScreen.ItemStatisticsList.ItemRow first) {
                return instance.getValue(type.itematic$get(first.itematic$registryEntry()));
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stats/StatsCounter;getValue(Lnet/minecraft/stats/StatType;Ljava/lang/Object;)I",
                    ordinal = 1
                ),
                slice = @Slice(
                    from = @At(
                        value = "FIELD:LAST",
                        target = "Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList;sortColumn:Lnet/minecraft/stats/StatType;",
                        opcode = Opcodes.GETFIELD
                    )
                )
            )
            private <T> int getStatForSecondItemUseRegistryEntry(StatsCounter instance, StatType<Item> type, T stat, StatsScreen.ItemStatisticsList.ItemRow first, StatsScreen.ItemStatisticsList.ItemRow second) {
                return instance.getValue(type.itematic$get(second.itematic$registryEntry()));
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Item;getId(Lnet/minecraft/world/item/Item;)I"
                )
            )
            private int getRawIdReturnZero(Item item) {
                return 0;
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Integer;compare(II)I",
                    ordinal = 0
                )
            )
            private int compareUseRegistryKeys(int x, int y, StatsScreen.ItemStatisticsList.ItemRow first, StatsScreen.ItemStatisticsList.ItemRow second) {
                return first.itematic$registryEntry().compareTo(second.itematic$registryEntry());
            }
        }

        @Mixin(StatsScreen.ItemStatisticsList.ItemRow.class)
        public static class StatEntryExtender implements ItemStatsListWidgetAccess.StatEntryAccess {
            @Shadow
            @Final
            private StatsScreen.ItemStatisticsList.ItemRow.ItemRowWidget itemRowWidget;

            @Unique
            private Holder<Item> item;

            @Redirect(
                method = "<init>",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Item;getDefaultInstance()Lnet/minecraft/world/item/ItemStack;"
                )
            )
            private ItemStack useEmptyStack(Item instance) {
                return ItemStack.EMPTY;
            }

            @ModifyConstant(
                method = "renderContent(Lnet/minecraft/client/gui/GuiGraphics;IIZF)V",
                constant = @Constant(
                    classValue = BlockItem.class,
                    ordinal = 0
                )
            )
            private boolean instanceOfBlockItemUseItemBehaviorCheck(Object reference, Class<BlockItem> clazz, @Share("block") LocalRef<BlockItemBehavior> block) {
                Optional<BlockItemBehavior> optionalBlock = this.item.value().itematic$getBehavior(ItemBehaviorType.BLOCK);
                optionalBlock.ifPresent(block::set);
                return optionalBlock.isPresent();
            }

            @Redirect(
                method = "renderContent(Lnet/minecraft/client/gui/GuiGraphics;IIZF)V",
                at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;item:Lnet/minecraft/world/item/Item;",
                    opcode = Opcodes.GETFIELD
                )
            )
            private Item getItemUseNull(StatsScreen.ItemStatisticsList.ItemRow instance) {
                return null;
            }

            @Redirect(
                method = "renderContent(Lnet/minecraft/client/gui/GuiGraphics;IIZF)V",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/BlockItem;getBlock()Lnet/minecraft/world/level/block/Block;"
                )
            )
            private Block getBlockReturnNull(BlockItem instance) {
                return null;
            }

            @Redirect(
                method = "renderContent(Lnet/minecraft/client/gui/GuiGraphics;IIZF)V",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;",
                    ordinal = 0
                ),
                slice = @Slice(
                    from = @At(
                        value = "FIELD",
                        target = "Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList;blockColumns:Ljava/util/List;",
                        opcode = Opcodes.GETFIELD
                    )
                )
            )
            private <T> Stat<Block> getOrCreateStatForBlockUseItemBehavior(StatType<Block> instance, T key, @Share("block") LocalRef<BlockItemBehavior> block) {
                return instance.itematic$get(block.get().block().defaultBlock());
            }

            @Redirect(
                method = "renderContent(Lnet/minecraft/client/gui/GuiGraphics;IIZF)V",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;",
                    ordinal = 0
                ),
                slice = @Slice(
                    from = @At(
                        value = "FIELD",
                        target = "Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList;itemColumns:Ljava/util/List;",
                        opcode = Opcodes.GETFIELD
                    )
                )
            )
            private <T> Stat<Item> getOrCreateStatForItemUseItemBehavior(StatType<Item> instance, T key) {
                return instance.itematic$get(this.item);
            }

            @Override
            public Holder<Item> itematic$registryEntry() {
                return this.item;
            }

            @Override
            public void itematic$setRegistryEntry(Holder<Item> entry) {
                this.item = entry;
                this.itemRowWidget.itematic$setStack(new ItemStack(entry));
            }
        }
    }
}
