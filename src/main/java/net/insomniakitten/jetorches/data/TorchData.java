package net.insomniakitten.jetorches.data;

import net.insomniakitten.jetorches.JETorchesConfig;
import net.insomniakitten.jetorches.color.ColorSupplier;
import net.insomniakitten.jetorches.util.OreNameSupplier;
import net.insomniakitten.jetorches.util.MaterialTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum TorchData implements IStringSerializable, OreNameSupplier, ColorSupplier {

    STONE("torchStone", 14, 0xFFDE6C, SoundType.STONE, EnumParticleTypes.FLAME),
    NETHER("torchNetherrack", 10, 0xFF7200, SoundType.STONE, EnumParticleTypes.SMOKE_NORMAL),
    PRISMARINE("torchPrismarine", 15, 0x66E8E0, SoundType.GLASS, EnumParticleTypes.WATER_DROP) {
        @Override
        public boolean canWorkUnderwater() {
            return JETorchesConfig.prismarineUnderwater;
        }
    },
    OBSIDIAN("torchObsidian", 13, 0x6300C0, SoundType.STONE, 0.2F, 3000.0F, EnumParticleTypes.FLAME),
    GOLDEN("torchGold", 14, 0xEBBC3C, SoundType.METAL, 0.0F, 30.0F, EnumParticleTypes.FLAME);

    public static final TorchData[] VALUES = values();

    private final String ore;
    private final int light;
    private final int color;
    private final SoundType sound;
    private final float hardness;
    private final float resistance;

    private final EnumParticleTypes particle;

    TorchData(String ore, int light, int color, SoundType sound, float hardness, float resistance, EnumParticleTypes particle) {
        this.ore = ore;
        this.light = light;
        this.color = color;
        this.sound = sound;
        this.hardness = hardness;
        this.resistance = resistance;
        this.particle = particle;
    }

    TorchData(String ore, int light, int color, SoundType sound, EnumParticleTypes particle) {
        this(ore, light, color, sound, 0.0F, 0.0F, particle);
    }

    public float getLight() {
        return (1.0F / 15.0F) * light;
    }

    public Material getMaterial() {
        return canWorkUnderwater()
               ? MaterialTorch.UNDERWATER
               : MaterialTorch.NORMAL;
    }

    public SoundType getSound() {
        return sound;
    }

    public float getHardness() {
        return hardness;
    }

    public float getResistance() {
        return resistance;
    }

    public EnumParticleTypes getParticle() {
        return particle;
    }

    public boolean canWorkUnderwater() {
        return false;
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
        return color;
    }

    @Override
    public float getRadius() {
        return getLight() * 6.0F;
    }


}
