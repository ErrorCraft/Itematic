package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.access.item.ItemUsageContextAccess;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemUsageContext.class)
public class ItemUsageContextExtender implements ItemUsageContextAccess {
    @Shadow
    @Final
    @Nullable
    private PlayerEntity player;

    @Shadow
    @Final
    private ItemStack stack;

    @Override
    public ItemStackExchanger itematic$stackExchanger() {
        if (this.player == null) {
            return ItemStackExchanger.EMPTY;
        }

        return ItemStackExchanger.forEntity(this.player, this.stack);
    }
}
