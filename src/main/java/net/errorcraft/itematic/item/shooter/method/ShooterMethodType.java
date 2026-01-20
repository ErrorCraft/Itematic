package net.errorcraft.itematic.item.shooter.method;

import com.mojang.serialization.MapCodec;

public record ShooterMethodType<T extends ShooterMethod>(MapCodec<T> codec) {
}
