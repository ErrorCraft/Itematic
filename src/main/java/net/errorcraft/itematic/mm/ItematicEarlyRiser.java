package net.errorcraft.itematic.mm;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class ItematicEarlyRiser implements Runnable {
    @Override
    public void run() {
        MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();
        String recipeBookCategory = remapper.mapClassName("intermediary", "net.minecraft.class_5421");
        ClassTinkerers.enumBuilder(recipeBookCategory)
            .addEnum("ITEMATIC$BREWING")
            .build();
    }
}
