package errorcraft.itematic.mixin.data.server.tag;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TagProvider.class)
public class TagProviderExtender {
    @ModifyReturnValue(
        method = "method_49660",
        at = @At("RETURN"),
        remap = false
    )
    private boolean tagEntryPredicateCheckDefaultNamespace(boolean original, @Local Identifier id) {
        return original || Identifier.DEFAULT_NAMESPACE.equals(id.getNamespace());
    }
}
