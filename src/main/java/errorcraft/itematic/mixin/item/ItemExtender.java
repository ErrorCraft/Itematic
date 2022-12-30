package errorcraft.itematic.mixin.item;

import errorcraft.itematic.access.item.ItemAccess;
import errorcraft.itematic.item.ItemBase;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Item.class)
public class ItemExtender implements ItemAccess {
    private ItemBase base;

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
}
