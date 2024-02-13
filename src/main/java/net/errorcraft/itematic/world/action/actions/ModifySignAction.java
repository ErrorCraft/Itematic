package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public record ModifySignAction(ActionContextParameter position, Optional<DyeColor> color, Optional<Boolean> glow, Optional<Boolean> wax) implements Action<ModifySignAction> {
    public static final Codec<ModifySignAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ActionContextParameter.CODEC.fieldOf("position").forGetter(ModifySignAction::position),
        Codecs.createStrictOptionalFieldCodec(DyeColor.CODEC, "color").forGetter(ModifySignAction::color),
        Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "glow").forGetter(ModifySignAction::glow),
        Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "wax").forGetter(ModifySignAction::wax)
    ).apply(instance, ModifySignAction::new));

    @Override
    public ActionType<ModifySignAction> type() {
        return ActionTypes.MODIFY_SIGN;
    }

    @Override
    public boolean execute(ActionContext context) {
        BlockPos pos = context.blockPos(this.position);
        if (!(context.world().getBlockEntity(pos) instanceof SignBlockEntity blockEntity)) {
            return false;
        }
        return context.player(ActionContextParameter.THIS)
            .map(player -> this.modify(blockEntity, player))
            .orElse(false);
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

    public static ModifySignAction dye(ActionContextParameter position, DyeColor color) {
        return new ModifySignAction(position, Optional.of(color), Optional.empty(), Optional.empty());
    }

    public static ModifySignAction glow(ActionContextParameter position, boolean glow) {
        return new ModifySignAction(position, Optional.empty(), Optional.of(glow), Optional.empty());
    }

    public static ModifySignAction wax(ActionContextParameter position, boolean wax) {
        return new ModifySignAction(position, Optional.empty(), Optional.empty(), Optional.of(wax));
    }
}
