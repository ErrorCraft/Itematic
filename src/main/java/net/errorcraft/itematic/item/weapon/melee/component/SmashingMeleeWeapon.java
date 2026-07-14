package net.errorcraft.itematic.item.weapon.melee.component;

import com.mojang.serialization.Codec;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;

public class SmashingMeleeWeapon {
    public static final SmashingMeleeWeapon INSTANCE = new SmashingMeleeWeapon();
    public static final Codec<SmashingMeleeWeapon> CODEC = Codec.unit(INSTANCE);
    private static final MaceItem DUMMY = new MaceItem(new Item.Settings());

    private SmashingMeleeWeapon() {}

    public void hit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        DUMMY.postHit(stack, target, attacker);
    }
}
