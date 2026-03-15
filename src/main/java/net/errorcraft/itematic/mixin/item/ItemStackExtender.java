package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.access.item.ItemStackAccess;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItemUtil;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ShooterItemComponent;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.item.shooter.method.ShooterMethodTypes;
import net.errorcraft.itematic.util.Util;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(ItemStack.class)
public abstract class ItemStackExtender implements ComponentHolder, ItemStackAccess, FabricItemStack {
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
    MergedComponentMap components;

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract void damage(int amount, ServerWorld world, @Nullable ServerPlayerEntity player, Consumer<Item> breakCallback);

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
    private NewActionContext context;

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
        this.setFields(item, changes);
    }

    @Redirect(
        method = {
            "<init>(Lnet/minecraft/registry/entry/RegistryEntry;)V",
            "<init>(Lnet/minecraft/registry/entry/RegistryEntry;I)V",
            "<init>(Lnet/minecraft/registry/entry/RegistryEntry;ILnet/minecraft/component/ComponentChanges;)V"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/entry/RegistryEntry;value()Ljava/lang/Object;"
        )
    )
    private static <T> T registryEntryValueReturnNullToPreventUnboundRegistryEntryIssues(RegistryEntry<T> instance) {
        return null;
    }

    @Redirect(
        method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemConvertible;asItem()Lnet/minecraft/item/Item;"
        )
    )
    private static Item asItemReturnNullToPreventNullPointerExceptionStatic(ItemConvertible instance) {
        return null;
    }

    @Redirect(
        method = "<init>(Lnet/minecraft/item/ItemConvertible;ILnet/minecraft/component/MergedComponentMap;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemConvertible;asItem()Lnet/minecraft/item/Item;"
        )
    )
    private Item asItemReturnNullToPreventNullPointerException(ItemConvertible instance) {
        return null;
    }

    @Redirect(
        method = {
            "<init>(Lnet/minecraft/item/ItemConvertible;I)V",
            "<init>(Lnet/minecraft/registry/entry/RegistryEntry;ILnet/minecraft/component/ComponentChanges;)V"
        },
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
            target = "Lnet/minecraft/component/MergedComponentMap;create(Lnet/minecraft/component/ComponentMap;Lnet/minecraft/component/ComponentChanges;)Lnet/minecraft/component/MergedComponentMap;"
        )
    )
    private static MergedComponentMap createComponentMapReturnNullToPreventNullPointerException(ComponentMap baseComponents, ComponentChanges changes) {
        return null;
    }

    @Inject(
        method = "<init>(Lnet/minecraft/item/ItemConvertible;ILnet/minecraft/component/MergedComponentMap;)V",
        at = @At("TAIL")
    )
    private void checkItemValue(ItemConvertible item, int count, MergedComponentMap components, CallbackInfo info) {
        if (item != null) {
            LOGGER.warn(Util.stackTraceMessage("Tried to create an item stack from an item or item-like value directly. This is no longer supported and should be modified to use a holder instead."));
        }
    }

    @Inject(
        method = {
            "use",
            "useOnBlock",
            "useOnEntity"
        },
        at = @At("HEAD"),
        cancellable = true
    )
    public void checkEmptyStackActionResult(CallbackInfoReturnable<ActionResult> info) {
        if (this.isEmpty()) {
            info.setReturnValue(ActionResult.PASS);
        }
    }

    @Inject(
        method = "postHit",
        at = @At("HEAD"),
        cancellable = true
    )
    public void checkEmptyStackBoolean(CallbackInfoReturnable<Boolean> info) {
        if (this.isEmpty()) {
            info.setReturnValue(false);
        }
    }

    @Inject(
        method = {
            "usageTick",
            "onStoppedUsing",
            "postMine",
            "onCraftByPlayer"
        },
        at = @At("HEAD"),
        cancellable = true
    )
    public void checkEmptyStack(CallbackInfo info) {
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

    @ModifyExpressionValue(
        method = "getItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"
        )
    )
    private boolean isEmptyCheckUnboundRegistryEntry(boolean original) {
        return original || !this.entry.hasKeyAndValue();
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
            target = "(Lnet/minecraft/item/ItemConvertible;ILnet/minecraft/component/MergedComponentMap;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseRegistryEntry(ItemConvertible item, int count, MergedComponentMap components) {
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
        if (!this.itematic$hasBehavior(ItemComponentTypes.STACKABLE)) {
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
        method = "canRepairWith",
        at = @At("HEAD"),
        cancellable = true
    )
    public void containsDataComponentUseItemBehaviorComponent(ItemStack ingredient, CallbackInfoReturnable<Boolean> info) {
        if (!this.itematic$hasBehavior(ItemComponentTypes.REPAIRABLE)) {
            info.setReturnValue(false);
        }
    }

    @ModifyExpressionValue(
        method = "isEnchantable",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;contains(Lnet/minecraft/component/ComponentType;)Z"
        )
    )
    public boolean containsEnchantableDataComponentAlsoCheckItemBehaviorComponent(boolean original) {
        return original && this.itematic$hasBehavior(ItemComponentTypes.ENCHANTABLE);
    }

    @Inject(
        method = {
            "areItemsEqual",
            "areItemsAndComponentsEqual"
        },
        at = @At("HEAD"),
        cancellable = true
    )
    private static void checkEmptyStacksPrematurely(ItemStack stack, ItemStack otherStack, CallbackInfoReturnable<Boolean> info) {
        if (stack.isEmpty() && otherStack.isEmpty()) {
            info.setReturnValue(true);
        }
    }

    @Redirect(
        method = {
            "areItemsEqual",
            "areItemsAndComponentsEqual"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean isOfUseRegistryEntryCheck(ItemStack instance, Item item, ItemStack left, ItemStack right) {
        return instance.itemMatches(right.getRegistryEntry());
    }

    @Inject(
        method = "isUsedOnRelease",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkForChargeableShooter(CallbackInfoReturnable<Boolean> info) {
        this.itematic$getBehavior(ItemComponentTypes.SHOOTER)
            .map(ShooterItemComponent::method)
            .filter(method -> method.type() == ShooterMethodTypes.CHARGEABLE)
            .ifPresent(method -> info.setReturnValue(true));
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
        method = "getItemName",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkEmptyStack(CallbackInfoReturnable<Text> info) {
        if (this.isEmpty()) {
            info.setReturnValue(Text.empty());
        }
    }

    @WrapWithCondition(
        method = "getTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/List;Lnet/minecraft/item/tooltip/TooltipType;)V"
        )
    )
    private boolean appendTooltipCheckRegistryEntry(Item instance, ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        return this.entry != null;
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

    @ModifyReturnValue(
        method = "calculateDamage",
        at = @At("RETURN")
    )
    private int limitDamageApplied(int original) {
        return this.itematic$getBehavior(ItemComponentTypes.DAMAGEABLE)
            .map(c -> Math.min(c.maximumDamage((ItemStack)(Object) this) - this.getDamage(), original))
            .orElse(original);
    }

    @Inject(
        method = "onDurabilityChange",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;setDamage(I)V",
            shift = At.Shift.AFTER
        )
    )
    private void invokeDamageItemEvent(int damage, @Nullable ServerPlayerEntity player, Consumer<Item> breakCallback, CallbackInfo info) {
        if (this.context == null) {
            return;
        }

        this.itematic$invokeEvent(ItemEvents.DAMAGE_ITEM, this.context);
    }

    @Inject(
        method = "onDurabilityChange",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"
        )
    )
    private void invokeBreakItemEvent(int damage, @Nullable ServerPlayerEntity player, Consumer<Item> breakCallback, CallbackInfo info) {
        if (this.context == null) {
            return;
        }

        this.itematic$invokeEvent(ItemEvents.BREAK_ITEM, this.context);
    }

    @Redirect(
        method = {
            "useOnBlock",
            "postHit",
            "postMine",
            "onCraftByPlayer"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
        )
    )
    private <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key) {
        return instance.itematic$getOrCreateStat(this.entry);
    }

    @WrapOperation(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/ActionResult$Success;withNewHandStack(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/util/ActionResult$Success;"
        )
    )
    private ActionResult.Success doNotModifyResultingItemStackIfNotUseable(ActionResult.Success instance, ItemStack newHandStack, Operation<ActionResult.Success> original) {
        if (!this.itematic$hasBehavior(ItemComponentTypes.USEABLE)) {
            return instance;
        }

        return original.call(instance, newHandStack);
    }

    @Inject(
        method = "applyRemainderAndCooldown",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkForUseableBehavior(LivingEntity user, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        if (!this.itematic$hasBehavior(ItemComponentTypes.USEABLE)) {
            info.setReturnValue((ItemStack)(Object) this);
        }
    }

    /**
     * @author ErrorCraft
     * @reason Uses a registry entry on the item stack for data-driven items.
     */
    @Overwrite
    public String toString() {
        return this.count + " " + this.itematic$key().getValue().toString();
    }

    @Inject(
        method = "hashCode",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void checkEmptyStack(ItemStack stack, CallbackInfoReturnable<Integer> info) {
        if (stack != null && (stack.isEmpty() || !stack.getRegistryEntry().hasKeyAndValue())) {
            info.setReturnValue(0);
        }
    }

    @Override
    public boolean canBeEnchantedWith(RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        // Use the original implementation again
        return enchantment.value().isAcceptableItem((ItemStack)(Object) this);
    }

    @Override
    public RegistryKey<Item> itematic$key() {
        if (this.entry == null) {
            return ItemKeys.AIR;
        }

        return this.entry.getKey().orElse(ItemKeys.AIR);
    }

    @Override
    public void itematic$setComponents(MergedComponentMap components) {
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
    public void itematic$damage(int amount, NewActionContext context) {
        this.context = context;
        Entity entity = context.get(LootContextParameters.THIS_ENTITY);
        this.damage(
            amount,
            context.world(),
            entity instanceof ServerPlayerEntity player ? player : null,
            item -> this.onItemBroken(item, entity, context)
        );
        this.context = null;
    }

    @Override
    public <T extends ItemComponent<T>> boolean itematic$hasBehavior(ItemComponentType<T> type) {
        return this.entry != null && this.entry.value().itematic$hasBehavior(type);
    }

    @Override
    public <T extends ItemComponent<T>> Optional<T> itematic$getBehavior(ItemComponentType<T> type) {
        if (this.entry == null) {
            return Optional.empty();
        }

        return this.entry.value().itematic$getBehavior(type);
    }

    @Override
    public boolean itematic$invokeEvent(ItemEvent event, NewActionContext context) {
        if (this.isEmpty()) {
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
    public boolean itematic$hasEventListener(ItemEvent event) {
        if (this.entry == null) {
            return false;
        }

        return this.entry.value().itematic$hasEventListener(event);
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
    public boolean itematic$mayStartUsing(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        if (this.entry == null) {
            return false;
        }

        return this.entry.value().itematic$mayStartUsing(world, user, hand, stack);
    }

    @Override
    public double itematic$attackSpeedMultiplier() {
        if (!this.itematic$hasBehavior(ItemComponentTypes.WEAPON)) {
            return 1.0d;
        }

        return this.getOrDefault(ItematicDataComponentTypes.ATTACK_SPEED_MULTIPLIER, 1.0d);
    }

    @Unique
    private void setFields(RegistryEntry<Item> entry) {
        this.entry = entry;
        if (entry.hasKeyAndValue()) {
            this.components = new MergedComponentMap(entry.value().getComponents());
            entry.value().postProcessComponents((ItemStack)(Object) this);
        } else {
            this.components = new MergedComponentMap(ComponentMap.EMPTY);
        }
    }

    @Unique
    private void setFields(RegistryEntry<Item> entry, ComponentChanges changes) {
        this.entry = entry;
        if (entry.hasKeyAndValue()) {
            this.components = MergedComponentMap.create(entry.value().getComponents(), changes);
            entry.value().postProcessComponents((ItemStack)(Object) this);
        } else {
            this.components = new MergedComponentMap(ComponentMap.EMPTY);
        }
    }

    @Unique
    private void onItemBroken(Item item, Entity entity, NewActionContext context) {
        EquipmentSlot slot = context.get(ItematicContextParameters.EQUIPMENT_SLOT);
        if (slot != null && entity instanceof LivingEntity livingEntity) {
            livingEntity.sendEquipmentBreakStatus(item, slot);
        }

        this.itematic$invokeEvent(ItemEvents.BREAK_ITEM, context);
    }
}
