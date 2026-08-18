package net.errorcraft.itematic.mixin.world.item.context;

import net.errorcraft.itematic.access.world.item.context.UseOnContextAccess;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(UseOnContext.class)
public class UseOnContextExtender implements UseOnContextAccess {
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
