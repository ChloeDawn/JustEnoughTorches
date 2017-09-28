package net.insomniakitten.jetorches.type;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum LampType implements IStringSerializable {

    LAPIS("blockLampLapis", 0.3F, 1.5F),
    OBSIDIAN("blockLampObsidian", 2.0F, 3000.0F),
    QUARTZ("blockLampQuartz", 0.5F, 3.0F);

    private final String oreDict;
    private final float hardness;
    private final float resistance;

    LampType(String oreDict, float hardness, float resistance) {
        this.oreDict = oreDict;
        this.hardness = hardness;
        this.resistance = resistance;
    }

    public static LampType getType(int meta) {
        return values()[meta % LampType.values().length];
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public int getMetadata() {
        return ordinal();
    }

    public float getHardness() {
        return hardness;
    }

    public float getResistance() {
        return resistance;
    }

    public String getOreDict() {
        return oreDict;
    }

}
