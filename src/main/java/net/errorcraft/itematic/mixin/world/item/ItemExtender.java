package net.errorcraft.itematic.mixin.world.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.access.world.item.ItemAccess;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
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
import net.minecraft.core.component.DataComponentInitializers;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(Item.class)
public abstract class ItemExtender implements ItemAccess, FabricItem {
    @Unique
    private ItemDisplay display;

    @Unique
    private ItemAttributeModifiers attributeModifiers;

    @Unique
    private ItemBehaviorSet behavior;

    @Unique
    private DataComponentMap dataComponents;

    @Unique
    private ActionEventMap<ItemEvent> events;

    @WrapOperation(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/DefaultedRegistry;holderByNameCodec()Lcom/mojang/serialization/Codec;"
        )
    )
    private static Codec<Holder<Item>> doNotUseStaticRegistry(DefaultedRegistry<Item> instance, Operation<Codec<Holder<Item>>> original) {
        return RegistryFixedCodec.create(Registries.ITEM);
    }

    @WrapOperation(
        method = "lambda$static$0",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/Holder;is(Lnet/minecraft/core/Holder;)Z"
        )
    )
    private static boolean isAirCheckId(Holder<Item> instance, Holder<Item> holder, Operation<Boolean> original) {
        return instance.is(ItemIds.AIR);
    }

    @WrapOperation(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item$Properties;itemIdOrThrow()Lnet/minecraft/resources/ResourceKey;"
        )
    )
    @Nullable
    private ResourceKey<Item> doNotGetItemIdToPreventNullPointerException(Item.Properties instance, Operation<ResourceKey<Item>> original) {
        return null;
    }

    @WrapOperation(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/component/DataComponentInitializers;add(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/core/component/DataComponentInitializers$Initializer;)V"
        )
    )
    private void doNotAddDataComponentInitializer(DataComponentInitializers instance, ResourceKey<Item> key, DataComponentInitializers.Initializer<Item> initializer, Operation<Void> original) {}

    @WrapMethod(
        method = "components"
    )
    private DataComponentMap useFieldInsteadOfIntrusiveHolder(Operation<DataComponentMap> original) {
        return this.dataComponents;
    }

    @WrapMethod(
        method = "getDefaultMaxStackSize"
    )
    private int alsoCheckStackableItemBehavior(Operation<Integer> original) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.STACKABLE)) {
            return Items.UNSTACKABLE_MAX_STACK_SIZE;
        }

        return original.call();
    }

    @WrapMethod(
        method = "use"
    )
    public InteractionResult useItemBehavior(Level level, Player player, InteractionHand hand, Operation<InteractionResult> original) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStackExchanger stackExchanger = ItemStackExchanger.forEntity(player, stack);
        ItemResult result = ItemResult.PASS;
        for (ItemBehavior<?> behavior : this.behavior) {
            ItemResult newResult = behavior.use(level, player, hand, stack, stackExchanger);
            result = result.max(newResult);
        }

        if (level instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, player)
                .add(LootContextParams.ORIGIN, player.position())
                .add(LootContextParams.TOOL, stack)
                .add(ItematicContextKeys.HAND, hand)
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

    @WrapMethod(
        method = "useOn"
    )
    public InteractionResult useItemBehavior(UseOnContext context, Operation<InteractionResult> original) {
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
                .add(ItematicContextKeys.INTERACTED_POSITION, context.getClickedPos().getCenter())
                .add(LootContextParams.TOOL, stack)
                .add(ItematicContextKeys.HAND, context.getHand())
                .add(ItematicContextKeys.SIDE, context.getClickedFace())
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

    @WrapMethod(
        method = "interactLivingEntity"
    )
    public InteractionResult useItemBehavior(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type, Operation<InteractionResult> original) {
        ItemStackExchanger stackExchanger = ItemStackExchanger.forEntity(player, itemStack);
        ItemResult result = ItemResult.PASS;
        for (ItemBehavior<?> behavior : this.behavior) {
            ItemResult newResult = behavior.useOnEntity(player, target, type, itemStack, stackExchanger);
            result = result.max(newResult);
        }

        if (player.level() instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, player)
                .add(LootContextParams.ORIGIN, player.position())
                .add(LootContextParams.TARGET_ENTITY, target)
                .add(ItematicContextKeys.INTERACTED_POSITION, target.position())
                .add(LootContextParams.TOOL, itemStack)
                .add(ItematicContextKeys.HAND, type)
                .build();
            if (this.itematic$invokeEvent(ItemEvent.USE_ON_ENTITY, context)) {
                result = result.max(ItemResult.CONSUME);
            }
        }

        tryUpdateItemStack(player, type, itemStack, stackExchanger);
        InteractionResult trueResult = result.toActionResult();
        if (trueResult instanceof InteractionResult.Success success) {
            trueResult = success.heldItemTransformedTo(stackExchanger.result());
        }

        return trueResult;
    }

    @WrapMethod(
        method = "hurtEnemy"
    )
    public void useItemBehavior(ItemStack itemStack, LivingEntity mob, LivingEntity attacker, Operation<Void> original) {
        ItemStackExchanger stackExchanger = ItemStackExchanger.forEntity(attacker, itemStack);
        for (ItemBehavior<?> behavior : this.behavior) {
            behavior.postHit(itemStack, mob, attacker, stackExchanger);
        }

        if (attacker.level() instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, attacker)
                .add(LootContextParams.ORIGIN, attacker.position())
                .add(LootContextParams.TARGET_ENTITY, mob)
                .add(ItematicContextKeys.INTERACTED_POSITION, mob.position())
                .add(LootContextParams.TOOL, itemStack)
                .add(ItematicContextKeys.EQUIPMENT_SLOT, EquipmentSlot.MAINHAND)
                .build();
            this.itematic$invokeEvent(ItemEvent.HIT_ENTITY, context);
        }

        tryUpdateItemStack(attacker, InteractionHand.MAIN_HAND, itemStack, stackExchanger);
    }

    @Inject(
        method = "postHurtEnemy",
        at = @At("HEAD")
    )
    private void postDamageEntityUseItemBehavior(ItemStack itemStack, LivingEntity mob, LivingEntity attacker, CallbackInfo info) {
        this.itematic$getBehavior(ItemBehaviorType.WEAPON)
            .ifPresent(weapon -> weapon.postDamageEntity(itemStack, mob, attacker));
    }

    @WrapMethod(
        method = "mineBlock"
    )
    public boolean useItemBehavior(ItemStack itemStack, Level level, BlockState state, BlockPos pos, LivingEntity owner, Operation<Boolean> original) {
        boolean result = false;
        ItemStackExchanger stackExchanger = ItemStackExchanger.forEntity(owner, itemStack);
        for (ItemBehavior<?> behavior : this.behavior) {
            result |= behavior.postMine(itemStack, level, state, pos, owner, stackExchanger);
        }

        if (level instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, owner)
                .add(LootContextParams.ORIGIN, owner.position())
                .add(ItematicContextKeys.INTERACTED_POSITION, pos.getCenter())
                .add(LootContextParams.TOOL, itemStack)
                .add(ItematicContextKeys.EQUIPMENT_SLOT, EquipmentSlot.MAINHAND)
                .build();
            this.itematic$invokeEvent(ItemEvent.BROKE_BLOCK, context);
        }

        tryUpdateItemStack(owner, InteractionHand.MAIN_HAND, itemStack, stackExchanger);
        return result;
    }

    @WrapMethod(
        method = "onUseTick"
    )
    public void useItemBehavior(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining, Operation<Void> original) {
        int usedTicks = livingEntity.itematic$usedItemTicks();
        for (ItemBehavior<?> behavior : this.behavior) {
            behavior.using(itemStack, level, livingEntity, usedTicks, ticksRemaining);
        }
    }

    @Inject(
        method = "onDestroyed",
        at = @At("HEAD")
    )
    private void onItemEntityDestroyedUseItemBehavior(ItemEntity itemEntity, CallbackInfo info) {
        this.itematic$getBehavior(ItemBehaviorType.BLOCK)
            .ifPresent(block -> block.onDestroyed(itemEntity));
        this.itematic$getBehavior(ItemBehaviorType.ITEM_HOLDER)
            .ifPresent(itemHolder -> itemHolder.onDestroyed(itemEntity));
    }

    @WrapMethod(
        method = "releaseUsing"
    )
    public boolean useItemBehavior(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime, Operation<Boolean> original) {
        boolean result = false;
        int usedTicks = entity.itematic$usedItemTicks();
        ItemStackExchanger stackExchanger = ItemStackExchanger.forEntity(entity, itemStack);
        for (ItemBehavior<?> behavior : this.behavior) {
            result |= behavior.stopUsing(itemStack, level, entity, usedTicks, remainingTime, stackExchanger);
        }

        if (level instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, entity)
                .add(LootContextParams.ORIGIN, entity.position())
                .add(LootContextParams.TOOL, itemStack)
                .add(ItematicContextKeys.HAND, entity.getUsedItemHand())
                .build();
            this.itematic$invokeEvent(ItemEvent.STOPPED_USING, context);
        }

        tryUpdateItemStack(entity, InteractionHand.MAIN_HAND, itemStack, stackExchanger);
        return result;
    }

    @WrapMethod(
        method = "finishUsingItem"
    )
    public ItemStack useItemBehavior(ItemStack itemStack, Level level, LivingEntity entity, Operation<ItemStack> original) {
        int usedTicks = entity.itematic$usedItemTicks();
        ItemStackExchanger stackExchanger = ItemStackExchanger.forEntity(entity, itemStack);
        for (ItemBehavior<?> behavior : this.behavior) {
            behavior.finishUsing(level, entity, itemStack, usedTicks, stackExchanger);
        }

        if (level instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, entity)
                .add(LootContextParams.ORIGIN, entity.position())
                .add(LootContextParams.TOOL, itemStack)
                .add(ItematicContextKeys.HAND, entity.getUsedItemHand())
                .build();
            this.itematic$invokeEvent(ItemEvent.FINISHED_USING, context);
        }

        this.itematic$getBehavior(ItemBehaviorType.CONSUMABLE)
            .ifPresent(consumable -> consumable.consume(entity, itemStack, stackExchanger, level, entity.getUsedItemHand()));
        return stackExchanger.result();
    }

    @Inject(
        method = "inventoryTick",
        at = @At("HEAD")
    )
    public void callInventoryTickListeners(ItemStack itemStack, ServerLevel level, Entity owner, EquipmentSlot slot, CallbackInfo info) {
        itemStack.getAllOfType(InventoryTickListener.class)
            .forEach(inventoryTickListener -> inventoryTickListener.itematic$onInventoryTick(level, itemStack, owner, slot));
    }

    @WrapMethod(
        method = "overrideStackedOnOther"
    )
    public boolean useItemBehavior(ItemStack self, Slot slot, ClickAction clickAction, Player player, Operation<Boolean> original) {
        boolean result = false;
        for (ItemBehavior<?> behavior : this.behavior) {
            result |= behavior.clickOnSlot(self, slot, clickAction, player);
        }
        return result;
    }

    @WrapMethod(
        method = "overrideOtherStackedOnMe"
    )
    public boolean useItemBehavior(ItemStack self, ItemStack other, Slot slot, ClickAction clickAction, Player player, SlotAccess carriedItem, Operation<Boolean> original) {
        boolean result = false;
        ItemStackExchanger stackExchanger = ItemStackExchanger.forEntity(player, other);
        for (ItemBehavior<?> behavior : this.behavior) {
            result |= behavior.clickedOnWithStack(self, other, slot, clickAction, player, stackExchanger);
        }

        carriedItem.set(stackExchanger.result());
        return result;
    }

    @ModifyReturnValue(
        method = "getAttackDamageBonus",
        at = @At("TAIL")
    )
    private float getBonusAttackDamageUseItemBehavior(float original, Entity victim, float damage, DamageSource damageSource) {
        return this.itematic$getBehavior(ItemBehaviorType.WEAPON)
            .map(weapon -> weapon.bonusAttackDamage(victim, damage, damageSource))
            .orElse(0.0f);
    }

    @Inject(
        method = "onCraftedPostProcess",
        at = @At("HEAD")
    )
    public void onCraft(ItemStack itemStack, Level level, CallbackInfo info) {
        for (ItemBehavior<?> behavior : this.behavior) {
            behavior.onCraft(itemStack, level);
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

    @WrapOperation(
        method = "getDestroySpeed",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/component/Tool;getMiningSpeed(Lnet/minecraft/world/level/block/state/BlockState;)F"
        )
    )
    private float getMiningSpeedPassItemStack(Tool instance, BlockState state, Operation<Float> original, ItemStack itemStack) {
        return instance.itematic$getMiningSpeed(itemStack, state);
    }

    @WrapOperation(
        method = "isCorrectToolForDrops",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/component/Tool;isCorrectForDrops(Lnet/minecraft/world/level/block/state/BlockState;)Z"
        )
    )
    private boolean isCorrectForDropsPassItemStack(Tool instance, BlockState state, Operation<Boolean> original, ItemStack itemStack) {
        return instance.itematic$isCorrectForDrops(itemStack, state);
    }

    @WrapMethod(
        method = "canFitInsideContainerItems"
    )
    public boolean useItemBehavior(Operation<Boolean> original) {
        return this.itematic$getBehavior(ItemBehaviorType.BLOCK)
            .map(BlockItemBehavior::canBeNested)
            .orElse(true);
    }

    @WrapMethod(
        method = "getUseAnimation"
    )
    public ItemUseAnimation getUseActionUseItemBehavior(ItemStack itemStack, Operation<ItemUseAnimation> original) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.USEABLE)) {
            return ItemUseAnimation.NONE;
        }

        return itemStack.getOrDefault(ItematicDataComponents.USE_ANIMATION, ItemUseAnimation.NONE);
    }

    @WrapMethod(
        method = "getUseDuration"
    )
    public int useItemBehavior(ItemStack itemStack, LivingEntity user, Operation<Integer> original) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.USEABLE)) {
            return 0;
        }

        UseDuration useDuration = itemStack.get(ItematicDataComponents.USE_DURATION);
        if (useDuration == null) {
            return 0;
        }

        return useDuration.ticks(itemStack, user);
    }

    @WrapMethod(
        method = "getDescriptionId"
    )
    public final String useItemDisplay(Operation<String> original) {
        return this.display.translationKey();
    }

    @WrapMethod(
        method = "getName(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;"
    )
    public Component checkBehavior(ItemStack itemStack, Operation<Component> original) {
        Optional<String> potionHolderTranslationKey = this.itematic$getBehavior(ItemBehaviorType.POTION_HOLDER)
            .map(potionHolder -> potionHolder.translationKey(itemStack, this.display.translationKey()));
        if (potionHolderTranslationKey.isPresent()) {
            return Component.translatable(potionHolderTranslationKey.get());
        }

        Optional<String> bannerPatternHolderTranslationKey = this.itematic$getBehavior(ItemBehaviorType.BANNER_PATTERN_HOLDER)
            .flatMap(bannerPatternHolder -> bannerPatternHolder.translationKey(itemStack, this.display.translationKey()));
        if (bannerPatternHolderTranslationKey.isPresent()) {
            return Component.translatable(bannerPatternHolderTranslationKey.get());
        }

        return original.call(itemStack);
    }

    @WrapMethod(
        method = "getTooltipImage"
    )
    public Optional<TooltipComponent> useItemBehavior(ItemStack itemStack, Operation<Optional<TooltipComponent>> original) {
        return this.itematic$getBehavior(ItemBehaviorType.ITEM_HOLDER)
            .flatMap(itemHolder -> itemHolder.tooltipData(itemStack));
    }

    @WrapMethod(
        method = "getName(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;"
    )
    private Component alsoCheckTextHolderItemBehavior(ItemStack itemStack, Operation<Component> original) {
        if (!itemStack.itematic$hasBehavior(ItemBehaviorType.TEXT_HOLDER)) {
            return original.call(itemStack);
        }

        WrittenBookContent writtenBookContent = itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT);
        if (writtenBookContent == null) {
            return original.call(itemStack);
        }

        String title = writtenBookContent.title().raw();
        if (StringUtil.isBlank(title)) {
            return original.call(itemStack);
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
        this.dataComponents = this.createDefaultDataComponents();
    }

    @Override
    public <T extends ItemBehavior<T>> boolean itematic$hasBehavior(ItemBehaviorType<T> type) {
        if (this.behavior == null) {
            return false;
        }

        return this.behavior.has(type);
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
            .map(food -> food.mayStartUsing(user, stack))
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
    private DataComponentMap createDefaultDataComponents() {
        DataComponentMap.Builder builder = DataComponentMap.builder()
            .addAll(DataComponents.COMMON_ITEM_COMPONENTS);
        this.display.addComponents(builder);
        for (ItemBehavior<?> behavior : this.behavior) {
            behavior.addComponents(builder);
        }

        if (!this.attributeModifiers.modifiers().isEmpty()) {
            builder.set(DataComponents.ATTRIBUTE_MODIFIERS, this.attributeModifiers);
        }

        return builder.build();
    }

    @Mixin(Item.Properties.class)
    public static class PropertiesExtender {
        @WrapMethod(
            method = {
                "usingConvertsTo",
                "craftRemainder(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item$Properties;"
            }
        )
        private Item.Properties doNotUseToPreventCreatingNewItemStackTemplateWithItem(Item item, Operation<Item.Properties> original) {
            return (Item.Properties) (Object) this;
        }

        @WrapMethod(
            method = "effectiveDescriptionId"
        )
        @Nullable
        private String effectiveDescriptionIdUseNull(Operation<String> original) {
            return null;
        }

        @WrapMethod(
            method = "effectiveModel"
        )
        @Nullable
        private Identifier effectiveModelUseNull(Operation<Identifier> original) {
            return null;
        }
    }
}
