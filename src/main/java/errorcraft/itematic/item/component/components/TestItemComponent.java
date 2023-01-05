package errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import errorcraft.itematic.item.component.ItemComponent;
import errorcraft.itematic.item.component.ItemComponentType;
import errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public record TestItemComponent(boolean testBoolean) implements ItemComponent {
    public static Codec<TestItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.fieldOf("test_boolean").forGetter(TestItemComponent::testBoolean)
    ).apply(instance, TestItemComponent::new));

    @Override
    public ItemComponentType getType() {
        return ItemComponentTypes.TEST;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        user.sendMessage(Text.of("Use item (on client: " + world.isClient + ")"));
        if (!world.isClient) {
            user.sendMessage(Text.of("Test boolean was set to " + this.testBoolean + "!"));
        }
        return TypedActionResult.consume(stack);
    }
}
