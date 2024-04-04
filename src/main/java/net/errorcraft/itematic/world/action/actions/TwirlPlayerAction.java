package net.errorcraft.itematic.world.action.actions;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.network.packet.s2c.play.TwirlS2CPacket;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionType;
import net.errorcraft.itematic.world.action.ActionTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public record TwirlPlayerAction() implements Action<TwirlPlayerAction> {
    public static final TwirlPlayerAction INSTANCE = new TwirlPlayerAction();
    public static final MapCodec<TwirlPlayerAction> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public ActionType<TwirlPlayerAction> type() {
        return ActionTypes.TWIRL_PLAYER;
    }

    @Override
    public boolean execute(ActionContext context) {
        PlayerEntity player = context.player(ActionContextParameter.THIS).orElse(null);
        if (player == null) {
            return false;
        }
        int riptideLevel = EnchantmentHelper.getRiptide(context.stack());
        if (player instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.networkHandler.sendPacket(new TwirlS2CPacket(riptideLevel));
        }
        execute(riptideLevel, player, context.world());
        return true;
    }

    public static void execute(int riptideLevel, PlayerEntity player, World world) {
        double riptideFactor = 3.0d * ((1.0d + riptideLevel) / 4.0d);
        float yaw = player.getYaw();
        float pitch = player.getPitch();
        double x = -Math.sin(yaw * (Math.PI / 180.0d)) * Math.cos(pitch * (Math.PI / 180.0d));
        double y = -Math.sin(pitch * (Math.PI / 180.0d));
        double z = Math.cos(yaw * (Math.PI / 180)) * Math.cos(pitch * (Math.PI / 180.0d));
        double distance = Math.sqrt(x * x + y * y + z * z);
        player.addVelocity(x * riptideFactor / distance, y * riptideFactor / distance, z * riptideFactor / distance);
        player.useRiptide(20);
        if (player.isOnGround()) {
            player.move(MovementType.SELF, new Vec3d(0.0d, 1.2d, 0.0d));
        }
        world.playSoundFromEntity(null, player, soundEvent(riptideLevel), SoundCategory.PLAYERS, 1.0f, 1.0f);
    }

    private static SoundEvent soundEvent(int level) {
        if (level >= 3) {
            return SoundEvents.ITEM_TRIDENT_RIPTIDE_3;
        }
        if (level == 2) {
            return SoundEvents.ITEM_TRIDENT_RIPTIDE_2;
        }
        return SoundEvents.ITEM_TRIDENT_RIPTIDE_1;
    }
}
