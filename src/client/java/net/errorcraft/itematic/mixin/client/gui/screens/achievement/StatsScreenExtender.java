package net.errorcraft.itematic.mixin.client.gui.screens.achievement;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.access.client.gui.screens.achievement.StatsScreenAccess;
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
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mixin(StatsScreen.class)
public abstract class StatsScreenExtender implements StatsScreenAccess {
    @Shadow
    @Final
    private StatsCounter stats;

    @Override
    public StatsCounter itematic$stats() {
        return this.stats;
    }

    @Mixin(StatsScreen.ItemStatisticsList.class)
    @SuppressWarnings({
        "rawtypes",
        "unchecked"
    })
    public static class ItemStatisticsListExtender extends ContainerObjectSelectionList {
        @Shadow
        @Final
        StatsScreen this$0;

        @Shadow
        @Final
        protected List<StatType<Item>> itemColumns;

        @Shadow
        @Final
        protected List<StatType<Block>> blockColumns;

        public ItemStatisticsListExtender(Minecraft minecraft, int width, int height, int y, int itemHeight) {
            super(minecraft, width, height, y, itemHeight);
        }

        @Inject(
            method = "<init>",
            at = @At(
                value = "INVOKE",
                target = "Lcom/google/common/collect/Sets;newIdentityHashSet()Ljava/util/Set;"
            )
        )
        private void storeItemsSet(StatsScreen statsScreen, Minecraft minecraft, CallbackInfo info, @Share("items") LocalRef<Set<Holder<Item>>> items) {
            if (minecraft.level == null) {
                items.set(Set.of());
            } else {
                items.set(this.entries(minecraft.level.registryAccess()));
            }
        }

        @Redirect(
            method = "<init>",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/core/DefaultedRegistry;iterator()Ljava/util/Iterator;"
            )
        )
        private <T> Iterator<T> iteratorUseEmptyIterator(DefaultedRegistry<T> instance) {
            return Collections.emptyIterator();
        }

        @Redirect(
            method = "<init>",
            at = @At(
                value = "INVOKE",
                target = "Ljava/util/Set;isEmpty()Z"
            )
        )
        private <T> boolean isEmptyUseHolderSet(Set<T> instance, @Share("items") LocalRef<Set<Holder<Item>>> items) {
            return items.get().isEmpty();
        }

        @Inject(
            method = "<init>",
            at = @At(
                value = "INVOKE",
                target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"
            )
        )
        private void addEntries(StatsScreen statsScreen, Minecraft minecraft, CallbackInfo info, @Share("items") LocalRef<Set<Holder<Item>>> items) {
            for (Holder<Item> item : items.get()) {
                StatsScreen.ItemStatisticsList.ItemRow itemRow = StatsScreenAccessor.ItemStatisticsListAccessor.ItemRowAccessor.create((StatsScreen.ItemStatisticsList)(Object) this, null);
                itemRow.itematic$setItem(item);
                this.addEntry(itemRow);
            }
        }

        @Unique
        private Set<Holder<Item>> entries(RegistryAccess registries) {
            StatsCounter statHandler = this.this$0.itematic$stats();
            Set<Holder<Item>> entries = new HashSet<>();
            Registry<Item> items = registries.lookupOrThrow(Registries.ITEM);
            Registry<Block> blocks = registries.lookupOrThrow(Registries.BLOCK);
            for (Holder<Item> item : items.asHolderIdMap()) {
                for (StatType<Item> statType : this.itemColumns) {
                    if (this.hasNoStatFor(statType, item, statHandler)) {
                        continue;
                    }

                    entries.add(item);
                    break;
                }
            }

            for (Holder<Block> block : blocks.asHolderIdMap()) {
                for (StatType<Block> statType : this.blockColumns) {
                    if (this.hasNoStatFor(statType, block, statHandler)) {
                        continue;
                    }

                    ResourceKey<Item> itemId = block.value().itematic$asItemId();
                    items.get(itemId).ifPresent(entries::add);
                    break;
                }
            }

            entries.removeIf(item -> item.is(ItemIds.AIR));
            return entries;
        }

        @Unique
        private <T> boolean hasNoStatFor(StatType<T> statType, Holder<T> holder, StatsCounter statHandler) {
            if (!statType.itematic$contains(holder)) {
                return true;
            }

            return statHandler.getValue(statType.itematic$get(holder)) <= 0;
        }

