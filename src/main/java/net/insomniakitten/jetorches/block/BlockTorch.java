package net.insomniakitten.jetorches.block;

import com.google.common.collect.ImmutableMap;
import net.insomniakitten.jetorches.JETorches;
import net.insomniakitten.jetorches.JETorchesConfig;
import net.insomniakitten.jetorches.JETorchesRegistry;
import net.insomniakitten.jetorches.type.TorchType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    private final TorchType type;

    public BlockTorch(TorchType type) {
        super(type.getMaterial());
        this.type = type;
        setRegistryName(JETorches.ID, "torch_" + type.getName());
        setUnlocalizedName(JETorches.ID + ".torch_" + type.getName());
        setLightLevel(type.getLightLevel() / 15.0F);
        setHardness(type.getHardness());
        setResistance(type.getResistance());
        setSoundType(type.getSoundType());
        setTickRandomly(true);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.values()[meta & 7];
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
            world.spawnParticle(type.getParticle(), posX, posY, posZ, 0.00D, 0.00D, 0.00D);
            if (type.getParticle() == EnumParticleTypes.FLAME) {
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
    public int damageDropped(IBlockState state) {
        return JETorchesRegistry.BLOCK_TORCHES.indexOf(this);
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
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        IBlockState stateAt = world.getBlockState(pos.offset(side));
        IBlockState stateAbove = world.getBlockState(pos.up());
        return stateAt.getBlock() instanceof BlockLiquid && !Blocks.AIR.equals(stateAbove.getBlock());
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(JETorchesRegistry.ITEM_TORCH, 1, JETorchesRegistry.BLOCK_TORCHES.indexOf(this)));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(JETorchesRegistry.ITEM_TORCH, 1, JETorchesRegistry.BLOCK_TORCHES.indexOf(this));
    }

    @Override
    public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos pos, IBlockState state, Entity entity, double yToTest, Material material, boolean checkHead) {
        boolean waterAbove = Material.WATER.equals(world.getBlockState(pos.up()).getMaterial());
        boolean isPrismarine = TorchType.PRISMARINE.equals(((BlockTorch) state.getBlock()).getType());
        if (JETorchesConfig.prismarineUnderwater && isPrismarine && waterAbove) {
            boolean adjacentWater = false;
            Vec3i offset = new Vec3i(1, 0, 1);
            BlockPos min = pos.subtract(offset);
            BlockPos max = pos.add(offset);
            Iterable<MutableBlockPos> adjacentBlocks = BlockPos.getAllInBoxMutable(min, max);
            for (MutableBlockPos target : adjacentBlocks) {
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
        float k = 0.00F;
        if (!(entity instanceof EntityLivingBase)) {
            return new Vec3d(0.02F + k, 0.02F + k, 0.20F + k);
        } else {
            EntityLivingBase living = (EntityLivingBase) entity;
            if (living.isInsideOfMaterial(Material.WATER)) {
                k = EnchantmentHelper.getRespirationModifier(living) * 0.20F;
                if (living.isPotionActive(MobEffects.WATER_BREATHING)) {
                    k = k * 0.30F + 0.60F;
                }
                return new Vec3d(0.02F + k, 0.02F + k, 0.20F + k);
            }
            return lastColor;
        }
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

    public final TorchType getType() {
        return type;
    }

}
