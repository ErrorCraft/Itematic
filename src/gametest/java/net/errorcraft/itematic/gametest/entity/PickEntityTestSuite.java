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
    public void getPickStackOnArmadilloGivesArmadilloSpawnEggItemStack(GameTestHelper helper) {
        Armadillo armadillo = helper.spawn(EntityType.ARMADILLO, SPAWN_POSITION);
        ItemStack stack = armadillo.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.ARMADILLO_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnAllayGivesAllaySpawnEggItemStack(GameTestHelper helper) {
        Allay allay = helper.spawn(EntityType.ALLAY, SPAWN_POSITION);
        ItemStack stack = allay.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.ALLAY_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnAxolotlGivesAxolotlSpawnEggItemStack(GameTestHelper helper) {
        Axolotl axolotl = helper.spawn(EntityType.AXOLOTL, SPAWN_POSITION);
        ItemStack stack = axolotl.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.AXOLOTL_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnBatGivesBatSpawnEggItemStack(GameTestHelper helper) {
        Bat bat = helper.spawn(EntityType.BAT, SPAWN_POSITION);
        ItemStack stack = bat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.BAT_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnBeeGivesBeeSpawnEggItemStack(GameTestHelper helper) {
        Bee bee = helper.spawn(EntityType.BEE, SPAWN_POSITION);
        ItemStack stack = bee.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.BEE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnBlazeGivesBlazeSpawnEggItemStack(GameTestHelper helper) {
        Blaze blaze = helper.spawn(EntityType.BLAZE, SPAWN_POSITION);
        ItemStack stack = blaze.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.BLAZE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCatGivesCatSpawnEggItemStack(GameTestHelper helper) {
        Cat cat = helper.spawn(EntityType.CAT, SPAWN_POSITION);
        ItemStack stack = cat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.CAT_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCamelGivesCamelSpawnEggItemStack(GameTestHelper helper) {
        Camel camel = helper.spawn(EntityType.CAMEL, SPAWN_POSITION);
        ItemStack stack = camel.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.CAMEL_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCaveSpiderGivesCaveSpiderSpawnEggItemStack(GameTestHelper helper) {
        CaveSpider caveSpider = helper.spawn(EntityType.CAVE_SPIDER, SPAWN_POSITION);
        ItemStack stack = caveSpider.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.CAVE_SPIDER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnChickenGivesChickenSpawnEggItemStack(GameTestHelper helper) {
        Chicken chicken = helper.spawn(EntityType.CHICKEN, SPAWN_POSITION);
        ItemStack stack = chicken.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.CHICKEN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCodGivesCodSpawnEggItemStack(GameTestHelper helper) {
        Cod cod = helper.spawn(EntityType.COD, SPAWN_POSITION);
        ItemStack stack = cod.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.COD_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCowGivesCowSpawnEggItemStack(GameTestHelper helper) {
        Cow cow = helper.spawn(EntityType.COW, SPAWN_POSITION);
        ItemStack stack = cow.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.COW_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCreeperGivesCreeperSpawnEggItemStack(GameTestHelper helper) {
        Creeper creeper = helper.spawn(EntityType.CREEPER, SPAWN_POSITION);
        ItemStack stack = creeper.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.CREEPER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnDolphinGivesDolphinSpawnEggItemStack(GameTestHelper helper) {
        Dolphin dolphin = helper.spawn(EntityType.DOLPHIN, SPAWN_POSITION);
        ItemStack stack = dolphin.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.DOLPHIN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnDonkeyGivesDonkeySpawnEggItemStack(GameTestHelper helper) {
        Donkey donkey = helper.spawn(EntityType.DONKEY, SPAWN_POSITION);
        ItemStack stack = donkey.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.DONKEY_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnDrownedGivesDrownedSpawnEggItemStack(GameTestHelper helper) {
        Drowned drowned = helper.spawn(EntityType.DROWNED, SPAWN_POSITION);
        ItemStack stack = drowned.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.DROWNED_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnElderGuardianGivesElderGuardianSpawnEggItemStack(GameTestHelper helper) {
        ElderGuardian elderGuardian = helper.spawn(EntityType.ELDER_GUARDIAN, SPAWN_POSITION);
        ItemStack stack = elderGuardian.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.ELDER_GUARDIAN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnEnderDragonGivesEnderDragonSpawnEggItemStack(GameTestHelper helper) {
        EnderDragon enderDragon = helper.spawn(EntityType.ENDER_DRAGON, SPAWN_POSITION);
        ItemStack stack = enderDragon.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.ENDER_DRAGON_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnEndermanGivesEndermanSpawnEggItemStack(GameTestHelper helper) {
        EnderMan enderman = helper.spawn(EntityType.ENDERMAN, SPAWN_POSITION);
        ItemStack stack = enderman.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.ENDERMAN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnEndermiteGivesEndermiteSpawnEggItemStack(GameTestHelper helper) {
        Endermite endermite = helper.spawn(EntityType.ENDERMITE, SPAWN_POSITION);
        ItemStack stack = endermite.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.ENDERMITE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnEvokerGivesEvokerSpawnEggItemStack(GameTestHelper helper) {
        Evoker evoker = helper.spawn(EntityType.EVOKER, SPAWN_POSITION);
        ItemStack stack = evoker.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.EVOKER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnFoxGivesFoxSpawnEggItemStack(GameTestHelper helper) {
        Fox fox = helper.spawn(EntityType.FOX, SPAWN_POSITION);
        ItemStack stack = fox.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.FOX_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnFrogGivesFrogSpawnEggItemStack(GameTestHelper helper) {
        Frog frog = helper.spawn(EntityType.FROG, SPAWN_POSITION);
        ItemStack stack = frog.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.FROG_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnGhastGivesGhastSpawnEggItemStack(GameTestHelper helper) {
        Ghast ghast = helper.spawn(EntityType.GHAST, SPAWN_POSITION);
        ItemStack stack = ghast.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.GHAST_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnGlowSquidGivesGlowSquidSpawnEggItemStack(GameTestHelper helper) {
        GlowSquid glowSquid = helper.spawn(EntityType.GLOW_SQUID, SPAWN_POSITION);
        ItemStack stack = glowSquid.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.GLOW_SQUID_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnGoatGivesGoatSpawnEggItemStack(GameTestHelper helper) {
        Goat goat = helper.spawn(EntityType.GOAT, SPAWN_POSITION);
        ItemStack stack = goat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.GOAT_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnGuardianGivesGuardianSpawnEggItemStack(GameTestHelper helper) {
        Guardian guardian = helper.spawn(EntityType.GUARDIAN, SPAWN_POSITION);
        ItemStack stack = guardian.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.GUARDIAN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnHoglinGivesHoglinSpawnEggItemStack(GameTestHelper helper) {
        Hoglin hoglin = helper.spawn(EntityType.HOGLIN, SPAWN_POSITION);
        ItemStack stack = hoglin.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.HOGLIN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnHorseGivesHorseSpawnEggItemStack(GameTestHelper helper) {
        Horse horse = helper.spawn(EntityType.HORSE, SPAWN_POSITION);
        ItemStack stack = horse.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.HORSE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnHuskGivesHuskSpawnEggItemStack(GameTestHelper helper) {
        Husk husk = helper.spawn(EntityType.HUSK, SPAWN_POSITION);
        ItemStack stack = husk.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.HUSK_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnIronGolemGivesIronGolemSpawnEggItemStack(GameTestHelper helper) {
        IronGolem ironGolem = helper.spawn(EntityType.IRON_GOLEM, SPAWN_POSITION);
        ItemStack stack = ironGolem.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.IRON_GOLEM_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnLlamaGivesLlamaSpawnEggItemStack(GameTestHelper helper) {
        Llama llama = helper.spawn(EntityType.LLAMA, SPAWN_POSITION);
        ItemStack stack = llama.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.LLAMA_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnMagmaCubeGivesMagmaCubeSpawnEggItemStack(GameTestHelper helper) {
        MagmaCube magmaCube = helper.spawn(EntityType.MAGMA_CUBE, SPAWN_POSITION);
        ItemStack stack = magmaCube.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.MAGMA_CUBE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnMooshroomGivesMooshroomSpawnEggItemStack(GameTestHelper helper) {
        MushroomCow mooshroom = helper.spawn(EntityType.MOOSHROOM, SPAWN_POSITION);
        ItemStack stack = mooshroom.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.MOOSHROOM_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnMuleGivesMuleSpawnEggItemStack(GameTestHelper helper) {
        Mule mule = helper.spawn(EntityType.MULE, SPAWN_POSITION);
        ItemStack stack = mule.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.MULE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnOcelotGivesOcelotSpawnEggItemStack(GameTestHelper helper) {
        Ocelot ocelot = helper.spawn(EntityType.OCELOT, SPAWN_POSITION);
        ItemStack stack = ocelot.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.OCELOT_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPandaGivesPandaSpawnEggItemStack(GameTestHelper helper) {
        Panda panda = helper.spawn(EntityType.PANDA, SPAWN_POSITION);
        ItemStack stack = panda.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.PANDA_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnParrotGivesParrotSpawnEggItemStack(GameTestHelper helper) {
        Parrot parrot = helper.spawn(EntityType.PARROT, SPAWN_POSITION);
        ItemStack stack = parrot.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.PARROT_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPhantomGivesPhantomSpawnEggItemStack(GameTestHelper helper) {
        Phantom phantom = helper.spawn(EntityType.PHANTOM, SPAWN_POSITION);
        ItemStack stack = phantom.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.PHANTOM_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPigGivesPigSpawnEggItemStack(GameTestHelper helper) {
        Pig pig = helper.spawn(EntityType.PIG, SPAWN_POSITION);
        ItemStack stack = pig.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.PIG_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPiglinGivesPiglinSpawnEggItemStack(GameTestHelper helper) {
        Piglin piglin = helper.spawn(EntityType.PIGLIN, SPAWN_POSITION);
        ItemStack stack = piglin.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.PIGLIN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPiglinBruteGivesPiglinBruteSpawnEggItemStack(GameTestHelper helper) {
        PiglinBrute piglinBrute = helper.spawn(EntityType.PIGLIN_BRUTE, SPAWN_POSITION);
        ItemStack stack = piglinBrute.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.PIGLIN_BRUTE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPillagerGivesPillagerSpawnEggItemStack(GameTestHelper helper) {
        Pillager pillager = helper.spawn(EntityType.PILLAGER, SPAWN_POSITION);
        ItemStack stack = pillager.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.PILLAGER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPolarBearGivesPolarBearSpawnEggItemStack(GameTestHelper helper) {
        PolarBear polarBear = helper.spawn(EntityType.POLAR_BEAR, SPAWN_POSITION);
        ItemStack stack = polarBear.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.POLAR_BEAR_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnPufferfishGivesPufferfishSpawnEggItemStack(GameTestHelper helper) {
        Pufferfish pufferfish = helper.spawn(EntityType.PUFFERFISH, SPAWN_POSITION);
        ItemStack stack = pufferfish.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.PUFFERFISH_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnRabbitGivesRabbitSpawnEggItemStack(GameTestHelper helper) {
        Rabbit rabbit = helper.spawn(EntityType.RABBIT, SPAWN_POSITION);
        ItemStack stack = rabbit.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.RABBIT_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnRavagerGivesRavagerSpawnEggItemStack(GameTestHelper helper) {
        Ravager ravager = helper.spawn(EntityType.RAVAGER, SPAWN_POSITION);
        ItemStack stack = ravager.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.RAVAGER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSalmonGivesSalmonSpawnEggItemStack(GameTestHelper helper) {
        Salmon salmon = helper.spawn(EntityType.SALMON, SPAWN_POSITION);
        ItemStack stack = salmon.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.SALMON_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSheepGivesSheepSpawnEggItemStack(GameTestHelper helper) {
        Sheep sheep = helper.spawn(EntityType.SHEEP, SPAWN_POSITION);
        ItemStack stack = sheep.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.SHEEP_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnShulkerGivesShulkerSpawnEggItemStack(GameTestHelper helper) {
        Shulker shulker = helper.spawn(EntityType.SHULKER, SPAWN_POSITION);
        ItemStack stack = shulker.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.SHULKER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSilverfishGivesSilverfishSpawnEggItemStack(GameTestHelper helper) {
        Silverfish silverfish = helper.spawn(EntityType.SILVERFISH, SPAWN_POSITION);
        ItemStack stack = silverfish.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.SILVERFISH_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSkeletonGivesSkeletonSpawnEggItemStack(GameTestHelper helper) {
        Skeleton skeleton = helper.spawn(EntityType.SKELETON, SPAWN_POSITION);
        ItemStack stack = skeleton.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.SKELETON_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSkeletonHorseGivesSkeletonHorseSpawnEggItemStack(GameTestHelper helper) {
        SkeletonHorse skeletonHorse = helper.spawn(EntityType.SKELETON_HORSE, SPAWN_POSITION);
        ItemStack stack = skeletonHorse.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.SKELETON_HORSE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSlimeGivesSlimeSpawnEggItemStack(GameTestHelper helper) {
        Slime slime = helper.spawn(EntityType.SLIME, SPAWN_POSITION);
        ItemStack stack = slime.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.SLIME_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSnifferGivesSnifferSpawnEggItemStack(GameTestHelper helper) {
        Sniffer sniffer = helper.spawn(EntityType.SNIFFER, SPAWN_POSITION);
        ItemStack stack = sniffer.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.SNIFFER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSnowGolemGivesSnowGolemSpawnEggItemStack(GameTestHelper helper) {
        SnowGolem snowGolem = helper.spawn(EntityType.SNOW_GOLEM, SPAWN_POSITION);
        ItemStack stack = snowGolem.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.SNOW_GOLEM_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSpiderGivesSpiderSpawnEggItemStack(GameTestHelper helper) {
        Spider spider = helper.spawn(EntityType.SPIDER, SPAWN_POSITION);
        ItemStack stack = spider.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.SPIDER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSquidGivesSquidSpawnEggItemStack(GameTestHelper helper) {
        Squid squid = helper.spawn(EntityType.SQUID, SPAWN_POSITION);
        ItemStack stack = squid.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.SQUID_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnStrayGivesStraySpawnEggItemStack(GameTestHelper helper) {
        Stray stray = helper.spawn(EntityType.STRAY, SPAWN_POSITION);
        ItemStack stack = stray.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.STRAY_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnStriderGivesStriderSpawnEggItemStack(GameTestHelper helper) {
        Strider strider = helper.spawn(EntityType.STRIDER, SPAWN_POSITION);
        ItemStack stack = strider.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.STRIDER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnTadpoleGivesTadpoleSpawnEggItemStack(GameTestHelper helper) {
        Tadpole tadpole = helper.spawn(EntityType.TADPOLE, SPAWN_POSITION);
        ItemStack stack = tadpole.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.TADPOLE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnTraderLlamaGivesTraderLlamaSpawnEggItemStack(GameTestHelper helper) {
        TraderLlama traderLlama = helper.spawn(EntityType.TRADER_LLAMA, SPAWN_POSITION);
        ItemStack stack = traderLlama.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.TRADER_LLAMA_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnTropicalFishGivesTropicalFishSpawnEggItemStack(GameTestHelper helper) {
        TropicalFish tropicalFish = helper.spawn(EntityType.TROPICAL_FISH, SPAWN_POSITION);
        ItemStack stack = tropicalFish.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.TROPICAL_FISH_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnTurtleGivesTurtleSpawnEggItemStack(GameTestHelper helper) {
        Turtle turtle = helper.spawn(EntityType.TURTLE, SPAWN_POSITION);
        ItemStack stack = turtle.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.TURTLE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnVexGivesVexSpawnEggItemStack(GameTestHelper helper) {
        Vex vex = helper.spawn(EntityType.VEX, SPAWN_POSITION);
        ItemStack stack = vex.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.VEX_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnVillagerGivesVillagerSpawnEggItemStack(GameTestHelper helper) {
        Villager villager = helper.spawn(EntityType.VILLAGER, SPAWN_POSITION);
        ItemStack stack = villager.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.VILLAGER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnVindicatorGivesVindicatorSpawnEggItemStack(GameTestHelper helper) {
        Vindicator vindicator = helper.spawn(EntityType.VINDICATOR, SPAWN_POSITION);
        ItemStack stack = vindicator.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.VINDICATOR_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnWanderingTraderGivesWanderingTraderSpawnEggItemStack(GameTestHelper helper) {
        WanderingTrader wanderingTrader = helper.spawn(EntityType.WANDERING_TRADER, SPAWN_POSITION);
        ItemStack stack = wanderingTrader.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.WANDERING_TRADER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnWardenGivesWardenSpawnEggItemStack(GameTestHelper helper) {
        Warden warden = helper.spawn(EntityType.WARDEN, SPAWN_POSITION);
        ItemStack stack = warden.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.WARDEN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnWitchGivesWitchSpawnEggItemStack(GameTestHelper helper) {
        Witch witch = helper.spawn(EntityType.WITCH, SPAWN_POSITION);
        ItemStack stack = witch.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.WITCH_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnWitherGivesWitherSpawnEggItemStack(GameTestHelper helper) {
        WitherBoss wither = helper.spawn(EntityType.WITHER, SPAWN_POSITION);
        ItemStack stack = wither.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.WITHER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnWitherSkeletonGivesWitherSkeletonSpawnEggItemStack(GameTestHelper helper) {
        WitherSkeleton witherSkeleton = helper.spawn(EntityType.WITHER_SKELETON, SPAWN_POSITION);
        ItemStack stack = witherSkeleton.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.WITHER_SKELETON_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnWolfGivesWolfSpawnEggItemStack(GameTestHelper helper) {
        Wolf wolf = helper.spawn(EntityType.WOLF, SPAWN_POSITION);
        ItemStack stack = wolf.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.WOLF_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnZoglinGivesZoglinSpawnEggItemStack(GameTestHelper helper) {
        Zoglin zoglin = helper.spawn(EntityType.ZOGLIN, SPAWN_POSITION);
        ItemStack stack = zoglin.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.ZOGLIN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnZombieGivesZombieSpawnEggItemStack(GameTestHelper helper) {
        Zombie zombie = helper.spawn(EntityType.ZOMBIE, SPAWN_POSITION);
        ItemStack stack = zombie.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.ZOMBIE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnZombieHorseGivesZombieHorseSpawnEggItemStack(GameTestHelper helper) {
        ZombieHorse zombieHorse = helper.spawn(EntityType.ZOMBIE_HORSE, SPAWN_POSITION);
        ItemStack stack = zombieHorse.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.ZOMBIE_HORSE_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnZombieVillagerGivesZombieVillagerSpawnEggItemStack(GameTestHelper helper) {
        ZombieVillager zombieVillager = helper.spawn(EntityType.ZOMBIE_VILLAGER, SPAWN_POSITION);
        ItemStack stack = zombieVillager.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.ZOMBIE_VILLAGER_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnZombifiedPiglinGivesZombifiedPiglinSpawnEggItemStack(GameTestHelper helper) {
        ZombifiedPiglin zombifiedPiglin = helper.spawn(EntityType.ZOMBIFIED_PIGLIN, SPAWN_POSITION);
        ItemStack stack = zombifiedPiglin.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.ZOMBIFIED_PIGLIN_SPAWN_EGG)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnAcaciaBoatGivesAcaciaBoatItemStack(GameTestHelper helper) {
        Boat acaciaBoat = helper.spawn(EntityType.ACACIA_BOAT, SPAWN_POSITION);
        ItemStack stack = acaciaBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.ACACIA_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnAcaciaChestBoatGivesAcaciaChestBoatItemStack(GameTestHelper helper) {
        ChestBoat acaciaChestBoat = helper.spawn(EntityType.ACACIA_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = acaciaChestBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.ACACIA_CHEST_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnBambooRaftGivesBambooRaftItemStack(GameTestHelper helper) {
        Raft bambooRaft = helper.spawn(EntityType.BAMBOO_RAFT, SPAWN_POSITION);
        ItemStack stack = bambooRaft.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.BAMBOO_RAFT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnBambooChestRaftGivesBambooChestRaftItemStack(GameTestHelper helper) {
        ChestRaft bambooChestRaft = helper.spawn(EntityType.BAMBOO_CHEST_RAFT, SPAWN_POSITION);
        ItemStack stack = bambooChestRaft.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.BAMBOO_CHEST_RAFT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnBirchBoatGivesBirchBoatItemStack(GameTestHelper helper) {
        Boat birchBoat = helper.spawn(EntityType.BIRCH_BOAT, SPAWN_POSITION);
        ItemStack stack = birchBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.BIRCH_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnBirchChestBoatGivesBirchChestBoatItemStack(GameTestHelper helper) {
        ChestBoat birchChestBoat = helper.spawn(EntityType.BIRCH_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = birchChestBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.BIRCH_CHEST_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCherryBoatGivesCherryBoatItemStack(GameTestHelper helper) {
        Boat cherryBoat = helper.spawn(EntityType.CHERRY_BOAT, SPAWN_POSITION);
        ItemStack stack = cherryBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.CHERRY_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnCherryChestBoatGivesCherryChestBoatItemStack(GameTestHelper helper) {
        ChestBoat cherryChestBoat = helper.spawn(EntityType.CHERRY_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = cherryChestBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.CHERRY_CHEST_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnDarkOakBoatGivesDarkOakBoatItemStack(GameTestHelper helper) {
        Boat darkOakBoat = helper.spawn(EntityType.DARK_OAK_BOAT, SPAWN_POSITION);
        ItemStack stack = darkOakBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.DARK_OAK_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnDarkOakChestBoatGivesDarkOakChestBoatItemStack(GameTestHelper helper) {
        ChestBoat darkOakChestBoat = helper.spawn(EntityType.DARK_OAK_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = darkOakChestBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.DARK_OAK_CHEST_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnJungleBoatGivesJungleBoatItemStack(GameTestHelper helper) {
        Boat jungleBoat = helper.spawn(EntityType.JUNGLE_BOAT, SPAWN_POSITION);
        ItemStack stack = jungleBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.JUNGLE_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnJungleChestBoatGivesJungleChestBoatItemStack(GameTestHelper helper) {
        ChestBoat jungleChestBoat = helper.spawn(EntityType.JUNGLE_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = jungleChestBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.JUNGLE_CHEST_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnMangroveBoatGivesMangroveBoatItemStack(GameTestHelper helper) {
        Boat mangroveBoat = helper.spawn(EntityType.MANGROVE_BOAT, SPAWN_POSITION);
        ItemStack stack = mangroveBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.MANGROVE_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnMangroveChestBoatGivesMangroveChestBoatItemStack(GameTestHelper helper) {
        ChestBoat mangroveChestBoat = helper.spawn(EntityType.MANGROVE_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = mangroveChestBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.MANGROVE_CHEST_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnOakBoatGivesOakBoatItemStack(GameTestHelper helper) {
        Boat oakBoat = helper.spawn(EntityType.OAK_BOAT, SPAWN_POSITION);
        ItemStack stack = oakBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.OAK_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnOakChestBoatGivesOakChestBoatItemStack(GameTestHelper helper) {
        ChestBoat oakChestBoat = helper.spawn(EntityType.OAK_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = oakChestBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.OAK_CHEST_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSpruceBoatGivesSpruceBoatItemStack(GameTestHelper helper) {
        Boat spruceBoat = helper.spawn(EntityType.SPRUCE_BOAT, SPAWN_POSITION);
        ItemStack stack = spruceBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.SPRUCE_BOAT)
        );
    }

    @GameTest(structure = "itematic:entity.platform")
    public void getPickStackOnSpruceChestBoatGivesSpruceChestBoatItemStack(GameTestHelper helper) {
        ChestBoat spruceChestBoat = helper.spawn(EntityType.SPRUCE_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = spruceChestBoat.getPickResult();
        helper.succeedIf(() -> Assert.itemStack(helper, stack)
            .is(ItemIds.SPRUCE_CHEST_BOAT)
        );
    }
}
