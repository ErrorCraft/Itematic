package net.errorcraft.itematic.gametest.entity.passive;

import net.errorcraft.itematic.assertion.Assert;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.DyeColor;

public class SheepEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:entity.platform")
    public void breedingRedAndYellowSheepResultsInOrangeSheep(GameTestHelper context) {
        Sheep firstSheep = context.spawn(EntityType.SHEEP, SPAWN_POSITION);
        firstSheep.setColor(DyeColor.RED);
        Sheep secondSheep = context.spawn(EntityType.SHEEP, SPAWN_POSITION);
        secondSheep.setColor(DyeColor.YELLOW);
        Sheep childSheep = firstSheep.getBreedOffspring(context.getLevel(), secondSheep);
        context.succeedIf(() -> {
            Assert.isNotNull(context, childSheep, "child Sheep");
            Assert.areEqual(context, childSheep.getColor(), DyeColor.ORANGE, "child Sheep");
        });
    }

    @GameTest(structure = "itematic:entity.platform")
    public void breedingRedAndLimeSheepResultsInEitherColorSheep(GameTestHelper context) {
        Sheep firstSheep = context.spawn(EntityType.SHEEP, SPAWN_POSITION);
        firstSheep.setColor(DyeColor.RED);
        Sheep secondSheep = context.spawn(EntityType.SHEEP, SPAWN_POSITION);
        secondSheep.setColor(DyeColor.LIME);
        Sheep childSheep = firstSheep.getBreedOffspring(context.getLevel(), secondSheep);
        context.succeedIf(() -> {
            Assert.isNotNull(context, childSheep, "child Sheep");
            DyeColor color = childSheep.getColor();
            Assert.isTrue(
                context,
                color == DyeColor.RED || color == DyeColor.LIME,
                () -> "Expected child Sheep to be red or lime, got " + color + " instead"
            );
        });
    }
}
