package net.errorcraft.itematic.mixin.world.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.errorcraft.itematic.access.world.item.ItemInstanceAccess;
import net.errorcraft.itematic.access.world.item.ItemStackAccess;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.ItematicUtil;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.ShooterItemBehavior;
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethod;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.TypedInstance;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.KineticWeapon;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.apache.commons.lang3.math.Fraction;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(ItemStack.class)
public abstract class ItemStackExtender implements DataComponentHolder, TypedInstance<Item>, ItemStackAccess, ItemInstanceAccess, FabricItemStack {
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
    private @Nullable Holder<Item> item;

    @Shadow
    @Final
    @Mutable
    private PatchedDataComponentMap components;

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract void hurtAndBreak(int amount, ServerLevel level, @Nullable ServerPlayer player, Consumer<Item> onBreak);

    @Shadow
    public abstract int getDamageValue();

    @Shadow
    public abstract void shrink(int amount);

    @Shadow
    public abstract int getCount();

    @Unique
    private static final Component FAILED_TO_LOAD_NAME = Component.translatable("item.failed_to_load")
        .withStyle(ChatFormatting.RED);

    @Unique
    private static final Component FAILED_TO_LOAD_DESCRIPTION_RETAINED_INFORMATION = Component.translatable("item.failed_to_load.retained_information")
        .withStyle(ChatFormatting.GRAY);

    @Unique
    private static final Component FAILED_TO_LOAD_DESCRIPTION_UPDATE_DATA_PACKS = Component.translatable("item.failed_to_load.update_data_packs")
        .withStyle(ChatFormatting.GRAY);

    @Unique
    private static final ScopedValue<ActionContext> ACTION_CONTEXT = ScopedValue.newInstance();

    @Unique
    @Nullable
    private ResourceKey<Item> failedKey;

    @WrapOperation(
        method = {
            "<clinit>",
            "lenientOptionalFieldOf"
        },
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/ItemStack;CODEC:Lcom/mojang/serialization/Codec;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private static Codec<ItemStack> useFailableItemStackCodec(Operation<Codec<ItemStack>> original) {
        return ItemStacks.POSSIBLY_FAILED_CODEC;
    }

    @ModifyArg(
        method = {
            "<init>(Lnet/minecraft/world/level/ItemLike;)V",
            "<init>(Lnet/minecraft/world/level/ItemLike;I)V"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/core/Holder;I)V"
        )
    )
    @Nullable
    private static Holder<Item> useNullForDirectItemsAndLogWarning(Holder<Item> item) {
        LOGGER.warn(ItematicUtil.stackTraceMessage("Tried to create an item stack from an item or item-like value directly. This is no longer supported and should be modified to use a holder instead."));
        return null;
    }

    @WrapOperation(
        method = {
            "<init>(Lnet/minecraft/core/Holder;I)V",
            "<init>(Lnet/minecraft/core/Holder;ILnet/minecraft/core/component/DataComponentPatch;)V"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/Holder;components()Lnet/minecraft/core/component/DataComponentMap;"
        )
    )
    @Nullable
    private static DataComponentMap doNotGetComponentsToPreventNullPointerException(Holder<Item> instance, Operation<DataComponentMap> original) {
        return null;
    }

