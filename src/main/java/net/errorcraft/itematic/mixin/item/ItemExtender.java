package net.errorcraft.itematic.mixin.item;

import com.google.common.collect.Interner;
import net.errorcraft.itematic.access.item.ItemAccess;
import net.errorcraft.itematic.component.ItematicDataComponentTypes;
import net.errorcraft.itematic.component.type.UseDurationDataComponent;
import net.errorcraft.itematic.inventory.StackReferenceUtil;
import net.errorcraft.itematic.item.ItemBase;
import net.errorcraft.itematic.item.ItemUtil;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentSet;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.BlockItemComponent;
import net.errorcraft.itematic.item.component.components.DamageableItemComponent;
import net.errorcraft.itematic.item.component.components.EnchantableItemComponent;
import net.errorcraft.itematic.item.component.components.RepairableItemComponent;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.item.event.ItemEventMap;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.component.type.WrittenBookContentComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(Item.class)
public abstract class ItemExtender implements ItemAccess, FabricItem {
    @Shadow
    @Final
    @Mutable
    private ComponentMap components;

    @Unique
    private static final Interner<ComponentMap> COMPONENT_INTERNER = ItemAccessor.SettingsAccessor.componentInterner();
    @Unique
    private ItemBase base;
    @Unique
    private ItemComponentSet itemComponents;
    @Unique
    private ItemEventMap events;

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
        method = "use",
        at = @At("HEAD"),
        cancellable = true
    )
    public void useUseItemComponent(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info) {
        ItemStack stack = user.getStackInHand(hand).copy();
        ItemStack stackBeforeUsing = stack.copy();
        StackReference stackReference = StackReferenceUtil.of(stack);
        ActionResult result = ActionResult.PASS;
        for (ItemComponent<?> component : this.itemComponents) {
            ActionResult newResult = component.use(world, user, hand, stack, stackReference::set);
            if (newResult == ActionResult.FAIL) {
                info.setReturnValue(TypedActionResult.fail(stackReference.get()));
                return;
            }
            result = result.itematic$merge(newResult);
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld, stack, stackReference::set, hand)
                .entityPosition(ActionContextParameter.THIS, user)
                .build();
            this.itematic$invokeEvent(ItemEvents.USE, context);
        }

        if (stack.getMaxUseTime(user) <= 0 && result.isAccepted()) {
            ItemStack remainder = stackReference.get().itematic$applyUseEffects(user, stackBeforeUsing);
            stackReference.set(remainder);
        }

        info.setReturnValue(new TypedActionResult<>(result, stackReference.get()));
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        StackReference stackReference = StackReferenceUtil.of(stack);
        ActionResult result = ActionResult.PASS;
        for (ItemComponent<?> component : this.itemComponents) {
            ActionResult newResult = component.useOnBlock(context, stackReference::set);
            if (newResult == ActionResult.FAIL) {
                return newResult;
            }
            result = result.itematic$merge(newResult);
        }

        if (context.getWorld() instanceof ServerWorld serverWorld) {
            ActionContext actionContext = ActionContext.builder(serverWorld, stack, stackReference::set, context.getHand())
                .entityPosition(ActionContextParameter.THIS, context.getPlayer())
                .position(ActionContextParameter.TARGET, context.getBlockPos())
                .side(context.getSide())
                .build();
            this.itematic$invokeEvent(ItemEvents.USE_ON_BLOCK, actionContext);
        }

        tryUpdateItemStack(context.getPlayer(), context.getHand(), stack, stackReference);
        return result;
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        StackReference stackReference = StackReferenceUtil.of(stack);
        ActionResult result = ActionResult.PASS;
        for (ItemComponent<?> component : this.itemComponents) {
            ActionResult newResult = component.useOnEntity(user, entity, hand, stack, stackReference::set);
            if (newResult == ActionResult.FAIL) {
                return newResult;
            }
            result = result.itematic$merge(newResult);
        }

        if (user.getWorld() instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld, stack, stackReference::set, hand)
                .entityPosition(ActionContextParameter.THIS, user)
                .entityPosition(ActionContextParameter.TARGET, entity)
                .build();
            this.itematic$invokeEvent(ItemEvents.USE_ON_ENTITY, context);
        }

        tryUpdateItemStack(user, hand, stack, stackReference);
        return result;
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = false;
        StackReference stackReference = StackReferenceUtil.of(stack);
        for (ItemComponent<?> component : this.itemComponents) {
            result |= component.postHit(stack, target, attacker, stackReference::set);
        }

        if (attacker.getWorld() instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld, stack, stackReference::set, EquipmentSlot.MAINHAND)
                .entityPosition(ActionContextParameter.THIS, attacker)
                .entityPosition(ActionContextParameter.TARGET, target)
                .build();
            this.itematic$invokeEvent(ItemEvents.HIT_ENTITY, context);
        }

        tryUpdateItemStack(attacker, Hand.MAIN_HAND, stack, stackReference);
        return result;
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        boolean result = false;
        StackReference stackReference = StackReferenceUtil.of(stack);
        for (ItemComponent<?> component : this.itemComponents) {
            result |= component.postMine(stack, world, state, pos, miner, stackReference::set);
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld, stack, stackReference::set, EquipmentSlot.MAINHAND)
                .entityPosition(ActionContextParameter.THIS, miner)
                .position(ActionContextParameter.TARGET, pos.toCenterPos())
                .build();
            this.itematic$invokeEvent(ItemEvents.BROKE_BLOCK, context);
        }

        tryUpdateItemStack(miner, Hand.MAIN_HAND, stack, stackReference);
        return result;
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        int usedTicks = user.itematic$itemUsedTicks();
        for (ItemComponent<?> component : this.itemComponents) {
            component.using(stack, world, user, usedTicks, remainingUseTicks);
        }
    }

    @Inject(
        method = "onItemEntityDestroyed",
        at = @At("HEAD")
    )
    private void onItemEntityDestroyedUseItemComponent(ItemEntity entity, CallbackInfo info) {
        this.itematic$getComponent(ItemComponentTypes.BLOCK)
            .ifPresent(c -> c.onDestroyed(entity));
        this.itematic$getComponent(ItemComponentTypes.ITEM_HOLDER)
            .ifPresent(c -> c.onDestroyed(entity));
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        ItemStack stackBeforeUsing = stack.copy();
        boolean result = false;
        int usedTicks = user.itematic$itemUsedTicks();
        StackReference stackReference = StackReferenceUtil.of(stack);
        for (ItemComponent<?> component : this.itemComponents) {
            result |= component.stopUsing(stack, world, user, usedTicks, remainingUseTicks, stackReference::set);
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld, stack, stackReference::set, user.getActiveHand())
                .entityPosition(ActionContextParameter.THIS, user)
                .build();
            this.itematic$invokeEvent(ItemEvents.STOPPED_USING, context);
        }

        if (result) {
            stackReference.set(stackReference.get().itematic$applyUseEffects(user, stackBeforeUsing));
        }

        tryUpdateItemStack(user, Hand.MAIN_HAND, stack, stackReference);
    }

    @Inject(
        method = "finishUsing",
        at = @At("HEAD"),
        cancellable = true
    )
    public void finishUsingUseItemComponent(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {
        ItemStack stackBeforeUsing = stack.copy();
        int usedTicks = user.itematic$itemUsedTicks();
        StackReference stackReference = StackReferenceUtil.of(stack);
        for (ItemComponent<?> component : this.itemComponents) {
            component.finishUsing(world, user, stack, usedTicks, stackReference::set);
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld, stack, stackReference::set, user.getActiveHand())
                .entityPosition(ActionContextParameter.THIS, user)
                .build();
            this.itematic$invokeEvent(ItemEvents.FINISHED_USING, context);
        }

        this.itematic$getComponent(ItemComponentTypes.CONSUMABLE)
            .ifPresent(c -> c.consume(user, stack, stackReference::set, world, user.getActiveHand()));
        stackReference.set(stackReference.get().itematic$applyUseEffects(user, stackBeforeUsing));
        info.setReturnValue(stackReference.get());
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        for (ItemComponent<?> component : this.itemComponents) {
            component.inventoryTick(stack, world, entity, slot, selected);
        }
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        boolean result = false;
        for (ItemComponent<?> component : this.itemComponents) {
            result |= component.clickOnSlot(stack, slot, clickType, player);
        }
        return result;
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        boolean result = false;
        for (ItemComponent<?> component : this.itemComponents) {
            result |= component.clickedOnWithStack(stack, otherStack, slot, clickType, player, cursorStackReference::set);
        }
        return result;
    }

    @Inject(
        method = "onCraft",
        at = @At("HEAD")
    )
    public void onCraft(ItemStack stack, World world, CallbackInfo info) {
        for (ItemComponent<?> component : this.itemComponents) {
            component.onCraft(stack, world);
        }
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        this.base.tooltip()
            .ifPresent(tooltip::addAll);
        for (ItemComponent<?> component : this.itemComponents) {
            component.appendTooltip(stack, context, tooltip, type);
        }
    }

    @Redirect(
        method = "isEnchantable",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"
        )
    )
    public int getMaxCountUseStackCount(ItemStack instance) {
        return instance.getCount();
    }

    @Redirect(
        method = "isEnchantable",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;contains(Lnet/minecraft/component/ComponentType;)Z"
        )
    )
    public boolean containsMaxDamageUseItemComponentCheck(ItemStack instance, ComponentType<?> type) {
        return instance.itematic$hasComponent(ItemComponentTypes.ENCHANTABLE);
    }

    @Inject(
        method = "canMine",
        at = @At("HEAD"),
        cancellable = true
    )
    private void useDebugStickItemComponent(BlockState state, World world, BlockPos pos, PlayerEntity miner, CallbackInfoReturnable<Boolean> info) {
        this.itematic$getComponent(ItemComponentTypes.DEBUG_STICK)
            .ifPresent(c -> {
                c.use(miner, state, world, pos);
                info.setReturnValue(false);
            });
    }

    @Redirect(
        method = "getMiningSpeed",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/type/ToolComponent;getSpeed(Lnet/minecraft/block/BlockState;)F"
        )
    )
    private float getSpeedPassItemStack(ToolComponent instance, BlockState state, ItemStack stack) {
        return instance.itematic$getSpeed(stack, state);
    }

    @Redirect(
        method = "isCorrectForDrops",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/type/ToolComponent;isCorrectForDrops(Lnet/minecraft/block/BlockState;)Z"
        )
    )
    private boolean isCorrectForDropsPassItemStack(ToolComponent instance, BlockState state, ItemStack stack) {
        return instance.itematic$isCorrectForDrops(stack, state);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.itematic$getComponent(ItemComponentTypes.REPAIRABLE)
            .map(RepairableItemComponent::items)
            .map(ingredient::isIn)
            .orElse(false);
    }

    @Inject(
        method = "hasGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    public void checkPointableItemComponent(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if (this.itematic$hasComponent(ItemComponentTypes.POINTABLE) && stack.contains(DataComponentTypes.LODESTONE_TRACKER)) {
            info.setReturnValue(true);
        }
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean canBeNested() {
        return this.itematic$getComponent(ItemComponentTypes.BLOCK)
            .map(BlockItemComponent::canBeNested)
            .orElse(true);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public int getEnchantability() {
        return this.itematic$getComponent(ItemComponentTypes.ENCHANTABLE)
            .map(EnchantableItemComponent::enchantability)
            .orElse(0);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public SoundEvent getBreakSound() {
        return this.itematic$getComponent(ItemComponentTypes.DAMAGEABLE)
            .flatMap(DamageableItemComponent::breakSound)
            .map(RegistryEntry::value)
            .orElse(SoundEvents.ENTITY_ITEM_BREAK);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean isNetworkSynced() {
        return this.itematic$hasComponent(ItemComponentTypes.MAP_HOLDER);
    }

    @Inject(
        method = "getUseAction",
        at = @At("HEAD"),
        cancellable = true
    )
    public void getUseActionUseItemComponent(ItemStack stack, CallbackInfoReturnable<UseAction> info) {
        if (!this.itematic$hasComponent(ItemComponentTypes.USEABLE)) {
            info.setReturnValue(UseAction.NONE);
            return;
        }

        info.setReturnValue(stack.getOrDefault(ItematicDataComponentTypes.USE_ANIMATION, UseAction.NONE));
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        if (!this.itematic$hasComponent(ItemComponentTypes.USEABLE)) {
            return 0;
        }

        UseDurationDataComponent useDuration = this.components.get(ItematicDataComponentTypes.USE_DURATION);
        if (useDuration == null) {
            return 0;
        }

        return useDuration.ticks(stack, user);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBase implementation for data-driven items.
     */
    @Overwrite
    public String getTranslationKey() {
        return this.base.translationKey();
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public String getTranslationKey(ItemStack stack) {
        return this.itematic$getComponent(ItemComponentTypes.POINTABLE)
            .flatMap(c -> c.lodestoneTranslationKey(stack))
            .or(() -> this.itematic$getComponent(ItemComponentTypes.POTION_HOLDER)
                .map(c -> c.translationKey(stack, this.getTranslationKey())))
            .or(() -> this.itematic$getComponent(ItemComponentTypes.BANNER_PATTERN_HOLDER)
                .flatMap(c -> c.translationKey(stack, this.getTranslationKey())))
            .orElseGet(this::getTranslationKey);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return this.itematic$getComponent(ItemComponentTypes.ITEM_HOLDER)
            .flatMap(c -> c.tooltipData(stack));
    }

    @Inject(
        method = "getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/text/Text;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkTextHolderItemComponent(ItemStack stack, CallbackInfoReturnable<Text> info) {
        if (!stack.itematic$hasComponent(ItemComponentTypes.TEXT_HOLDER)) {
            return;
        }

        WrittenBookContentComponent writtenBookContent = stack.get(DataComponentTypes.WRITTEN_BOOK_CONTENT);
        if (writtenBookContent == null) {
            return;
        }

        String title = writtenBookContent.title().raw();
        if (StringHelper.isBlank(title)) {
            return;
        }
        info.setReturnValue(Text.literal(title));
    }

    @Override
    public ItemBase itematic$itemBase() {
        return this.base;
    }

    @Override
    public void itematic$setItemBase(ItemBase base) {
        this.base = base;
    }

    @Override
    public ItemComponentSet itematic$components() {
        return this.itemComponents;
    }

    @Override
    public void itematic$setComponents(ItemComponentSet components) {
        this.itemComponents = components;
        this.components = this.initializeComponents();
    }

    @Override
    public <T extends ItemComponent<T>> boolean itematic$hasComponent(ItemComponentType<T> type) {
        if (this.itemComponents == null) {
            return false;
        }
        return this.itemComponents.contains(type);
    }

    @Override
    public <T extends ItemComponent<T>> Optional<T> itematic$getComponent(ItemComponentType<T> type) {
        if (this.itemComponents == null) {
            return Optional.empty();
        }
        return this.itemComponents.get(type);
    }

    @Override
    public ItemEventMap itematic$events() {
        return this.events;
    }

    @Override
    public void itematic$setEvents(ItemEventMap events) {
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
    public boolean itematic$mayStartUsing(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        return this.itematic$getComponent(ItemComponentTypes.FOOD)
            .map(c -> c.mayStartUsing(user, stack))
            .orElse(true);
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        return true;
    }

    @Unique
    private static void tryUpdateItemStack(LivingEntity target, Hand hand, ItemStack stack, StackReference stackReference) {
        if (target == null) {
            return;
        }

        ItemStack newStack = stackReference.get();
        if (stack == newStack) {
            return;
        }

        target.setStackInHand(hand, newStack);
    }

    @Unique
    private ComponentMap initializeComponents() {
        ComponentMap.Builder componentsBuilder = ComponentMap.builder()
            .addAll(DataComponentTypes.DEFAULT_ITEM_COMPONENTS);
        this.base.addComponents(componentsBuilder);
        AttributeModifiersComponent.Builder attributeModifiersBuilder = AttributeModifiersComponent.builder();
        for (ItemComponent<?> component : this.itemComponents) {
            component.addComponents(componentsBuilder);
            component.addAttributeModifiers(attributeModifiersBuilder, this.itemComponents);
        }

        AttributeModifiersComponent attributeModifiers = attributeModifiersBuilder.build();
        if (!attributeModifiers.modifiers().isEmpty()) {
            componentsBuilder.add(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributeModifiers);
        }

        return COMPONENT_INTERNER.intern(componentsBuilder.build());
    }
}
