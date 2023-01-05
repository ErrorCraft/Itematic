package errorcraft.itematic.mixin.item;

import errorcraft.itematic.access.item.ItemAccess;
import errorcraft.itematic.item.ItemBase;
import errorcraft.itematic.item.component.ItemComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Set;

@Mixin(Item.class)
public class ItemExtender implements ItemAccess {
    private ItemBase base;
    private Set<ItemComponent> components;

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
        ItemStack itemStack = user.getStackInHand(hand);
        for (ItemComponent component : this.components) {
            TypedActionResult<ItemStack> result = component.use(world, user, hand, itemStack);
            if (result.getResult() != ActionResult.PASS) {
                return result;
            }
            itemStack = result.getValue();
        }
        return TypedActionResult.pass(itemStack);
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
    public Set<ItemComponent> getComponents() {
        return this.components;
    }

    @Override
    public void setComponents(Set<ItemComponent> components) {
        this.components = components;
    }
}