    @ModifyArg(
        method = "<init>(Lnet/minecraft/core/Holder;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/component/PatchedDataComponentMap;<init>(Lnet/minecraft/core/component/DataComponentMap;)V"
        )
    )
    private static DataComponentMap checkNullAndUnboundHolderForConstructor(DataComponentMap prototype, @Local(name = "item", argsOnly = true) @Nullable Holder<Item> item) {
        if (item == null || !item.isBound()) {
            return DataComponentMap.EMPTY;
        }

        return item.value().components();
    }

    @ModifyArg(
        method = "<init>(Lnet/minecraft/core/Holder;ILnet/minecraft/core/component/DataComponentPatch;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/component/PatchedDataComponentMap;fromPatch(Lnet/minecraft/core/component/DataComponentMap;Lnet/minecraft/core/component/DataComponentPatch;)Lnet/minecraft/core/component/PatchedDataComponentMap;"
        )
    )
    private static DataComponentMap checkNullAndUnboundHolderForFromPatch(DataComponentMap prototype, @Local(name = "item", argsOnly = true) @Nullable Holder<Item> item) {
        if (item == null || !item.isBound()) {
            return DataComponentMap.EMPTY;
        }

        return item.value().components();
    }

    @ModifyExpressionValue(
        method = "validateComponents",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/component/DataComponentMap;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/core/component/DataComponents;BUNDLE_CONTENTS:Lnet/minecraft/core/component/DataComponentType;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    @Nullable
    private static Object alsoGetItemHolderRulesDataComponent(Object original, DataComponentMap components, @Share("itemHolderRules") LocalRef<ItemHolderRules> itemHolderRulesReference) {
        ItemHolderRules itemHolderRules = components.get(ItematicDataComponents.ITEM_HOLDER_RULES);
        if (itemHolderRules == null) {
            return null;
        }

        itemHolderRulesReference.set(itemHolderRules);
        return original;
    }

    @WrapOperation(
        method = "validateComponents",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/component/BundleContents;weight()Lcom/mojang/serialization/DataResult;"
        )
    )
    private static DataResult<Fraction> useItemHolderRulesDataComponent(BundleContents instance, Operation<DataResult<Fraction>> original, @Share("itemHolderRules") LocalRef<ItemHolderRules> itemHolderRulesReference) {
        return instance.itematic$occupancy(itemHolderRulesReference.get());
    }

    @WrapMethod(
        method = "use"
    )
    private InteractionResult checkInteractableStackForUse(Level level, Player player, InteractionHand hand, Operation<InteractionResult> original) {
        if (this.itematic$cannotBeInteractedWith()) {
            return InteractionResult.PASS;
        }

        return original.call(level, player, hand);
    }

    @WrapMethod(
        method = "useOn"
    )
    private InteractionResult checkInteractableStackForUseOn(UseOnContext context, Operation<InteractionResult> original) {
        if (this.itematic$cannotBeInteractedWith()) {
            return InteractionResult.PASS;
        }

        return original.call(context);
    }

    @WrapMethod(
        method = "hurtEnemy"
    )
    private boolean checkInteractableStackForHurtEnemy(LivingEntity mob, LivingEntity attacker, Operation<Boolean> original) {
        if (this.itematic$cannotBeInteractedWith()) {
            return false;
        }

        return original.call(mob, attacker);
    }

    @WrapMethod(
        method = "interactLivingEntity"
    )
    private InteractionResult checkInteractableStackForInteractLivingEntity(Player player, LivingEntity target, InteractionHand hand, Operation<InteractionResult> original) {
        if (this.itematic$cannotBeInteractedWith()) {
            return InteractionResult.PASS;
        }

        return original.call(player, target, hand);
    }

    @ModifyReturnValue(
        method = "copy",
        at = @At("TAIL")
    )
    private ItemStack setFailedToLoad(ItemStack original) {
        if (this.failedKey != null) {
            original.itematic$setFailedKey(this.failedKey);
        }

        return original;
    }

    @WrapMethod(
        method = "canDestroyBlock"
    )
    private boolean checkSuccessfullyLoaded(BlockState state, Level level, BlockPos pos, Player player, Operation<Boolean> original) {
        if (!this.itematic$isSuccessfullyLoaded()) {
            return false;
        }

        return original.call(state, level, pos, player);
    }

    @WrapMethod(
        method = "onUseTick"
    )
    private void checkInteractableStackForOnUseTick(Level level, LivingEntity livingEntity, int ticksRemaining, Operation<Void> original) {
        if (this.itematic$cannotBeInteractedWith()) {
            return;
        }

        original.call(level, livingEntity, ticksRemaining);
    }

    @WrapMethod(
        method = "releaseUsing"
    )
    private void checkInteractableStackForReleaseUsing(Level level, LivingEntity entity, int remainingTime, Operation<Void> original) {
        if (this.itematic$cannotBeInteractedWith()) {
            return;
        }

        original.call(level, entity, remainingTime);
    }

    @WrapMethod(
        method = "mineBlock"
    )
    private void checkInteractableStackForMineBlock(Level level, BlockState state, BlockPos pos, Player owner, Operation<Void> original) {
        if (this.itematic$cannotBeInteractedWith()) {
            return;
        }

        original.call(level, state, pos, owner);
    }

    @WrapOperation(
        method = "inventoryTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;inventoryTick(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/EquipmentSlot;)V"
        )
    )
    private void checkFailed(Item instance, ItemStack itemStack, ServerLevel level, Entity owner, EquipmentSlot slot, Operation<Void> original) {
        if (!this.itematic$isSuccessfullyLoaded()) {
            return;
        }

        original.call(instance, itemStack, level, owner, slot);
    }

    @WrapMethod(
        method = "onCraftedBy"
    )
    private void checkInteractableStackForOnCraftedBy(Player player, int craftCount, Operation<Void> original) {
        if (this.itematic$cannotBeInteractedWith()) {
            return;
        }

        original.call(player, craftCount);
    }

    @WrapOperation(
        method = "typeHolder",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;builtInRegistryHolder()Lnet/minecraft/core/Holder$Reference;"
        )
    )
    private Holder.@Nullable Reference<Item> useNull(Item instance, Operation<Holder.Reference<Item>> original) {
        return null;
    }

    @WrapMethod(
        method = "getItem"
    )
    private Item checkInteractableStackForGetItem(Operation<Item> original) {
        if (this.itematic$cannotBeInteractedWith()) {
            return Items.AIR;
        }

        return original.call();
    }

    @WrapOperation(
        method = "isEmpty",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/Holder;value()Ljava/lang/Object;"
        )
    )
    @Nullable
    private <T> T checkNull(@Nullable Holder<T> instance, Operation<T> original) {
        if (instance == null) {
            return null;
        }

        return original.call(instance);
    }

    @ModifyReturnValue(
        method = "isEmpty",
        at = @At("TAIL")
    )
    private boolean checkNullForEmptyStack(boolean original) {
        return original || (this.failedKey == null && (this.item == null || this.item.is(ItemIds.AIR)));
    }

    @ModifyReturnValue(
        method = "isItemEnabled",
        at = @At("RETURN")
    )
    private boolean checkSuccessfullyLoaded(boolean original) {
        return original && this.itematic$isSuccessfullyLoaded();
    }

    @WrapMethod(
        method = "is(Ljava/util/function/Predicate;)Z"
    )
    public boolean checkInteractableStackForIsPredicate(Predicate<Holder<Item>> item, Operation<Boolean> original) {
        if (this.itematic$cannotBeInteractedWith()) {
            return false;
        }

        return original.call(item);
    }

    @WrapOperation(
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
    private Object getKineticWeaponReturnNull(ItemStack instance, DataComponentType<KineticWeapon> type, Operation<Object> original) {
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
    private static boolean checkInteractableStacksPrematurely(ItemStack a, ItemStack b, Operation<Boolean> original) {
        if (a.itematic$cannotBeInteractedWith() && b.itematic$cannotBeInteractedWith()) {
            return true;
        }

        return original.call(a, b);
    }

    @WrapOperation(
        method = {
            "isSameItem",
            "isSameItemSameComponents"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isItemCheckHolder(ItemStack instance, Object o, Operation<Boolean> original, @Local(name = "b", argsOnly = true) ItemStack b) {
        return instance.is(b.typeHolder());
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
        method = {
            "getHoverName",
            "getDisplayName"
        }
    )
    private Component checkSuccessfullyLoaded(Operation<Component> original) {
        if (!this.itematic$isSuccessfullyLoaded()) {
            return FAILED_TO_LOAD_NAME;
        }

        return original.call();
    }

    @WrapMethod(
        method = "getItemName"
    )
    private Component checkInteractableStackForGetItemName(Operation<Component> original) {
        if (this.itematic$cannotBeInteractedWith()) {
            return Component.empty();
        }

        return original.call();
    }

    @WrapMethod(
        method = "addDetailsToTooltip"
    )
    private void checkSuccessfullyLoaded(Item.TooltipContext context, TooltipDisplay display, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> builder, Operation<Void> original) {
        if (this.itematic$isSuccessfullyLoaded()) {
            original.call(context, display, player, tooltipFlag, builder);
            return;
        }

        builder.accept(
            Component.translatable(
                "item.failed_to_load.could_not_find_item",
                Component.translationArg(this.itematic$key().identifier())
            ).withStyle(ChatFormatting.GRAY)
        );
        builder.accept(FAILED_TO_LOAD_DESCRIPTION_RETAINED_INFORMATION);
        builder.accept(FAILED_TO_LOAD_DESCRIPTION_UPDATE_DATA_PACKS);
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
            this.item.value().itematic$addTooltip(
                (ItemStack)(Object) this,
                context,
                builder,
                tooltipFlag
            );
        }
    }

    @WrapOperation(
        method = "addDetailsToTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/DefaultedRegistry;getKey(Ljava/lang/Object;)Lnet/minecraft/resources/Identifier;"
        )
    )
    private <T> Identifier getIdHolder(DefaultedRegistry<Item> instance, T t, Operation<Identifier> original) {
        return this.itematic$key().identifier();
    }

    @WrapMethod(
        method = "overrideStackedOnOther"
    )
    private boolean checkInteractableStackForOverrideStackedOnOther(Slot slot, ClickAction clickAction, Player player, Operation<Boolean> original) {
        if (this.itematic$cannotBeInteractedWith() || slot.getItem().itematic$cannotBeInteractedWith()) {
            return false;
        }

        return original.call(slot, clickAction, player);
    }

    @WrapMethod(
        method = "overrideOtherStackedOnMe"
    )
    private boolean checkInteractableStackForOverrideOtherStackedOnMe(ItemStack other, Slot slot, ClickAction clickAction, Player player, SlotAccess carriedItem, Operation<Boolean> original) {
        if (this.itematic$cannotBeInteractedWith() || other.itematic$cannotBeInteractedWith()) {
            return false;
        }

        return original.call(other, slot, clickAction, player, carriedItem);
    }

    @WrapOperation(
        method = "postHurtEnemy",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
        )
    )
    @Nullable
    private Object getWeaponDataComponentUseNull(ItemStack instance, DataComponentType<Weapon> type, Operation<Object> original) {
        return null;
    }

    @ModifyReturnValue(
        method = "processDurabilityChange",
        at = @At("RETURN")
    )
    private int limitDamageApplied(int original) {
        return this.itematic$getBehavior(ItemBehaviorType.DAMAGEABLE)
            .map(damageable -> Math.min(damageable.maximumDamage((ItemStack)(Object) this) - this.getDamageValue(), original))
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
    private void invokeDamageItemEvent(int newDamage, @Nullable ServerPlayer player, Consumer<Item> onBreak, CallbackInfo info) {
        if (!ACTION_CONTEXT.isBound()) {
            return;
        }

        this.itematic$invokeEvent(ItemEvent.DAMAGE_ITEM, ACTION_CONTEXT.get());
    }

    @Inject(
        method = "applyDamage",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"
        )
    )
    private void invokeBreakItemEvent(int newDamage, @Nullable ServerPlayer player, Consumer<Item> onBreak, CallbackInfo info) {
        if (!ACTION_CONTEXT.isBound()) {
            return;
        }

        this.itematic$invokeEvent(ItemEvent.BREAK_ITEM, ACTION_CONTEXT.get());
    }

    @WrapOperation(
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
    private <T> Stat<Item> getStatUseHolder(StatType<Item> instance, T argument, Operation<Stat<T>> original) {
        return instance.itematic$get(this.item);
    }

    @WrapOperation(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/InteractionResult$Success;heldItemTransformedTo(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/InteractionResult$Success;"
        )
    )
    private InteractionResult.Success doNotModifyResultingItemStackIfNotUseable(InteractionResult.Success instance, ItemStack itemStack, Operation<InteractionResult.Success> original) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.USEABLE)) {
            return instance;
        }

        return original.call(instance, itemStack);
    }

    @WrapMethod(
        method = "applyAfterUseComponentSideEffects"
    )
    private ItemStack checkForUseableBehavior(LivingEntity user, ItemStack stackBeforeUsing, Operation<ItemStack> original) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.USEABLE)) {
            return (ItemStack)(Object) this;
        }

        return original.call(user, stackBeforeUsing);
    }

    @WrapOperation(
        method = "lambda$getDamageSource$1",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;getItemDamageSource(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/damagesource/DamageSource;"
        )
    )
    @Nullable
    @SuppressWarnings("ConstantValue")
    private DamageSource getDamageSourceUseItemBehavior(Item instance, LivingEntity attacker, Operation<DamageSource> original) {
        return this.itematic$getBehavior(ItemBehaviorType.WEAPON)
            .map(weapon -> weapon.damageSource((ItemStack)(Object) this, attacker))
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
    private static int checkInteractableStackForHashItemAndComponents(@Nullable ItemStack item, Operation<Integer> original) {
        if (item != null && (item.itematic$cannotBeInteractedWith() || !item.typeHolder().isBound())) {
            return 0;
        }

        return original.call(item);
    }

    @Override
    public boolean canBeEnchantedWith(Holder<Enchantment> enchantment, EnchantingContext context) {
        // Use the original implementation again
        return enchantment.value().canEnchant((ItemStack)(Object) this);
    }

    @Override
    public boolean itematic$isSuccessfullyLoaded() {
        return this.failedKey == null;
    }

    @Override
    public boolean itematic$cannotBeInteractedWith() {
        return !this.itematic$isSuccessfullyLoaded() || this.isEmpty();
    }

    @Override
    public void itematic$setFailedKey(ResourceKey<Item> failedKey) {
        this.failedKey = failedKey;
    }

    @Override
    public ResourceKey<Item> itematic$key() {
        if (this.failedKey != null) {
            return this.failedKey;
        }

        if (this.item == null) {
            return ItemIds.AIR;
        }

        return this.item.unwrapKey().orElse(ItemIds.AIR);
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

        return this.transmuteCopyIgnoreEmpty(item, count);
    }

    @Override
    public void itematic$damage(int amount, ActionContext context) {
        if (!(context.level() instanceof ServerLevel level)) {
            return;
        }

        ScopedValue.where(ACTION_CONTEXT, context)
            .run(() -> {
                LivingEntity entity = context.get(LootContextParams.THIS_ENTITY, LivingEntity.class);
                this.hurtAndBreak(
                    amount,
                    level,
                    entity instanceof ServerPlayer player ? player : null,
                    item -> this.onItemBroken(item, entity, context)
                );
            });
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
        if (this.itematic$cannotBeInteractedWith()) {
            return false;
        }

        try {
            return this.item.value().itematic$invokeEvent(event, context);
        } catch (StackOverflowError e) {
            return false;
        }
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
    private ItemStack transmuteCopyIgnoreEmpty(Holder<Item> item, int count) {
        return new ItemStack(item, count, this.components.asPatch());
    }

    @Unique
    private void onItemBroken(Item item, @Nullable LivingEntity entity, ActionContext context) {
        EquipmentSlot slot = context.get(ItematicContextKeys.EQUIPMENT_SLOT);
        if (slot != null && entity != null) {
            entity.onEquippedItemBroken(item, slot);
        }

        this.itematic$invokeEvent(ItemEvent.BREAK_ITEM, context);
    }

    @Mixin(targets = "net/minecraft/world/item/ItemStack$1")
    public static class StreamCodecExtender {
        @WrapMethod(
            method = "decode(Lnet/minecraft/network/RegistryFriendlyByteBuf;)Lnet/minecraft/world/item/ItemStack;"
        )
        @SuppressWarnings("DataFlowIssue")
        private ItemStack checkForFailed(RegistryFriendlyByteBuf input, Operation<ItemStack> original) {
            if (input.readBoolean()) {
                return original.call(input);
            }

            ResourceKey<Item> item = input.readResourceKey(Registries.ITEM);
            ItemStack stack = new ItemStack(null, 1, DataComponentPatch.EMPTY);
            stack.itematic$setFailedKey(item);
            return stack;
        }

        @WrapMethod(
            method = "encode(Lnet/minecraft/network/RegistryFriendlyByteBuf;Lnet/minecraft/world/item/ItemStack;)V"
        )
        private void checkForFailed(RegistryFriendlyByteBuf output, ItemStack itemStack, Operation<Void> original) {
            if (itemStack.itematic$isSuccessfullyLoaded()) {
                output.writeBoolean(true);
                original.call(output, itemStack);
                return;
            }

            output.writeBoolean(false);
            output.writeResourceKey(itemStack.itematic$key());
        }
    }

    @Mixin(targets = "net/minecraft/world/item/ItemStack$3")
    public static class ValidatedStreamCodecExtender {
        @WrapOperation(
            method = "decode(Lnet/minecraft/network/RegistryFriendlyByteBuf;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/ItemStack;CODEC:Lcom/mojang/serialization/Codec;",
                opcode = Opcodes.GETSTATIC
            )
        )
        private Codec<ItemStack> useFailableItemStackCodec(Operation<Codec<ItemStack>> original) {
            return ItemStacks.POSSIBLY_FAILED_CODEC;
        }
    }
}
