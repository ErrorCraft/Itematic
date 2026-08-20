package net.errorcraft.itematic.mixin.world.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.access.world.item.ItemStackAccess;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.ItematicUtil;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.Items;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.ShooterItemBehavior;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethod;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.KineticWeapon;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
    public abstract void hurtAndBreak(int amount, ServerLevel level, @Nullable ServerPlayer player, Consumer<Item> breakCallback);

    @Shadow
    public abstract int getDamageValue();

    @Shadow
    public abstract void shrink(int amount);

    @Shadow
    public abstract int getMaxStackSize();

    @Shadow
    public abstract int getCount();

    @Unique
    private final Set<ItemEvent> activeEvents = new HashSet<>();

    @Unique
    @Nullable
    private Holder<Item> item;

    @Unique
    @Nullable
    private ActionContext context;

    @Inject(
        method = "<init>(Lnet/minecraft/core/Holder;)V",
        at = @At("TAIL")
    )
    private void holderConstructorSetFields(Holder<Item> entry, CallbackInfo info) {
        this.setFields(entry);
    }

    @Inject(
        method = "<init>(Lnet/minecraft/core/Holder;I)V",
        at = @At("TAIL")
    )
    private void holderConstructorSetFields(Holder<Item> entry, int count, CallbackInfo info) {
        this.setFields(entry);
    }

    @Inject(
        method = "<init>(Lnet/minecraft/core/Holder;ILnet/minecraft/core/component/DataComponentPatch;)V",
        at = @At("TAIL")
    )
    private void holderConstructorSetFields(Holder<Item> item, int count, DataComponentPatch changes, CallbackInfo info) {
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
    @Nullable
    private static <T> T holderValueUseNullToPreventUnboundHolderIssues(Holder<T> instance) {
        return null;
    }

    @Redirect(
        method = {
            "<init>(Lnet/minecraft/world/level/ItemLike;I)V",
            "<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/core/component/PatchedDataComponentMap;)V"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/ItemLike;asItem()Lnet/minecraft/world/item/Item;"
        )
    )
    @Nullable
    private static Item asItemUseNullToPreventNullPointerExceptionStatic(ItemLike instance) {
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
    @Nullable
    private static DataComponentMap componentsUseNullToPreventNullPointerException(Item instance) {
        return null;
    }

    @Redirect(
        method = "<init>(Lnet/minecraft/core/Holder;ILnet/minecraft/core/component/DataComponentPatch;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/component/PatchedDataComponentMap;fromPatch(Lnet/minecraft/core/component/DataComponentMap;Lnet/minecraft/core/component/DataComponentPatch;)Lnet/minecraft/core/component/PatchedDataComponentMap;"
        )
    )
    @Nullable
    private static PatchedDataComponentMap fromPatchUseNullToPreventNullPointerException(DataComponentMap baseComponents, DataComponentPatch changes) {
        return null;
    }

    @Inject(
        method = "<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/core/component/PatchedDataComponentMap;)V",
        at = @At("TAIL")
    )
    private void checkItemValue(@Nullable ItemLike item, int count, PatchedDataComponentMap components, CallbackInfo info) {
        if (item != null) {
            LOGGER.warn(ItematicUtil.stackTraceMessage("Tried to create an item stack from an item or item-like value directly. This is no longer supported and should be modified to use a holder instead."));
        }
    }

    @WrapMethod(
        method = "use"
    )
    private InteractionResult checkEmptyStackForUse(Level level, Player player, InteractionHand hand, Operation<InteractionResult> original) {
        if (this.isEmpty()) {
            return InteractionResult.PASS;
        }

        return original.call(level, player, hand);
    }

    @WrapMethod(
        method = "useOn"
    )
    private InteractionResult checkEmptyStackForUseOn(UseOnContext context, Operation<InteractionResult> original) {
        if (this.isEmpty()) {
            return InteractionResult.PASS;
        }

        return original.call(context);
    }

    @WrapMethod(
        method = "interactLivingEntity"
    )
    private InteractionResult checkEmptyStackForInteractLivingEntity(Player player, LivingEntity target, InteractionHand hand, Operation<InteractionResult> original) {
        if (this.isEmpty()) {
            return InteractionResult.PASS;
        }

        return original.call(player, target, hand);
    }

    @WrapMethod(
        method = "hurtEnemy"
    )
    private boolean checkEmptyStackForHurtEnemy(LivingEntity mob, LivingEntity attacker, Operation<Boolean> original) {
        if (this.isEmpty()) {
            return false;
        }

        return original.call(mob, attacker);
    }

    @WrapMethod(
        method = "canDestroyBlock"
    )
    private boolean checkEmptyStackForCanDestroyBlock(BlockState state, Level level, BlockPos pos, Player player, Operation<Boolean> original) {
        if (this.isEmpty()) {
            return true;
        }

        return original.call(state, level, pos, player);
    }

    @WrapMethod(
        method = "onUseTick"
    )
    private void checkEmptyStackForOnUseTick(Level level, LivingEntity livingEntity, int ticksRemaining, Operation<Void> original) {
        if (this.isEmpty()) {
            return;
        }

        original.call(level, livingEntity, ticksRemaining);
    }

    @WrapMethod(
        method = "releaseUsing"
    )
    private void checkEmptyStackForReleaseUsing(Level level, LivingEntity entity, int remainingTime, Operation<Void> original) {
        if (this.isEmpty()) {
            return;
        }

        original.call(level, entity, remainingTime);
    }

    @WrapMethod(
        method = "mineBlock"
    )
    private void checkEmptyStackForMineBlock(Level level, BlockState state, BlockPos pos, Player owner, Operation<Void> original) {
        if (this.isEmpty()) {
            return;
        }

        original.call(level, state, pos, owner);
    }
    @WrapMethod(
        method = "onCraftedBy"
    )

    private void checkEmptyStackForOnCraftedBy(Player player, int craftCount, Operation<Void> original) {
        if (this.isEmpty()) {
            return;
        }

        original.call(player, craftCount);
    }

    @WrapMethod(
        method = "getItemHolder"
    )
    @Nullable
    private Holder<Item> useHolderField(Operation<Holder<Item>> original) {
        return this.item;
    }

    @Redirect(
        method = "getItem",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/ItemStack;item:Lnet/minecraft/world/item/Item;",
            opcode = Opcodes.GETFIELD
        )
    )
    @SuppressWarnings("DataFlowIssue")
    private Item useHolderToPreventNullPointerException(ItemStack instance) {
        return this.item.value();
    }

    @Redirect(
        method = "copy()Lnet/minecraft/world/item/ItemStack;",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/core/component/PatchedDataComponentMap;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    @SuppressWarnings("DataFlowIssue")
    private ItemStack newItemStackUseRegistryEntry(ItemLike item, int count, PatchedDataComponentMap components) {
        ItemStack copy = new ItemStack(this.item, count);
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
    @SuppressWarnings("DataFlowIssue")
    private Holder<Item> getEntryUseField(Holder<Item> item) {
        return this.item;
    }

    @WrapMethod(
        method = "getMaxStackSize"
    )
    private int alsoCheckStackableItemBehavior(Operation<Integer> original) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.STACKABLE)) {
            return Items.UNSTACKABLE_MAX_STACK_SIZE;
        }

        return original.call();
    }

    @WrapMethod(
        method = "getTags"
    )
    @SuppressWarnings("DataFlowIssue")
    public Stream<TagKey<Item>> useHolder(Operation<Stream<TagKey<Item>>> original) {
        if (this.isEmpty()) {
            return original.call();
        }

        return this.item.tags();
    }

    @ModifyReturnValue(
        method = "isEmpty",
        at = @At("TAIL")
    )
    private boolean checkNullForEmptyStack(boolean original) {
        return original
            || this.item == null
            || this.itematic$is(ItemIds.AIR);
    }

    @Redirect(
        method = "isStackable",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"
        )
    )
    private boolean isDamageableUseFalse(ItemStack instance) {
        return false;
    }

    @WrapMethod(
        method = "is(Lnet/minecraft/tags/TagKey;)Z"
    )
    @SuppressWarnings("DataFlowIssue")
    public boolean useHolder(TagKey<Item> tag, Operation<Boolean> original) {
        if (this.isEmpty()) {
            return false;
        }

        return this.item.is(tag);
    }

    @WrapMethod(
        method = "is(Ljava/util/function/Predicate;)Z"
    )
    @SuppressWarnings("DataFlowIssue")
    public boolean useHolder(Predicate<Holder<Item>> predicate, Operation<Boolean> original) {
        if (this.isEmpty()) {
            return false;
        }

        return predicate.test(this.item);
    }

    @WrapMethod(
        method = "is(Lnet/minecraft/core/Holder;)Z"
    )
    public boolean useHolder(Holder<Item> item, Operation<Boolean> original) {
        if (this.isEmpty()) {
            return false;
        }

        return this.item == item;
    }

    @WrapMethod(
        method = "is(Lnet/minecraft/core/HolderSet;)Z"
    )
    private boolean checkEmptyStackForIsHolderSet(HolderSet<Item> items, Operation<Boolean> original) {
        if (this.isEmpty()) {
            return false;
        }

        return original.call(items);
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
    @Nullable
    private Object getKineticWeaponReturnNull(ItemStack instance, DataComponentType<KineticWeapon> componentType) {
        return null;
    }

    @WrapMethod(
        method = "isValidRepairItem"
    )
    public boolean alsoCheckRepairableItemBehavior(ItemStack repairItem, Operation<Boolean> original) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.REPAIRABLE)) {
            return false;
        }

        return original.call(repairItem);
    }

    @ModifyExpressionValue(
        method = "isEnchantable",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"
        )
    )
    public boolean alsoCheckEnchantableItemBehavior(boolean original) {
        return original && this.itematic$hasBehavior(ItemBehaviorType.ENCHANTABLE);
    }

    @WrapMethod(
        method = {
            "isSameItem",
            "isSameItemSameComponents"
        }
    )
    private static boolean checkEmptyStacksPrematurely(ItemStack a, ItemStack b, Operation<Boolean> original) {
        if (a.isEmpty() && b.isEmpty()) {
            return true;
        }

        return original.call(a, b);
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
    private static boolean isItemCheckHolder(ItemStack instance, Item item, ItemStack a, ItemStack b) {
        return instance.is(b.getItemHolder());
    }

    @WrapMethod(
        method = "useOnRelease"
    )
    private boolean alsoCheckShooterItemBehavior(Operation<Boolean> original) {
        ShooterMethodType<?> shooterMethodType = this.itematic$getBehavior(ItemBehaviorType.SHOOTER)
            .map(ShooterItemBehavior::method)
            .map(ShooterMethod::type)
            .orElse(null);
        if (shooterMethodType == ShooterMethodType.CHARGEABLE) {
            return true;
        }

        return original.call();
    }

    @WrapMethod(
        method = "getItemName"
    )
    private Component checkEmptyStackForGetItemName(Operation<Component> original) {
        if (this.isEmpty()) {
            return Component.empty();
        }

        return original.call();
    }

    @WrapOperation(
        method = "addDetailsToTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V"
        )
    )
    @SuppressWarnings("DataFlowIssue")
    private void addTooltipFromItem(Item instance, ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag, Operation<Void> original) {
        if (!this.isEmpty()) {
            this.item.value().itematic$addTooltip((ItemStack) (Object) this, context, builder, tooltipFlag);
        }
    }

    @Redirect(
        method = "addDetailsToTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/DefaultedRegistry;getKey(Ljava/lang/Object;)Lnet/minecraft/resources/Identifier;"
        )
    )
    private <T> Identifier getIdHolder(DefaultedRegistry<T> instance, T t) {
        return this.itematic$key().identifier();
    }

    @WrapMethod(
        method = "overrideStackedOnOther"
    )
    private boolean checkEmptyStackForOverrideStackedOnOther(Slot slot, ClickAction clickAction, Player player, Operation<Boolean> original) {
        if (this.isEmpty()) {
            return false;
        }

        return original.call(slot, clickAction, player);
    }

    @WrapMethod(
        method = "overrideOtherStackedOnMe"
    )
    private boolean checkEmptyStackForOverrideOtherStackedOnMe(ItemStack other, Slot slot, ClickAction clickAction, Player player, SlotAccess carriedItem, Operation<Boolean> original) {
        if (this.isEmpty()) {
            return false;
        }

        return original.call(other, slot, clickAction, player, carriedItem);
    }

    @Redirect(
        method = "postHurtEnemy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
        )
    )
    @Nullable
    private Object getWeaponDataComponentUseNull(ItemStack instance, DataComponentType<Weapon> type) {
        return null;
    }

    @ModifyReturnValue(
        method = "processDurabilityChange",
        at = @At("RETURN")
    )
    private int limitDamageApplied(int original) {
        return this.itematic$getBehavior(ItemBehaviorType.DAMAGEABLE)
            .map(damageable -> Math.min(damageable.maximumDamage((ItemStack) (Object) this) - this.getDamageValue(), original))
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
    @SuppressWarnings("DataFlowIssue")
    private <T> Stat<Item> getStatUseHolder(StatType<Item> instance, T key) {
        return instance.itematic$get(this.item);
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

    @WrapMethod(
        method = "applyAfterUseComponentSideEffects"
    )
    private ItemStack checkForUseableBehavior(LivingEntity user, ItemStack stackBeforeUsing, Operation<ItemStack> original) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.USEABLE)) {
            return (ItemStack) (Object) this;
        }

        return original.call(user, stackBeforeUsing);
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

    @WrapMethod(
        method = "toString"
    )
    public String toStringUseHolder(Operation<String> original) {
        return this.count + " " + this.itematic$key().identifier();
    }

    @WrapMethod(
        method = "hashItemAndComponents"
    )
    private static int checkEmptyStack(@Nullable ItemStack item, Operation<Integer> original) {
        if (item != null && (item.isEmpty() || !item.getItemHolder().isBound())) {
            return 0;
        }

        return original.call(item);
    }

    @Override
    public boolean canBeEnchantedWith(Holder<Enchantment> enchantment, EnchantingContext context) {
        // Use the original implementation again
        return enchantment.value().canEnchant((ItemStack) (Object) this);
    }

    @Override
    public ResourceKey<Item> itematic$key() {
        if (this.item == null) {
            return ItemIds.AIR;
        }

        return this.item.unwrapKey().orElse(ItemIds.AIR);
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
    public ItemStack itematic$transmuteCopy(Holder<Item> item) {
        return this.itematic$transmuteCopy(item, this.count);
    }

    @Override
    public ItemStack itematic$transmuteCopy(Holder<Item> item, int count) {
        if (this.isEmpty()) {
            return EMPTY;
        }

        return this.itematic$transmuteCopyIgnoreEmpty(item, count);
    }

    @Override
    public ItemStack itematic$transmuteCopyIgnoreEmpty(Holder<Item> item, int count) {
        return new ItemStack(item, count, this.components.asPatch());
    }

    @Override
    public boolean itematic$is(ResourceKey<Item> item) {
        return this.item != null && this.item.isBound() && this.item.is(item);
    }

    @Override
    public void itematic$damage(int amount, ActionContext context) {
        if (!(context.level() instanceof ServerLevel level)) {
            return;
        }

        this.context = context;
        LivingEntity entity = context.get(LootContextParams.THIS_ENTITY, LivingEntity.class);
        this.hurtAndBreak(
            amount,
            level,
            entity instanceof ServerPlayer player ? player : null,
            item -> this.onItemBroken(item, entity, context)
        );
        this.context = null;
    }

    @Override
    public <T extends ItemBehavior<T>> boolean itematic$hasBehavior(ItemBehaviorType<T> type) {
        return this.item != null && this.item.value().itematic$hasBehavior(type);
    }

    @Override
    public <T extends ItemBehavior<T>> Optional<T> itematic$getBehavior(ItemBehaviorType<T> type) {
        if (this.item == null) {
            return Optional.empty();
        }

        return this.item.value().itematic$getBehavior(type);
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public boolean itematic$invokeEvent(ItemEvent event, ActionContext context) {
        if (this.isEmpty()) {
            return false;
        }

        if (this.activeEvents.contains(event)) {
            return false;
        }

        this.activeEvents.add(event);
        boolean result = this.item.value().itematic$invokeEvent(event, context);
        this.activeEvents.remove(event);
        return result;
    }

    @Override
    public boolean itematic$hasEventListener(ItemEvent event) {
        if (this.item == null) {
            return false;
        }

        return this.item.value().itematic$hasEventListener(event);
    }

    @Override
    public boolean itematic$mayStartUsing(Level level, Player user, InteractionHand hand, ItemStack stack) {
        if (this.item == null) {
            return false;
        }

        return this.item.value().itematic$mayStartUsing(level, user, hand, stack);
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
        this.item = entry;
        if (entry.isBound()) {
            this.components = new PatchedDataComponentMap(entry.value().components());
        } else {
            this.components = new PatchedDataComponentMap(DataComponentMap.EMPTY);
        }
    }

    @Unique
    private void setFields(Holder<Item> entry, DataComponentPatch changes) {
        this.item = entry;
        if (entry.isBound()) {
            this.components = PatchedDataComponentMap.fromPatch(entry.value().components(), changes);
        } else {
            this.components = PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, changes);
        }
    }

    @Unique
    private void onItemBroken(Item item, @Nullable LivingEntity entity, ActionContext context) {
        EquipmentSlot slot = context.get(ItematicContextKeys.EQUIPMENT_SLOT);
        if (slot != null && entity != null) {
            entity.onEquippedItemBroken(item, slot);
        }

        this.itematic$invokeEvent(ItemEvent.BREAK_ITEM, context);
    }
}
