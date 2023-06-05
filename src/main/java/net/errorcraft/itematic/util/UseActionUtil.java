package net.errorcraft.itematic.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.UseAction;

public class UseActionUtil {
    public static final Codec<UseAction> CODEC = StringIdentifiable.createCodec(UseAction::values);

    private UseActionUtil() {}
}
