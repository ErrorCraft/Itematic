package net.errorcraft.itematic.mm;

import com.chocohead.mm.api.ClassTinkerers;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class ItematicEarlyRiser implements Runnable {
    @Override
    public void run() {
        MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();
        String recipeBookType = remapper.mapClassName("intermediary", "net.minecraft.class_5421");
        ClassTinkerers.enumBuilder(recipeBookType)
            .addEnum("ITEMATIC$BREWING")
            .build();
        String lootContextEntityTarget = remapper.mapClassName("intermediary", "net.minecraft.class_47$class_50");
        String contextParameter = 'L' + remapper.mapClassName("intermediary", "net.minecraft.class_169") + ';';
        ClassTinkerers.enumBuilder(lootContextEntityTarget, String.class, contextParameter)
            .addEnum("ITEMATIC$TARGET_ENTITY", "target_entity", ItematicContextParameters.TARGET_ENTITY)
            .build();
    }
}
