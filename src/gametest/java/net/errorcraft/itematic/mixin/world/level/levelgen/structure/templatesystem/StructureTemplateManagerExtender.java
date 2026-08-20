package net.errorcraft.itematic.mixin.world.level.levelgen.structure.templatesystem;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.nio.file.Path;
import java.nio.file.Paths;

@Mixin(StructureTemplateManager.class)
public class StructureTemplateManagerExtender {
    @Unique
    @Nullable
    private static final String STRUCTURE_OUTPUT_DIRECTORY = System.getProperty("itematic.gametest.structure-output-directory");

    @ModifyExpressionValue(
        method = "createAndValidatePathToGeneratedStructure",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;generatedDir:Ljava/nio/file/Path;",
            opcode = Opcodes.GETFIELD
        )
    )
    private Path useCustomStructureOutputDirectory(Path original) {
        if (STRUCTURE_OUTPUT_DIRECTORY != null) {
            return Paths.get(STRUCTURE_OUTPUT_DIRECTORY);
        }

        return original;
    }

    @ModifyConstant(
        method = "createAndValidatePathToGeneratedStructure",
        constant = @Constant(
            stringValue = "structures"
        )
    )
    private String useSingularNameForStructureOutputDirectory(String constant) {
        return "structure";
    }
}
