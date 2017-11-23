package net.insomniakitten.jetorches.type;

import net.insomniakitten.jetorches.JETorchesConfig;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum TorchType implements IStringSerializable {

    STONE("torchStone", 14, SoundType.STONE, EnumParticleTypes.FLAME),
    NETHER("torchNetherrack", 10, SoundType.STONE, EnumParticleTypes.SMOKE_NORMAL),
    PRISMARINE("torchPrismarine", 15, SoundType.GLASS, EnumParticleTypes.WATER_DROP),
    OBSIDIAN("torchObsidian", 13, 0.2F, 3000.0F, SoundType.STONE, EnumParticleTypes.FLAME),
    GOLDEN("torchGold", 14, 0.0F, 30.0F, SoundType.METAL, EnumParticleTypes.FLAME);

    private static final Material MATERIAL_STABLE = new Material(MapColor.AIR) {
        @Override
        public boolean isSolid() {
            return false;
        }

        @Override
        public boolean blocksLight() {
            return false;
        }
    };

    private final String oreDict;
    private final int lightLevel;
    private final float hardness;
    private final float resistance;
    private final SoundType soundType;
    private final EnumParticleTypes particle;

    TorchType(String oreDict, int light, SoundType sound, EnumParticleTypes particle) {
        this(oreDict, light, 0.0f, 0.0f, sound, particle);
    }

    TorchType(String oreDict, int light, float h, float r, SoundType sound, EnumParticleTypes particle) {
        this.oreDict = oreDict;
        this.lightLevel = light;
        this.hardness = h;
        this.resistance = r;
        this.soundType = sound;
        this.particle = particle;
    }

    public static TorchType getType(int meta) {
        return values()[meta % TorchType.values().length];
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public int getMetadata() {
        return ordinal();
    }

    public float getLightLevel() {
        return this.lightLevel;
    }

    public float getHardness() {
        return hardness;
    }

    public float getResistance() {
        return resistance;
    }

    public Material getMaterial() {
        return equals(PRISMARINE) && JETorchesConfig.prismarineUnderwater ? MATERIAL_STABLE : Material.CIRCUITS;
    }

    public SoundType getSoundType() {
        return soundType;
    }

    public EnumParticleTypes getParticle() {
        return this.particle;
    }

    public String getOreDict() {
        return oreDict;
    }

}
