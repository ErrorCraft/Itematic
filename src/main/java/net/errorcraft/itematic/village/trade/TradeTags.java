package net.errorcraft.itematic.village.trade;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class TradeTags {
    public static final TagKey<Trade> FARMER_NOVICE = of("farmer_novice");
    public static final TagKey<Trade> FARMER_APPRENTICE = of("farmer_apprentice");
    public static final TagKey<Trade> FARMER_JOURNEYMAN = of("farmer_journeyman");
    public static final TagKey<Trade> FARMER_EXPERT = of("farmer_expert");
    public static final TagKey<Trade> FARMER_MASTER = of("farmer_master");
    public static final TagKey<Trade> FISHERMAN_NOVICE = of("fisherman_novice");
    public static final TagKey<Trade> FISHERMAN_APPRENTICE = of("fisherman_apprentice");
    public static final TagKey<Trade> FISHERMAN_JOURNEYMAN = of("fisherman_journeyman");
    public static final TagKey<Trade> FISHERMAN_EXPERT = of("fisherman_expert");
    public static final TagKey<Trade> FISHERMAN_MASTER = of("fisherman_master");
    public static final TagKey<Trade> SHEPHERD_NOVICE = of("shepherd_novice");
    public static final TagKey<Trade> SHEPHERD_APPRENTICE = of("shepherd_apprentice");
    public static final TagKey<Trade> SHEPHERD_JOURNEYMAN = of("shepherd_journeyman");
    public static final TagKey<Trade> SHEPHERD_EXPERT = of("shepherd_expert");
    public static final TagKey<Trade> SHEPHERD_MASTER = of("shepherd_master");
    public static final TagKey<Trade> FLETCHER_NOVICE = of("fletcher_novice");
    public static final TagKey<Trade> FLETCHER_APPRENTICE = of("fletcher_apprentice");
    public static final TagKey<Trade> FLETCHER_JOURNEYMAN = of("fletcher_journeyman");
    public static final TagKey<Trade> FLETCHER_EXPERT = of("fletcher_expert");
    public static final TagKey<Trade> FLETCHER_MASTER = of("fletcher_master");
    public static final TagKey<Trade> LIBRARIAN_NOVICE = of("librarian_novice");
    public static final TagKey<Trade> LIBRARIAN_APPRENTICE = of("librarian_apprentice");
    public static final TagKey<Trade> LIBRARIAN_JOURNEYMAN = of("librarian_journeyman");
    public static final TagKey<Trade> LIBRARIAN_EXPERT = of("librarian_expert");
    public static final TagKey<Trade> LIBRARIAN_MASTER = of("librarian_master");
    public static final TagKey<Trade> CARTOGRAPHER_NOVICE = of("cartographer_novice");
    public static final TagKey<Trade> CARTOGRAPHER_APPRENTICE = of("cartographer_apprentice");
    public static final TagKey<Trade> CARTOGRAPHER_JOURNEYMAN = of("cartographer_journeyman");
    public static final TagKey<Trade> CARTOGRAPHER_EXPERT = of("cartographer_expert");
    public static final TagKey<Trade> CARTOGRAPHER_MASTER = of("cartographer_master");
    public static final TagKey<Trade> CLERIC_NOVICE = of("cleric_novice");
    public static final TagKey<Trade> CLERIC_APPRENTICE = of("cleric_apprentice");
    public static final TagKey<Trade> CLERIC_JOURNEYMAN = of("cleric_journeyman");
    public static final TagKey<Trade> CLERIC_EXPERT = of("cleric_expert");
    public static final TagKey<Trade> CLERIC_MASTER = of("cleric_master");
    public static final TagKey<Trade> ARMORER_NOVICE = of("armorer_novice");
    public static final TagKey<Trade> ARMORER_APPRENTICE = of("armorer_apprentice");
    public static final TagKey<Trade> ARMORER_JOURNEYMAN = of("armorer_journeyman");
    public static final TagKey<Trade> ARMORER_EXPERT = of("armorer_expert");
    public static final TagKey<Trade> ARMORER_MASTER = of("armorer_master");
    public static final TagKey<Trade> WEAPONSMITH_NOVICE = of("weaponsmith_novice");
    public static final TagKey<Trade> WEAPONSMITH_APPRENTICE = of("weaponsmith_apprentice");
    public static final TagKey<Trade> WEAPONSMITH_JOURNEYMAN = of("weaponsmith_journeyman");
    public static final TagKey<Trade> WEAPONSMITH_EXPERT = of("weaponsmith_expert");
    public static final TagKey<Trade> WEAPONSMITH_MASTER = of("weaponsmith_master");
    public static final TagKey<Trade> TOOLSMITH_NOVICE = of("toolsmith_novice");
    public static final TagKey<Trade> TOOLSMITH_APPRENTICE = of("toolsmith_apprentice");
    public static final TagKey<Trade> TOOLSMITH_JOURNEYMAN = of("toolsmith_journeyman");
    public static final TagKey<Trade> TOOLSMITH_EXPERT = of("toolsmith_expert");
    public static final TagKey<Trade> TOOLSMITH_MASTER = of("toolsmith_master");
    public static final TagKey<Trade> BUTCHER_NOVICE = of("butcher_novice");
    public static final TagKey<Trade> BUTCHER_APPRENTICE = of("butcher_apprentice");
    public static final TagKey<Trade> BUTCHER_JOURNEYMAN = of("butcher_journeyman");
    public static final TagKey<Trade> BUTCHER_EXPERT = of("butcher_expert");
    public static final TagKey<Trade> BUTCHER_MASTER = of("butcher_master");
    public static final TagKey<Trade> LEATHERWORKER_NOVICE = of("leatherworker_novice");
    public static final TagKey<Trade> LEATHERWORKER_APPRENTICE = of("leatherworker_apprentice");
    public static final TagKey<Trade> LEATHERWORKER_JOURNEYMAN = of("leatherworker_journeyman");
    public static final TagKey<Trade> LEATHERWORKER_EXPERT = of("leatherworker_expert");
    public static final TagKey<Trade> LEATHERWORKER_MASTER = of("leatherworker_master");
    public static final TagKey<Trade> MASON_NOVICE = of("mason_novice");
    public static final TagKey<Trade> MASON_APPRENTICE = of("mason_apprentice");
    public static final TagKey<Trade> MASON_JOURNEYMAN = of("mason_journeyman");
    public static final TagKey<Trade> MASON_EXPERT = of("mason_expert");
    public static final TagKey<Trade> MASON_MASTER = of("mason_master");
    public static final TagKey<Trade> WANDERING_TRADER_REGULAR = of("wandering_trader_regular");
    public static final TagKey<Trade> WANDERING_TRADER_SPECIAL = of("wandering_trader_special");



    private TradeTags() {}

    private static TagKey<Trade> of(String id) {
        return TagKey.of(ItematicRegistryKeys.TRADE, new Identifier(id));
    }
}
