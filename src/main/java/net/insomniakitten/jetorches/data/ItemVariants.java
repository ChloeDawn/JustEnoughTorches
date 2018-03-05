package net.insomniakitten.jetorches.data;

import com.google.common.collect.Lists;
import net.insomniakitten.jetorches.util.OreNameSupplier;
import net.minecraft.util.IStringSerializable;

import java.util.List;
import java.util.Locale;

public enum ItemVariants implements IStringSerializable, OreNameSupplier {

    STICK_STONE("stickStone"),
    STICK_NETHER("stickNetherrack"),
    STICK_PRISMARINE("stickPrismarine"),
    STICK_OBSIDIAN("stickObsidian"),
    STICK_GOLDEN("stickGold");

    public static final ItemVariants[] VALUES = values();

    private final String ore;
    private final int size;

    ItemVariants(String ore, int size) {
        this.size = size;
        this.ore = ore;
    }

    ItemVariants(String ore) {
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
    public List<String> getOreNames() {
        return Lists.newArrayList(ore);
    }

}
