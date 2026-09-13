package net.errorcraft.itematic.mixin.client.resources.model;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.client.resources.model.ModelBakeryAccess;
import net.errorcraft.itematic.client.resources.model.FailedToLoadModels;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;
import java.util.function.BiFunction;

@Mixin(ModelBakery.class)
public class ModelBakeryExtender {
    @Shadow
    @Final
    private static Matrix4fc IDENTITY;

    @Shadow
    @Final
    private EntityModelSet entityModelSet;

    @Shadow
    @Final
    private SpriteGetter sprites;

    @Shadow
    @Final
    private PlayerSkinRenderCache playerSkinRenderCache;

    @Unique
    private static final ScopedValue<ItemModel> FAILED_TO_LOAD_ITEM_MODEL = ScopedValue.newInstance();

    @ModifyArg(
        method = "bakeModels",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/concurrent/CompletableFuture;thenCombine(Ljava/util/concurrent/CompletionStage;Ljava/util/function/BiFunction;)Ljava/util/concurrent/CompletableFuture;"
        )
    )
    private BiFunction<Map<BlockState, BlockStateModel>, Map<Identifier, ItemModel>, ModelBakery.BakingResult> passFailedToLoadItemModel(BiFunction<Map<BlockState, BlockStateModel>, Map<Identifier, ItemModel>, ModelBakery.BakingResult> fn, @Local(name = "missingModels") ModelBakery.MissingModels missingModels, @Local(name = "baker") ModelBakery.ModelBakerImpl baker) {
        return (bakedBlockStateModels, bakedItemStateModels) -> ScopedValue
            .where(FAILED_TO_LOAD_ITEM_MODEL, this.loadFailedToLoadItemModel(baker, missingModels))
            .call(() -> fn.apply(bakedBlockStateModels, bakedItemStateModels));
    }

    @ModifyReturnValue(
        method = "lambda$bakeModels$3",
        at = @At("TAIL")
    )
    private static ModelBakery.BakingResult setFailedToLoadedItemModel(ModelBakery.BakingResult original) {
        original.itematic$setFailedToLoadItemModel(FAILED_TO_LOAD_ITEM_MODEL.get());
        return original;
    }

    @Unique
    private ItemModel loadFailedToLoadItemModel(ModelBaker baker, ModelBakery.MissingModels missingModels) {
        return FailedToLoadModels.ITEM_MODEL_REFERENCE.bake(
            new ItemModel.BakingContext(
                baker,
                this.entityModelSet,
                this.sprites,
                this.playerSkinRenderCache,
                missingModels.item(),
                null
            ),
            IDENTITY
        );
    }

    @Mixin(ModelBakery.BakingResult.class)
    public static class BakingResultExtender implements ModelBakeryAccess.BakingResultAccess {
        @Unique
        private ItemModel failedToLoadItemModel;

        @Override
        public ItemModel itematic$failedToLoadItemModel() {
            return this.failedToLoadItemModel;
        }

        @Override
        public void itematic$setFailedToLoadItemModel(ItemModel failedToLoadItemModel) {
            this.failedToLoadItemModel = failedToLoadItemModel;
        }
    }
}
