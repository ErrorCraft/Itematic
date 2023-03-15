package errorcraft.itematic.mixin.item;

import errorcraft.itematic.access.item.ItemAccess;
import errorcraft.itematic.item.ItemBase;
import errorcraft.itematic.item.component.ItemComponent;
import errorcraft.itematic.item.component.ItemComponentSet;
import errorcraft.itematic.item.component.ItemComponentTypes;
import errorcraft.itematic.item.component.components.DamageableItemComponent;
import errorcraft.itematic.item.component.components.ToolItemComponent;
import errorcraft.itematic.item.component.components.UseDurationItemComponent;
import errorcraft.itematic.util.ActionResultUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Item.class)
public class ItemExtender implements ItemAccess {
    private ItemBase base;
    private ItemComponentSet components;

    /**
     * @author ErrorCraft
     * @reason Uses the ItemBase implementation for data-driven items.
     */
    @Overwrite
    public final int getMaxCount() {
        return this.base.maxCount();
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
        return result;
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public ActionResult useOnBlock(ItemUsageContext context) {
        TypedActionResult<ItemStack> result = TypedActionResult.pass(context.getStack());
        for (ItemComponent component : this.components) {
            TypedActionResult<ItemStack> newResult = component.useOnBlock(context);
            if (newResult.getResult() == ActionResult.FAIL) {
                return newResult.getResult();
            }
            result = ActionResultUtil.max(newResult, result.getResult());
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
        return result;
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
        return stack;
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean isFood() {
        return this.components.contains(ItemComponentTypes.FOOD);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean isDamageable() {
        if (this.components == null) {
            return false;
        }
        return this.components.contains(ItemComponentTypes.DAMAGEABLE);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public int getMaxUseTime(ItemStack stack) {
        return this.components.get(ItemComponentTypes.USE_DURATION).map(UseDurationItemComponent::ticks).orElse(0);
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
        if (this.components == null) {
            return 0;
        }
        return this.components.get(ItemComponentTypes.DAMAGEABLE).map(DamageableItemComponent::durability).orElse(0);
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        if (this.components == null) {
            return 1.0f;
        }
        return this.components.get(ItemComponentTypes.TOOL).map(ToolItemComponent::miningSpeed).orElse(1.0f);
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
    public ItemBase getItemBase() {
        return this.base;
    }

    @Override
    public void setItemBase(ItemBase base) {
        this.base = base;
    }

    @Override
    public ItemComponentSet getComponents() {
        return this.components;
    }

    @Override
    public void setComponents(ItemComponentSet components) {
        this.components = components;
    }
}
