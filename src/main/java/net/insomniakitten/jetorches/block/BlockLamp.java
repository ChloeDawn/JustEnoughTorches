package net.insomniakitten.jetorches.block;

import net.insomniakitten.jetorches.JETorches;
import net.insomniakitten.jetorches.JETorchesConfig;
import net.insomniakitten.jetorches.color.ColoredLight;
import net.insomniakitten.jetorches.data.LampVariant;
import net.insomniakitten.jetorches.util.SidedLightHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public final class BlockLamp extends Block {

    private static final PropertyBool POWERED = PropertyBool.create("powered");

    private final LampVariant lamp;

    public BlockLamp(LampVariant lamp) {
        super(Material.GLASS);
        this.lamp = lamp;
        setRegistryName(JETorches.ID, "lamp_" + lamp.getName());
        setUnlocalizedName(JETorches.ID + ".lamp_" + lamp.getName());
        setSoundType(SoundType.GLASS);
        setHardness(lamp.getHardness());
        setResistance(lamp.getResistance());
        setCreativeTab(JETorches.TAB);
    }

    public LampVariant getVariant() {
        return lamp;
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        boolean powered = meta != 0;
        return getDefaultState().withProperty(POWERED, powered);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWERED) ? 1 : 0;
    }

    @Override
    @Deprecated
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        updatePoweredState(state, world, pos);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        updatePoweredState(state, world, pos);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWERED);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(POWERED) ? SidedLightHandler.getLightValue(state, world, pos, 15) : 0;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return JETorchesConfig.coloredLighting && state.getValue(POWERED);
    }

    @Override
    @Nullable
    public TileEntity createTileEntity(World world, IBlockState state) {
        int color = getVariant().getColor();
        float radius = getVariant().getRadius();
        return hasTileEntity(state) ? new ColoredLight(color, radius) : null;
    }

    protected void updatePoweredState(IBlockState state, World world, BlockPos pos) {
        if (!world.isRemote) {
            boolean isPowered = world.isBlockPowered(pos);
            if (isPowered != state.getValue(POWERED)) {
                world.setBlockState(pos, state.withProperty(POWERED, isPowered), 2);
            }
        }
    }

}
