package errorcraft.itematic.mixin.block;

import errorcraft.itematic.item.component.ItemComponentTypes;
import errorcraft.itematic.item.component.components.CompostableItemComponent;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ComposterBlock.class)
public class ComposterBlockExtender {
    @Redirect(
        method = "onUse",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    private boolean containsKeyUseItemComponentCheck(Object2FloatMap<ItemConvertible> instance, Object o, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        return player.getStackInHand(hand).hasComponent(ItemComponentTypes.COMPOSTABLE);
    }

    @Redirect(
        method = "compost",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    private static boolean containsKeyUseItemComponentCheck(Object2FloatMap<ItemConvertible> instance, Object o, Entity user, BlockState state, ServerWorld world, ItemStack stack) {
        return stack.hasComponent(ItemComponentTypes.COMPOSTABLE);
    }

    @Redirect(
        method = "addToComposter",
        at = @At(
            value = "INVOKE",
            target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;getFloat(Ljava/lang/Object;)F",
            remap = false
        )
    )
    private static float getFloatUseItemComponent(Object2FloatMap<ItemConvertible> instance, Object o, @Nullable Entity user, BlockState state, WorldAccess world, BlockPos pos, ItemStack stack) {
        return stack.getComponent(ItemComponentTypes.COMPOSTABLE).map(CompostableItemComponent::levelIncreaseChance).orElse(0.0f);
    }

    @Mixin(targets = "net/minecraft/block/ComposterBlock$ComposterInventory")
    public static class ComposterInventoryExtender {
        @Redirect(
            method = "canInsert",
            at = @At(
                value = "INVOKE",
                target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z",
                remap = false
            )
        )
        private boolean containsKeyUseItemComponentCheck(Object2FloatMap<ItemConvertible> instance, Object o, int slot, ItemStack stack) {
            return stack.hasComponent(ItemComponentTypes.COMPOSTABLE);
        }
    }
}
