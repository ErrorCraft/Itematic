package net.errorcraft.itematic.mixin.world.item;

import net.minecraft.world.item.CushionItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CushionItem.class)
public interface CushionItemAccessor {
    @Invoker("recalculateContextForSpecialCollisionShapes")
    static UseOnContext recalculateContextForSpecialCollisionShapes(final UseOnContext context) {
        throw new AssertionError();
    }
}
