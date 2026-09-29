package net.errorcraft.itematic.mixin.world.entity.decoration;

import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.decoration.Cushion;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Cushion.class)
public abstract class CushionExtender extends BlockAttachedEntity {
    @Shadow
    public abstract DyeColor getColor();

    @Shadow
    public abstract void setColor(DyeColor color);

    protected CushionExtender(EntityType<? extends BlockAttachedEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public @Nullable <T> T get(DataComponentType<? extends T> type) {
        if (type == ItematicDataComponents.CUSHION_COLOR) {
            return castComponentValue(type, this.getColor());
        }

        return super.get(type);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        this.applyImplicitComponentIfPresent(components, ItematicDataComponents.CUSHION_COLOR);
        super.applyImplicitComponents(components);
    }

    @Override
    protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
        if (type == ItematicDataComponents.CUSHION_COLOR) {
            this.setColor(castComponentValue(ItematicDataComponents.CUSHION_COLOR, value));
            return true;
        }

        return super.applyImplicitComponent(type, value);
    }
}
