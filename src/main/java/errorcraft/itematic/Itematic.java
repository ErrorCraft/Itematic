package errorcraft.itematic;

import errorcraft.itematic.item.component.ItemComponentTypes;
import net.fabricmc.api.ModInitializer;

public class Itematic implements ModInitializer {
    @Override
    public void onInitialize() {
        ItemComponentTypes.init();
    }
}
