package net.errorcraft.itematic.mixin.block.entity;

import com.mojang.logging.LogUtils;
import net.errorcraft.itematic.access.block.entity.DecoratedPotBlockEntityAccess;
import net.errorcraft.itematic.block.entity.DecoratedPotBlockEntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(DecoratedPotBlockEntity.class)
public class DecoratedPotBlockEntityExtender extends BlockEntity implements DecoratedPotBlockEntityAccess {
    @Shadow
    @Final
    public static String SHERDS_NBT_KEY;

    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();

    @Unique
    private DecoratedPotBlockEntityUtil.Sherds sherds;

    public DecoratedPotBlockEntityExtender(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Redirect(
        method = "writeNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/DecoratedPotBlockEntity$Sherds;toNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;"
        )
    )
    private NbtCompound toNbtUseRegistryEntry(DecoratedPotBlockEntity.Sherds instance, NbtCompound nbt) {
        if (this.sherds == null || this.sherds.isEmpty()) {
            return nbt;
        }
        RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, this.itematic$getRegistryManager());
        DecoratedPotBlockEntityUtil.Sherds.CODEC.encodeStart(ops, this.sherds)
            .resultOrPartial(LOGGER::error)
            .ifPresent(sherds -> nbt.put(SHERDS_NBT_KEY, sherds));
        return nbt;
    }

    @Redirect(
        method = "readNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/DecoratedPotBlockEntity$Sherds;fromNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/block/entity/DecoratedPotBlockEntity$Sherds;"
        )
    )
    private DecoratedPotBlockEntity.Sherds fromNbtUseRegistryEntry(NbtCompound nbt) {
        this.sherds = DecoratedPotBlockEntityUtil.fromNbt(this.itematic$getRegistryManager(), nbt);
        return null;
    }

    @Redirect(
        method = "asStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/DecoratedPotBlockEntity;getStackWith(Lnet/minecraft/block/entity/DecoratedPotBlockEntity$Sherds;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack getStackWithUseCreateStack(DecoratedPotBlockEntity.Sherds sherds) {
        return DecoratedPotBlockEntityUtil.createStack(this.itematic$getRegistryManager(), this.sherds);
    }

    @Override
    public Optional<DecoratedPotBlockEntityUtil.Sherds> itematic$sherds() {
        return Optional.ofNullable(this.sherds);
    }

    @Override
    public void itematic$readNbtFromStack(World world, ItemStack stack) {
        this.sherds = DecoratedPotBlockEntityUtil.fromNbt(world.getRegistryManager(), BlockItem.getBlockEntityNbt(stack));
    }
}
