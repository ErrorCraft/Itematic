package net.errorcraft.itematic.world.item.component;

import net.errorcraft.itematic.references.SoundEventIds;
import net.errorcraft.itematic.world.level.levelgen.blockpredicates.MatchingPropertiesBlockPredicate;
import net.errorcraft.itematic.world.level.levelgen.feature.stateproviders.ApplyPropertiesProvider;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;

import java.util.List;

public class ItematicBlockTransformers {
    public static final ResourceKey<BlockTransformer> FILL_END_PORTAL_FRAME = createKey("fill_end_portal_frame");

    private ItematicBlockTransformers() {}

    public static void bootstrap(BootstrapContext<BlockTransformer> context) {
        HolderGetter<SoundEvent> soundEvents = context.lookup(Registries.SOUND_EVENT);

        context.register(
            FILL_END_PORTAL_FRAME,
            new BlockTransformer(
                List.of(
                    BlockTransformer.BlockTransformData
                        .builder(
                            RuleBasedStateProvider.builder()
                                .ifTrueThenProvide(
                                    MatchingPropertiesBlockPredicate.of(
                                        StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(BlockStateProperties.EYE, false)
                                    ),
                                    new ApplyPropertiesProvider(
                                        BlockItemStatePropertiesBuilder.create()
                                            .property(BlockStateProperties.EYE, true)
                                            .build()
                                    )
                                )
                                .build()
                        )
                        .consumeOnUse(true)
                        .itemDamagePerUse(0)
                        .sound(soundEvents.getOrThrow(SoundEventIds.END_PORTAL_FRAME_FILL))
                        .itematic$pushEntitiesUpwards()
                        .build()
                )
            )
        );
    }

    private static ResourceKey<BlockTransformer> createKey(String name) {
        return ResourceKey.create(Registries.BLOCK_TRANSFORMER, Identifier.withDefaultNamespace(name));
    }
}
