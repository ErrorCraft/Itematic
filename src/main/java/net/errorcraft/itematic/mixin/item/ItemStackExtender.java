package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.access.item.ItemStackAccess;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.Util;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.Items;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.ShooterItemBehavior;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.KineticWeapon;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(ItemStack.class)
public abstract class ItemStackExtender implements DataComponentHolder, ItemStackAccess, FabricItemStack {
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
    PatchedDataComponentMap components;

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract ItemStack split(int amount);

    @Shadow
    public abstract void hurtAndBreak(int amount, ServerLevel world, @Nullable ServerPlayer player, Consumer<Item> breakCallback);

    @Shadow
    public abstract int getDamageValue();

    @Shadow
    public abstract void shrink(int amount);

    @Shadow
    public abstract int getMaxStackSize();

    @Shadow
    public abstract ItemStack copyWithCount(int count);

    @Shadow
    public abstract int getCount();

    @Unique
    private final Set<ItemEvent> activeEvents = new HashSet<>();

    @Unique
    private Holder<Item> entry;

    @Unique
    private ActionContext context;

    @Inject(
        method = "<init>(Lnet/minecraft/core/Holder;)V",
        at = @At("TAIL")
    )
    private void registryEntryConstructorSetFields(Holder<Item> entry, CallbackInfo info) {
        this.setFields(entry);
    }

    @Inject(
        method = "<init>(Lnet/minecraft/core/Holder;I)V",
        at = @At("TAIL")
    )
    private void registryEntryConstructorSetFields(Holder<Item> entry, int count, CallbackInfo info) {
        this.setFields(entry);
    }

    @Inject(
        method = "<init>(Lnet/minecraft/core/Holder;ILnet/minecraft/core/component/DataComponentPatch;)V",
        at = @At("TAIL")
    )
    private void componentChangesConstructorSetFields(Holder<Item> item, int count, DataComponentPatch changes, CallbackInfo info) {
        this.setFields(item, changes);
    }

    @Redirect(
        method = {
            "<init>(Lnet/minecraft/core/Holder;)V",
            "<init>(Lnet/minecraft/core/Holder;I)V",
            "<init>(Lnet/minecraft/core/Holder;ILnet/minecraft/core/component/DataComponentPatch;)V"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/Holder;value()Ljava/lang/Object;"
        )
    )
    private static <T> T registryEntryValueReturnNullToPreventUnboundRegistryEntryIssues(Holder<T> instance) {
        return null;
    }

