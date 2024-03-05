package net.errorcraft.itematic.mixin.client.render.model;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderExtender {
    @Unique
    private static final String ITEM_MODEL_PREFIX = "models/item/";
    @Unique
    private static final int ITEM_MODEL_PREFIX_LENGTH = ITEM_MODEL_PREFIX.length();
    @Unique
    private static final int FILE_SUFFIX_LENGTH = ".json".length();

    @Shadow
    protected abstract void addModel(ModelIdentifier modelId);

    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getIds()Ljava/util/Set;"
        )
    )
    private Set<Identifier> getIdsForItemsReturnEmptySet(DefaultedRegistry<Item> instance) {
        return Set.of();
    }

    @Inject(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
            ordinal = 0,
            shift = At.Shift.AFTER
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=items"
            )
        )
    )
    private void addAllLoadedItemModels(BlockColors blockColors, Profiler profiler, Map<Identifier, JsonUnbakedModel> jsonUnbakedModels, Map<Identifier, List<ModelLoader.SourceTrackedData>> blockStates, CallbackInfo info) {
        for (Identifier identifier : jsonUnbakedModels.keySet()) {
            if (identifier.getPath().startsWith(ITEM_MODEL_PREFIX)) {
                Identifier actualId = identifier.withPath(path -> path.substring(ITEM_MODEL_PREFIX_LENGTH, path.length() - FILE_SUFFIX_LENGTH));
                this.addModel(new ModelIdentifier(actualId, "inventory"));
            }
        }
    }
}
