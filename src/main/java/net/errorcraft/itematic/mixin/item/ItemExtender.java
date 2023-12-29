package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.access.item.ItemAccess;
import net.errorcraft.itematic.inventory.StackReferenceUtil;
import net.errorcraft.itematic.item.ItemBase;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentSet;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.*;
import net.errorcraft.itematic.item.event.ItemEvent;
import net.errorcraft.itematic.item.event.ItemEventMap;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.util.ActionResultUtil;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.MutableActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(Item.class)
public class ItemExtender implements ItemAccess {
    @Unique
    private ItemBase base;
    @Unique
    private ItemComponentSet components;
    @Unique
    private ItemEventMap events;

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBase implementation for data-driven items.
     */
    @Overwrite
    public final int getMaxCount() {
        return this.base.maxCount();
    }

    @Redirect(
        method = "getRarity",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/Item;rarity:Lnet/minecraft/util/Rarity;",
            opcode = Opcodes.GETFIELD
        )
    )
    private Rarity getRarityUseItemBaseImplementation(Item instance) {
        return this.base.display().rarity();
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        StackReference stackReference = StackReferenceUtil.of(stack);
        ActionResult result = ActionResult.PASS;
        for (ItemComponent component : this.components) {
            ActionResult newResult = component.use(world, user, hand, stack, stackReference::set);
            if (newResult == ActionResult.FAIL) {
                return TypedActionResult.fail(stackReference.get());
            }
            result = ActionResultUtil.max(result, newResult);
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = MutableActionContext.stackUsage(serverWorld, stack, stackReference::set, hand)
                .entityPosition(ActionContextParameter.THIS, user);
            this.itematic$invokeEvent(ItemEvents.USE, context);
        }
        return new TypedActionResult<>(result, stackReference.get());
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!this.allowsPlacement(context)) {
            return ActionResult.PASS;
        }

        ItemStack stack = context.getStack();
        StackReference stackReference = StackReferenceUtil.of(stack);
        ActionResult result = ActionResult.PASS;
        for (ItemComponent component : this.components) {
            ActionResult newResult = component.useOnBlock(context, stackReference::set);
            if (newResult == ActionResult.FAIL) {
                return newResult;
            }
            result = ActionResultUtil.max(result, newResult);
        }

        if (context.getWorld() instanceof ServerWorld serverWorld) {
            ActionContext actionContext = MutableActionContext.stackUsage(serverWorld, stack, stackReference::set, context.getHand())
                .entityPosition(ActionContextParameter.THIS, context.getPlayer())
                .position(ActionContextParameter.TARGET, context.getBlockPos())
                .side(context.getSide());
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
        for (ItemComponent component : this.components) {
            ActionResult newResult = component.useOnEntity(user, entity, hand, stack, stackReference::set);
            if (newResult == ActionResult.FAIL) {
                return newResult;
            }
            result = ActionResultUtil.max(result, newResult);
        }

        if (user.getWorld() instanceof ServerWorld serverWorld) {
            ActionContext context = MutableActionContext.stackUsage(serverWorld, stack, stackReference::set, hand)
                .entityPosition(ActionContextParameter.THIS, user)
                .entityPosition(ActionContextParameter.TARGET, entity);
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
        for (ItemComponent component : this.components) {
            result |= component.postHit(stack, target, attacker, stackReference::set);
        }

        if (attacker.getWorld() instanceof ServerWorld serverWorld) {
            ActionContext context = MutableActionContext.stackUsage(serverWorld, stack, stackReference::set, EquipmentSlot.MAINHAND)
                .entityPosition(ActionContextParameter.THIS, attacker)
                .entityPosition(ActionContextParameter.TARGET, target);
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
        for (ItemComponent component : this.components) {
            result |= component.postMine(stack, world, state, pos, miner, stackReference::set);
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = MutableActionContext.stackUsage(serverWorld, stack, stackReference::set, EquipmentSlot.MAINHAND)
                .entityPosition(ActionContextParameter.THIS, miner)
                .position(ActionContextParameter.TARGET, pos.toCenterPos());
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
        for (ItemComponent component : this.components) {
            component.using(stack, world, user, remainingUseTicks);
        }
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        StackReference stackReference = StackReferenceUtil.of(stack);
        for (ItemComponent component : this.components) {
            component.stopUsing(stack, world, user, remainingUseTicks, stackReference::set);
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = MutableActionContext.stackUsage(serverWorld, stack, stackReference::set, user.getActiveHand())
                .entityPosition(ActionContextParameter.THIS, user);
            this.itematic$invokeEvent(ItemEvents.STOPPED_USING, context);
        }

        tryUpdateItemStack(user, Hand.MAIN_HAND, stack, stackReference);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        StackReference stackReference = StackReferenceUtil.of(stack);
        for (ItemComponent component : this.components) {
            component.finishUsing(world, user, stack, stackReference::set);
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = MutableActionContext.stackUsage(serverWorld, stack, stackReference::set, user.getActiveHand())
                .entityPosition(ActionContextParameter.THIS, user);
            this.itematic$invokeEvent(ItemEvents.FINISHED_USING, context);
        }

        this.itematic$getComponent(ItemComponentTypes.CONSUMABLE)
            .ifPresent(c -> c.consume(user, stack, stackReference::set, user.getActiveHand()));
        return stackReference.get();
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        for (ItemComponent component : this.components) {
            component.inventoryTick(stack, world, entity, slot, selected);
        }
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        this.base.display().tooltip().ifPresent(tooltip::addAll);
        for (ItemComponent component : this.components) {
            component.appendTooltip(stack, world, tooltip, context);
        }
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean isFood() {
        return this.itematic$hasComponent(ItemComponentTypes.FOOD);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean isDamageable() {
        return this.itematic$hasComponent(ItemComponentTypes.DAMAGEABLE);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1 && this.itematic$hasComponent(ItemComponentTypes.ENCHANTABLE);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean isUsedOnRelease(ItemStack stack) {
        return this.itematic$getComponent(ItemComponentTypes.SHOOTER)
            .map(ShooterItemComponent::chargeable)
            .orElse(false);
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

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean hasGlint(ItemStack stack) {
        return this.itematic$getComponent(ItemComponentTypes.GLINT)
            .map(GlintItemComponent::glint)
            .orElseGet(() -> {
                if (this.itematic$hasComponent(ItemComponentTypes.POINTABLE) && CompassItem.hasLodestone(stack)) {
                    return true;
                }
                return stack.hasEnchantments();
            });
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public int getEnchantability() {
        return this.itematic$getComponent(ItemComponentTypes.ENCHANTABLE).map(EnchantableItemComponent::enchantability).orElse(0);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public UseAction getUseAction(ItemStack stack) {
        return this.itematic$getComponent(ItemComponentTypes.USE_ANIMATION).map(UseAnimationItemComponent::animation).orElse(UseAction.NONE);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public int getMaxUseTime(ItemStack stack) {
        return this.itematic$getComponent(ItemComponentTypes.USE_DURATION).map(UseDurationItemComponent::ticks).orElse(0);
    }

    @Redirect(
        method = { "getItemBarStep", "getItemBarColor" },
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/Item;maxDamage:I",
            opcode = Opcodes.GETFIELD
        )
    )
    public int maxDamageFieldUseMaxDamageMethodCall(Item instance) {
        return this.getMaxDamage();
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public final int getMaxDamage() {
        return this.itematic$getComponent(ItemComponentTypes.DAMAGEABLE).map(DamageableItemComponent::durability).orElse(0);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return this.itematic$getComponent(ItemComponentTypes.TOOL).map(component -> component.getMiningSpeed(state)).orElse(1.0f);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBase implementation for data-driven items.
     */
    @Overwrite
    public String getTranslationKey() {
        return this.base.display().translationKey();
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public String getTranslationKey(ItemStack stack) {
        return this.itematic$getComponent(ItemComponentTypes.POINTABLE)
            .flatMap(c -> c.lodestoneTranslationKey(stack))
            .orElseGet(this::getTranslationKey);
    }

    @Inject(
        method = "getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/text/Text;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void checkTextHolderItemComponent(ItemStack stack, CallbackInfoReturnable<Text> info) {
        if (stack.itematic$hasComponent(ItemComponentTypes.TEXT_HOLDER)) {
            stack.itematic$nbt()
                .map(nbt -> nbt.getString(WrittenBookItem.TITLE_KEY))
                .filter(title -> !StringHelper.isEmpty(title))
                .map(Text::literal)
                .ifPresent(info::setReturnValue);
        }
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
        return this.components;
    }

    @Override
    public void itematic$setComponents(ItemComponentSet components) {
        this.components = components;
    }

    @Override
    public <T extends ItemComponent> boolean itematic$hasComponent(ItemComponentType<T> type) {
        if (this.components == null) {
            return false;
        }
        return this.components.contains(type);
    }

    @Override
    public <T extends ItemComponent> Optional<T> itematic$getComponent(ItemComponentType<T> type) {
        if (this.components == null) {
            return Optional.empty();
        }
        return this.components.get(type);
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
    public boolean itematic$mayStartUsing(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        return this.itematic$getComponent(ItemComponentTypes.FOOD)
            .map(c -> c.mayStartUsing(user))
            .orElse(true);
    }

    @Unique
    private boolean allowsPlacement(ItemUsageContext context) {
        return !context.ignoresPlacementComponent() && this.itematic$getComponent(ItemComponentTypes.CAN_PLACE_ON_FLUIDS)
            .map(CanPlaceOnFluidsItemComponent::allowOriginalPlacement)
            .orElse(true);
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
}
