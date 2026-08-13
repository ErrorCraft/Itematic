package net.errorcraft.itematic.gametest.entity;

import net.errorcraft.itematic.assertion.Assert;
import net.errorcraft.itematic.references.ItemIds;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.animal.equine.*;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.Ocelot;
import net.minecraft.world.entity.animal.fish.Cod;
import net.minecraft.world.entity.animal.fish.Pufferfish;
import net.minecraft.world.entity.animal.fish.Salmon;
import net.minecraft.world.entity.animal.fish.TropicalFish;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.animal.panda.Panda;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.animal.squid.GlowSquid;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.animal.turtle.Turtle;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.skeleton.Stray;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.monster.zombie.*;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.entity.vehicle.boat.ChestRaft;
import net.minecraft.world.entity.vehicle.boat.Raft;
import net.minecraft.world.item.ItemStack;

public class PickEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnArmadilloGivesArmadilloSpawnEggItemStack(GameTestHelper context) {
        Armadillo armadillo = context.spawn(EntityType.ARMADILLO, SPAWN_POSITION);
        ItemStack stack = armadillo.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.ARMADILLO_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnAllayGivesAllaySpawnEggItemStack(GameTestHelper context) {
        Allay allay = context.spawn(EntityType.ALLAY, SPAWN_POSITION);
        ItemStack stack = allay.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.ALLAY_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnAxolotlGivesAxolotlSpawnEggItemStack(GameTestHelper context) {
        Axolotl axolotl = context.spawn(EntityType.AXOLOTL, SPAWN_POSITION);
        ItemStack stack = axolotl.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.AXOLOTL_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnBatGivesBatSpawnEggItemStack(GameTestHelper context) {
        Bat bat = context.spawn(EntityType.BAT, SPAWN_POSITION);
        ItemStack stack = bat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.BAT_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnBeeGivesBeeSpawnEggItemStack(GameTestHelper context) {
        Bee bee = context.spawn(EntityType.BEE, SPAWN_POSITION);
        ItemStack stack = bee.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.BEE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnBlazeGivesBlazeSpawnEggItemStack(GameTestHelper context) {
        Blaze blaze = context.spawn(EntityType.BLAZE, SPAWN_POSITION);
        ItemStack stack = blaze.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.BLAZE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCatGivesCatSpawnEggItemStack(GameTestHelper context) {
        Cat cat = context.spawn(EntityType.CAT, SPAWN_POSITION);
        ItemStack stack = cat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.CAT_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCamelGivesCamelSpawnEggItemStack(GameTestHelper context) {
        Camel camel = context.spawn(EntityType.CAMEL, SPAWN_POSITION);
        ItemStack stack = camel.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.CAMEL_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCaveSpiderGivesCaveSpiderSpawnEggItemStack(GameTestHelper context) {
        CaveSpider caveSpider = context.spawn(EntityType.CAVE_SPIDER, SPAWN_POSITION);
        ItemStack stack = caveSpider.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.CAVE_SPIDER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnChickenGivesChickenSpawnEggItemStack(GameTestHelper context) {
        Chicken chicken = context.spawn(EntityType.CHICKEN, SPAWN_POSITION);
        ItemStack stack = chicken.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.CHICKEN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCodGivesCodSpawnEggItemStack(GameTestHelper context) {
        Cod cod = context.spawn(EntityType.COD, SPAWN_POSITION);
        ItemStack stack = cod.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.COD_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCowGivesCowSpawnEggItemStack(GameTestHelper context) {
        Cow cow = context.spawn(EntityType.COW, SPAWN_POSITION);
        ItemStack stack = cow.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.COW_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCreeperGivesCreeperSpawnEggItemStack(GameTestHelper context) {
        Creeper creeper = context.spawn(EntityType.CREEPER, SPAWN_POSITION);
        ItemStack stack = creeper.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.CREEPER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnDolphinGivesDolphinSpawnEggItemStack(GameTestHelper context) {
        Dolphin dolphin = context.spawn(EntityType.DOLPHIN, SPAWN_POSITION);
        ItemStack stack = dolphin.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.DOLPHIN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnDonkeyGivesDonkeySpawnEggItemStack(GameTestHelper context) {
        Donkey donkey = context.spawn(EntityType.DONKEY, SPAWN_POSITION);
        ItemStack stack = donkey.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.DONKEY_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnDrownedGivesDrownedSpawnEggItemStack(GameTestHelper context) {
        Drowned drowned = context.spawn(EntityType.DROWNED, SPAWN_POSITION);
        ItemStack stack = drowned.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.DROWNED_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnElderGuardianGivesElderGuardianSpawnEggItemStack(GameTestHelper context) {
        ElderGuardian elderGuardian = context.spawn(EntityType.ELDER_GUARDIAN, SPAWN_POSITION);
        ItemStack stack = elderGuardian.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.ELDER_GUARDIAN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnEnderDragonGivesEnderDragonSpawnEggItemStack(GameTestHelper context) {
        EnderDragon enderDragon = context.spawn(EntityType.ENDER_DRAGON, SPAWN_POSITION);
        ItemStack stack = enderDragon.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.ENDER_DRAGON_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnEndermanGivesEndermanSpawnEggItemStack(GameTestHelper context) {
        EnderMan enderman = context.spawn(EntityType.ENDERMAN, SPAWN_POSITION);
        ItemStack stack = enderman.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.ENDERMAN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnEndermiteGivesEndermiteSpawnEggItemStack(GameTestHelper context) {
        Endermite endermite = context.spawn(EntityType.ENDERMITE, SPAWN_POSITION);
        ItemStack stack = endermite.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.ENDERMITE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnEvokerGivesEvokerSpawnEggItemStack(GameTestHelper context) {
        Evoker evoker = context.spawn(EntityType.EVOKER, SPAWN_POSITION);
        ItemStack stack = evoker.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.EVOKER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnFoxGivesFoxSpawnEggItemStack(GameTestHelper context) {
        Fox fox = context.spawn(EntityType.FOX, SPAWN_POSITION);
        ItemStack stack = fox.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.FOX_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnFrogGivesFrogSpawnEggItemStack(GameTestHelper context) {
        Frog frog = context.spawn(EntityType.FROG, SPAWN_POSITION);
        ItemStack stack = frog.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.FROG_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnGhastGivesGhastSpawnEggItemStack(GameTestHelper context) {
        Ghast ghast = context.spawn(EntityType.GHAST, SPAWN_POSITION);
        ItemStack stack = ghast.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.GHAST_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnGlowSquidGivesGlowSquidSpawnEggItemStack(GameTestHelper context) {
        GlowSquid glowSquid = context.spawn(EntityType.GLOW_SQUID, SPAWN_POSITION);
        ItemStack stack = glowSquid.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.GLOW_SQUID_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnGoatGivesGoatSpawnEggItemStack(GameTestHelper context) {
        Goat goat = context.spawn(EntityType.GOAT, SPAWN_POSITION);
        ItemStack stack = goat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.GOAT_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnGuardianGivesGuardianSpawnEggItemStack(GameTestHelper context) {
        Guardian guardian = context.spawn(EntityType.GUARDIAN, SPAWN_POSITION);
        ItemStack stack = guardian.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.GUARDIAN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnHoglinGivesHoglinSpawnEggItemStack(GameTestHelper context) {
        Hoglin hoglin = context.spawn(EntityType.HOGLIN, SPAWN_POSITION);
        ItemStack stack = hoglin.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.HOGLIN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnHorseGivesHorseSpawnEggItemStack(GameTestHelper context) {
        Horse horse = context.spawn(EntityType.HORSE, SPAWN_POSITION);
        ItemStack stack = horse.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.HORSE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnHuskGivesHuskSpawnEggItemStack(GameTestHelper context) {
        Husk husk = context.spawn(EntityType.HUSK, SPAWN_POSITION);
        ItemStack stack = husk.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.HUSK_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnIronGolemGivesIronGolemSpawnEggItemStack(GameTestHelper context) {
        IronGolem ironGolem = context.spawn(EntityType.IRON_GOLEM, SPAWN_POSITION);
        ItemStack stack = ironGolem.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.IRON_GOLEM_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnLlamaGivesLlamaSpawnEggItemStack(GameTestHelper context) {
        Llama llama = context.spawn(EntityType.LLAMA, SPAWN_POSITION);
        ItemStack stack = llama.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.LLAMA_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnMagmaCubeGivesMagmaCubeSpawnEggItemStack(GameTestHelper context) {
        MagmaCube magmaCube = context.spawn(EntityType.MAGMA_CUBE, SPAWN_POSITION);
        ItemStack stack = magmaCube.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.MAGMA_CUBE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnMooshroomGivesMooshroomSpawnEggItemStack(GameTestHelper context) {
        MushroomCow mooshroom = context.spawn(EntityType.MOOSHROOM, SPAWN_POSITION);
        ItemStack stack = mooshroom.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.MOOSHROOM_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnMuleGivesMuleSpawnEggItemStack(GameTestHelper context) {
        Mule mule = context.spawn(EntityType.MULE, SPAWN_POSITION);
        ItemStack stack = mule.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.MULE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnOcelotGivesOcelotSpawnEggItemStack(GameTestHelper context) {
        Ocelot ocelot = context.spawn(EntityType.OCELOT, SPAWN_POSITION);
        ItemStack stack = ocelot.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.OCELOT_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPandaGivesPandaSpawnEggItemStack(GameTestHelper context) {
        Panda panda = context.spawn(EntityType.PANDA, SPAWN_POSITION);
        ItemStack stack = panda.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.PANDA_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnParrotGivesParrotSpawnEggItemStack(GameTestHelper context) {
        Parrot parrot = context.spawn(EntityType.PARROT, SPAWN_POSITION);
        ItemStack stack = parrot.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.PARROT_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPhantomGivesPhantomSpawnEggItemStack(GameTestHelper context) {
        Phantom phantom = context.spawn(EntityType.PHANTOM, SPAWN_POSITION);
        ItemStack stack = phantom.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.PHANTOM_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPigGivesPigSpawnEggItemStack(GameTestHelper context) {
        Pig pig = context.spawn(EntityType.PIG, SPAWN_POSITION);
        ItemStack stack = pig.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.PIG_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPiglinGivesPiglinSpawnEggItemStack(GameTestHelper context) {
        Piglin piglin = context.spawn(EntityType.PIGLIN, SPAWN_POSITION);
        ItemStack stack = piglin.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.PIGLIN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPiglinBruteGivesPiglinBruteSpawnEggItemStack(GameTestHelper context) {
        PiglinBrute piglinBrute = context.spawn(EntityType.PIGLIN_BRUTE, SPAWN_POSITION);
        ItemStack stack = piglinBrute.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.PIGLIN_BRUTE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPillagerGivesPillagerSpawnEggItemStack(GameTestHelper context) {
        Pillager pillager = context.spawn(EntityType.PILLAGER, SPAWN_POSITION);
        ItemStack stack = pillager.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.PILLAGER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPolarBearGivesPolarBearSpawnEggItemStack(GameTestHelper context) {
        PolarBear polarBear = context.spawn(EntityType.POLAR_BEAR, SPAWN_POSITION);
        ItemStack stack = polarBear.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.POLAR_BEAR_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPufferfishGivesPufferfishSpawnEggItemStack(GameTestHelper context) {
        Pufferfish pufferfish = context.spawn(EntityType.PUFFERFISH, SPAWN_POSITION);
        ItemStack stack = pufferfish.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.PUFFERFISH_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnRabbitGivesRabbitSpawnEggItemStack(GameTestHelper context) {
        Rabbit rabbit = context.spawn(EntityType.RABBIT, SPAWN_POSITION);
        ItemStack stack = rabbit.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.RABBIT_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnRavagerGivesRavagerSpawnEggItemStack(GameTestHelper context) {
        Ravager ravager = context.spawn(EntityType.RAVAGER, SPAWN_POSITION);
        ItemStack stack = ravager.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.RAVAGER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSalmonGivesSalmonSpawnEggItemStack(GameTestHelper context) {
        Salmon salmon = context.spawn(EntityType.SALMON, SPAWN_POSITION);
        ItemStack stack = salmon.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.SALMON_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSheepGivesSheepSpawnEggItemStack(GameTestHelper context) {
        Sheep sheep = context.spawn(EntityType.SHEEP, SPAWN_POSITION);
        ItemStack stack = sheep.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.SHEEP_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnShulkerGivesShulkerSpawnEggItemStack(GameTestHelper context) {
        Shulker shulker = context.spawn(EntityType.SHULKER, SPAWN_POSITION);
        ItemStack stack = shulker.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.SHULKER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSilverfishGivesSilverfishSpawnEggItemStack(GameTestHelper context) {
        Silverfish silverfish = context.spawn(EntityType.SILVERFISH, SPAWN_POSITION);
        ItemStack stack = silverfish.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.SILVERFISH_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSkeletonGivesSkeletonSpawnEggItemStack(GameTestHelper context) {
        Skeleton skeleton = context.spawn(EntityType.SKELETON, SPAWN_POSITION);
        ItemStack stack = skeleton.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.SKELETON_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSkeletonHorseGivesSkeletonHorseSpawnEggItemStack(GameTestHelper context) {
        SkeletonHorse skeletonHorse = context.spawn(EntityType.SKELETON_HORSE, SPAWN_POSITION);
        ItemStack stack = skeletonHorse.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.SKELETON_HORSE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSlimeGivesSlimeSpawnEggItemStack(GameTestHelper context) {
        Slime slime = context.spawn(EntityType.SLIME, SPAWN_POSITION);
        ItemStack stack = slime.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.SLIME_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSnifferGivesSnifferSpawnEggItemStack(GameTestHelper context) {
        Sniffer sniffer = context.spawn(EntityType.SNIFFER, SPAWN_POSITION);
        ItemStack stack = sniffer.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.SNIFFER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSnowGolemGivesSnowGolemSpawnEggItemStack(GameTestHelper context) {
        SnowGolem snowGolem = context.spawn(EntityType.SNOW_GOLEM, SPAWN_POSITION);
        ItemStack stack = snowGolem.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.SNOW_GOLEM_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSpiderGivesSpiderSpawnEggItemStack(GameTestHelper context) {
        Spider spider = context.spawn(EntityType.SPIDER, SPAWN_POSITION);
        ItemStack stack = spider.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.SPIDER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSquidGivesSquidSpawnEggItemStack(GameTestHelper context) {
        Squid squid = context.spawn(EntityType.SQUID, SPAWN_POSITION);
        ItemStack stack = squid.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.SQUID_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnStrayGivesStraySpawnEggItemStack(GameTestHelper context) {
        Stray stray = context.spawn(EntityType.STRAY, SPAWN_POSITION);
        ItemStack stack = stray.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.STRAY_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnStriderGivesStriderSpawnEggItemStack(GameTestHelper context) {
        Strider strider = context.spawn(EntityType.STRIDER, SPAWN_POSITION);
        ItemStack stack = strider.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.STRIDER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnTadpoleGivesTadpoleSpawnEggItemStack(GameTestHelper context) {
        Tadpole tadpole = context.spawn(EntityType.TADPOLE, SPAWN_POSITION);
        ItemStack stack = tadpole.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.TADPOLE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnTraderLlamaGivesTraderLlamaSpawnEggItemStack(GameTestHelper context) {
        TraderLlama traderLlama = context.spawn(EntityType.TRADER_LLAMA, SPAWN_POSITION);
        ItemStack stack = traderLlama.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.TRADER_LLAMA_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnTropicalFishGivesTropicalFishSpawnEggItemStack(GameTestHelper context) {
        TropicalFish tropicalFish = context.spawn(EntityType.TROPICAL_FISH, SPAWN_POSITION);
        ItemStack stack = tropicalFish.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.TROPICAL_FISH_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnTurtleGivesTurtleSpawnEggItemStack(GameTestHelper context) {
        Turtle turtle = context.spawn(EntityType.TURTLE, SPAWN_POSITION);
        ItemStack stack = turtle.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.TURTLE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnVexGivesVexSpawnEggItemStack(GameTestHelper context) {
        Vex vex = context.spawn(EntityType.VEX, SPAWN_POSITION);
        ItemStack stack = vex.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.VEX_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnVillagerGivesVillagerSpawnEggItemStack(GameTestHelper context) {
        Villager villager = context.spawn(EntityType.VILLAGER, SPAWN_POSITION);
        ItemStack stack = villager.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.VILLAGER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnVindicatorGivesVindicatorSpawnEggItemStack(GameTestHelper context) {
        Vindicator vindicator = context.spawn(EntityType.VINDICATOR, SPAWN_POSITION);
        ItemStack stack = vindicator.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.VINDICATOR_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnWanderingTraderGivesWanderingTraderSpawnEggItemStack(GameTestHelper context) {
        WanderingTrader wanderingTrader = context.spawn(EntityType.WANDERING_TRADER, SPAWN_POSITION);
        ItemStack stack = wanderingTrader.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.WANDERING_TRADER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnWardenGivesWardenSpawnEggItemStack(GameTestHelper context) {
        Warden warden = context.spawn(EntityType.WARDEN, SPAWN_POSITION);
        ItemStack stack = warden.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.WARDEN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnWitchGivesWitchSpawnEggItemStack(GameTestHelper context) {
        Witch witch = context.spawn(EntityType.WITCH, SPAWN_POSITION);
        ItemStack stack = witch.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.WITCH_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnWitherGivesWitherSpawnEggItemStack(GameTestHelper context) {
        WitherBoss wither = context.spawn(EntityType.WITHER, SPAWN_POSITION);
        ItemStack stack = wither.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.WITHER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnWitherSkeletonGivesWitherSkeletonSpawnEggItemStack(GameTestHelper context) {
        WitherSkeleton witherSkeleton = context.spawn(EntityType.WITHER_SKELETON, SPAWN_POSITION);
        ItemStack stack = witherSkeleton.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.WITHER_SKELETON_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnWolfGivesWolfSpawnEggItemStack(GameTestHelper context) {
        Wolf wolf = context.spawn(EntityType.WOLF, SPAWN_POSITION);
        ItemStack stack = wolf.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.WOLF_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnZoglinGivesZoglinSpawnEggItemStack(GameTestHelper context) {
        Zoglin zoglin = context.spawn(EntityType.ZOGLIN, SPAWN_POSITION);
        ItemStack stack = zoglin.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.ZOGLIN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnZombieGivesZombieSpawnEggItemStack(GameTestHelper context) {
        Zombie zombie = context.spawn(EntityType.ZOMBIE, SPAWN_POSITION);
        ItemStack stack = zombie.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.ZOMBIE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnZombieHorseGivesZombieHorseSpawnEggItemStack(GameTestHelper context) {
        ZombieHorse zombieHorse = context.spawn(EntityType.ZOMBIE_HORSE, SPAWN_POSITION);
        ItemStack stack = zombieHorse.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.ZOMBIE_HORSE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnZombieVillagerGivesZombieVillagerSpawnEggItemStack(GameTestHelper context) {
        ZombieVillager zombieVillager = context.spawn(EntityType.ZOMBIE_VILLAGER, SPAWN_POSITION);
        ItemStack stack = zombieVillager.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.ZOMBIE_VILLAGER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnZombifiedPiglinGivesZombifiedPiglinSpawnEggItemStack(GameTestHelper context) {
        ZombifiedPiglin zombifiedPiglin = context.spawn(EntityType.ZOMBIFIED_PIGLIN, SPAWN_POSITION);
        ItemStack stack = zombifiedPiglin.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.ZOMBIFIED_PIGLIN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnAcaciaBoatGivesAcaciaBoatItemStack(GameTestHelper context) {
        Boat acaciaBoat = context.spawn(EntityType.ACACIA_BOAT, SPAWN_POSITION);
        ItemStack stack = acaciaBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.ACACIA_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnAcaciaChestBoatGivesAcaciaChestBoatItemStack(GameTestHelper context) {
        ChestBoat acaciaChestBoat = context.spawn(EntityType.ACACIA_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = acaciaChestBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.ACACIA_CHEST_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnBambooRaftGivesBambooRaftItemStack(GameTestHelper context) {
        Raft bambooRaft = context.spawn(EntityType.BAMBOO_RAFT, SPAWN_POSITION);
        ItemStack stack = bambooRaft.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.BAMBOO_RAFT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnBambooChestRaftGivesBambooChestRaftItemStack(GameTestHelper context) {
        ChestRaft bambooChestRaft = context.spawn(EntityType.BAMBOO_CHEST_RAFT, SPAWN_POSITION);
        ItemStack stack = bambooChestRaft.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.BAMBOO_CHEST_RAFT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnBirchBoatGivesBirchBoatItemStack(GameTestHelper context) {
        Boat birchBoat = context.spawn(EntityType.BIRCH_BOAT, SPAWN_POSITION);
        ItemStack stack = birchBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.BIRCH_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnBirchChestBoatGivesBirchChestBoatItemStack(GameTestHelper context) {
        ChestBoat birchChestBoat = context.spawn(EntityType.BIRCH_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = birchChestBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.BIRCH_CHEST_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCherryBoatGivesCherryBoatItemStack(GameTestHelper context) {
        Boat cherryBoat = context.spawn(EntityType.CHERRY_BOAT, SPAWN_POSITION);
        ItemStack stack = cherryBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.CHERRY_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCherryChestBoatGivesCherryChestBoatItemStack(GameTestHelper context) {
        ChestBoat cherryChestBoat = context.spawn(EntityType.CHERRY_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = cherryChestBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.CHERRY_CHEST_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnDarkOakBoatGivesDarkOakBoatItemStack(GameTestHelper context) {
        Boat darkOakBoat = context.spawn(EntityType.DARK_OAK_BOAT, SPAWN_POSITION);
        ItemStack stack = darkOakBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.DARK_OAK_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnDarkOakChestBoatGivesDarkOakChestBoatItemStack(GameTestHelper context) {
        ChestBoat darkOakChestBoat = context.spawn(EntityType.DARK_OAK_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = darkOakChestBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.DARK_OAK_CHEST_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnJungleBoatGivesJungleBoatItemStack(GameTestHelper context) {
        Boat jungleBoat = context.spawn(EntityType.JUNGLE_BOAT, SPAWN_POSITION);
        ItemStack stack = jungleBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.JUNGLE_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnJungleChestBoatGivesJungleChestBoatItemStack(GameTestHelper context) {
        ChestBoat jungleChestBoat = context.spawn(EntityType.JUNGLE_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = jungleChestBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.JUNGLE_CHEST_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnMangroveBoatGivesMangroveBoatItemStack(GameTestHelper context) {
        Boat mangroveBoat = context.spawn(EntityType.MANGROVE_BOAT, SPAWN_POSITION);
        ItemStack stack = mangroveBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.MANGROVE_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnMangroveChestBoatGivesMangroveChestBoatItemStack(GameTestHelper context) {
        ChestBoat mangroveChestBoat = context.spawn(EntityType.MANGROVE_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = mangroveChestBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.MANGROVE_CHEST_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnOakBoatGivesOakBoatItemStack(GameTestHelper context) {
        Boat oakBoat = context.spawn(EntityType.OAK_BOAT, SPAWN_POSITION);
        ItemStack stack = oakBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.OAK_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnOakChestBoatGivesOakChestBoatItemStack(GameTestHelper context) {
        ChestBoat oakChestBoat = context.spawn(EntityType.OAK_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = oakChestBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.OAK_CHEST_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSpruceBoatGivesSpruceBoatItemStack(GameTestHelper context) {
        Boat spruceBoat = context.spawn(EntityType.SPRUCE_BOAT, SPAWN_POSITION);
        ItemStack stack = spruceBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.SPRUCE_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSpruceChestBoatGivesSpruceChestBoatItemStack(GameTestHelper context) {
        ChestBoat spruceChestBoat = context.spawn(EntityType.SPRUCE_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = spruceChestBoat.getPickResult();
        context.succeedIf(() -> Assert.itemStack(context, stack)
            .is(ItemIds.SPRUCE_CHEST_BOAT)
        );
    }
}
