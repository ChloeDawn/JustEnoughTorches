package net.insomniakitten.jetorches.util;

import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.ILightEventConsumer;
import com.elytradev.mirage.lighting.Light;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;

@Optional.Interface(iface = "com.elytradev.mirage.lighting.ILightEventConsumer", modid = "mirage")
public final class ColoredLight extends TileEntity implements ILightEventConsumer {

    private int red;
    private int green;
    private int blue;
    private float radius;

    public ColoredLight() {}

    public ColoredLight(Color color, float radius) {
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.radius = radius;
    }

    public ColoredLight(int color, float radius) {
        this(new Color(color), radius);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound color = compound.getCompoundTag("color");
        red = color.getInteger("r");
        green = color.getInteger("g");
        blue = color.getInteger("b");
        radius = compound.getFloat("radius");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagCompound color = new NBTTagCompound();
        color.setInteger("r", red);
        color.setInteger("g", green);
        color.setInteger("b", blue);
        compound.setTag("color", color);
        compound.setFloat("radius", radius);
        return compound;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Optional.Method(modid = "mirage")
    public void gatherLights(GatherLightsEvent event) {
        if (world != null && pos != null) {
            event.add(Light.builder()
                    .color(red, green, blue)
                    .radius(radius)
                    .pos(pos)
                    .build());
        }
    }

}
