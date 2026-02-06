package net.errorcraft.itematic.gametest.entity;

import net.errorcraft.itematic.gametest.Assert;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.entity.vehicle.ChestRaftEntity;
import net.minecraft.entity.vehicle.RaftEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

public class PickEntityTestSuite {
    private static final BlockPos SPAWN_POSITION = new BlockPos(1, 1, 1);

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnArmadilloGivesArmadilloSpawnEggItemStack(TestContext context) {
        ArmadilloEntity armadillo = context.spawnEntity(EntityType.ARMADILLO, SPAWN_POSITION);
        ItemStack stack = armadillo.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.ARMADILLO_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnAllayGivesAllaySpawnEggItemStack(TestContext context) {
        AllayEntity allay = context.spawnEntity(EntityType.ALLAY, SPAWN_POSITION);
        ItemStack stack = allay.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.ALLAY_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnAxolotlGivesAxolotlSpawnEggItemStack(TestContext context) {
        AxolotlEntity axolotl = context.spawnEntity(EntityType.AXOLOTL, SPAWN_POSITION);
        ItemStack stack = axolotl.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.AXOLOTL_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnBatGivesBatSpawnEggItemStack(TestContext context) {
        BatEntity bat = context.spawnEntity(EntityType.BAT, SPAWN_POSITION);
        ItemStack stack = bat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.BAT_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnBeeGivesBeeSpawnEggItemStack(TestContext context) {
        BeeEntity bee = context.spawnEntity(EntityType.BEE, SPAWN_POSITION);
        ItemStack stack = bee.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.BEE_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnBlazeGivesBlazeSpawnEggItemStack(TestContext context) {
        BlazeEntity blaze = context.spawnEntity(EntityType.BLAZE, SPAWN_POSITION);
        ItemStack stack = blaze.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.BLAZE_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnCatGivesCatSpawnEggItemStack(TestContext context) {
        CatEntity cat = context.spawnEntity(EntityType.CAT, SPAWN_POSITION);
        ItemStack stack = cat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.CAT_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnCamelGivesCamelSpawnEggItemStack(TestContext context) {
        CamelEntity camel = context.spawnEntity(EntityType.CAMEL, SPAWN_POSITION);
        ItemStack stack = camel.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.CAMEL_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnCaveSpiderGivesCaveSpiderSpawnEggItemStack(TestContext context) {
        CaveSpiderEntity caveSpider = context.spawnEntity(EntityType.CAVE_SPIDER, SPAWN_POSITION);
        ItemStack stack = caveSpider.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.CAVE_SPIDER_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnChickenGivesChickenSpawnEggItemStack(TestContext context) {
        ChickenEntity chicken = context.spawnEntity(EntityType.CHICKEN, SPAWN_POSITION);
        ItemStack stack = chicken.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.CHICKEN_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnCodGivesCodSpawnEggItemStack(TestContext context) {
        CodEntity cod = context.spawnEntity(EntityType.COD, SPAWN_POSITION);
        ItemStack stack = cod.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.COD_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnCowGivesCowSpawnEggItemStack(TestContext context) {
        CowEntity cow = context.spawnEntity(EntityType.COW, SPAWN_POSITION);
        ItemStack stack = cow.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.COW_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnCreeperGivesCreeperSpawnEggItemStack(TestContext context) {
        CreeperEntity creeper = context.spawnEntity(EntityType.CREEPER, SPAWN_POSITION);
        ItemStack stack = creeper.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.CREEPER_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnDolphinGivesDolphinSpawnEggItemStack(TestContext context) {
        DolphinEntity dolphin = context.spawnEntity(EntityType.DOLPHIN, SPAWN_POSITION);
        ItemStack stack = dolphin.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.DOLPHIN_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnDonkeyGivesDonkeySpawnEggItemStack(TestContext context) {
        DonkeyEntity donkey = context.spawnEntity(EntityType.DONKEY, SPAWN_POSITION);
        ItemStack stack = donkey.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.DONKEY_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnDrownedGivesDrownedSpawnEggItemStack(TestContext context) {
        DrownedEntity drowned = context.spawnEntity(EntityType.DROWNED, SPAWN_POSITION);
        ItemStack stack = drowned.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.DROWNED_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnElderGuardianGivesElderGuardianSpawnEggItemStack(TestContext context) {
        ElderGuardianEntity elderGuardian = context.spawnEntity(EntityType.ELDER_GUARDIAN, SPAWN_POSITION);
        ItemStack stack = elderGuardian.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.ELDER_GUARDIAN_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnEnderDragonGivesEnderDragonSpawnEggItemStack(TestContext context) {
        EnderDragonEntity enderDragon = context.spawnEntity(EntityType.ENDER_DRAGON, SPAWN_POSITION);
        ItemStack stack = enderDragon.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.ENDER_DRAGON_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnEndermanGivesEndermanSpawnEggItemStack(TestContext context) {
        EndermanEntity enderman = context.spawnEntity(EntityType.ENDERMAN, SPAWN_POSITION);
        ItemStack stack = enderman.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.ENDERMAN_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnEndermiteGivesEndermiteSpawnEggItemStack(TestContext context) {
        EndermiteEntity endermite = context.spawnEntity(EntityType.ENDERMITE, SPAWN_POSITION);
        ItemStack stack = endermite.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.ENDERMITE_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnEvokerGivesEvokerSpawnEggItemStack(TestContext context) {
        EvokerEntity evoker = context.spawnEntity(EntityType.EVOKER, SPAWN_POSITION);
        ItemStack stack = evoker.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.EVOKER_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnFoxGivesFoxSpawnEggItemStack(TestContext context) {
        FoxEntity fox = context.spawnEntity(EntityType.FOX, SPAWN_POSITION);
        ItemStack stack = fox.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.FOX_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnFrogGivesFrogSpawnEggItemStack(TestContext context) {
        FrogEntity frog = context.spawnEntity(EntityType.FROG, SPAWN_POSITION);
        ItemStack stack = frog.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.FROG_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnGhastGivesGhastSpawnEggItemStack(TestContext context) {
        GhastEntity ghast = context.spawnEntity(EntityType.GHAST, SPAWN_POSITION);
        ItemStack stack = ghast.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.GHAST_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnGlowSquidGivesGlowSquidSpawnEggItemStack(TestContext context) {
        GlowSquidEntity glowSquid = context.spawnEntity(EntityType.GLOW_SQUID, SPAWN_POSITION);
        ItemStack stack = glowSquid.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.GLOW_SQUID_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnGoatGivesGoatSpawnEggItemStack(TestContext context) {
        GoatEntity goat = context.spawnEntity(EntityType.GOAT, SPAWN_POSITION);
        ItemStack stack = goat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.GOAT_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnGuardianGivesGuardianSpawnEggItemStack(TestContext context) {
        GuardianEntity guardian = context.spawnEntity(EntityType.GUARDIAN, SPAWN_POSITION);
        ItemStack stack = guardian.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.GUARDIAN_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnHoglinGivesHoglinSpawnEggItemStack(TestContext context) {
        HoglinEntity hoglin = context.spawnEntity(EntityType.HOGLIN, SPAWN_POSITION);
        ItemStack stack = hoglin.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.HOGLIN_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnHorseGivesHorseSpawnEggItemStack(TestContext context) {
        HorseEntity horse = context.spawnEntity(EntityType.HORSE, SPAWN_POSITION);
        ItemStack stack = horse.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.HORSE_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnHuskGivesHuskSpawnEggItemStack(TestContext context) {
        HuskEntity husk = context.spawnEntity(EntityType.HUSK, SPAWN_POSITION);
        ItemStack stack = husk.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.HUSK_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnIronGolemGivesIronGolemSpawnEggItemStack(TestContext context) {
        IronGolemEntity ironGolem = context.spawnEntity(EntityType.IRON_GOLEM, SPAWN_POSITION);
        ItemStack stack = ironGolem.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.IRON_GOLEM_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnLlamaGivesLlamaSpawnEggItemStack(TestContext context) {
        LlamaEntity llama = context.spawnEntity(EntityType.LLAMA, SPAWN_POSITION);
        ItemStack stack = llama.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.LLAMA_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnMagmaCubeGivesMagmaCubeSpawnEggItemStack(TestContext context) {
        MagmaCubeEntity magmaCube = context.spawnEntity(EntityType.MAGMA_CUBE, SPAWN_POSITION);
        ItemStack stack = magmaCube.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.MAGMA_CUBE_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnMooshroomGivesMooshroomSpawnEggItemStack(TestContext context) {
        MooshroomEntity mooshroom = context.spawnEntity(EntityType.MOOSHROOM, SPAWN_POSITION);
        ItemStack stack = mooshroom.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.MOOSHROOM_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnMuleGivesMuleSpawnEggItemStack(TestContext context) {
        MuleEntity mule = context.spawnEntity(EntityType.MULE, SPAWN_POSITION);
        ItemStack stack = mule.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.MULE_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnOcelotGivesOcelotSpawnEggItemStack(TestContext context) {
        OcelotEntity ocelot = context.spawnEntity(EntityType.OCELOT, SPAWN_POSITION);
        ItemStack stack = ocelot.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.OCELOT_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnPandaGivesPandaSpawnEggItemStack(TestContext context) {
        PandaEntity panda = context.spawnEntity(EntityType.PANDA, SPAWN_POSITION);
        ItemStack stack = panda.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.PANDA_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnParrotGivesParrotSpawnEggItemStack(TestContext context) {
        ParrotEntity parrot = context.spawnEntity(EntityType.PARROT, SPAWN_POSITION);
        ItemStack stack = parrot.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.PARROT_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnPhantomGivesPhantomSpawnEggItemStack(TestContext context) {
        PhantomEntity phantom = context.spawnEntity(EntityType.PHANTOM, SPAWN_POSITION);
        ItemStack stack = phantom.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.PHANTOM_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnPigGivesPigSpawnEggItemStack(TestContext context) {
        PigEntity pig = context.spawnEntity(EntityType.PIG, SPAWN_POSITION);
        ItemStack stack = pig.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.PIG_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnPiglinGivesPiglinSpawnEggItemStack(TestContext context) {
        PiglinEntity piglin = context.spawnEntity(EntityType.PIGLIN, SPAWN_POSITION);
        ItemStack stack = piglin.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.PIGLIN_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnPiglinBruteGivesPiglinBruteSpawnEggItemStack(TestContext context) {
        PiglinBruteEntity piglinBrute = context.spawnEntity(EntityType.PIGLIN_BRUTE, SPAWN_POSITION);
        ItemStack stack = piglinBrute.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.PIGLIN_BRUTE_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnPillagerGivesPillagerSpawnEggItemStack(TestContext context) {
        PillagerEntity pillager = context.spawnEntity(EntityType.PILLAGER, SPAWN_POSITION);
        ItemStack stack = pillager.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.PILLAGER_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnPolarBearGivesPolarBearSpawnEggItemStack(TestContext context) {
        PolarBearEntity polarBear = context.spawnEntity(EntityType.POLAR_BEAR, SPAWN_POSITION);
        ItemStack stack = polarBear.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.POLAR_BEAR_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnPufferfishGivesPufferfishSpawnEggItemStack(TestContext context) {
        PufferfishEntity pufferfish = context.spawnEntity(EntityType.PUFFERFISH, SPAWN_POSITION);
        ItemStack stack = pufferfish.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.PUFFERFISH_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnRabbitGivesRabbitSpawnEggItemStack(TestContext context) {
        RabbitEntity rabbit = context.spawnEntity(EntityType.RABBIT, SPAWN_POSITION);
        ItemStack stack = rabbit.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.RABBIT_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnRavagerGivesRavagerSpawnEggItemStack(TestContext context) {
        RavagerEntity ravager = context.spawnEntity(EntityType.RAVAGER, SPAWN_POSITION);
        ItemStack stack = ravager.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.RAVAGER_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnSalmonGivesSalmonSpawnEggItemStack(TestContext context) {
        SalmonEntity salmon = context.spawnEntity(EntityType.SALMON, SPAWN_POSITION);
        ItemStack stack = salmon.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.SALMON_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnSheepGivesSheepSpawnEggItemStack(TestContext context) {
        SheepEntity sheep = context.spawnEntity(EntityType.SHEEP, SPAWN_POSITION);
        ItemStack stack = sheep.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.SHEEP_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnShulkerGivesShulkerSpawnEggItemStack(TestContext context) {
        ShulkerEntity shulker = context.spawnEntity(EntityType.SHULKER, SPAWN_POSITION);
        ItemStack stack = shulker.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.SHULKER_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnSilverfishGivesSilverfishSpawnEggItemStack(TestContext context) {
        SilverfishEntity silverfish = context.spawnEntity(EntityType.SILVERFISH, SPAWN_POSITION);
        ItemStack stack = silverfish.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.SILVERFISH_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnSkeletonGivesSkeletonSpawnEggItemStack(TestContext context) {
        SkeletonEntity skeleton = context.spawnEntity(EntityType.SKELETON, SPAWN_POSITION);
        ItemStack stack = skeleton.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.SKELETON_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnSkeletonHorseGivesSkeletonHorseSpawnEggItemStack(TestContext context) {
        SkeletonHorseEntity skeletonHorse = context.spawnEntity(EntityType.SKELETON_HORSE, SPAWN_POSITION);
        ItemStack stack = skeletonHorse.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.SKELETON_HORSE_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnSlimeGivesSlimeSpawnEggItemStack(TestContext context) {
        SlimeEntity slime = context.spawnEntity(EntityType.SLIME, SPAWN_POSITION);
        ItemStack stack = slime.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.SLIME_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnSnifferGivesSnifferSpawnEggItemStack(TestContext context) {
        SnifferEntity sniffer = context.spawnEntity(EntityType.SNIFFER, SPAWN_POSITION);
        ItemStack stack = sniffer.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.SNIFFER_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnSnowGolemGivesSnowGolemSpawnEggItemStack(TestContext context) {
        SnowGolemEntity snowGolem = context.spawnEntity(EntityType.SNOW_GOLEM, SPAWN_POSITION);
        ItemStack stack = snowGolem.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.SNOW_GOLEM_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnSpiderGivesSpiderSpawnEggItemStack(TestContext context) {
        SpiderEntity spider = context.spawnEntity(EntityType.SPIDER, SPAWN_POSITION);
        ItemStack stack = spider.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.SPIDER_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnSquidGivesSquidSpawnEggItemStack(TestContext context) {
        SquidEntity squid = context.spawnEntity(EntityType.SQUID, SPAWN_POSITION);
        ItemStack stack = squid.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.SQUID_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnStrayGivesStraySpawnEggItemStack(TestContext context) {
        StrayEntity stray = context.spawnEntity(EntityType.STRAY, SPAWN_POSITION);
        ItemStack stack = stray.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.STRAY_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnStriderGivesStriderSpawnEggItemStack(TestContext context) {
        StriderEntity strider = context.spawnEntity(EntityType.STRIDER, SPAWN_POSITION);
        ItemStack stack = strider.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.STRIDER_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnTadpoleGivesTadpoleSpawnEggItemStack(TestContext context) {
        TadpoleEntity tadpole = context.spawnEntity(EntityType.TADPOLE, SPAWN_POSITION);
        ItemStack stack = tadpole.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.TADPOLE_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnTraderLlamaGivesTraderLlamaSpawnEggItemStack(TestContext context) {
        TraderLlamaEntity traderLlama = context.spawnEntity(EntityType.TRADER_LLAMA, SPAWN_POSITION);
        ItemStack stack = traderLlama.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.TRADER_LLAMA_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnTropicalFishGivesTropicalFishSpawnEggItemStack(TestContext context) {
        TropicalFishEntity tropicalFish = context.spawnEntity(EntityType.TROPICAL_FISH, SPAWN_POSITION);
        ItemStack stack = tropicalFish.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.TROPICAL_FISH_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnTurtleGivesTurtleSpawnEggItemStack(TestContext context) {
        TurtleEntity turtle = context.spawnEntity(EntityType.TURTLE, SPAWN_POSITION);
        ItemStack stack = turtle.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.TURTLE_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnVexGivesVexSpawnEggItemStack(TestContext context) {
        VexEntity vex = context.spawnEntity(EntityType.VEX, SPAWN_POSITION);
        ItemStack stack = vex.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.VEX_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnVillagerGivesVillagerSpawnEggItemStack(TestContext context) {
        VillagerEntity villager = context.spawnEntity(EntityType.VILLAGER, SPAWN_POSITION);
        ItemStack stack = villager.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.VILLAGER_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnVindicatorGivesVindicatorSpawnEggItemStack(TestContext context) {
        VindicatorEntity vindicator = context.spawnEntity(EntityType.VINDICATOR, SPAWN_POSITION);
        ItemStack stack = vindicator.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.VINDICATOR_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnWanderingTraderGivesWanderingTraderSpawnEggItemStack(TestContext context) {
        WanderingTraderEntity wanderingTrader = context.spawnEntity(EntityType.WANDERING_TRADER, SPAWN_POSITION);
        ItemStack stack = wanderingTrader.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.WANDERING_TRADER_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnWardenGivesWardenSpawnEggItemStack(TestContext context) {
        WardenEntity warden = context.spawnEntity(EntityType.WARDEN, SPAWN_POSITION);
        ItemStack stack = warden.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.WARDEN_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnWitchGivesWitchSpawnEggItemStack(TestContext context) {
        WitchEntity witch = context.spawnEntity(EntityType.WITCH, SPAWN_POSITION);
        ItemStack stack = witch.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.WITCH_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnWitherGivesWitherSpawnEggItemStack(TestContext context) {
        WitherEntity wither = context.spawnEntity(EntityType.WITHER, SPAWN_POSITION);
        ItemStack stack = wither.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.WITHER_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnWitherSkeletonGivesWitherSkeletonSpawnEggItemStack(TestContext context) {
        WitherSkeletonEntity witherSkeleton = context.spawnEntity(EntityType.WITHER_SKELETON, SPAWN_POSITION);
        ItemStack stack = witherSkeleton.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.WITHER_SKELETON_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnWolfGivesWolfSpawnEggItemStack(TestContext context) {
        WolfEntity wolf = context.spawnEntity(EntityType.WOLF, SPAWN_POSITION);
        ItemStack stack = wolf.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.WOLF_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnZoglinGivesZoglinSpawnEggItemStack(TestContext context) {
        ZoglinEntity zoglin = context.spawnEntity(EntityType.ZOGLIN, SPAWN_POSITION);
        ItemStack stack = zoglin.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.ZOGLIN_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnZombieGivesZombieSpawnEggItemStack(TestContext context) {
        ZombieEntity zombie = context.spawnEntity(EntityType.ZOMBIE, SPAWN_POSITION);
        ItemStack stack = zombie.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.ZOMBIE_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnZombieHorseGivesZombieHorseSpawnEggItemStack(TestContext context) {
        ZombieHorseEntity zombieHorse = context.spawnEntity(EntityType.ZOMBIE_HORSE, SPAWN_POSITION);
        ItemStack stack = zombieHorse.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.ZOMBIE_HORSE_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnZombieVillagerGivesZombieVillagerSpawnEggItemStack(TestContext context) {
        ZombieVillagerEntity zombieVillager = context.spawnEntity(EntityType.ZOMBIE_VILLAGER, SPAWN_POSITION);
        ItemStack stack = zombieVillager.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.ZOMBIE_VILLAGER_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnZombifiedPiglinGivesZombifiedPiglinSpawnEggItemStack(TestContext context) {
        ZombifiedPiglinEntity zombifiedPiglin = context.spawnEntity(EntityType.ZOMBIFIED_PIGLIN, SPAWN_POSITION);
        ItemStack stack = zombifiedPiglin.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.ZOMBIFIED_PIGLIN_SPAWN_EGG));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnAcaciaBoatGivesAcaciaBoatItemStack(TestContext context) {
        BoatEntity acaciaBoat = context.spawnEntity(EntityType.ACACIA_BOAT, SPAWN_POSITION);
        ItemStack stack = acaciaBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.ACACIA_BOAT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnAcaciaChestBoatGivesAcaciaChestBoatItemStack(TestContext context) {
        ChestBoatEntity acaciaChestBoat = context.spawnEntity(EntityType.ACACIA_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = acaciaChestBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.ACACIA_CHEST_BOAT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnBambooRaftGivesBambooRaftItemStack(TestContext context) {
        RaftEntity bambooRaft = context.spawnEntity(EntityType.BAMBOO_RAFT, SPAWN_POSITION);
        ItemStack stack = bambooRaft.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.BAMBOO_RAFT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnBambooChestRaftGivesBambooChestRaftItemStack(TestContext context) {
        ChestRaftEntity bambooChestRaft = context.spawnEntity(EntityType.BAMBOO_CHEST_RAFT, SPAWN_POSITION);
        ItemStack stack = bambooChestRaft.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.BAMBOO_CHEST_RAFT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnBirchBoatGivesBirchBoatItemStack(TestContext context) {
        BoatEntity birchBoat = context.spawnEntity(EntityType.BIRCH_BOAT, SPAWN_POSITION);
        ItemStack stack = birchBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.BIRCH_BOAT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnBirchChestBoatGivesBirchChestBoatItemStack(TestContext context) {
        ChestBoatEntity birchChestBoat = context.spawnEntity(EntityType.BIRCH_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = birchChestBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.BIRCH_CHEST_BOAT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnCherryBoatGivesCherryBoatItemStack(TestContext context) {
        BoatEntity cherryBoat = context.spawnEntity(EntityType.CHERRY_BOAT, SPAWN_POSITION);
        ItemStack stack = cherryBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.CHERRY_BOAT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnCherryChestBoatGivesCherryChestBoatItemStack(TestContext context) {
        ChestBoatEntity cherryChestBoat = context.spawnEntity(EntityType.CHERRY_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = cherryChestBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.CHERRY_CHEST_BOAT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnDarkOakBoatGivesDarkOakBoatItemStack(TestContext context) {
        BoatEntity darkOakBoat = context.spawnEntity(EntityType.DARK_OAK_BOAT, SPAWN_POSITION);
        ItemStack stack = darkOakBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.DARK_OAK_BOAT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnDarkOakChestBoatGivesDarkOakChestBoatItemStack(TestContext context) {
        ChestBoatEntity darkOakChestBoat = context.spawnEntity(EntityType.DARK_OAK_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = darkOakChestBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.DARK_OAK_CHEST_BOAT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnJungleBoatGivesJungleBoatItemStack(TestContext context) {
        BoatEntity jungleBoat = context.spawnEntity(EntityType.JUNGLE_BOAT, SPAWN_POSITION);
        ItemStack stack = jungleBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.JUNGLE_BOAT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnJungleChestBoatGivesJungleChestBoatItemStack(TestContext context) {
        ChestBoatEntity jungleChestBoat = context.spawnEntity(EntityType.JUNGLE_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = jungleChestBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.JUNGLE_CHEST_BOAT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnMangroveBoatGivesMangroveBoatItemStack(TestContext context) {
        BoatEntity mangroveBoat = context.spawnEntity(EntityType.MANGROVE_BOAT, SPAWN_POSITION);
        ItemStack stack = mangroveBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.MANGROVE_BOAT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnMangroveChestBoatGivesMangroveChestBoatItemStack(TestContext context) {
        ChestBoatEntity mangroveChestBoat = context.spawnEntity(EntityType.MANGROVE_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = mangroveChestBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.MANGROVE_CHEST_BOAT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnOakBoatGivesOakBoatItemStack(TestContext context) {
        BoatEntity oakBoat = context.spawnEntity(EntityType.OAK_BOAT, SPAWN_POSITION);
        ItemStack stack = oakBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.OAK_BOAT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnOakChestBoatGivesOakChestBoatItemStack(TestContext context) {
        ChestBoatEntity oakChestBoat = context.spawnEntity(EntityType.OAK_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = oakChestBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.OAK_CHEST_BOAT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnSpruceBoatGivesSpruceBoatItemStack(TestContext context) {
        BoatEntity spruceBoat = context.spawnEntity(EntityType.SPRUCE_BOAT, SPAWN_POSITION);
        ItemStack stack = spruceBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.SPRUCE_BOAT));
    }

    @GameTest(templateName = "itematic:entity.platform")
    public void getPickStackOnSpruceChestBoatGivesSpruceChestBoatItemStack(TestContext context) {
        ChestBoatEntity spruceChestBoat = context.spawnEntity(EntityType.SPRUCE_CHEST_BOAT, SPAWN_POSITION);
        ItemStack stack = spruceChestBoat.getPickBlockStack();
        context.addInstantFinalTask(() -> Assert.itemStackIsOf(stack, ItemKeys.SPRUCE_CHEST_BOAT));
    }
}
