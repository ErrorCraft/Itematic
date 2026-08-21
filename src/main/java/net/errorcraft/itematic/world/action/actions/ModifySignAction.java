package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
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
        return ActionType.MODIFY_SIGN;
    }

    @Override
    public boolean execute(ActionContext context) {
        Vec3 pos = context.get(this.position.contextParam());
        if (pos == null) {
            return false;
        }

        BlockPos blockPos = BlockPos.containing(pos);
        if (!(context.level().getBlockEntity(blockPos) instanceof SignBlockEntity blockEntity)) {
            return false;
        }

        if (context.get(LootContextParams.THIS_ENTITY) instanceof Player player) {
            return this.modify(blockEntity, player);
        }

        return false;
    }

    private boolean modify(SignBlockEntity blockEntity, Player player) {
        if (blockEntity.isWaxed()) {
            return false;
        }

        boolean front = blockEntity.isFacingFrontText(player);
        boolean result = false;
        result |= this.glow.map(glow -> blockEntity.updateText(text -> text.setHasGlowingText(glow), front))
            .orElse(false);
        result |= this.wax.map(blockEntity::setWaxed)
            .orElse(false);
        result |= this.color.map(color -> blockEntity.updateText(text -> text.setColor(color), front))
            .orElse(false);
        return result;
    }
}
