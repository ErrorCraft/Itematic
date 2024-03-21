package net.errorcraft.itematic.gametest.entity.passive;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

public class SheepEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 2, 1);

    @GameTest(templateName = "itematic:entity.platform")
    @SuppressWarnings("DataFlowIssue")
    public void breedingRedAndYellowSheepResultsInOrangeSheep(TestContext context) {
        SheepEntity firstSheep = context.spawnEntity(EntityType.SHEEP, SPAWN_POSITION);
        firstSheep.setColor(DyeColor.RED);
        SheepEntity secondSheep = context.spawnEntity(EntityType.SHEEP, SPAWN_POSITION);
        secondSheep.setColor(DyeColor.YELLOW);
        SheepEntity childSheep = firstSheep.createChild(context.getWorld(), secondSheep);
        context.addInstantFinalTask(() -> {
            context.assertTrue(childSheep != null, "Expected child sheep to exist");
            DyeColor color = childSheep.getColor();
            context.assertTrue(color == DyeColor.ORANGE, "Expected child sheep to be orange, got " + color + " instead");
        });
    }

    @GameTest(templateName = "itematic:entity.platform")
    @SuppressWarnings("DataFlowIssue")
    public void breedingRedAndLimeSheepResultsInEitherColorSheep(TestContext context) {
        SheepEntity firstSheep = context.spawnEntity(EntityType.SHEEP, SPAWN_POSITION);
        firstSheep.setColor(DyeColor.RED);
        SheepEntity secondSheep = context.spawnEntity(EntityType.SHEEP, SPAWN_POSITION);
        secondSheep.setColor(DyeColor.LIME);
        SheepEntity childSheep = firstSheep.createChild(context.getWorld(), secondSheep);
        context.addInstantFinalTask(() -> {
            context.assertTrue(childSheep != null, "Expected child sheep to exist");
            DyeColor color = childSheep.getColor();
            context.assertTrue(color == DyeColor.RED || color == DyeColor.LIME, "Expected child sheep to be red or lime, got " + color + " instead");
        });
    }
}
