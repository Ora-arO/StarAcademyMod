package abeshutt.staracademy.world.loot;

import abeshutt.staracademy.data.adapter.Adapters;
import abeshutt.staracademy.init.ModLootFunctionTypes;
import abeshutt.staracademy.math.roll.IntRoll;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;

import java.util.Optional;

public class SetMoneyBagLootFunction implements LootFunction {

    public static final MapCodec<SetMoneyBagLootFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Adapters.INT_ROLL.codecJson().optionalFieldOf("bronze").forGetter(SetMoneyBagLootFunction::getBronze),
            Adapters.INT_ROLL.codecJson().optionalFieldOf("silver").forGetter(SetMoneyBagLootFunction::getSilver),
            Adapters.INT_ROLL.codecJson().optionalFieldOf("gold").forGetter(SetMoneyBagLootFunction::getGold),
            Adapters.BOOLEAN.codecJson().fieldOf("combine").forGetter(SetMoneyBagLootFunction::isCombine)
        ).apply(instance, (bronze, silver, gold, combine) -> new SetMoneyBagLootFunction(
                bronze.orElse(null), silver.orElse(null), gold.orElse(null), combine)));

    private final IntRoll bronze;
    private final IntRoll silver;
    private final IntRoll gold;
    private final boolean combine;

    public SetMoneyBagLootFunction(IntRoll bronze, IntRoll silver, IntRoll gold, boolean combine) {
        this.bronze = bronze;
        this.silver = silver;
        this.gold = gold;
        this.combine = combine;
    }

    public Optional<IntRoll> getBronze() {
        return Optional.ofNullable(this.bronze);
    }

    public Optional<IntRoll> getSilver() {
        return Optional.ofNullable(this.silver);
    }

    public Optional<IntRoll> getGold() {
        return Optional.ofNullable(this.gold);
    }

    public boolean isCombine() {
        return this.combine;
    }

    @Override
    public LootFunctionType<? extends LootFunction> getType() {
        return ModLootFunctionTypes.SET_MONEY_BAG.get();
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext context) {
        // Keep the loot function id/codec for datapack compatibility even when Numismatic is absent.
        return stack;
    }

}

