package net.errorcraft.itematic.mixin.util;

import net.errorcraft.itematic.access.util.DyeColorAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DyeColor.class)
public class DyeColorExtender implements DyeColorAccess {
    @Shadow
    @Final
    public static DyeColor WHITE;

    @Shadow
    @Final
    public static DyeColor ORANGE;

    @Shadow
    @Final
    public static DyeColor MAGENTA;

    @Shadow
    @Final
    public static DyeColor LIGHT_BLUE;

    @Shadow
    @Final
    public static DyeColor YELLOW;

    @Shadow
    @Final
    public static DyeColor LIME;

    @Shadow
    @Final
    public static DyeColor PINK;

    @Shadow
    @Final
    public static DyeColor GRAY;

    @Shadow
    @Final
    public static DyeColor LIGHT_GRAY;

    @Shadow
    @Final
    public static DyeColor CYAN;

    @Shadow
    @Final
    public static DyeColor PURPLE;

    @Shadow
    @Final
    public static DyeColor BLUE;

    @Shadow
    @Final
    public static DyeColor BROWN;

    @Shadow
    @Final
    public static DyeColor GREEN;

    @Shadow
    @Final
    public static DyeColor RED;

    @Shadow
    @Final
    public static DyeColor BLACK;

    @Unique
    private RegistryKey<Item> itemKey;

    static {
        ((DyeColorExtender)(Object) WHITE).itemKey = ItemKeys.WHITE_DYE;
        ((DyeColorExtender)(Object) ORANGE).itemKey = ItemKeys.ORANGE_DYE;
        ((DyeColorExtender)(Object) MAGENTA).itemKey = ItemKeys.MAGENTA_DYE;
        ((DyeColorExtender)(Object) LIGHT_BLUE).itemKey = ItemKeys.LIGHT_BLUE_DYE;
        ((DyeColorExtender)(Object) YELLOW).itemKey = ItemKeys.YELLOW_DYE;
        ((DyeColorExtender)(Object) LIME).itemKey = ItemKeys.LIME_DYE;
        ((DyeColorExtender)(Object) PINK).itemKey = ItemKeys.PINK_DYE;
        ((DyeColorExtender)(Object) GRAY).itemKey = ItemKeys.GRAY_DYE;
        ((DyeColorExtender)(Object) LIGHT_GRAY).itemKey = ItemKeys.LIGHT_GRAY_DYE;
        ((DyeColorExtender)(Object) CYAN).itemKey = ItemKeys.CYAN_DYE;
        ((DyeColorExtender)(Object) PURPLE).itemKey = ItemKeys.PURPLE_DYE;
        ((DyeColorExtender)(Object) BLUE).itemKey = ItemKeys.BLUE_DYE;
        ((DyeColorExtender)(Object) BROWN).itemKey = ItemKeys.BROWN_DYE;
        ((DyeColorExtender)(Object) GREEN).itemKey = ItemKeys.GREEN_DYE;
        ((DyeColorExtender)(Object) RED).itemKey = ItemKeys.RED_DYE;
        ((DyeColorExtender)(Object) BLACK).itemKey = ItemKeys.BLACK_DYE;
    }

    @Override
    public RegistryKey<Item> itematic$itemKey() {
        return this.itemKey;
    }
}
