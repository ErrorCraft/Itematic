package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.access.item.ItemAccess;
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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Optional;

@Mixin(Item.class)
public class ItemExtender implements ItemAccess {
    private ItemBase base;
    private ItemComponentSet components;
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
        TypedActionResult<ItemStack> result = TypedActionResult.pass(user.getStackInHand(hand));
        for (ItemComponent component : this.components) {
            TypedActionResult<ItemStack> newResult = component.use(world, user, hand, result.getValue());
            if (newResult.getResult() == ActionResult.FAIL) {
                return newResult;
            }
            result = ActionResultUtil.max(newResult, result.getResult());
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = MutableActionContext.stackUsage(serverWorld, result.getValue(), hand)
                .entityPosition(ActionContextParameter.THIS, user);
            result.getValue().invokeEvent(ItemEvents.USE, context);
        }
        return result;
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

        TypedActionResult<ItemStack> result = TypedActionResult.pass(context.getStack());
        for (ItemComponent component : this.components) {
            TypedActionResult<ItemStack> newResult = component.useOnBlock(context);
            if (newResult.getResult() == ActionResult.FAIL) {
                return newResult.getResult();
            }
            result = ActionResultUtil.max(newResult, result.getResult());
        }

        if (context.getWorld() instanceof ServerWorld serverWorld) {
            ActionContext actionContext = MutableActionContext.stackUsage(serverWorld, result.getValue(), context.getHand())
                .entityPosition(ActionContextParameter.THIS, context.getPlayer())
                .position(ActionContextParameter.TARGET, context.getBlockPos())
                .side(context.getSide());
            result.getValue().invokeEvent(ItemEvents.USE_ON_BLOCK, actionContext);
        }
        return result.getResult();
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        TypedActionResult<ItemStack> result = TypedActionResult.pass(stack);
        for (ItemComponent component : this.components) {
            TypedActionResult<ItemStack> newResult = component.useOnEntity(user, entity, hand, stack);
            if (newResult.getResult() == ActionResult.FAIL) {
                return newResult.getResult();
            }
            result = ActionResultUtil.max(newResult, result.getResult());
        }

        if (user.getWorld() instanceof ServerWorld serverWorld) {
            ActionContext context = MutableActionContext.stackUsage(serverWorld, result.getValue(), hand)
                .entityPosition(ActionContextParameter.THIS, user)
                .entityPosition(ActionContextParameter.TARGET, entity);
            result.getValue().invokeEvent(ItemEvents.USE_ON_ENTITY, context);
        }
        return result.getResult();
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = false;
        for (ItemComponent component : this.components) {
            result |= component.postHit(stack, target, attacker);
        }

        if (attacker.getWorld() instanceof ServerWorld serverWorld) {
            ActionContext context = MutableActionContext.stackUsage(serverWorld, stack)
                .entityPosition(ActionContextParameter.THIS, attacker)
                .entityPosition(ActionContextParameter.TARGET, target);
            stack.invokeEvent(ItemEvents.HIT_ENTITY, context);
        }
        return result;
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        boolean result = false;
        for (ItemComponent component : this.components) {
            result |= component.postMine(stack, world, state, pos, miner);
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = MutableActionContext.stackUsage(serverWorld, stack)
                .entityPosition(ActionContextParameter.THIS, miner)
                .position(ActionContextParameter.TARGET, pos.toCenterPos());
            stack.invokeEvent(ItemEvents.BROKE_BLOCK, context);
        }
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
        for (ItemComponent component : this.components) {
            component.stopUsing(stack, world, user, remainingUseTicks);
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = MutableActionContext.stackUsage(serverWorld, stack, user.getActiveHand())
                .entityPosition(ActionContextParameter.THIS, user);
            stack.invokeEvent(ItemEvents.STOPPED_USING, context);
        }
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        for (ItemComponent component : this.components) {
            stack = component.finishUsing(world, user, stack);
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = MutableActionContext.stackUsage(serverWorld, stack, user.getActiveHand())
                .entityPosition(ActionContextParameter.THIS, user);
            stack.invokeEvent(ItemEvents.FINISHED_USING, context);
        }
        ItemStack resultStack = stack;
        return this.getComponent(ItemComponentTypes.CONSUMABLE)
            .map(c -> c.consume(user, resultStack))
            .orElse(stack);
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
        return this.hasComponent(ItemComponentTypes.FOOD);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean isDamageable() {
        return this.hasComponent(ItemComponentTypes.DAMAGEABLE);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1 && this.hasComponent(ItemComponentTypes.ENCHANTABLE);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean isUsedOnRelease(ItemStack stack) {
        return this.getComponent(ItemComponentTypes.SHOOTER)
            .map(ShooterItemComponent::chargeable)
            .orElse(false);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.getComponent(ItemComponentTypes.REPAIRABLE)
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
        return this.getComponent(ItemComponentTypes.FOIL)
            .map(FoilItemComponent::foil)
            .orElseGet(stack::hasEnchantments);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public int getEnchantability() {
        return this.getComponent(ItemComponentTypes.ENCHANTABLE).map(EnchantableItemComponent::enchantability).orElse(0);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public UseAction getUseAction(ItemStack stack) {
        return this.getComponent(ItemComponentTypes.USE_ANIMATION).map(UseAnimationItemComponent::animation).orElse(UseAction.NONE);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public int getMaxUseTime(ItemStack stack) {
        return this.getComponent(ItemComponentTypes.USE_DURATION).map(UseDurationItemComponent::ticks).orElse(0);
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
        return this.getComponent(ItemComponentTypes.DAMAGEABLE).map(DamageableItemComponent::durability).orElse(0);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return this.getComponent(ItemComponentTypes.TOOL).map(component -> component.getMiningSpeed(state)).orElse(1.0f);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBase implementation for data-driven items.
     */
    @Overwrite
    public String getTranslationKey() {
        return this.base.display().translationKey();
    }

    @Override
    public ItemBase itemBase() {
        return this.base;
    }

    @Override
    public void setItemBase(ItemBase base) {
        this.base = base;
    }

    @Override
    public ItemComponentSet components() {
        return this.components;
    }

    @Override
    public void setComponents(ItemComponentSet components) {
        this.components = components;
    }

    @Override
    public <T extends ItemComponent> boolean hasComponent(ItemComponentType<T> type) {
        if (this.components == null) {
            return false;
        }
        return this.components.contains(type);
    }

    @Override
    public <T extends ItemComponent> Optional<T> getComponent(ItemComponentType<T> type) {
        if (this.components == null) {
            return Optional.empty();
        }
        return this.components.get(type);
    }

    @Override
    public ItemEventMap events() {
        return this.events;
    }

    @Override
    public void setEvents(ItemEventMap events) {
        this.events = events;
    }

    @Override
    public void invokeEvent(ItemEvent event, ActionContext context) {
        this.events.invokeEvent(event, context);
    }

    @Override
    public boolean mayStartUsing(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        return this.getComponent(ItemComponentTypes.FOOD)
            .map(c -> c.mayStartUsing(user))
            .orElse(true);
    }

    private boolean allowsPlacement(ItemUsageContext context) {
        return !context.ignoresPlacementComponent() && this.getComponent(ItemComponentTypes.CAN_PLACE_ON_FLUIDS)
            .map(CanPlaceOnFluidsItemComponent::allowOriginalPlacement)
            .orElse(true);
    }
}
