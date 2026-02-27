package net.errorcraft.itematic.mm.client;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.item.ItemStack;

public class ItematicClientEarlyRiser implements Runnable {
    @Override
    public void run() {
        FabricLoader loader = FabricLoader.getInstance();

        // There is no early riser client entry point definition, so this check is done in main instead
        if (loader.getEnvironmentType() != EnvType.CLIENT) {
            return;
        }

        MappingResolver remapper = loader.getMappingResolver();
        String recipeBookGroup = remapper.mapClassName("intermediary", "net.minecraft.class_314");
        String itemStack = 'L' + remapper.mapClassName("intermediary", "net.minecraft.class_1799") + ';';
        String itemStackArray = '[' + itemStack;
        ClassTinkerers.enumBuilder(recipeBookGroup, itemStackArray)
            .addEnum("ITEMATIC$BREWING_SEARCH", () -> new Object[] {
                new ItemStack[0]
            })
            .addEnum("ITEMATIC$BREWING_MODIFY", () -> new Object[] {
                new ItemStack[0]
            })
            .addEnum("ITEMATIC$BREWING_AMPLIFY", () -> new Object[] {
                new ItemStack[0]
            })
            .build();
    }
}
