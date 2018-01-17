package net.insomniakitten.jetorches.color;

import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.ILightEventConsumer;
import com.elytradev.mirage.lighting.Light;
import net.insomniakitten.jetorches.JETorches;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.awt.Color;

@Optional.Interface(iface = "com.elytradev.mirage.lighting.ILightEventConsumer", modid = "mirage")
public final class ColoredLight extends TileEntity implements ILightEventConsumer {

    public static final String ID = JETorches.ID + ":colored_light";

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
    public final SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public final NBTTagCompound getUpdateTag() {
        return writeToNBT(super.getUpdateTag());
    }

    @Override
    @Nonnull
    public final ITextComponent getDisplayName() {
        String name = getBlockType().getUnlocalizedName();
        return new TextComponentTranslation(name + ".name");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Optional.Method(modid = "mirage")
    public void gatherLights(GatherLightsEvent event) {
        if (world != null && pos != null) {
            event.add(Light.builder()
                    .color(red, green, blue, 2.0F)
                    .radius(radius)
                    .pos(pos)
                    .build());
        }
    }

}
