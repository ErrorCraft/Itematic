package net.errorcraft.itematic.mixin.client.render.model;

//@Mixin(ModelLoader.class)
public abstract class ModelLoaderExtender {
//    @Shadow
//    protected abstract void addModelToBake(ModelIdentifier id, UnbakedModel model);
//
//    @Shadow
//    protected abstract void loadInventoryVariantItemModel(Identifier id);
//
//    @Unique
//    private static final String ITEM_MODEL_PREFIX = "models/item/";
//
//    @Unique
//    private static final int ITEM_MODEL_PREFIX_LENGTH = ITEM_MODEL_PREFIX.length();
//
//    @Unique
//    private static final int FILE_SUFFIX_LENGTH = ".json".length();
//
//    @Redirect(
//        method = "<init>",
//        at = @At(
//            value = "INVOKE",
//            target = "Lnet/minecraft/registry/DefaultedRegistry;getIds()Ljava/util/Set;"
//        )
//    )
//    private Set<Identifier> getIdsForItemsReturnEmptySet(DefaultedRegistry<Item> instance) {
//        return Set.of();
//    }
//
//    @Inject(
//        method = "<init>",
//        at = @At(
//            value = "INVOKE",
//            target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
//            ordinal = 0,
//            shift = At.Shift.AFTER
//        ),
//        slice = @Slice(
//            from = @At(
//                value = "CONSTANT",
//                args = "stringValue=items"
//            )
//        )
//    )
//    private void addAllLoadedItemModels(BlockColors blockColors, Profiler profiler, Map<Identifier, JsonUnbakedModel> jsonUnbakedModels, Map<Identifier, List<BlockStatesLoader.SourceTrackedData>> blockStates, CallbackInfo info) {
//        for (Identifier identifier : jsonUnbakedModels.keySet()) {
//            if (identifier.getPath().startsWith(ITEM_MODEL_PREFIX)) {
//                Identifier actualId = identifier.withPath(path -> path.substring(ITEM_MODEL_PREFIX_LENGTH, path.length() - FILE_SUFFIX_LENGTH));
//                this.loadInventoryVariantItemModel(actualId);
//            }
//        }
//    }
}
