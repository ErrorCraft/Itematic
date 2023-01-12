package errorcraft.itematic.mixin.item;

import errorcraft.itematic.access.item.ItemAccess;
import errorcraft.itematic.item.ItemBase;
import errorcraft.itematic.item.component.ItemComponent;
import errorcraft.itematic.item.component.ItemComponentSet;
import errorcraft.itematic.item.component.ItemComponentTypes;
import errorcraft.itematic.item.component.components.UseDurationItemComponent;
import errorcraft.itematic.util.ActionResultUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

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
    public int getMaxUseTime(ItemStack stack) {
        return this.components.get(ItemComponentTypes.USE_DURATION).map(UseDurationItemComponent::ticks).orElse(0);
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
