package net.insomniakitten.jetorches.data;

import net.insomniakitten.jetorches.util.IColored;
import net.insomniakitten.jetorches.util.IOreDict;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;
import java.util.function.Consumer;

public enum LampData implements IStringSerializable, IOreDict, IColored {

    LAPIS("blockLampLapis", 0.3F, 1.5F),
    OBSIDIAN("blockLampObsidian", 2.0F, 3000.0F),
    QUARTZ("blockLampQuartz", 0.5F, 3.0F);

    public static final LampData[] VALUES = values();

    private final String ore;
    private final float hardness;
    private final float resistance;

    LampData(String ore, float hardness, float resistance) {
        this.ore = ore;
        this.hardness = hardness;
        this.resistance = resistance;
    }

    public static void forEach(Consumer<LampData> consumer) {
        for (LampData lamp : VALUES) consumer.accept(lamp);
    }

    public float getHardness() {
        return hardness;
    }

    public float getResistance() {
        return resistance;
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getOreName() {
        return ore;
    }

    @Override
    public int getColor() {
        return 0;
    }

}
