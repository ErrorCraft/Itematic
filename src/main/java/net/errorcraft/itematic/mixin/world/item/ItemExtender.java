package net.errorcraft.itematic.mixin.world.item;

import com.google.common.annotations.VisibleForTesting;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.access.world.item.ItemAccess;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.ActionEventMap;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.ItemDisplay;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.Items;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorSet;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.BlockItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DebugStickItemBehavior;
import net.errorcraft.itematic.world.item.component.InventoryTickListener;
import net.errorcraft.itematic.world.item.use.duration.UseDuration;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(Item.class)
public abstract class ItemExtender implements ItemAccess, FabricItem {
    @Shadow
    @Final
    @Mutable
    private DataComponentMap components;

    @Unique
    private ItemDisplay display;

    @Unique
    private ItemAttributeModifiers attributeModifiers;

    @Unique
    private ItemBehaviorSet behavior;

    @Unique
    private ActionEventMap<ItemEvent> events;

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/DefaultedRegistry;holderByNameCodec()Lcom/mojang/serialization/Codec;"
        )
    )
    private static Codec<Holder<Item>> doNotUseStaticRegistry(DefaultedRegistry<Item> instance) {
        return RegistryFixedCodec.create(Registries.ITEM);
    }

    @Redirect(
        method = "method_65043",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/Holder;is(Lnet/minecraft/core/Holder;)Z"
        )
    )
    private static boolean isAirCheckId(Holder<Item> instance, Holder<Item> entry) {
        return instance.is(ItemIds.AIR);
    }

    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item$Properties;effectiveDescriptionId()Ljava/lang/String;"
        )
    )
    @Nullable
    private String effectiveDescriptionIdUseNull(Item.Properties instance) {
        return null;
    }

    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item$Properties;effectiveModel()Lnet/minecraft/resources/Identifier;"
        )
    )
    @Nullable
    private Identifier effectiveModelUseNull(Item.Properties instance) {
        return null;
    }

    @WrapMethod(
        method = "getDefaultMaxStackSize"
    )
    private int checkStackableItemBehavior(Operation<Integer> original) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.STACKABLE)) {
            return Items.UNSTACKABLE_MAX_STACK_SIZE;
        }

        return original.call();
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBehavior implementation for data-driven items.
     */
    @Overwrite
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        ItemStackExchanger stackExchanger = ItemStackExchanger.forEntity(user, stack);
        ItemResult result = ItemResult.PASS;
        for (ItemBehavior<?> behavior : this.behavior) {
            ItemResult newResult = behavior.use(level, user, hand, stack, stackExchanger);
            result = result.max(newResult);
        }

        if (level instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, user)
                .add(LootContextParams.ORIGIN, user.position())
                .add(LootContextParams.TOOL, stack)
                .add(ItematicContextParameters.HAND, hand)
                .build();
            if (this.itematic$invokeEvent(ItemEvent.USE, context)) {
                result = result.max(ItemResult.CONSUME);
            }
        }

        InteractionResult trueResult = result.toActionResult();
        if (trueResult instanceof InteractionResult.Success success) {
            return success.heldItemTransformedTo(stackExchanger.result());
        }

        return trueResult;
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBehavior implementation for data-driven items.
     */
    @Overwrite
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        ItemStackExchanger stackExchanger = context.itematic$stackExchanger();
        ItemResult result = ItemResult.PASS;
        for (ItemBehavior<?> behavior : this.behavior) {
            ItemResult newResult = behavior.useOnBlock(context, stackExchanger);
            result = result.max(newResult);
        }

        if (context.getLevel() instanceof ServerLevel serverLevel) {
            ActionContext actionContext = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .addOptional(LootContextParams.THIS_ENTITY, context.getPlayer())
                .addOptional(LootContextParams.ORIGIN, context.getPlayer(), Entity::position)
                .add(ItematicContextParameters.INTERACTED_POSITION, context.getClickedPos().getCenter())
                .add(LootContextParams.TOOL, stack)
                .add(ItematicContextParameters.HAND, context.getHand())
                .add(ItematicContextParameters.SIDE, context.getClickedFace())
                .build();
            if (this.itematic$invokeEvent(ItemEvent.USE_ON_BLOCK, actionContext)) {
                result = result.max(ItemResult.CONSUME);
            }
        }

        tryUpdateItemStack(context.getPlayer(), context.getHand(), stack, stackExchanger);
        InteractionResult trueResult = result.toActionResult();
        if (trueResult instanceof InteractionResult.Success success) {
            trueResult = success.heldItemTransformedTo(stackExchanger.result());
        }

        return trueResult;
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBehavior implementation for data-driven items.
     */
    @Overwrite
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        ItemStackExchanger stackExchanger = ItemStackExchanger.forEntity(user, stack);
        ItemResult result = ItemResult.PASS;
        for (ItemBehavior<?> behavior : this.behavior) {
            ItemResult newResult = behavior.useOnEntity(user, entity, hand, stack, stackExchanger);
            result = result.max(newResult);
        }

        if (user.level() instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, user)
                .add(LootContextParams.ORIGIN, user.position())
                .add(LootContextParams.TARGET_ENTITY, entity)
                .add(ItematicContextParameters.INTERACTED_POSITION, entity.position())
                .add(LootContextParams.TOOL, stack)
                .add(ItematicContextParameters.HAND, hand)
                .build();
            if (this.itematic$invokeEvent(ItemEvent.USE_ON_ENTITY, context)) {
                result = result.max(ItemResult.CONSUME);
            }
        }

        tryUpdateItemStack(user, hand, stack, stackExchanger);
        InteractionResult trueResult = result.toActionResult();
        if (trueResult instanceof InteractionResult.Success success) {
            trueResult = success.heldItemTransformedTo(stackExchanger.result());
        }

        return trueResult;
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBehavior implementation for data-driven items.
     */
    @Overwrite
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        ItemStackExchanger stackExchanger = ItemStackExchanger.forEntity(attacker, stack);
        for (ItemBehavior<?> behavior : this.behavior) {
            behavior.postHit(stack, target, attacker, stackExchanger);
        }

        if (attacker.level() instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, attacker)
                .add(LootContextParams.ORIGIN, attacker.position())
                .add(LootContextParams.TARGET_ENTITY, target)
                .add(ItematicContextParameters.INTERACTED_POSITION, target.position())
                .add(LootContextParams.TOOL, stack)
                .add(ItematicContextParameters.EQUIPMENT_SLOT, EquipmentSlot.MAINHAND)
                .build();
            this.itematic$invokeEvent(ItemEvent.HIT_ENTITY, context);
        }

        tryUpdateItemStack(attacker, InteractionHand.MAIN_HAND, stack, stackExchanger);
    }

    @Inject(
        method = "postHurtEnemy",
        at = @At("HEAD")
    )
    private void postDamageEntityUseItemBehavior(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfo info) {
        this.itematic$getBehavior(ItemBehaviorType.WEAPON)
            .ifPresent(weapon -> weapon.postDamageEntity(stack, target, attacker));
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBehavior implementation for data-driven items.
     */
    @Overwrite
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
        boolean result = false;
        ItemStackExchanger stackExchanger = ItemStackExchanger.forEntity(miner, stack);
        for (ItemBehavior<?> behavior : this.behavior) {
            result |= behavior.postMine(stack, level, state, pos, miner, stackExchanger);
        }

        if (level instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, miner)
                .add(LootContextParams.ORIGIN, miner.position())
                .add(ItematicContextParameters.INTERACTED_POSITION, pos.getCenter())
                .add(LootContextParams.TOOL, stack)
                .add(ItematicContextParameters.EQUIPMENT_SLOT, EquipmentSlot.MAINHAND)
                .build();
            this.itematic$invokeEvent(ItemEvent.BROKE_BLOCK, context);
        }

        tryUpdateItemStack(miner, InteractionHand.MAIN_HAND, stack, stackExchanger);
        return result;
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBehavior implementation for data-driven items.
     */
    @Overwrite
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        int usedTicks = user.itematic$usedItemTicks();
        for (ItemBehavior<?> behavior : this.behavior) {
            behavior.using(stack, level, user, usedTicks, remainingUseTicks);
        }
    }

    @Inject(
        method = "onDestroyed",
        at = @At("HEAD")
    )
    private void onItemEntityDestroyedUseItemBehavior(ItemEntity entity, CallbackInfo info) {
        this.itematic$getBehavior(ItemBehaviorType.BLOCK)
            .ifPresent(c -> c.onDestroyed(entity));
        this.itematic$getBehavior(ItemBehaviorType.ITEM_HOLDER)
            .ifPresent(c -> c.onDestroyed(entity));
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBehavior implementation for data-driven items.
     */
    @Overwrite
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity user, int remainingUseTicks) {
        boolean result = false;
        int usedTicks = user.itematic$usedItemTicks();
        ItemStackExchanger stackExchanger = ItemStackExchanger.forEntity(user, stack);
        for (ItemBehavior<?> behavior : this.behavior) {
            result |= behavior.stopUsing(stack, level, user, usedTicks, remainingUseTicks, stackExchanger);
        }

        if (level instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, user)
                .add(LootContextParams.ORIGIN, user.position())
                .add(LootContextParams.TOOL, stack)
                .add(ItematicContextParameters.HAND, user.getUsedItemHand())
                .build();
            this.itematic$invokeEvent(ItemEvent.STOPPED_USING, context);
        }

        tryUpdateItemStack(user, InteractionHand.MAIN_HAND, stack, stackExchanger);
        return result;
    }

    @WrapMethod(
        method = "finishUsingItem"
    )
    public ItemStack useItemBehavior(ItemStack stack, Level level, LivingEntity entity, Operation<ItemStack> original) {
        int usedTicks = entity.itematic$usedItemTicks();
        ItemStackExchanger stackExchanger = ItemStackExchanger.forEntity(entity, stack);
        for (ItemBehavior<?> behavior : this.behavior) {
            behavior.finishUsing(level, entity, stack, usedTicks, stackExchanger);
        }

        if (level instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, entity)
                .add(LootContextParams.ORIGIN, entity.position())
                .add(LootContextParams.TOOL, stack)
                .add(ItematicContextParameters.HAND, entity.getUsedItemHand())
                .build();
            this.itematic$invokeEvent(ItemEvent.FINISHED_USING, context);
        }

        this.itematic$getBehavior(ItemBehaviorType.CONSUMABLE)
            .ifPresent(consumable -> consumable.consume(entity, stack, stackExchanger, level, entity.getUsedItemHand()));
        return stackExchanger.result();
    }

    @Inject(
        method = "inventoryTick",
        at = @At("HEAD")
    )
    public void callInventoryTickListeners(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot, CallbackInfo info) {
        stack.getAllOfType(InventoryTickListener.class)
            .forEach(inventoryTickListener -> inventoryTickListener.itematic$onInventoryTick(level, stack, entity, slot));
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBehavior implementation for data-driven items.
     */
    @Overwrite
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickType, Player player) {
        boolean result = false;
        for (ItemBehavior<?> behavior : this.behavior) {
            result |= behavior.clickOnSlot(stack, slot, clickType, player);
        }
        return result;
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBehavior implementation for data-driven items.
     */
    @Overwrite
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction clickType, Player player, SlotAccess cursorStackReference) {
        boolean result = false;
        ItemStackExchanger stackExchanger = ItemStackExchanger.forEntity(player, otherStack);
        for (ItemBehavior<?> behavior : this.behavior) {
            result |= behavior.clickedOnWithStack(stack, otherStack, slot, clickType, player, stackExchanger);
        }

        cursorStackReference.set(stackExchanger.result());
        return result;
    }

    @ModifyReturnValue(
        method = "getAttackDamageBonus",
        at = @At("TAIL")
    )
    private float getBonusAttackDamageUseItemBehavior(float original, Entity target, float baseAttackDamage, DamageSource damageSource) {
        return this.itematic$getBehavior(ItemBehaviorType.WEAPON)
            .map(weapon -> weapon.bonusAttackDamage(target, baseAttackDamage, damageSource))
            .orElse(0.0f);
    }

    @Inject(
        method = "onCraftedPostProcess",
        at = @At("HEAD")
    )
    public void onCraft(ItemStack stack, Level level, CallbackInfo info) {
        for (ItemBehavior<?> behavior : this.behavior) {
            behavior.onCraft(stack, level);
        }
    }

    @WrapMethod(
        method = "canDestroyBlock"
    )
    private boolean useDebugStickItemBehavior(ItemStack itemStack, BlockState state, Level level, BlockPos pos, LivingEntity user, Operation<Boolean> original) {
        Optional<DebugStickItemBehavior> debugStick = this.itematic$getBehavior(ItemBehaviorType.DEBUG_STICK);
        if (debugStick.isPresent()) {
            debugStick.get().use(user, state, level, pos, itemStack);
            return false;
        }

        return original.call(itemStack, state, level, pos, user);
    }

    @Redirect(
        method = "getDestroySpeed",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/component/Tool;getMiningSpeed(Lnet/minecraft/world/level/block/state/BlockState;)F"
        )
    )
    private float getSpeedPassItemStack(Tool instance, BlockState state, ItemStack stack) {
        return instance.itematic$getSpeed(stack, state);
    }

    @Redirect(
        method = "isCorrectToolForDrops",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/component/Tool;isCorrectForDrops(Lnet/minecraft/world/level/block/state/BlockState;)Z"
        )
    )
    private boolean isCorrectForDropsPassItemStack(Tool instance, BlockState state, ItemStack stack) {
        return instance.itematic$isCorrectForDrops(stack, state);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBehavior implementation for data-driven items.
     */
    @Overwrite
    public boolean canFitInsideContainerItems() {
        return this.itematic$getBehavior(ItemBehaviorType.BLOCK)
            .map(BlockItemBehavior::canBeNested)
            .orElse(true);
    }

    @WrapMethod(
        method = "getUseAnimation"
    )
    public ItemUseAnimation getUseActionUseItemBehavior(ItemStack stack, Operation<ItemUseAnimation> original) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.USEABLE)) {
            return ItemUseAnimation.NONE;
        }

        return stack.getOrDefault(ItematicDataComponents.USE_ANIMATION, ItemUseAnimation.NONE);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBehavior implementation for data-driven items.
     */
    @Overwrite
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.USEABLE)) {
            return 0;
        }

        UseDuration useDuration = this.components.get(ItematicDataComponents.USE_DURATION);
        if (useDuration == null) {
            return 0;
        }

        return useDuration.ticks(stack, user);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemDisplay implementation for data-driven items.
     */
    @Overwrite
    @VisibleForTesting
    public final String getDescriptionId() {
        return this.display.translationKey();
    }

    @WrapMethod(
        method = "getName(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;"
    )
    public Component checkBehavior(ItemStack stack, Operation<Component> original) {
        Optional<String> potionHolderTranslationKey = this.itematic$getBehavior(ItemBehaviorType.POTION_HOLDER)
            .map(potionHolder -> potionHolder.translationKey(stack, this.display.translationKey()));
        if (potionHolderTranslationKey.isPresent()) {
            return Component.translatable(potionHolderTranslationKey.get());
        }

        Optional<String> bannerPatternHolderTranslationKey = this.itematic$getBehavior(ItemBehaviorType.BANNER_PATTERN_HOLDER)
            .flatMap(bannerPatternHolder -> bannerPatternHolder.translationKey(stack, this.display.translationKey()));
        if (bannerPatternHolderTranslationKey.isPresent()) {
            return Component.translatable(bannerPatternHolderTranslationKey.get());
        }

        return original.call(stack);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBehavior implementation for data-driven items.
     */
    @Overwrite
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return this.itematic$getBehavior(ItemBehaviorType.ITEM_HOLDER)
            .flatMap(c -> c.tooltipData(stack));
    }

    @WrapMethod(
        method = "getName(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;"
    )
    private Component checkTextHolderItemBehavior(ItemStack stack, Operation<Component> original) {
        if (!stack.itematic$hasBehavior(ItemBehaviorType.TEXT_HOLDER)) {
            return original.call(stack);
        }

        WrittenBookContent writtenBookContent = stack.get(DataComponents.WRITTEN_BOOK_CONTENT);
        if (writtenBookContent == null) {
            return original.call(stack);
        }

        String title = writtenBookContent.title().raw();
        if (StringUtil.isBlank(title)) {
            return original.call(stack);
        }

        return Component.literal(title);
    }

    @Override
    public ItemDisplay itematic$display() {
        return this.display;
    }

    @Override
    public void itematic$setDisplay(ItemDisplay display) {
        this.display = display;
    }

    @Override
    public ItemAttributeModifiers itematic$attributeModifiers() {
        return this.attributeModifiers;
    }

    @Override
    public void itematic$setAttributeModifiers(ItemAttributeModifiers attributeModifiers) {
        this.attributeModifiers = attributeModifiers;
    }

    @Override
    public ItemBehaviorSet itematic$behavior() {
        return this.behavior;
    }

    @Override
    public void itematic$setBehavior(ItemBehaviorSet components) {
        this.behavior = components;
        this.components = this.initializeComponents();
    }

    @Override
    public <T extends ItemBehavior<T>> boolean itematic$hasBehavior(ItemBehaviorType<T> type) {
        if (this.behavior == null) {
            return false;
        }

        return this.behavior.contains(type);
    }

    @Override
    public <T extends ItemBehavior<T>> Optional<T> itematic$getBehavior(ItemBehaviorType<T> type) {
        if (this.behavior == null) {
            return Optional.empty();
        }

        return this.behavior.get(type);
    }

    @Override
    public ActionEventMap<ItemEvent> itematic$events() {
        return this.events;
    }

    @Override
    public void itematic$setEvents(ActionEventMap<ItemEvent> events) {
        this.events = events;
    }

    @Override
    public boolean itematic$invokeEvent(ItemEvent event, ActionContext context) {
        return this.events.invokeEvent(event, context);
    }

    @Override
    public boolean itematic$hasEventListener(ItemEvent event) {
        return this.events.hasListener(event);
    }

    @Override
    public void itematic$addTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        this.display.tooltip().ifPresent(tooltip -> tooltip.forEach(builder));
        for (ItemBehavior<?> behavior : this.behavior) {
            behavior.appendTooltip(stack, context, builder, tooltipFlag);
        }
    }

    @Override
    public boolean itematic$mayStartUsing(Level level, Player user, InteractionHand hand, ItemStack stack) {
        return this.itematic$getBehavior(ItemBehaviorType.FOOD)
            .map(c -> c.mayStartUsing(user, stack))
            .orElse(true);
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
        return true;
    }

    @Unique
    private static void tryUpdateItemStack(@Nullable LivingEntity target, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (target == null) {
            return;
        }

        ItemStack newStack = stackExchanger.result();
        if (stack == newStack) {
            return;
        }

        target.setItemInHand(hand, newStack);
    }

    @Unique
    private DataComponentMap initializeComponents() {
        DataComponentMap.Builder componentsBuilder = DataComponentMap.builder()
            .addAll(DataComponents.COMMON_ITEM_COMPONENTS);
        this.display.addComponents(componentsBuilder);
        for (ItemBehavior<?> behavior : this.behavior) {
            behavior.addComponents(componentsBuilder);
        }

        if (!this.attributeModifiers.modifiers().isEmpty()) {
            componentsBuilder.set(DataComponents.ATTRIBUTE_MODIFIERS, this.attributeModifiers);
        }

        return componentsBuilder.build();
    }

    @Mixin(Item.Properties.class)
    public static class PropertiesExtender {
        @Redirect(
            method = "usingConvertsTo",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
            )
        )
        private ItemStack newItemStackUseEmptyItemStack(ItemLike item) {
            return ItemStack.EMPTY;
        }

        @Redirect(
            method = "buildAndValidateComponents",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/core/component/DataComponentMap$Builder;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Lnet/minecraft/core/component/DataComponentMap$Builder;"
            )
        )
        private <T> DataComponentMap.Builder doNotAddDataComponents(DataComponentMap.Builder instance, DataComponentType<T> type, @Nullable T value) {
            return instance;
        }
    }
}
