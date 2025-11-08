package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.access.item.ItemStackAccess;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.ImmuneToDamageComponent;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItemUtil;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.util.Util;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.advancement.criterion.ItemDurabilityChangedCriterion;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
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
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(ItemStack.class)
public abstract class ItemStackExtender implements ComponentHolder, ItemStackAccess {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    @Final
    public static ItemStack EMPTY;

    @Shadow
    private int count;

    @Shadow
    @Final
    @Mutable
    ComponentMapImpl components;

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
    public abstract int getMaxCount();

    @Shadow
    public abstract int getCount();

    @Unique
    private final Set<ItemEvent> activeEvents = new HashSet<>();

    @Unique
    private RegistryEntry<Item> entry;

    @Unique
    private ActionContext context;

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getEntryCodec()Lcom/mojang/serialization/Codec;"
        )
    )
    private static Codec<RegistryEntry<Item>> registryEntryCodecDoNotUseStaticItemRegistry(DefaultedRegistry<Item> instance) {
        return RegistryFixedCodec.of(RegistryKeys.ITEM);
    }

    @Inject(
        method = "<init>(Lnet/minecraft/registry/entry/RegistryEntry;)V",
        at = @At("TAIL")
    )
    private void registryEntryConstructorSetFields(RegistryEntry<Item> entry, CallbackInfo info) {
        this.setFields(entry);
    }

    @Inject(
        method = "<init>(Lnet/minecraft/registry/entry/RegistryEntry;I)V",
        at = @At("TAIL")
    )
    private void registryEntryConstructorSetFields(RegistryEntry<Item> entry, int count, CallbackInfo info) {
        this.setFields(entry);
    }

    @Inject(
        method = "<init>(Lnet/minecraft/registry/entry/RegistryEntry;ILnet/minecraft/component/ComponentChanges;)V",
        at = @At("TAIL")
    )
    private void componentChangesConstructorSetFields(RegistryEntry<Item> item, int count, ComponentChanges changes, CallbackInfo info) {
        this.entry = item;
        this.components = ComponentMapImpl.create(item.value().getComponents(), changes);
        item.value().postProcessComponents((ItemStack)(Object) this);
    }

    @Redirect(
        method = { "<init>(Lnet/minecraft/registry/entry/RegistryEntry;)V", "<init>(Lnet/minecraft/registry/entry/RegistryEntry;I)V", "<init>(Lnet/minecraft/registry/entry/RegistryEntry;ILnet/minecraft/component/ComponentChanges;)V" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/entry/RegistryEntry;value()Ljava/lang/Object;"
        )
    )
    private static <T> T registryEntryValueReturnNullToPreventUnboundRegistryEntryIssues(RegistryEntry<T> instance) {
        return null;
    }

    @Redirect(
        method = { "<init>(Lnet/minecraft/item/ItemConvertible;I)V" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemConvertible;asItem()Lnet/minecraft/item/Item;"
        )
    )
    private static Item asItemReturnNullToPreventNullPointerExceptionStatic(ItemConvertible instance) {
        return null;
    }

    @Redirect(
        method = "<init>(Lnet/minecraft/item/ItemConvertible;ILnet/minecraft/component/ComponentMapImpl;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemConvertible;asItem()Lnet/minecraft/item/Item;"
        )
    )
    private Item asItemReturnNullToPreventNullPointerException(ItemConvertible instance) {
        return null;
    }

    @Redirect(
        method = { "<init>(Lnet/minecraft/item/ItemConvertible;I)V", "<init>(Lnet/minecraft/registry/entry/RegistryEntry;ILnet/minecraft/component/ComponentChanges;)V" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;getComponents()Lnet/minecraft/component/ComponentMap;"
        )
    )
    private static ComponentMap getComponentsReturnNullToPreventNullPointerException(Item instance) {
        return null;
    }

    @Redirect(
        method = "<init>(Lnet/minecraft/registry/entry/RegistryEntry;ILnet/minecraft/component/ComponentChanges;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/ComponentMapImpl;create(Lnet/minecraft/component/ComponentMap;Lnet/minecraft/component/ComponentChanges;)Lnet/minecraft/component/ComponentMapImpl;"
        )
    )
    private static ComponentMapImpl createComponentMapReturnNullToPreventNullPointerException(ComponentMap baseComponents, ComponentChanges changes) {
        return null;
    }

    @Inject(
        method = "<init>(Lnet/minecraft/item/ItemConvertible;ILnet/minecraft/component/ComponentMapImpl;)V",
        at = @At("TAIL")
    )
    private void checkItemValue(ItemConvertible item, int count, ComponentMapImpl components, CallbackInfo info) {
        if (item != null) {
            LOGGER.warn(Util.stackTraceMessage("Tried to create an item stack from an item or item-like value directly. This is no longer supported and should be modified to use a holder instead."));
        }
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
        method = "useOnEntity",
        at = @At("HEAD"),
        cancellable = true
    )
    public void useOnEntityCheckNullEntry(PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> info) {
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

    @Inject(
        method = "onStoppedUsing",
        at = @At("HEAD"),
        cancellable = true
    )
    public void onStoppedUsingCheckNullEntry(World world, LivingEntity user, int remainingUseTicks, CallbackInfo info) {
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
        method = "copy",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;ILnet/minecraft/component/ComponentMapImpl;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseRegistryEntry(ItemConvertible item, int count, ComponentMapImpl components) {
        ItemStack copy = new ItemStack(this.entry, count);
        copy.itematic$setComponents(components);
        return copy;
    }

    @ModifyArg(
        method = "copyComponentsToNewStackIgnoreEmpty",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/registry/entry/RegistryEntry;ILnet/minecraft/component/ComponentChanges;)V"
        )
    )
    private RegistryEntry<Item> getEntryUseField(RegistryEntry<Item> item) {
        return this.entry;
    }

    @Inject(
        method = "getMaxCount",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkStackableItemComponent(CallbackInfoReturnable<Integer> info) {
        if (!this.itematic$hasComponent(ItemComponentTypes.STACKABLE)) {
            info.setReturnValue(ItemUtil.UNSTACKABLE_MAX_STACK_SIZE);
        }
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
            || this.itematic$isOf(ItemKeys.AIR);
    }

    @Redirect(
        method = "isStackable",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isDamageable()Z"
        )
    )
    private boolean isDamageableReturnFalse(ItemStack instance) {
        return false;
    }

    /**
     * @author ErrorCraft
     * @reason Uses an empty check instead of a default air item.
     */
    @Overwrite
    public boolean isIn(TagKey<Item> tag) {
        if (this.isEmpty()) {
            return false;
        }
        return this.entry.isIn(tag);
    }

    /**
     * @author ErrorCraft
     * @reason Uses an empty check instead of a default air item.
     */
    @Overwrite
    public boolean itemMatches(Predicate<RegistryEntry<Item>> predicate) {
        if (this.isEmpty()) {
            return false;
        }
        return predicate.test(this.entry);
    }

    /**
     * @author ErrorCraft
     * @reason Uses an empty check instead of a default air item.
     */
    @Overwrite
    public boolean itemMatches(RegistryEntry<Item> itemEntry) {
        if (this.isEmpty()) {
            return false;
        }
        return this.entry == itemEntry;
    }

    @Inject(
        method = "isIn(Lnet/minecraft/registry/entry/RegistryEntryList;)Z",
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
        method = { "areItemsEqual", "areItemsAndComponentsEqual" },
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

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean takesDamageFrom(DamageSource source) {
        ImmuneToDamageComponent immuneToDamage = this.get(ItematicDataComponentTypes.IMMUNE_TO_DAMAGE);
        return immuneToDamage == null || immuneToDamage.damage(source);
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
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForFilledMapUseItemComponentCheck(ItemStack instance, Item item) {
        return this.itematic$hasComponent(ItemComponentTypes.MAP_HOLDER);
    }

    @Redirect(
        method = "getTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/List;Lnet/minecraft/client/item/TooltipType;)V"
        )
    )
    private void appendTooltipUseRegistryEntry(Item instance, ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (this.entry != null) {
            this.entry.value().appendTooltip(stack, context, tooltip, type);
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
    private <T> Identifier getIdUseRegistryEntry(DefaultedRegistry<T> instance, T t) {
        return this.itematic$key().getValue();
    }

    @Inject(
        method = "onStackClicked",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onStackClickedUseRegistryEntryNullCheck(Slot slot, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
        if (this.isEmpty()) {
            info.setReturnValue(false);
        }
    }

    @Inject(
        method = "onClicked",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onClickedUseRegistryEntryNullCheck(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> info) {
        if (this.isEmpty()) {
            info.setReturnValue(false);
        }
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
            .map(c -> Math.min(c.maximumDamage((ItemStack)(Object) this) - this.getDamage(), amount.get()))
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

    @Redirect(
        method = { "useOnBlock", "method_56097", "postHit", "postMine", "onCraftByPlayer" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
        )
    )
    private <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key) {
        return instance.itematic$getOrCreateStat(this.entry);
    }

    /**
     * @author ErrorCraft
     * @reason Uses a registry entry on the item stack for data-driven items.
     */
    @Overwrite
    public String toString() {
        return this.count + " " + this.itematic$key().getValue().toString();
    }

    @Override
    public RegistryKey<Item> itematic$key() {
        if (this.entry == null) {
            return ItemKeys.AIR;
        }
        return this.entry.getKey().orElse(ItemKeys.AIR);
    }

    @Override
    public void itematic$setComponents(ComponentMapImpl components) {
        this.components = components;
    }

    @Override
    public void itematic$tryIncrement(int count) {
        if (this.isEmpty()) {
            return;
        }
        this.count = MathHelper.clamp(this.count + count, 0, this.getMaxCount());
    }

    @Override
    public int itematic$tryDecrement(int amount) {
        int actualAmount = Math.min(amount, this.getCount());
        this.decrement(actualAmount);
        return actualAmount;
    }

    @Override
    public ItemStack itematic$copyWithItem(RegistryEntry<Item> item) {
        return this.itematic$copyComponentsToNewStack(item, this.count);
    }

    @Override
    public ItemStack itematic$copyComponentsToNewStack(RegistryEntry<Item> item, int count) {
        if (this.isEmpty()) {
            return EMPTY;
        }
        return this.itematic$copyComponentsToNewStackIgnoreEmpty(item, count);
    }

    @Override
    public ItemStack itematic$copyComponentsToNewStackIgnoreEmpty(RegistryEntry<Item> item, int count) {
        return new ItemStack(item, count, this.components.getChanges());
    }

    @Override
    public boolean itematic$isOf(RegistryKey<Item> key) {
        return this.entry != null && this.entry.hasKeyAndValue() && this.entry.matchesKey(key);
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
        if (miner.isCreative() && this.isIn(ItematicItemTags.PREVENTS_MINING_IN_CREATIVE)) {
            return false;
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
    private void setFields(RegistryEntry<Item> entry) {
        this.entry = entry;
        if (entry.hasKeyAndValue()) {
            this.components = new ComponentMapImpl(entry.value().getComponents());
            entry.value().postProcessComponents((ItemStack)(Object) this);
        } else {
            this.components = new ComponentMapImpl(ComponentMap.EMPTY);
        }
    }

    @Unique
    private void onItemBroken(Entity entity, ActionContext context) {
        if (entity instanceof LivingEntity livingEntity) {
            context.slot().ifPresent(livingEntity::sendEquipmentBreakStatus);
        }
        this.decrement(1);
        this.itematic$invokeEvent(ItemEvents.BREAK_ITEM, context);
        if (entity instanceof PlayerEntity player && this.entry != null) {
            player.incrementStat(Stats.BROKEN.itematic$getOrCreateStat(this.entry));
        }
        this.setDamage(0);
    }
}
