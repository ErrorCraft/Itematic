package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public record ModifySignAction(PositionTarget position, Optional<DyeColor> color, Optional<Boolean> glow, Optional<Boolean> wax) implements Action<ModifySignAction> {
    public static final MapCodec<ModifySignAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        PositionTarget.CODEC.fieldOf("position").forGetter(ModifySignAction::position),
        DyeColor.CODEC.optionalFieldOf("color").forGetter(ModifySignAction::color),
        Codec.BOOL.optionalFieldOf("glow").forGetter(ModifySignAction::glow),
        Codec.BOOL.optionalFieldOf("wax").forGetter(ModifySignAction::wax)
    ).apply(instance, ModifySignAction::new));

    public static ModifySignAction dye(PositionTarget position, DyeColor color) {
        return new ModifySignAction(position, Optional.of(color), Optional.empty(), Optional.empty());
    }

    public static ModifySignAction glow(PositionTarget position, boolean glow) {
        return new ModifySignAction(position, Optional.empty(), Optional.of(glow), Optional.empty());
    }

    public static ModifySignAction wax(PositionTarget position, boolean wax) {
        return new ModifySignAction(position, Optional.empty(), Optional.empty(), Optional.of(wax));
    }

    @Override
    public ActionType<ModifySignAction> type() {
        return ActionTypes.MODIFY_SIGN;
    }

    @Override
    public boolean execute(NewActionContext context) {
        Vec3d pos = context.get(this.position.parameter());
        if (pos == null) {
            return false;
        }

        BlockPos blockPos = BlockPos.ofFloored(pos);
        if (!(context.world().getBlockEntity(blockPos) instanceof SignBlockEntity blockEntity)) {
            return false;
        }

        if (context.get(LootContextParameters.THIS_ENTITY) instanceof PlayerEntity player) {
            return this.modify(blockEntity, player);
        }

        return false;
    }

    private boolean modify(SignBlockEntity blockEntity, PlayerEntity player) {
        if (blockEntity.isWaxed()) {
            return false;
        }

        boolean front = blockEntity.isPlayerFacingFront(player);
        boolean result = false;
        result |= this.glow.map(glow -> blockEntity.changeText(text -> text.withGlowing(glow), front))
            .orElse(false);
        result |= this.wax.map(blockEntity::setWaxed)
            .orElse(false);
        result |= this.color.map(color -> blockEntity.changeText(text -> text.withColor(color), front))
            .orElse(false);
        return result;
    }
}
