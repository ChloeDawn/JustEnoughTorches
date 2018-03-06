package net.insomniakitten.jetorches.data;

import com.google.common.collect.Lists;
import net.insomniakitten.jetorches.color.ColorSupplier;
import net.insomniakitten.jetorches.util.OreNameSupplier;
import net.minecraft.util.IStringSerializable;

import java.util.List;
import java.util.Locale;

public enum LampVariant implements IStringSerializable, OreNameSupplier, ColorSupplier {

    LAPIS("blockLampLapis", 0x003BC0, 0.3F, 1.5F),
    OBSIDIAN("blockLampObsidian", 0x6300C0, 2.0F, 3000.0F),
    QUARTZ("blockLampQuartz", 0xFFFFFF, 0.5F, 3.0F);

    public static final LampVariant[] VALUES = values();

    private final String ore;
    private final int color;
    private final float hardness;
    private final float resistance;

    LampVariant(String ore, int color, float hardness, float resistance) {
        this.ore = ore;
        this.color = color;
        this.hardness = hardness;
        this.resistance = resistance;
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
    public List<String> getOreNames() {
        return Lists.newArrayList("blockLamp", ore);
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public float getRadius() {
        return 6.0F;
    }

}
