package dev.sapphic.torches.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class TorchBlock extends Block {
  private static final PropertyDirection FACING = PropertyDirection.create("facing", dir -> dir != EnumFacing.DOWN);

  private static final EnumFacing[] VALUES = FACING.getAllowedValues().toArray(new EnumFacing[0]);

  private static final ImmutableMap<EnumFacing, AxisAlignedBB> AABBS = Maps.immutableEnumMap(ImmutableMap.of(
    EnumFacing.UP, new AxisAlignedBB(0.4, 0.0, 0.4, 0.6, 0.6, 0.6),
    EnumFacing.NORTH, new AxisAlignedBB(0.35, 0.2, 0.7, 0.65, 0.8, 1.0),
    EnumFacing.SOUTH, new AxisAlignedBB(0.35, 0.2, 0.0, 0.65, 0.8, 0.3),
    EnumFacing.WEST, new AxisAlignedBB(0.7, 0.2, 0.35, 1.0, 0.8, 0.65),
    EnumFacing.EAST, new AxisAlignedBB(0.0, 0.2, 0.35, 0.3, 0.8, 0.65)
  ));

  private final EnumParticleTypes particle;

  protected TorchBlock(final Material material, final EnumParticleTypes particle) {
    super(material);
    this.particle = particle;
  }

  public TorchBlock(final EnumParticleTypes particle) {
    this(Material.CIRCUITS, particle);
  }

  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    return this.getDefaultState().withProperty(FACING, VALUES[meta & 7]);
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(FACING).ordinal() - 1;
  }

  @Override
  @Deprecated
  public IBlockState withRotation(final IBlockState state, final Rotation rot) {
    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }

  @Override
  @Deprecated
  public IBlockState withMirror(final IBlockState state, final Mirror mirror) {
    return state.withRotation(mirror.toRotation(state.getValue(FACING)));
  }

  @Override
  public Block setSoundType(final SoundType sound) {
    return super.setSoundType(sound);
  }

  @Override
  @Deprecated
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @Override
  @Deprecated
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
    return AABBS.get(state.getValue(FACING));
  }

  @Override
  @Deprecated
  public BlockFaceShape getBlockFaceShape(final IBlockAccess world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  @Override
  @Deprecated
  public AxisAlignedBB getCollisionBoundingBox(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
    return NULL_AABB;
  }

  @Override
  @Deprecated
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(final IBlockState state, final World world, final BlockPos pos, final Random rand) {
    if (world.isRemote) {
      double x = pos.getX() + 0.5;
      double y = pos.getY() + 0.7;
      double z = pos.getZ() + 0.5;
      if (state.getValue(FACING).getAxis().isHorizontal()) {
        final EnumFacing offset = state.getValue(FACING).getOpposite();
        x += 0.27 * offset.getXOffset();
        y += 0.22;
        z += 0.27 * offset.getZOffset();
      }
      world.spawnParticle(this.particle, x, y, z, 0.0, 0.0, 0.0);
      if (this.particle == EnumParticleTypes.FLAME) {
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0, 0.0, 0.0);
      }
    }
  }

  @Override
  @Deprecated
  public void neighborChanged(final IBlockState state, final World world, final BlockPos pos, final Block block, final BlockPos fromPos) {
    final EnumFacing facing = state.getValue(FACING);
    final EnumFacing.Axis axis = facing.getAxis();
    final BlockPos offset = pos.offset(facing.getOpposite());
    if ((axis.isHorizontal() && !this.isSolid(world, offset, facing))
      || (axis.isVertical() && !this.canPlaceOn(world, offset))) {
      this.dropBlockAsItem(world, pos, state, 0);
      world.setBlockToAir(pos);
    }
  }

  @Override
  public void onBlockAdded(final World world, final BlockPos pos, final IBlockState state) {
    if (!this.canPlaceAt(world, pos, state.getValue(FACING))) {
      this.dropBlockAsItem(world, pos, state, 0);
      world.setBlockToAir(pos);
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  @Override
  public boolean canPlaceBlockAt(final World world, final BlockPos pos) {
    for (final EnumFacing side : FACING.getAllowedValues()) {
      if (this.canPlaceAt(world, pos, side)) {
        return true;
      }
    }
    return false;
  }

  @Override
  @Deprecated
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
    if (this.canPlaceAt(world, pos, facing)) {
      return this.getDefaultState().withProperty(FACING, facing);
    }
    for (final EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
      if (this.canPlaceAt(world, pos, side)) {
        return this.getDefaultState().withProperty(FACING, side);
      }
    }
    return this.getDefaultState().withProperty(FACING, EnumFacing.UP);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }

  @Override
  public boolean doesSideBlockRendering(final IBlockState state, final IBlockAccess world, final BlockPos pos, final EnumFacing side) {
    return world.getBlockState(pos.offset(side)).getMaterial().isLiquid()
      && world.getBlockState(pos.up()).getMaterial().isLiquid();
  }

  private boolean canPlaceOn(final World world, final BlockPos pos) {
    final IBlockState state = world.getBlockState(pos);
    return state.getBlock().canPlaceTorchOnTop(state, world, pos);
  }

  private boolean isSolid(final World world, final BlockPos pos, final EnumFacing facing) {
    return world.getBlockState(pos).getBlockFaceShape(world, pos, facing) == BlockFaceShape.SOLID;
  }

  private boolean canPlaceAt(final World world, final BlockPos pos, final EnumFacing side) {
    final BlockPos offset = pos.offset(side.getOpposite());
    return this.canPlaceOn(world, offset) && (side != EnumFacing.DOWN) && this.isSolid(world, offset, side);
  }
}
