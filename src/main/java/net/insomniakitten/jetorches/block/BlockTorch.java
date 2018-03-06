package net.insomniakitten.jetorches.block;

import com.google.common.collect.ImmutableMap;
import net.insomniakitten.jetorches.JETorches;
import net.insomniakitten.jetorches.JETorchesConfig;
import net.insomniakitten.jetorches.color.ColoredLight;
import net.insomniakitten.jetorches.data.TorchVariant;
import net.insomniakitten.jetorches.util.SidedLightHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public final class BlockTorch extends Block {

    private static final PropertyDirection FACING = PropertyDirection
            .create("facing", facing -> facing != EnumFacing.DOWN);

    private static final ImmutableMap<EnumFacing, AxisAlignedBB> AABB_TORCH = ImmutableMap.of(
            EnumFacing.UP, new AxisAlignedBB(0.40D, 0.00D, 0.40D, 0.60D, 0.60D, 0.60D),
            EnumFacing.NORTH, new AxisAlignedBB(0.35D, 0.20D, 0.70D, 0.65D, 0.80D, 1.00D),
            EnumFacing.SOUTH, new AxisAlignedBB(0.35D, 0.20D, 0.00D, 0.65D, 0.80D, 0.30D),
            EnumFacing.WEST, new AxisAlignedBB(0.70D, 0.20D, 0.35D, 1.00D, 0.80D, 0.65D),
            EnumFacing.EAST, new AxisAlignedBB(0.00D, 0.20D, 0.35D, 0.30D, 0.80D, 0.65D)
    );

    private final TorchVariant torch;

    public BlockTorch(TorchVariant torch) {
        super(torch.getMaterial());
        this.torch = torch;
        setRegistryName(JETorches.ID, "torch_" + torch.getName());
        setUnlocalizedName(JETorches.ID + ".torch_" + torch.getName());
        setSoundType(torch.getSound());
        setHardness(torch.getHardness());
        setResistance(torch.getResistance());
        setLightLevel(torch.getLight());
        setCreativeTab(JETorches.TAB);
        setTickRandomly(true);

    }

    public final TorchVariant getVariant() {
        return torch;
    }

    @Override
    @Deprecated
    public Material getMaterial(IBlockState state) {
        return torch.getMaterial();
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.values()[meta & 7];
        if (facing == EnumFacing.DOWN) facing = EnumFacing.UP;
        return getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).ordinal();
    }

    @Override
    @Deprecated
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @Deprecated
    public IBlockState withMirror(IBlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.getValue(FACING)));
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return AABB_TORCH.get(state.getValue(FACING));
    }

    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    @Deprecated
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        double posX = pos.getX() + 0.50D;
        double posY = pos.getY() + 0.70D;
        double posZ = pos.getZ() + 0.50D;
        if (state.getValue(FACING).getAxis().isHorizontal()) {
            EnumFacing offset = state.getValue(FACING).getOpposite();
            posX += 0.27D * offset.getFrontOffsetX();
            posY += 0.22D;
            posZ += 0.27D * offset.getFrontOffsetZ();
        }
        if (world.isRemote) {
            world.spawnParticle(getVariant().getParticle(), posX, posY, posZ, 0.00D, 0.00D, 0.00D);
            if (getVariant().getParticle() == EnumParticleTypes.FLAME) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY, posZ, 0.00D, 0.00D, 0.00D);
            }
        }
    }

    @Override
    @Deprecated
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        EnumFacing facing = state.getValue(FACING);
        EnumFacing.Axis axis = facing.getAxis();
        BlockPos posAt = pos.offset(facing.getOpposite());
        if (axis.isHorizontal() && !isSolid(world, posAt, facing) || axis.isVertical() && !canPlaceOn(world, posAt)) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (!canPlaceAt(world, pos, state.getValue(FACING))) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        for (EnumFacing side : FACING.getAllowedValues()) {
            if (canPlaceAt(world, pos, side)) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Deprecated
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (canPlaceAt(world, pos, facing)) {
            return getDefaultState().withProperty(FACING, facing);
        }
        for (EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
            if (canPlaceAt(world, pos, side)) {
                return getDefaultState().withProperty(FACING, side);
            }
        }
        return getDefaultState().withProperty(FACING, EnumFacing.UP);
    }

    @Override
    @Deprecated
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.DESTROY;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return SidedLightHandler.getLightValue(state, world, pos).orElse(lightValue);
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return world.getBlockState(pos.offset(side)).getMaterial().isLiquid()
                && world.getBlockState(pos.up()).getMaterial().isLiquid();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return JETorchesConfig.coloredLighting;
    }

    @Override
    @Nullable
    public TileEntity createTileEntity(World world, IBlockState state) {
        int color = getVariant().getColor();
        float radius = getVariant().getRadius();
        return hasTileEntity(state) ? new ColoredLight(color, radius) : null;
    }

    @Override
    public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos pos, IBlockState state, Entity entity, double yToTest, Material material, boolean checkHead) {
        if (getVariant().canWorkUnderwater() && Material.WATER.equals(world.getBlockState(pos.up()).getMaterial())) {
            boolean adjacentWater = false;
            Vec3i offset = new Vec3i(1, 0, 1);
            BlockPos min = pos.subtract(offset);
            BlockPos max = pos.add(offset);
            for (MutableBlockPos target : BlockPos.getAllInBoxMutable(min, max)) {
                if (world.getBlockState(target).getMaterial().equals(Material.WATER)) {
                    adjacentWater = true;
                    break;
                }
            }
            return adjacentWater ? material.equals(Material.WATER) : null;
        }
        return null;
    }

    @Override
    public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d lastColor, float partialTicks) {
        if (getVariant().canWorkUnderwater()) {
            pos = pos.up();
            state = world.getBlockState(pos);
            if (state.getMaterial().isLiquid()) {
                float height = 0.0F;
                if (state.getBlock() instanceof BlockLiquid) {
                    int meta = state.getValue(BlockLiquid.LEVEL);
                    if (meta >= 8) meta = 0;
                    height = (float) (meta + 1) / 9.0F - 0.11111111F;
                }
                if (ActiveRenderInfo.projectViewFromEntity(entity, partialTicks).y > (pos.getY() + 1) - height) {
                    BlockPos upPos = pos.up();
                    IBlockState upState = world.getBlockState(upPos);
                    return upState.getBlock().getFogColor(world, upPos, upState, entity, lastColor, partialTicks);
                }
            }
        }
        return super.getFogColor(world, pos, state, entity, lastColor, partialTicks);
    }

    protected boolean canPlaceOn(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().canPlaceTorchOnTop(state, world, pos);
    }

    protected boolean isSolid(World world, BlockPos pos, EnumFacing facing) {
        IBlockState state = world.getBlockState(pos);
        BlockFaceShape shape = state.getBlockFaceShape(world, pos, facing);
        return shape == BlockFaceShape.SOLID;
    }

    protected boolean canPlaceAt(World world, BlockPos pos, EnumFacing side) {
        BlockPos posAt = pos.offset(side.getOpposite());
        return canPlaceOn(world, posAt) && side != EnumFacing.DOWN && isSolid(world, posAt, side);
    }

}
