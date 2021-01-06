package dev.sapphic.torches.block;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLogic;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class SubmersibleTorchBlock extends TorchBlock {
  private static final Material MATERIAL = new MaterialLogic(MapColor.AIR) {
    @Override
    public boolean blocksMovement() {
      return true;
    }

    @Override
    public EnumPushReaction getPushReaction() {
      return EnumPushReaction.NORMAL;
    }
  };

  public SubmersibleTorchBlock(final EnumParticleTypes particle) {
    super(MATERIAL, particle);
  }

  @Override
  public Boolean isEntityInsideMaterial(final IBlockAccess world, final BlockPos pos, final IBlockState state, final Entity entity, final double yToTest, final Material material, final boolean checkHead) {
    if (Material.WATER.equals(world.getBlockState(pos.up()).getMaterial())) {
      boolean adjacentWater = false;
      final Vec3i offset = new Vec3i(1, 0, 1);
      final BlockPos min = pos.subtract(offset);
      final BlockPos max = pos.add(offset);
      for (final MutableBlockPos target : BlockPos.getAllInBoxMutable(min, max)) {
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
  public Vec3d getFogColor(final World world, final BlockPos pos, final IBlockState state, final Entity entity, final Vec3d lastColor, final float partialTicks) {
    final BlockPos above = pos.up();
    final IBlockState other = world.getBlockState(above);
    if (other.getMaterial().isLiquid()) {
      float height = 0.0F;
      if (other.getBlock() instanceof BlockLiquid) {
        int meta = other.getValue(BlockLiquid.LEVEL);
        if (meta >= 8) {
          meta = 0;
        }
        height = ((meta + 1) / 9.0F) - 0.1F;
      }
      if (ActiveRenderInfo.projectViewFromEntity(entity, partialTicks).y > ((above.getY() + 1) - height)) {
        final BlockPos upPos = above.up();
        final IBlockState upState = world.getBlockState(upPos);
        return upState.getBlock().getFogColor(world, upPos, upState, entity, lastColor, partialTicks);
      }
    }
    return super.getFogColor(world, pos, state, entity, lastColor, partialTicks);
  }
}
