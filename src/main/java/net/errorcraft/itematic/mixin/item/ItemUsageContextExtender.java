package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.access.item.ItemUsageContextAccess;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(UseOnContext.class)
public class ItemUsageContextExtender implements ItemUsageContextAccess {
    @Shadow
    @Final
    @Nullable
    private Player player;

    @Shadow
    @Final
    private ItemStack itemStack;

    @Override
    public ItemStackExchanger itematic$stackExchanger() {
        if (this.player == null) {
            return ItemStackExchanger.EMPTY;
        }

        return ItemStackExchanger.forEntity(this.player, this.itemStack);
    }
}