        @Mixin(targets = "net/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRowComparator")
        public static class ItemRowComparatorExtender {
            @ModifyConstant(
                method = "compare(Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;)I",
                constant = @Constant(
                    classValue = BlockItem.class,
                    ordinal = 0
                )
            )
            private boolean instanceOfBlockItemForFirstItemUseItemBehavior(Object reference, Class<BlockItem> clazz, @Local(name = "one", argsOnly = true) StatsScreen.ItemStatisticsList.ItemRow one, @Share("firstBlock") LocalRef<BlockItemBehavior> firstBlockReference) {
                Optional<BlockItemBehavior> firstBlock = one.itematic$item()
                    .value()
                    .itematic$getBehavior(ItemBehaviorType.BLOCK);
                firstBlock.ifPresent(firstBlockReference::set);
                return firstBlock.isPresent();
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
            private boolean instanceOfBlockItemForSecondItemUseItemBehavior(Object reference, Class<BlockItem> clazz, @Local(name = "two", argsOnly = true) StatsScreen.ItemStatisticsList.ItemRow two, @Share("secondBlock") LocalRef<BlockItemBehavior> secondBlockReference) {
                Optional<BlockItemBehavior> secondBlock = two.itematic$item()
                    .value()
                    .itematic$getBehavior(ItemBehaviorType.BLOCK);
                secondBlock.ifPresent(secondBlockReference::set);
                return secondBlock.isPresent();
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;getItem()Lnet/minecraft/world/item/Item;"
                )
            )
            @Nullable
            private Item getItemUseNull(StatsScreen.ItemStatisticsList.ItemRow instance) {
                return null;
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/BlockItem;getBlock()Lnet/minecraft/world/level/block/Block;"
                )
            )
            @Nullable
            private Block getBlockUseNull(BlockItem instance) {
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
            private <T> int getStatForFirstBlockUseItemBehavior(StatsCounter instance, StatType<Block> type, T key, @Share("firstBlock") LocalRef<BlockItemBehavior> firstBlockReference) {
                return instance.getValue(type.itematic$get(firstBlockReference.get().block().defaultBlock()));
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
            private <T> int getStatForSecondBlockUseItemBehavior(StatsCounter instance, StatType<Block> type, T key, @Share("secondBlock") LocalRef<BlockItemBehavior> secondBlockReference) {
                return instance.getValue(type.itematic$get(secondBlockReference.get().block().defaultBlock()));
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
            private <T> int getStatForFirstItemUseHolder(StatsCounter instance, StatType<Item> type, T key, StatsScreen.ItemStatisticsList.ItemRow one) {
                return instance.getValue(type.itematic$get(one.itematic$item()));
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
            private <T> int getStatForSecondItemUseHolder(StatsCounter instance, StatType<Item> type, T key, @Local(name = "two", argsOnly = true) StatsScreen.ItemStatisticsList.ItemRow two) {
                return instance.getValue(type.itematic$get(two.itematic$item()));
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
            private int compareUseHolders(int x, int y, StatsScreen.ItemStatisticsList.ItemRow one, StatsScreen.ItemStatisticsList.ItemRow two) {
                return one.itematic$item().compareTo(two.itematic$item());
            }
        }

        @Mixin(StatsScreen.ItemStatisticsList.ItemRow.class)
        public static class ItemRowExtender implements ItemStatisticsListAccess.ItemRowAccess {
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
            private boolean instanceOfBlockItemUseItemBehavior(Object reference, Class<BlockItem> clazz, @Share("block") LocalRef<BlockItemBehavior> blockReference) {
                Optional<BlockItemBehavior> block = this.item.value()
                    .itematic$getBehavior(ItemBehaviorType.BLOCK);
                block.ifPresent(blockReference::set);
                return block.isPresent();
            }

            @Redirect(
                method = "renderContent(Lnet/minecraft/client/gui/GuiGraphics;IIZF)V",
                at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList$ItemRow;item:Lnet/minecraft/world/item/Item;",
                    opcode = Opcodes.GETFIELD
                )
            )
            @Nullable
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
            @Nullable
            private Block getBlockUseNull(BlockItem instance) {
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
            private <T> Stat<Block> getStatUseHolder(StatType<Block> instance, T argument, @Share("block") LocalRef<BlockItemBehavior> blockReference) {
                return instance.itematic$get(blockReference.get().block().defaultBlock());
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
            private <T> Stat<Item> getStatUseHolder(StatType<Item> instance, T argument) {
                return instance.itematic$get(this.item);
            }

            @Override
            public Holder<Item> itematic$item() {
                return this.item;
            }

            @Override
            public void itematic$setItem(Holder<Item> item) {
                this.item = item;
                this.itemRowWidget.itematic$setStack(new ItemStack(item));
            }
        }
    }
}
