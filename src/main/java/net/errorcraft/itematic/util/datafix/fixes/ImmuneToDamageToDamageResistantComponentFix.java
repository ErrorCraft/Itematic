package net.errorcraft.itematic.util.datafix.fixes;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.minecraft.datafixer.fix.ComponentFix;

import java.util.Optional;

public class ImmuneToDamageToDamageResistantComponentFix extends ComponentFix {
    public ImmuneToDamageToDamageResistantComponentFix(Schema outputSchema) {
        super(outputSchema, "ImmuneToDamageToDamageResistantComponentFix", "minecraft:immune_to_damage", "minecraft:damage_resistant");
    }

    @Override
    protected <T> Dynamic<T> fixComponent(Dynamic<T> dynamic) {
        Optional<String> types = dynamic.asString().result();
        if (types.isPresent()) {
            return dynamic.emptyMap()
                .set("types", dynamic.createString(types.get()));
        }

        return dynamic;
    }
}
