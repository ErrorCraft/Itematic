package net.errorcraft.itematic.item.armor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

public record ArmorMaterial(Identifier assetId) {
    public static final Codec<ArmorMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Identifier.CODEC.fieldOf("asset_id").forGetter(ArmorMaterial::assetId)
    ).apply(instance, ArmorMaterial::new));
    
    public Identifier textureId() {
        return this.assetId.withPath(path -> "models/armor/" + path + "_layer_1");
    }

    public Identifier leggingsTextureId() {
        return this.assetId.withPath(path -> "models/armor/" + path + "_layer_2");
    }
}
