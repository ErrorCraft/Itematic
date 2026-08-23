package net.errorcraft.itematic.mixin.core;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.TypedInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.stream.Stream;

@Mixin(TypedInstance.class)
public interface TypedInstanceExtender<T> {
    @Shadow
    @Nullable
    Holder<T> typeHolder();

    @WrapMethod(
        method = "tags"
    )
    private Stream<TagKey<T>> checkNull(Operation<Stream<TagKey<T>>> original) {
        if (this.typeHolder() == null) {
            return Stream.empty();
        }

        return original.call();
    }

    @WrapMethod(
        method = "is(Lnet/minecraft/tags/TagKey;)Z"
    )
    private boolean checkNull(TagKey<T> tag, Operation<Boolean> original) {
        return this.typeHolder() != null && original.call(tag);
    }

    @WrapMethod(
        method = "is(Lnet/minecraft/core/HolderSet;)Z"
    )
    private boolean checkNull(HolderSet<T> set, Operation<Boolean> original) {
        return this.typeHolder() != null && original.call(set);
    }

    @WrapMethod(
        method = "is(Ljava/lang/Object;)Z"
    )
    private boolean checkNull(T rawType, Operation<Boolean> original) {
        return this.typeHolder() != null && original.call(rawType);
    }

    @WrapMethod(
        method = "is(Lnet/minecraft/core/Holder;)Z"
    )
    private boolean checkNull(Holder<T> type, Operation<Boolean> original) {
        return this.typeHolder() != null && original.call(type);
    }

    @WrapMethod(
        method = "is(Lnet/minecraft/resources/ResourceKey;)Z"
    )
    private boolean checkNull(ResourceKey<T> type, Operation<Boolean> original) {
        return this.typeHolder() != null && original.call(type);
    }
}
