package net.errorcraft.itematic.gametest.entity.passive;

import net.errorcraft.itematic.assertion.Assert;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.test.TestContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

public class SheepEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:entity.platform")
    public void breedingRedAndYellowSheepResultsInOrangeSheep(TestContext context) {
        SheepEntity firstSheep = context.spawnEntity(EntityType.SHEEP, SPAWN_POSITION);
        firstSheep.setColor(DyeColor.RED);
        SheepEntity secondSheep = context.spawnEntity(EntityType.SHEEP, SPAWN_POSITION);
        secondSheep.setColor(DyeColor.YELLOW);
        SheepEntity childSheep = firstSheep.createChild(context.getWorld(), secondSheep);
        context.addFinalTask(() -> {
            Assert.isNotNull(context, childSheep, "child Sheep");
            Assert.areEqual(context, childSheep.getColor(), DyeColor.ORANGE, "child Sheep");
        });
    }

    @GameTest(structure = "itematic:entity.platform")
    public void breedingRedAndLimeSheepResultsInEitherColorSheep(TestContext context) {
        SheepEntity firstSheep = context.spawnEntity(EntityType.SHEEP, SPAWN_POSITION);
        firstSheep.setColor(DyeColor.RED);
        SheepEntity secondSheep = context.spawnEntity(EntityType.SHEEP, SPAWN_POSITION);
        secondSheep.setColor(DyeColor.LIME);
        SheepEntity childSheep = firstSheep.createChild(context.getWorld(), secondSheep);
        context.addFinalTask(() -> {
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
