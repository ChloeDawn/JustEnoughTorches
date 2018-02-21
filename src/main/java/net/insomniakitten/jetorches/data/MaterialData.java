package net.insomniakitten.jetorches.data;

import net.insomniakitten.jetorches.util.IOreDict;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum MaterialData implements IStringSerializable, IOreDict {

    STICK_STONE("stickStone"),
    STICK_NETHER("stickNetherrack"),
    STICK_PRISMARINE("stickPrismarine"),
    STICK_OBSIDIAN("stickObsidian"),
    STICK_GOLDEN("stickGold");

    public static final MaterialData[] VALUES = values();

    private final String ore;
    private final int size;

    MaterialData(String ore, int size) {
        this.size = size;
        this.ore = ore;
    }

    MaterialData(String ore) {
        this(ore, 64);
    }

    public int getSize() {
        return size;
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getOreName() {
        return ore;
    }

}
