package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.errorcraft.itematic.access.item.ItemStackAccess;
import net.errorcraft.itematic.item.ItemBase;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.advancement.criterion.ItemDurabilityChangedCriterion;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(ItemStack.class)
public abstract class ItemStackExtender implements ItemStackAccess {
    @Shadow
    @Final
    public static ItemStack EMPTY;

    @Shadow
    @Final
    private static String UNBREAKABLE_KEY;

    @Shadow
    @Nullable
    private NbtCompound nbt;

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract void damage(int amount, Random random, @Nullable ServerPlayerEntity serverPlayerEntity, Runnable runnable);

    @Shadow
    public abstract int getDamage();

    @Shadow
    public abstract void setDamage(int damage);

    @Shadow
    public abstract void decrement(int amount);

    @Shadow
    private static String method_55061() {
        return null;
    }

    @Unique
    private static Codec<RegistryEntry<Item>> ITEM_ENTRY_CODEC;

    @Unique
    private final Set<ItemEvent> activeEvents = new HashSet<>();

    @Unique
    private RegistryEntry<Item> entry;

    @Unique
    private ActionContext context;

    @Inject(
        method = "<clinit>",
        at = @At("HEAD")
    )
    private static void initializeItemEntryCodec(CallbackInfo info) {
        ITEM_ENTRY_CODEC = Codecs.validate(RegistryFixedCodec.of(RegistryKeys.ITEM), entry -> entry.matchesKey(ItemKeys.AIR) ? DataResult.error(ItemStackExtender::method_55061) : DataResult.success(entry));
    }

    @Redirect(
        method = { "method_28376", "method_55067" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getEntryCodec()Lcom/mojang/serialization/Codec;"
        )
    )
    private static Codec<RegistryEntry<Item>> doNotUseStaticItemRegistry(DefaultedRegistry<Item> instance) {
        return RegistryFixedCodec.of(RegistryKeys.ITEM);
    }

