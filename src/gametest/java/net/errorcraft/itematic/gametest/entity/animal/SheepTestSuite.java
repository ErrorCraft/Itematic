package net.errorcraft.itematic.gametest.entity.animal;

import net.errorcraft.itematic.assertion.Assert;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.DyeColor;

public class SheepTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:entity.platform")
    public void breedingRedAndYellowSheepResultsInOrangeSheep(GameTestHelper helper) {
        Sheep firstSheep = helper.spawn(EntityType.SHEEP, SPAWN_POSITION);
        firstSheep.setColor(DyeColor.RED);
        Sheep secondSheep = helper.spawn(EntityType.SHEEP, SPAWN_POSITION);
        secondSheep.setColor(DyeColor.YELLOW);
        Sheep childSheep = firstSheep.getBreedOffspring(helper.getLevel(), secondSheep);
        helper.succeedIf(() -> {
            Assert.isNotNull(helper, childSheep, "child Sheep");
            Assert.areEqual(helper, childSheep.getColor(), DyeColor.ORANGE, "child Sheep");
        });
    }

    @GameTest(structure = "itematic:entity.platform")
    public void breedingRedAndLimeSheepResultsInEitherColorSheep(GameTestHelper helper) {
        Sheep firstSheep = helper.spawn(EntityType.SHEEP, SPAWN_POSITION);
        firstSheep.setColor(DyeColor.RED);
        Sheep secondSheep = helper.spawn(EntityType.SHEEP, SPAWN_POSITION);
        secondSheep.setColor(DyeColor.LIME);
        Sheep childSheep = firstSheep.getBreedOffspring(helper.getLevel(), secondSheep);
        helper.succeedIf(() -> {
            Assert.isNotNull(helper, childSheep, "child Sheep");
            DyeColor color = childSheep.getColor();
            Assert.isTrue(
                helper,
                color == DyeColor.RED || color == DyeColor.LIME,
                () -> "Expected child Sheep to be red or lime, got " + color + " instead"
            );
        });
    }
}
