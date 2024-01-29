package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.access.block.entity.DecoratedPotBlockEntityAccess;
import net.errorcraft.itematic.block.entity.DecoratedPotBlockEntityUtil;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Mixin(DecoratedPotBlock.class)
public class DecoratedPotBlockExtender {
    @Redirect(
        method = "appendTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/DecoratedPotBlockEntity$Sherds;fromNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/block/entity/DecoratedPotBlockEntity$Sherds;"
        )
    )
    private DecoratedPotBlockEntity.Sherds fromNbtUseDefault(NbtCompound nbt) {
        return DecoratedPotBlockEntity.Sherds.DEFAULT;
    }

    @Redirect(
        method = "appendTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/DecoratedPotBlockEntity$Sherds;equals(Ljava/lang/Object;)Z"
        )
    )
    private boolean sherdsAreEqualUseNullCheck(DecoratedPotBlockEntity.Sherds instance, Object object, ItemStack stack, @Nullable BlockView world, @Share("sherds") LocalRef<DecoratedPotBlockEntityUtil.Sherds> sherdsRef) {
        if (!(world instanceof WorldView worldView)) {
            return true;
        }
        DecoratedPotBlockEntityUtil.Sherds sherds = DecoratedPotBlockEntityUtil.fromNbt(worldView.getRegistryManager(), BlockItem.getBlockEntityNbt(stack));
        if (sherds == null) {
            return true;
        }
        sherdsRef.set(sherds);
        return false;
    }

    @Redirect(
        method = "appendTooltip",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/stream/Stream;of([Ljava/lang/Object;)Ljava/util/stream/Stream;"
        )
    )
    private <T> Stream<RegistryEntry<Item>> streamForwardsSherdsUseRegistryEntry(T[] values, @Share("sherds") LocalRef<DecoratedPotBlockEntityUtil.Sherds> sherdsRef) {
        return sherdsRef.get().streamForwards();
    }

    @ModifyArg(
        method = "appendTooltip",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/stream/Stream;forEach(Ljava/util/function/Consumer;)V"
        )
    )
    private <T> Consumer<RegistryEntry<Item>> streamOfUseRegistryEntry(Consumer<? super T> action, @Local(argsOnly = true) List<Text> tooltip) {
        return sherd -> tooltip.add(new ItemStack(sherd).getName().copyContentOnly().formatted(Formatting.GRAY));
    }

    @Redirect(
        method = "method_49815",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/DecoratedPotBlockEntity$Sherds;stream()Ljava/util/stream/Stream;"
        )
    )
    private static Stream<RegistryEntry<Item>> streamSherdsUseRegistryEntry(DecoratedPotBlockEntity.Sherds instance, DecoratedPotBlockEntity blockEntity) {
        return ((DecoratedPotBlockEntityAccess) blockEntity).itematic$sherds()
            .map(DecoratedPotBlockEntityUtil.Sherds::stream)
            .orElseGet(Stream::empty);
    }

    @Redirect(
        method = "method_49815",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/stream/Stream;map(Ljava/util/function/Function;)Ljava/util/stream/Stream;"
        )
    )
    private static <T, R> Stream<ItemStack> mapToItemStackUseRegistryEntry(Stream<RegistryEntry<Item>> instance, Function<? super T, ? extends R> function) {
        return instance.map(ItemStack::new);
    }

    @ModifyArg(
        method = "onPlaced",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"
        )
    )
    private <T> Consumer<DecoratedPotBlockEntityAccess> readNbtFromStackUseRegistryEntry(Consumer<? super T> action, @Local(argsOnly = true) World world, @Local(argsOnly = true) ItemStack stack) {
        return blockEntity -> blockEntity.itematic$readNbtFromStack(world, stack);
    }
}
