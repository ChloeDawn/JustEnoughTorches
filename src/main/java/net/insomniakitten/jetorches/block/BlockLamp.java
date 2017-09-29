package net.insomniakitten.jetorches.block;

import net.insomniakitten.jetorches.JETorches;
import net.insomniakitten.jetorches.type.LampType;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLamp extends Block {

    private static final PropertyBool POWERED = PropertyBool.create("powered");
    private static final PropertyEnum<LampType> TYPE = PropertyEnum.create("type", LampType.class);

    public BlockLamp() {
        super(Material.GLASS);
        setRegistryName("lamp");
        setUnlocalizedName(JETorches.ID + ".lamp");
        setCreativeTab(JETorches.CTAB);
        setSoundType(SoundType.GLASS);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        boolean powered = (meta & 1) != 0;
        LampType type = LampType.getType(meta >> 2);
        return getDefaultState().withProperty(POWERED, powered).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int powered = state.getValue(POWERED) ? 1 : 0;
        int type = state.getValue(TYPE).getMetadata() << 2;
        return powered | type;
    }

    @Override
    @Deprecated
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return state.getValue(TYPE).getHardness();
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
        return new BlockStateContainer(this, POWERED, TYPE);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public void getDrops(
            NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(this, 1, state.getValue(TYPE).getMetadata()));
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        return world.getBlockState(pos).getValue(TYPE).getResistance();
    }

    @Override
    public ItemStack getPickBlock(
            IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(TYPE).getMetadata());
    }

    @Override
    public IBlockState getStateForPlacement(
            World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
            EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(POWERED, false).withProperty(TYPE, LampType.getType(meta));
    }

    protected void updatePoweredState(IBlockState state, World world, BlockPos pos) {
        if (!world.isRemote) {
            boolean isPowered = world.isBlockPowered(pos);
            if (isPowered != state.getValue(POWERED)) {
                world.setBlockState(pos, state.withProperty(POWERED, isPowered));
            }
        }
    }

    public static class LampStateMapper extends StateMapperBase {

        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            String type = state.getValue(TYPE).getName();
            String powered = String.valueOf(state.getValue(POWERED));
            ResourceLocation rl = new ResourceLocation(JETorches.ID, "lamp_" + type);
            return new ModelResourceLocation(rl, "powered=" + powered);
        }

    }

}