    @Redirect(
        method = "method_55063",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getCodec()Lcom/mojang/serialization/Codec;"
        )
    )
    private static Codec<RegistryEntry<Item>> valueDoNotUseStaticItemRegistry(DefaultedRegistry<Item> instance) {
        return RegistryFixedCodec.of(RegistryKeys.ITEM);
    }

    @Redirect(
        method = "method_55066",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/ItemStack;ITEM_CODEC:Lcom/mojang/serialization/Codec;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private static Codec<RegistryEntry<Item>> getItemCodecUseRegistryEntryCodec() {
        return ITEM_ENTRY_CODEC;
    }

    @ModifyArg(
        method = { "method_55063", "method_55066" },
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/MapCodec;forGetter(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;",
            ordinal = 0,
            remap = false
        )
    )
    private static Function<ItemStack, RegistryEntry<Item>> forGetterUseRegistryEntry(Function<ItemStack, Item> getter) {
        return ItemStack::getRegistryEntry;
    }

    @ModifyArg(
        method = { "method_55063", "method_55066" },
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/datafixers/Products$P2;apply(Lcom/mojang/datafixers/kinds/Applicative;Ljava/util/function/BiFunction;)Lcom/mojang/datafixers/kinds/App;",
            remap = false
        )
    )
    private static BiFunction<RegistryEntry<Item>, Integer, ItemStack> applyUseRegistryEntryItemStackConstructor(BiFunction<Item, Integer, ItemStack> function) {
        return ItemStack::new;
    }

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        )
    )
    private static Codec<ItemStack> ingredientEntryCodecUseRegistryEntryVersion(Codec<Item> instance, Function<? super Item, ? extends ItemStack> to, Function<? super ItemStack, ? extends Item> from) {
        return ITEM_ENTRY_CODEC.xmap(ItemStack::new, ItemStack::getRegistryEntry);
    }

    @Inject(
        method = "<init>(Lnet/minecraft/registry/entry/RegistryEntry;)V",
        at = @At("TAIL")
    )
    private void constructorSetRegistryEntry(RegistryEntry<Item> entry, CallbackInfo info) {
        this.entry = entry;
    }

    @Inject(
        method = "<init>(Lnet/minecraft/registry/entry/RegistryEntry;I)V",
        at = @At("TAIL")
    )
    private void constructorSetRegistryEntry(RegistryEntry<Item> entry, int count, CallbackInfo info) {
        this.entry = entry;
    }

    @Redirect(
        method = { "<init>(Lnet/minecraft/registry/entry/RegistryEntry;)V", "<init>(Lnet/minecraft/registry/entry/RegistryEntry;I)V" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/entry/RegistryEntry;value()Ljava/lang/Object;"
        )
    )
    private static <T> T constructorValueReturnNullToPreventUnboundRegistryEntryIssues(RegistryEntry<T> instance) {
        return null;
    }

    @Redirect(
        method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemConvertible;asItem()Lnet/minecraft/item/Item;"
        )
    )
    private Item constructorAsItemReturnNullToPreventNullPointerException(ItemConvertible instance) {
        return null;
    }

    @Redirect(
        method = { "<init>(Lnet/minecraft/item/ItemConvertible;I)V", "setNbt" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;isDamageable()Z"
        )
    )
    private boolean isDamageableUseItemStackVersion(Item instance) {
        return this.isDamageable();
    }

    @Inject(
        method = "useOnBlock",
        at = @At("HEAD"),
        cancellable = true
    )
    public void useOnBlockCheckNullEntry(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
        if (this.isEmpty()) {
            info.setReturnValue(ActionResult.PASS);
        }
    }

    @Inject(
        method = "usageTick",
        at = @At("HEAD"),
        cancellable = true
    )
    public void usageTickCheckNullEntry(World world, LivingEntity user, int remainingUseTicks, CallbackInfo info) {
        if (this.isEmpty()) {
            info.cancel();
        }
    }

    /**
     * @author ErrorCraft
     * @reason Uses a registry entry on the item stack instead of an intrusive registry entry.
     */
    @Overwrite
    public RegistryEntry<Item> getRegistryEntry() {
        return this.entry;
    }

    @Redirect(
        method = "getItem",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/ItemStack;item:Lnet/minecraft/item/Item;",
            opcode = Opcodes.GETFIELD
        )
    )
    private Item getItemGetItemFieldUseRegistryEntryToPreventNullPointerException(ItemStack instance) {
        return this.entry.value();
    }

    @Redirect(
        method = "writeNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getId(Ljava/lang/Object;)Lnet/minecraft/util/Identifier;"
        )
    )
    @NotNull
    private <T> Identifier getIdUseEntry(DefaultedRegistry<T> instance, T value) {
        return this.itematic$key().getValue();
    }

    @Redirect(
        method = "copy",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseRegistryEntry(ItemConvertible item, int count) {
        return new ItemStack(this.entry, count);
    }

    /**
     * @author ErrorCraft
     * @reason Uses a null check instead of a default air item.
     */
    @Overwrite
    public int getMaxCount() {
        return this.entry == null ? ItemBase.MAX_MAX_COUNT : this.entry.value().getMaxCount();
    }

    @Inject(
        method = "getRarity",
        at = @At("HEAD"),
        cancellable = true
    )
    public void getRarityCheckNullEntry(CallbackInfoReturnable<Rarity> info) {
        if (this.entry == null) {
            info.setReturnValue(Rarity.COMMON);
        }
    }

    /**
     * @author ErrorCraft
     * @reason Uses a null check instead of a default air item.
     */
    @Overwrite
    public int getMaxDamage() {
        if (this.entry == null) {
            return 0;
        }
        return this.entry.value().getMaxDamage();
    }

    /**
     * @author ErrorCraft
     * @reason Uses a null check instead of a default air item.
     */
    @Overwrite
    public Stream<TagKey<Item>> streamTags() {
        if (this.entry == null) {
            return Stream.empty();
        }
        return this.entry.streamTags();
    }

    @ModifyReturnValue(
        method = "isEmpty",
        at = @At("TAIL")
    )
    private boolean checkNullEntryForEmptyStack(boolean original) {
        return original
            || this.entry == null
            || !this.entry.hasKeyAndValue()
            || this.itematic$isOf(ItemKeys.AIR);
    }

    /**
     * @author ErrorCraft
     * @reason Uses a null check instead of a default air item.
     */
    @Overwrite
    public boolean isIn(TagKey<Item> tag) {
        if (this.entry == null) {
            return false;
        }
        return this.entry.isIn(tag);
    }

    /**
     * @author ErrorCraft
     * @reason Uses a null check instead of a default air item.
     */
    @Overwrite
    public boolean isDamageable() {
        if (this.isEmpty()) {
            return false;
        }
        if (!this.entry.value().isDamageable()) {
            return false;
        }
        return this.nbt == null || !this.nbt.getBoolean(UNBREAKABLE_KEY);
    }

    /**
     * @author ErrorCraft
     * @reason Uses a null check instead of a default air item.
     */
    @Overwrite
    public boolean itemMatches(Predicate<RegistryEntry<Item>> predicate) {
        if (this.entry == null) {
            return false;
        }
        return predicate.test(this.entry);
    }

    /**
     * @author ErrorCraft
     * @reason Uses a null check instead of a default air item.
     */
    @Overwrite
    public boolean itemMatches(RegistryEntry<Item> itemEntry) {
        return this.entry == itemEntry;
    }

    @Inject(
        method = "itemMatches(Lnet/minecraft/registry/entry/RegistryEntryList;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkNullRegistryEntry(RegistryEntryList<Item> registryEntryList, CallbackInfoReturnable<Boolean> info) {
        if (this.entry == null) {
            info.setReturnValue(false);
        }
    }

    @Inject(
        method = "isEnchantable",
        at = @At("HEAD"),
        cancellable = true
    )
    public void isEnchantableCheckNullEntry(CallbackInfoReturnable<Boolean> info) {
        if (this.entry == null) {
            info.setReturnValue(false);
        }
    }

    @Redirect(
        method = { "areItemsEqual", "areItemsAndNbtEqual" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean isOfUseRegistryEntryCheck(ItemStack instance, Item item, ItemStack left, ItemStack right) {
        return instance.itemMatches(right.getRegistryEntry());
    }

    @Inject(
        method = "hasGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    public void hasGlintCheckNullEntry(CallbackInfoReturnable<Boolean> info) {
        if (this.entry == null) {
            info.setReturnValue(false);
        }
    }

    @Inject(
        method = "getName",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"
        ),
        cancellable = true
    )
    private void getNameGetItemCheckNullEntry(CallbackInfoReturnable<Text> info) {
        if (this.entry == null) {
            info.setReturnValue(Text.empty());
        }
    }

    @Redirect(
        method = "getTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V"
        )
    )
    private void getTooltipAppendTooltipUseRegistryEntry(Item instance, ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.entry != null) {
            this.entry.value().appendTooltip(stack, world, tooltip, context);
        }
    }

    @Redirect(
        method = "getTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getId(Ljava/lang/Object;)Lnet/minecraft/util/Identifier;"
        )
    )
    @NotNull
    private <T> Identifier getTooltipGetIdUseRegistryEntry(DefaultedRegistry<T> instance, T t) {
        if (this.entry == null) {
            return ItemKeys.AIR.getValue();
        }
        return this.entry.getKey().map(RegistryKey::getValue).orElse(ItemKeys.AIR.getValue());
    }

    @Inject(
        method = "postMine",
        at = @At("HEAD"),
        cancellable = true
    )
    private void postMineUseRegistryEntryNullCheck(World world, BlockState state, BlockPos pos, PlayerEntity miner, CallbackInfo info) {
        if (this.isEmpty()) {
            info.cancel();
        }
    }

    @Inject(
        method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/lang/Runnable;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;setDamage(I)V",
            shift = At.Shift.AFTER
        )
    )
    private void invokeDamageToolEvent(int amount, Random random, ServerPlayerEntity serverPlayerEntity, Runnable breakCallback, CallbackInfo info) {
        if (this.context == null) {
            return;
        }
        this.itematic$invokeEvent(ItemEvents.DAMAGE_ITEM, this.context);
    }

    @WrapWithCondition(
        method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/lang/Runnable;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/advancement/criterion/ItemDurabilityChangedCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/item/ItemStack;I)V"
        )
    )
    private boolean limitDamageApplied(ItemDurabilityChangedCriterion instance, ServerPlayerEntity player, ItemStack stack, int durability, @Local(argsOnly = true) LocalIntRef amount) {
        this.itematic$getComponent(ItemComponentTypes.DAMAGEABLE)
            .map(c -> Math.min(c.maximumDamage() - this.getDamage(), amount.get()))
            .ifPresent(amount::set);
        return amount.get() != 0;
    }

    @Inject(
        method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/lang/Runnable;)V",
        at = @At(
            value = "INVOKE",
            target = "Ljava/lang/Runnable;run()V"
        )
    )
    private void invokeBreakToolEvent(int amount, Random random, ServerPlayerEntity serverPlayerEntity, Runnable runnable, CallbackInfo info) {
        if (this.context == null) {
            return;
        }
        this.itematic$invokeEvent(ItemEvents.BREAK_ITEM, this.context);
    }

    @Override
    public RegistryKey<Item> itematic$key() {
        if (this.entry == null) {
            return ItemKeys.AIR;
        }
        return this.entry.getKey().orElse(ItemKeys.AIR);
    }

    @Override
    public Optional<NbtCompound> itematic$nbt() {
        return Optional.ofNullable(this.nbt);
    }

    @Override
    public ItemStack itematic$copyWithItem(RegistryEntry<Item> item, int count) {
        if (this.isEmpty()) {
            return EMPTY;
        }
        return this.itematic$copyWithItemIgnoreEmpty(item, count);
    }

    @Override
    public ItemStack itematic$copyWithItemIgnoreEmpty(RegistryEntry<Item> item, int count) {
        ItemStack stack = new ItemStack(item, count);
        if (this.nbt != null) {
            stack.setNbt(this.nbt.copy());
        }
        return stack;
    }

    @Override
    public boolean itematic$isOf(RegistryKey<Item> key) {
        return this.entry != null && this.entry.matchesKey(key);
    }

    @Override
    public void itematic$damage(int amount, ActionContext context) {
        if (context.player(ActionContextParameter.THIS).map(PlayerEntity::isCreative).orElse(false)) {
            return;
        }
        this.context = context;
        Entity entity = context.entity(ActionContextParameter.THIS).orElse(null);
        this.damage(amount, context.world().getRandom(), entity instanceof ServerPlayerEntity player ? player : null, () -> this.onItemBroken(entity, context));
        this.context = null;
    }

    @Override
    public <T extends ItemComponent<T>> boolean itematic$hasComponent(ItemComponentType<T> type) {
        return this.entry != null && this.entry.value().itematic$hasComponent(type);
    }

    @Override
    public <T extends ItemComponent<T>> Optional<T> itematic$getComponent(ItemComponentType<T> type) {
        if (this.entry == null) {
            return Optional.empty();
        }
        return this.entry.value().itematic$getComponent(type);
    }

    @Override
    public boolean itematic$invokeEvent(ItemEvent event, ActionContext context) {
        if (this.entry == null) {
            return false;
        }
        if (this.activeEvents.contains(event)) {
            return false;
        }
        this.activeEvents.add(event);
        boolean result = this.entry.value().itematic$invokeEvent(event, context);
        this.activeEvents.remove(event);
        return result;
    }

    @Override
    public boolean itematic$canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        if (this.entry == null) {
            return true;
        }
        return this.entry.value().canMine(state, world, pos, miner);
    }

    @Override
    public boolean itematic$isNetworkSynced() {
        if (this.entry == null) {
            return false;
        }
        return this.entry.value().isNetworkSynced();
    }

    @Override
    public boolean itematic$mayStartUsing(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        if (this.entry == null) {
            return false;
        }
        return this.entry.value().itematic$mayStartUsing(world, user, hand, stack);
    }

    @Unique
    private void onItemBroken(Entity entity, ActionContext context) {
        if (entity instanceof LivingEntity livingEntity) {
            context.slot().ifPresent(livingEntity::sendEquipmentBreakStatus);
        }
        this.decrement(1);
        this.itematic$invokeEvent(ItemEvents.BREAK_ITEM, context);
        if (entity instanceof PlayerEntity player && this.entry != null) {
            player.incrementStat(Stats.BROKEN.getOrCreateStat(this.entry.value()));
        }
        this.setDamage(0);
    }

    @Mixin(targets = "net/minecraft/item/ItemStack$1")
    public static class PacketCodecExtender {
        @Unique
        private static final PacketCodec<RegistryByteBuf, RegistryEntry<Item>> REGISTRY_ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(RegistryKeys.ITEM);

        @Redirect(
            method = "decode(Lnet/minecraft/network/RegistryByteBuf;)Lnet/minecraft/item/ItemStack;",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/network/codec/PacketCodec;decode(Ljava/lang/Object;)Ljava/lang/Object;"
            )
        )
        private Object decodeUseRegistryEntry(PacketCodec<RegistryByteBuf, Item> instance, Object byteBuf, RegistryByteBuf registryByteBuf, @Share("itemEntry") LocalRef<RegistryEntry<Item>> itemEntry) {
            itemEntry.set(REGISTRY_ENTRY_PACKET_CODEC.decode(registryByteBuf));
            return null;
        }

        @Redirect(
            method = "decode(Lnet/minecraft/network/RegistryByteBuf;)Lnet/minecraft/item/ItemStack;",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/item/ItemStack;"
            )
        )
        private ItemStack newItemStackUseRegistryEntry(ItemConvertible item, int count, @Share("itemEntry") LocalRef<RegistryEntry<Item>> itemEntry) {
            return new ItemStack(itemEntry.get(), count);
        }

        @Redirect(
            method = "encode(Lnet/minecraft/network/RegistryByteBuf;Lnet/minecraft/item/ItemStack;)V",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/network/codec/PacketCodec;encode(Ljava/lang/Object;Ljava/lang/Object;)V"
            )
        )
        private void encodeUseRegistryEntry(PacketCodec<RegistryByteBuf, Item> instance, Object byteBuf, Object item, RegistryByteBuf registryByteBuf, ItemStack stack) {
            REGISTRY_ENTRY_PACKET_CODEC.encode(registryByteBuf, stack.getRegistryEntry());
        }
    }
}