    @Redirect(
        method = "<init>(Lnet/minecraft/world/level/ItemLike;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/ItemLike;asItem()Lnet/minecraft/world/item/Item;"
        )
    )
    private static Item asItemReturnNullToPreventNullPointerExceptionStatic(ItemLike instance) {
        return null;
    }

    @Redirect(
        method = "<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/core/component/PatchedDataComponentMap;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/ItemLike;asItem()Lnet/minecraft/world/item/Item;"
        )
    )
    private Item asItemReturnNullToPreventNullPointerException(ItemLike instance) {
        return null;
    }

    @Redirect(
        method = {
            "<init>(Lnet/minecraft/world/level/ItemLike;I)V",
            "<init>(Lnet/minecraft/core/Holder;ILnet/minecraft/core/component/DataComponentPatch;)V"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;components()Lnet/minecraft/core/component/DataComponentMap;"
        )
    )
    private static DataComponentMap getComponentsReturnNullToPreventNullPointerException(Item instance) {
        return null;
    }

    @Redirect(
        method = "<init>(Lnet/minecraft/core/Holder;ILnet/minecraft/core/component/DataComponentPatch;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/component/PatchedDataComponentMap;fromPatch(Lnet/minecraft/core/component/DataComponentMap;Lnet/minecraft/core/component/DataComponentPatch;)Lnet/minecraft/core/component/PatchedDataComponentMap;"
        )
    )
    private static PatchedDataComponentMap createComponentMapReturnNullToPreventNullPointerException(DataComponentMap baseComponents, DataComponentPatch changes) {
        return null;
    }

    @Inject(
        method = "<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/core/component/PatchedDataComponentMap;)V",
        at = @At("TAIL")
    )
    private void checkItemValue(ItemLike item, int count, PatchedDataComponentMap components, CallbackInfo info) {
        if (item != null) {
            LOGGER.warn(Util.stackTraceMessage("Tried to create an item stack from an item or item-like value directly. This is no longer supported and should be modified to use a holder instead."));
        }
    }

    @Inject(
        method = {
            "use",
            "useOn",
            "interactLivingEntity"
        },
        at = @At("HEAD"),
        cancellable = true
    )
    public void checkEmptyStackActionResult(CallbackInfoReturnable<InteractionResult> info) {
        if (this.isEmpty()) {
            info.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(
        method = "hurtEnemy",
        at = @At("HEAD"),
        cancellable = true
    )
    public void checkEmptyStackBooleanFalse(CallbackInfoReturnable<Boolean> info) {
        if (this.isEmpty()) {
            info.setReturnValue(false);
        }
    }

    @Inject(
        method = "canDestroyBlock",
        at = @At("HEAD"),
        cancellable = true
    )
    public void checkEmptyStackBooleanTrue(CallbackInfoReturnable<Boolean> info) {
        if (this.isEmpty()) {
            info.setReturnValue(true);
        }
    }

    @Inject(
        method = {
            "onUseTick",
            "releaseUsing",
            "mineBlock",
            "onCraftedBy"
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
    public Holder<Item> getItemHolder() {
        return this.entry;
    }

    @ModifyExpressionValue(
        method = "getItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"
        )
    )
    private boolean isEmptyCheckUnboundRegistryEntry(boolean original) {
        return original || !this.entry.isBound();
    }

    @Redirect(
        method = "getItem",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/ItemStack;item:Lnet/minecraft/world/item/Item;",
            opcode = Opcodes.GETFIELD
        )
    )
    private Item getItemGetItemFieldUseRegistryEntryToPreventNullPointerException(ItemStack instance) {
        return this.entry.value();
    }

    @Redirect(
        method = "copy()Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/core/component/PatchedDataComponentMap;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseRegistryEntry(ItemLike item, int count, PatchedDataComponentMap components) {
        ItemStack copy = new ItemStack(this.entry, count);
        copy.itematic$setComponents(components);
        return copy;
    }

    @ModifyArg(
        method = "transmuteCopyIgnoreEmpty",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/core/Holder;ILnet/minecraft/core/component/DataComponentPatch;)V"
        )
    )
    private Holder<Item> getEntryUseField(Holder<Item> item) {
        return this.entry;
    }

    @Inject(
        method = "getMaxStackSize",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkStackableItemBehavior(CallbackInfoReturnable<Integer> info) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.STACKABLE)) {
            info.setReturnValue(Items.UNSTACKABLE_MAX_STACK_SIZE);
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
    public Stream<TagKey<Item>> getTags() {
        if (this.entry == null) {
            return Stream.empty();
        }

        return this.entry.tags();
    }

    @ModifyReturnValue(
        method = "isEmpty",
        at = @At("TAIL")
    )
    private boolean checkNullEntryForEmptyStack(boolean original) {
        return original
            || this.entry == null
            || this.itematic$isOf(ItemIds.AIR);
    }

    @Redirect(
        method = "isStackable",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"
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
    public boolean is(TagKey<Item> tag) {
        if (this.isEmpty()) {
            return false;
        }

        return this.entry.is(tag);
    }

    /**
     * @author ErrorCraft
     * @reason Uses an empty check instead of a default air item.
     */
    @Overwrite
    public boolean is(Predicate<Holder<Item>> predicate) {
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
    public boolean is(Holder<Item> itemEntry) {
        if (this.isEmpty()) {
            return false;
        }

        return this.entry == itemEntry;
    }

    @Inject(
        method = "is(Lnet/minecraft/core/HolderSet;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkNullRegistryEntry(HolderSet<Item> registryEntryList, CallbackInfoReturnable<Boolean> info) {
        if (this.isEmpty()) {
            info.setReturnValue(false);
        }
    }

    @Redirect(
        method = "onUseTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/core/component/DataComponents;KINETIC_WEAPON:Lnet/minecraft/core/component/DataComponentType;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private Object getKineticWeaponReturnNull(ItemStack instance, DataComponentType<KineticWeapon> componentType) {
        return null;
    }

    @Inject(
        method = "isValidRepairItem",
        at = @At("HEAD"),
        cancellable = true
    )
    public void containsDataComponentUseItemBehaviorComponent(ItemStack ingredient, CallbackInfoReturnable<Boolean> info) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.REPAIRABLE)) {
            info.setReturnValue(false);
        }
    }

    @ModifyExpressionValue(
        method = "isEnchantable",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"
        )
    )
    public boolean containsEnchantableDataComponentAlsoCheckItemBehaviorComponent(boolean original) {
        return original && this.itematic$hasBehavior(ItemBehaviorType.ENCHANTABLE);
    }

    @Inject(
        method = {
            "isSameItem",
            "isSameItemSameComponents"
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
            "isSameItem",
            "isSameItemSameComponents"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private static boolean isOfUseRegistryEntryCheck(ItemStack instance, Item item, ItemStack left, ItemStack right) {
        return instance.is(right.getItemHolder());
    }

    @Inject(
        method = "useOnRelease",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkForChargeableShooter(CallbackInfoReturnable<Boolean> info) {
        this.itematic$getBehavior(ItemBehaviorType.SHOOTER)
            .map(ShooterItemBehavior::method)
            .filter(method -> method.type() == ShooterMethodType.CHARGEABLE)
            .ifPresent(method -> info.setReturnValue(true));
    }

    @Inject(
        method = "hasFoil",
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
    private void checkEmptyStack(CallbackInfoReturnable<Component> info) {
        if (this.isEmpty()) {
            info.setReturnValue(Component.empty());
        }
    }

    @Inject(
        method = "addDetailsToTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V"
        )
    )
    private void addTooltipFromItem(Item.TooltipContext context, TooltipDisplay displayComponent, Player player, TooltipFlag type, Consumer<Component> textConsumer, CallbackInfo info) {
        if (this.entry != null) {
            this.entry.value().itematic$addTooltip((ItemStack) (Object) this, context, textConsumer, type);
        }
    }

    @Redirect(
        method = "addDetailsToTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/DefaultedRegistry;getKey(Ljava/lang/Object;)Lnet/minecraft/resources/Identifier;"
        )
    )
    @NotNull
    private <T> Identifier getIdUseRegistryEntry(DefaultedRegistry<T> instance, T t) {
        return this.itematic$key().identifier();
    }

    @Inject(
        method = "overrideStackedOnOther",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onStackClickedUseRegistryEntryNullCheck(Slot slot, ClickAction clickType, Player player, CallbackInfoReturnable<Boolean> info) {
        if (this.isEmpty()) {
            info.setReturnValue(false);
        }
    }

    @Inject(
        method = "overrideOtherStackedOnMe",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onClickedUseRegistryEntryNullCheck(ItemStack stack, Slot slot, ClickAction clickType, Player player, SlotAccess cursorStackReference, CallbackInfoReturnable<Boolean> info) {
        if (this.isEmpty()) {
            info.setReturnValue(false);
        }
    }

    @Redirect(
        method = "postHurtEnemy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
        )
    )
    private Object getWeaponDataComponentReturnNull(ItemStack instance, DataComponentType<Weapon> type) {
        return null;
    }

    @ModifyReturnValue(
        method = "processDurabilityChange",
        at = @At("RETURN")
    )
    private int limitDamageApplied(int original) {
        return this.itematic$getBehavior(ItemBehaviorType.DAMAGEABLE)
            .map(c -> Math.min(c.maximumDamage((ItemStack) (Object) this) - this.getDamageValue(), original))
            .orElse(original);
    }

    @Inject(
        method = "applyDamage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V",
            shift = At.Shift.AFTER
        )
    )
    private void invokeDamageItemEvent(int damage, @Nullable ServerPlayer player, Consumer<Item> breakCallback, CallbackInfo info) {
        if (this.context == null) {
            return;
        }

        this.itematic$invokeEvent(ItemEvent.DAMAGE_ITEM, this.context);
    }

    @Inject(
        method = "applyDamage",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"
        )
    )
    private void invokeBreakItemEvent(int damage, @Nullable ServerPlayer player, Consumer<Item> breakCallback, CallbackInfo info) {
        if (this.context == null) {
            return;
        }

        this.itematic$invokeEvent(ItemEvent.BREAK_ITEM, this.context);
    }

    @Redirect(
        method = {
            "useOn",
            "hurtEnemy",
            "mineBlock",
            "onCraftedBy"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
        )
    )
    private <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key) {
        return instance.itematic$getOrCreateStat(this.entry);
    }

    @WrapOperation(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/InteractionResult$Success;heldItemTransformedTo(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/InteractionResult$Success;"
        )
    )
    private InteractionResult.Success doNotModifyResultingItemStackIfNotUseable(InteractionResult.Success instance, ItemStack newHandStack, Operation<InteractionResult.Success> original) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.USEABLE)) {
            return instance;
        }

        return original.call(instance, newHandStack);
    }

    @Inject(
        method = "applyAfterUseComponentSideEffects",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkForUseableBehavior(LivingEntity user, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.USEABLE)) {
            info.setReturnValue((ItemStack) (Object) this);
        }
    }

    @Redirect(
        method = "method_75224",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;getItemDamageSource(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/damagesource/DamageSource;"
        )
    )
    @Nullable
    @SuppressWarnings("ConstantValue")
    private DamageSource getDamageSourceUseItemBehavior(Item instance, LivingEntity user) {
        return this.itematic$getBehavior(ItemBehaviorType.WEAPON)
            .map(weapon -> weapon.damageSource((ItemStack)(Object) this, user))
            .orElse(null);
    }

    /**
     * @author ErrorCraft
     * @reason Uses a registry entry on the item stack for data-driven items.
     */
    @Overwrite
    public String toString() {
        return this.count + " " + this.itematic$key().identifier();
    }

    @Inject(
        method = "hashItemAndComponents",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void checkEmptyStack(ItemStack stack, CallbackInfoReturnable<Integer> info) {
        if (stack != null && (stack.isEmpty() || !stack.getItemHolder().isBound())) {
            info.setReturnValue(0);
        }
    }

    @Override
    public boolean canBeEnchantedWith(Holder<Enchantment> enchantment, EnchantingContext context) {
        // Use the original implementation again
        return enchantment.value().canEnchant((ItemStack) (Object) this);
    }

    @Override
    public ResourceKey<Item> itematic$key() {
        if (this.entry == null) {
            return ItemIds.AIR;
        }

        return this.entry.unwrapKey().orElse(ItemIds.AIR);
    }

    @Override
    public void itematic$setComponents(PatchedDataComponentMap components) {
        this.components = components;
    }

    @Override
    public void itematic$tryIncrement(int count) {
        if (this.isEmpty()) {
            return;
        }

        this.count = Mth.clamp(this.count + count, 0, this.getMaxStackSize());
    }

    @Override
    public int itematic$tryDecrement(int amount) {
        int actualAmount = Math.min(amount, this.getCount());
        this.shrink(actualAmount);
        return actualAmount;
    }

    @Override
    public ItemStack itematic$copyWithItem(Holder<Item> item) {
        return this.itematic$copyComponentsToNewStack(item, this.count);
    }

    @Override
    public ItemStack itematic$copyComponentsToNewStack(Holder<Item> item, int count) {
        if (this.isEmpty()) {
            return EMPTY;
        }

        return this.itematic$copyComponentsToNewStackIgnoreEmpty(item, count);
    }

    @Override
    public ItemStack itematic$copyComponentsToNewStackIgnoreEmpty(Holder<Item> item, int count) {
        return new ItemStack(item, count, this.components.asPatch());
    }

    @Override
    public boolean itematic$isOf(ResourceKey<Item> key) {
        return this.entry != null && this.entry.isBound() && this.entry.is(key);
    }

    @Override
    public void itematic$damage(int amount, ActionContext context) {
        if (!(context.world() instanceof ServerLevel world)) {
            return;
        }

        this.context = context;
        Entity entity = context.get(LootContextParams.THIS_ENTITY);
        this.hurtAndBreak(
            amount,
            world,
            entity instanceof ServerPlayer player ? player : null,
            item -> this.onItemBroken(item, entity, context)
        );
        this.context = null;
    }

    @Override
    public <T extends ItemBehavior<T>> boolean itematic$hasBehavior(ItemBehaviorType<T> type) {
        return this.entry != null && this.entry.value().itematic$hasBehavior(type);
    }

    @Override
    public <T extends ItemBehavior<T>> Optional<T> itematic$getBehavior(ItemBehaviorType<T> type) {
        if (this.entry == null) {
            return Optional.empty();
        }

        return this.entry.value().itematic$getBehavior(type);
    }

    @Override
    public boolean itematic$invokeEvent(ItemEvent event, ActionContext context) {
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
    public boolean itematic$mayStartUsing(Level world, Player user, InteractionHand hand, ItemStack stack) {
        if (this.entry == null) {
            return false;
        }

        return this.entry.value().itematic$mayStartUsing(world, user, hand, stack);
    }

    @Override
    public double itematic$attackSpeedMultiplier() {
        if (!this.itematic$hasBehavior(ItemBehaviorType.WEAPON)) {
            return 1.0d;
        }

        return this.getOrDefault(ItematicDataComponents.ATTACK_SPEED_MULTIPLIER, 1.0d);
    }

    @Unique
    private void setFields(Holder<Item> entry) {
        this.entry = entry;
        if (entry.isBound()) {
            this.components = new PatchedDataComponentMap(entry.value().components());
        } else {
            this.components = new PatchedDataComponentMap(DataComponentMap.EMPTY);
        }
    }

    @Unique
    private void setFields(Holder<Item> entry, DataComponentPatch changes) {
        this.entry = entry;
        if (entry.isBound()) {
            this.components = PatchedDataComponentMap.fromPatch(entry.value().components(), changes);
        } else {
            this.components = PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, changes);
        }
    }

    @Unique
    private void onItemBroken(Item item, Entity entity, ActionContext context) {
        EquipmentSlot slot = context.get(ItematicContextParameters.EQUIPMENT_SLOT);
        if (slot != null && entity instanceof LivingEntity livingEntity) {
            livingEntity.onEquippedItemBroken(item, slot);
        }

        this.itematic$invokeEvent(ItemEvent.BREAK_ITEM, context);
    }
}
