package net.insomniakitten.jetorches.data;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public final class MaterialTorch extends Material {

    public static final MaterialTorch NORMAL = new MaterialTorch(MapColor.AIR, false);
    public static final MaterialTorch UNDERWATER = new MaterialTorch(MapColor.AIR, true);

    private final boolean underwater;

    private MaterialTorch(MapColor color, boolean underwater) {
        super(color);
        this.underwater = underwater;
        setAdventureModeExempt();
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean blocksLight() {
        return false;
    }

    @Override
    public boolean blocksMovement() {
        return underwater;
    }

    @Override
    public EnumPushReaction getMobilityFlag() {
        return underwater
               ? EnumPushReaction.NORMAL
               : EnumPushReaction.DESTROY;
    }

}
