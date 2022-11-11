package errorcraft.itematic.mixin.item;

import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ItemGroupExtender {
    @Mixin(ItemGroup.Builder.class)
    public static class BuilderExtender {
        @Shadow
        @Final
        private static ItemGroup.EntryCollector EMPTY_ENTRIES;

        @Shadow
        private ItemGroup.Type type;

        @Shadow
        private ItemGroup.EntryCollector entryCollector;

        @Inject(
            method = "build",
            at = @At("HEAD")
        )
        private void yes(CallbackInfoReturnable<ItemGroup> info) {
            if (this.type == ItemGroup.Type.CATEGORY) {
                this.entryCollector = EMPTY_ENTRIES;
            }
        }
    }
}
