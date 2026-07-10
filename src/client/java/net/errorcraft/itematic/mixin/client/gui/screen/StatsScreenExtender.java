package net.errorcraft.itematic.mixin.client.gui.screen;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.access.client.gui.screen.StatsScreenAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BlockItemComponent;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import net.minecraft.stat.StatType;
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
    StatHandler statHandler;

    @Override
    public StatHandler itematic$statHandler() {
        return this.statHandler;
    }

    @Mixin(StatsScreen.ItemStatsListWidget.class)
    @SuppressWarnings({
        "rawtypes",
        "unchecked"
    })
    public static class ItemStatsListWidgetExtender extends ElementListWidget {
        @Shadow
        @Final
        StatsScreen field_18752;

        @Shadow
        @Final
        protected List<StatType<Item>> itemStatTypes;

        @Shadow
        @Final
        protected List<StatType<Block>> blockStatTypes;

        public ItemStatsListWidgetExtender(MinecraftClient client, int i, int j, int k, int l) {
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
        private void storeItemsSet(StatsScreen statsScreen, MinecraftClient client, CallbackInfo info, @Share("items") LocalRef<Set<RegistryEntry<Item>>> items) {
            if (client.world == null) {
                items.set(Set.of());
            }

            items.set(this.entries(client.world.getRegistryManager()));
        }

        @Redirect(
            method = "<init>",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/registry/DefaultedRegistry;iterator()Ljava/util/Iterator;"
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
        private <T> boolean isEmptyUseRegistryEntrySet(Set<T> instance, @Share("items") LocalRef<Set<RegistryEntry<Item>>> items) {
            return items.get().isEmpty();
        }

        @Inject(
            method = "<init>",
            at = @At(
                value = "INVOKE",
                target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"
            )
        )
        private void addEntries(StatsScreen statsScreen, MinecraftClient client, CallbackInfo info, @Share("items") LocalRef<Set<RegistryEntry<Item>>> items) {
            for (RegistryEntry<Item> entry : items.get()) {
                StatsScreen.ItemStatsListWidget.StatEntry itemEntry = StatsScreenAccessor.ItemStatsListWidgetAccessor.StatEntryAccessor.create((StatsScreen.ItemStatsListWidget)(Object) this, null);
                itemEntry.itematic$setRegistryEntry(entry);
                this.addEntry(itemEntry);
            }
        }

        @Unique
        private Set<RegistryEntry<Item>> entries(DynamicRegistryManager registryManager) {
            StatHandler statHandler = this.field_18752.itematic$statHandler();
            Set<RegistryEntry<Item>> entries = new HashSet<>();
            Registry<Item> items = registryManager.getOrThrow(RegistryKeys.ITEM);
            Registry<Block> blocks = registryManager.getOrThrow(RegistryKeys.BLOCK);
            for (RegistryEntry<Item> entry : items.getIndexedEntries()) {
                for (StatType<Item> statType : this.itemStatTypes) {
                    if (this.hasNoStatFor(statType, entry, statHandler)) {
                        continue;
                    }

                    entries.add(entry);
                    break;
                }
            }

            for (RegistryEntry<Block> entry : blocks.getIndexedEntries()) {
                for (StatType<Block> statType : this.blockStatTypes) {
                    if (this.hasNoStatFor(statType, entry, statHandler)) {
                        continue;
                    }

                    RegistryKey<Item> itemKey = entry.value().itematic$asItemKey();
                    items.getOptional(itemKey).ifPresent(entries::add);
                    break;
                }
            }

            entries.removeIf(entry -> entry.matchesKey(ItemKeys.AIR));
            return entries;
        }

        @Unique
        private <T> boolean hasNoStatFor(StatType<T> statType, RegistryEntry<T> entry, StatHandler statHandler) {
            if (!statType.itematic$hasStat(entry)) {
                return true;
            }

            return statHandler.getStat(statType.itematic$getOrCreateStat(entry)) <= 0;
        }

        @Mixin(targets = "net.minecraft.client.gui.screen.StatsScreen$ItemStatsListWidget$ItemComparator")
        public static class ItemComparatorExtender {
            @ModifyConstant(
                method = "compare(Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;)I",
                constant = @Constant(
                    classValue = BlockItem.class,
                    ordinal = 0
                )
            )
            private boolean instanceOfBlockItemForFirstItemUseItemComponentCheck(Object reference, Class<BlockItem> clazz, StatsScreen.ItemStatsListWidget.StatEntry first, @Share("blockItemComponentFirst") LocalRef<BlockItemComponent> blockItemComponentFirst) {
                Optional<BlockItemComponent> optionalComponent = first.itematic$registryEntry().value().itematic$getBehavior(ItemComponentTypes.BLOCK);
                optionalComponent.ifPresent(blockItemComponentFirst::set);
                return optionalComponent.isPresent();
            }

            @ModifyConstant(
                method = "compare(Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;)I",
                constant = @Constant(
                    classValue = BlockItem.class,
                    ordinal = 0
                ),
                slice = @Slice(
                    from = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/item/BlockItem;getBlock()Lnet/minecraft/block/Block;"
                    )
                )
            )
            private boolean instanceOfBlockItemForSecondItemUseItemComponentCheck(Object reference, Class<BlockItem> clazz, StatsScreen.ItemStatsListWidget.StatEntry first, StatsScreen.ItemStatsListWidget.StatEntry second, @Share("blockItemComponentSecond") LocalRef<BlockItemComponent> blockItemComponentSecond) {
                Optional<BlockItemComponent> optionalComponent = second.itematic$registryEntry().value().itematic$getBehavior(ItemComponentTypes.BLOCK);
                optionalComponent.ifPresent(blockItemComponentSecond::set);
                return optionalComponent.isPresent();
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;getItem()Lnet/minecraft/item/Item;"
                )
            )
            private Item getItemReturnNull(StatsScreen.ItemStatsListWidget.StatEntry instance) {
                return null;
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/BlockItem;getBlock()Lnet/minecraft/block/Block;"
                )
            )
            private Block getBlockReturnNull(BlockItem instance) {
                return null;
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/StatHandler;getStat(Lnet/minecraft/stat/StatType;Ljava/lang/Object;)I",
                    ordinal = 0
                ),
                slice = @Slice(
                    from = @At(
                        value = "FIELD",
                        target = "Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget;blockStatTypes:Ljava/util/List;",
                        opcode = Opcodes.GETFIELD
                    ),
                    to = @At(
                        value = "FIELD:LAST",
                        target = "Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget;selectedStatType:Lnet/minecraft/stat/StatType;",
                        opcode = Opcodes.GETFIELD
                    )
                )
            )
            private <T> int getStatForFirstBlockUseItemComponent(StatHandler instance, StatType<Block> type, T stat, @Share("blockItemComponentFirst") LocalRef<BlockItemComponent> blockItemComponentFirst) {
                return instance.getStat(type.itematic$getOrCreateStat(blockItemComponentFirst.get().block().defaultBlock()));
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/StatHandler;getStat(Lnet/minecraft/stat/StatType;Ljava/lang/Object;)I",
                    ordinal = 1
                ),
                slice = @Slice(
                    from = @At(
                        value = "FIELD",
                        target = "Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget;blockStatTypes:Ljava/util/List;",
                        opcode = Opcodes.GETFIELD
                    ),
                    to = @At(
                        value = "FIELD:LAST",
                        target = "Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget;selectedStatType:Lnet/minecraft/stat/StatType;",
                        opcode = Opcodes.GETFIELD
                    )
                )
            )
            private <T> int getStatForSecondBlockUseItemComponent(StatHandler instance, StatType<Block> type, T stat, @Share("blockItemComponentSecond") LocalRef<BlockItemComponent> blockItemComponentSecond) {
                return instance.getStat(type.itematic$getOrCreateStat(blockItemComponentSecond.get().block().defaultBlock()));
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/StatHandler;getStat(Lnet/minecraft/stat/StatType;Ljava/lang/Object;)I",
                    ordinal = 0
                ),
                slice = @Slice(
                    from = @At(
                        value = "FIELD:LAST",
                        target = "Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget;selectedStatType:Lnet/minecraft/stat/StatType;",
                        opcode = Opcodes.GETFIELD
                    )
                )
            )
            private <T> int getStatForFirstItemUseRegistryEntry(StatHandler instance, StatType<Item> type, T stat, StatsScreen.ItemStatsListWidget.StatEntry first) {
                return instance.getStat(type.itematic$getOrCreateStat(first.itematic$registryEntry()));
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/StatHandler;getStat(Lnet/minecraft/stat/StatType;Ljava/lang/Object;)I",
                    ordinal = 1
                ),
                slice = @Slice(
                    from = @At(
                        value = "FIELD:LAST",
                        target = "Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget;selectedStatType:Lnet/minecraft/stat/StatType;",
                        opcode = Opcodes.GETFIELD
                    )
                )
            )
            private <T> int getStatForSecondItemUseRegistryEntry(StatHandler instance, StatType<Item> type, T stat, StatsScreen.ItemStatsListWidget.StatEntry first, StatsScreen.ItemStatsListWidget.StatEntry second) {
                return instance.getStat(type.itematic$getOrCreateStat(second.itematic$registryEntry()));
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;getRawId(Lnet/minecraft/item/Item;)I"
                )
            )
            private int getRawIdReturnZero(Item item) {
                return 0;
            }

            @Redirect(
                method = "compare(Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;)I",
                at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Integer;compare(II)I",
                    ordinal = 0
                )
            )
            private int compareUseRegistryKeys(int x, int y, StatsScreen.ItemStatsListWidget.StatEntry first, StatsScreen.ItemStatsListWidget.StatEntry second) {
                return first.itematic$registryEntry().compareTo(second.itematic$registryEntry());
            }
        }

        @Mixin(StatsScreen.ItemStatsListWidget.StatEntry.class)
        public static class StatEntryExtender implements ItemStatsListWidgetAccess.StatEntryAccess {
            @Shadow
            @Final
            private StatsScreen.ItemStatsListWidget.StatEntry.ItemStackInSlotWidget button;

            @Unique
            private RegistryEntry<Item> entry;

            @Redirect(
                method = "<init>",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;getDefaultStack()Lnet/minecraft/item/ItemStack;"
                )
            )
            private ItemStack useEmptyStack(Item instance) {
                return ItemStack.EMPTY;
            }

            @ModifyConstant(
                method = "render(Lnet/minecraft/client/gui/DrawContext;IIZF)V",
                constant = @Constant(
                    classValue = BlockItem.class,
                    ordinal = 0
                )
            )
            private boolean instanceOfBlockItemUseItemComponentCheck(Object reference, Class<BlockItem> clazz, @Share("blockItemComponent") LocalRef<BlockItemComponent> blockItemComponent) {
                Optional<BlockItemComponent> optionalComponent = this.entry.value().itematic$getBehavior(ItemComponentTypes.BLOCK);
                optionalComponent.ifPresent(blockItemComponent::set);
                return optionalComponent.isPresent();
            }

            @Redirect(
                method = "render(Lnet/minecraft/client/gui/DrawContext;IIZF)V",
                at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget$StatEntry;item:Lnet/minecraft/item/Item;",
                    opcode = Opcodes.GETFIELD
                )
            )
            private Item getItemUseNull(StatsScreen.ItemStatsListWidget.StatEntry instance) {
                return null;
            }

            @Redirect(
                method = "render(Lnet/minecraft/client/gui/DrawContext;IIZF)V",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/BlockItem;getBlock()Lnet/minecraft/block/Block;"
                )
            )
            private Block getBlockReturnNull(BlockItem instance) {
                return null;
            }

            @Redirect(
                method = "render(Lnet/minecraft/client/gui/DrawContext;IIZF)V",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;",
                    ordinal = 0
                ),
                slice = @Slice(
                    from = @At(
                        value = "FIELD",
                        target = "Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget;blockStatTypes:Ljava/util/List;",
                        opcode = Opcodes.GETFIELD
                    )
                )
            )
            private <T> Stat<Block> getOrCreateStatForBlockUseItemComponent(StatType<Block> instance, T key, @Share("blockItemComponent") LocalRef<BlockItemComponent> blockItemComponent) {
                return instance.itematic$getOrCreateStat(blockItemComponent.get().block().defaultBlock());
            }

            @Redirect(
                method = "render(Lnet/minecraft/client/gui/DrawContext;IIZF)V",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;",
                    ordinal = 0
                ),
                slice = @Slice(
                    from = @At(
                        value = "FIELD",
                        target = "Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget;itemStatTypes:Ljava/util/List;",
                        opcode = Opcodes.GETFIELD
                    )
                )
            )
            private <T> Stat<Item> getOrCreateStatForItemUseItemComponent(StatType<Item> instance, T key) {
                return instance.itematic$getOrCreateStat(this.entry);
            }

            @Override
            public RegistryEntry<Item> itematic$registryEntry() {
                return this.entry;
            }

            @Override
            public void itematic$setRegistryEntry(RegistryEntry<Item> entry) {
                this.entry = entry;
                this.button.itematic$setStack(new ItemStack(entry));
            }
        }
    }
}
