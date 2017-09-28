package net.insomniakitten.jetorches.type;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum MaterialType implements IStringSerializable {

    STICK_STONE("stickStone"),
    STICK_NETHER("stickNetherrack"),
    STICK_PRISMARINE("stickPrismarine"),
    STICK_OBSIDIAN("stickObsidian"),
    STICK_GOLDEN("stickGold");

    private final int stackSize;
    private final String oreDict;

    MaterialType(String oreDict) {
        this(64, oreDict);
    }

    MaterialType(int stackSize, String oreDict) {
        this.stackSize = stackSize;
        this.oreDict = oreDict;
    }

    public static MaterialType get(int meta) {
        return values()[meta % MaterialType.values().length];
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public int getMetadata() {
        return ordinal();
    }

    public int getStackSize() {
        return stackSize;
    }

    public String getOreDict() {
        return oreDict;
    }

}
