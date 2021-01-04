package dev.sapphic.torches.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Locale;

public final class LampBlock extends Block {
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  public static final PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);

  private static final Type[] TYPES = Type.values();

  public LampBlock() {
    super(Material.GLASS);
    this.setSoundType(SoundType.GLASS);
    this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false).withProperty(TYPE, Type.LAPIS));
  }

  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    return this.getDefaultState().withProperty(POWERED, (meta & 0b1) != 0).withProperty(TYPE, TYPES[meta >> 0b1]);
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return (state.getValue(POWERED) ? 1 : 0) | (state.getValue(TYPE).ordinal() << 0b1);
  }

  @Override
  @Deprecated
  public float getBlockHardness(final IBlockState state, final World world, final BlockPos pos) {
    return state.getValue(TYPE).hardness;
  }

  @Override
  @Deprecated
  public void neighborChanged(final IBlockState state, final World world, final BlockPos pos, final Block block, final BlockPos offset) {
    this.updatePoweredState(state, world, pos);
  }

  @Override
  public void onBlockAdded(final World world, final BlockPos pos, final IBlockState state) {
    this.updatePoweredState(state, world, pos);
  }

  @Override
  public int damageDropped(final IBlockState state) {
    return state.getValue(TYPE).ordinal();
  }

  @Override
  public void getSubBlocks(final CreativeTabs tab, final NonNullList<ItemStack> stacks) {
    for (int metadata = 0; metadata < TYPES.length; metadata++) {
      stacks.add(new ItemStack(this, 1, metadata));
    }
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, POWERED, TYPE);
  }

  @Override
  public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
    return state.getValue(POWERED) ? 15 : 0;
  }

  @Override
  public float getExplosionResistance(final World world, final BlockPos pos, final @Nullable Entity exploder, final Explosion explosion) {
    return world.getBlockState(pos).getValue(TYPE).resistance;
  }

  @Override
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer, final EnumHand hand) {
    return this.getDefaultState().withProperty(TYPE, TYPES[meta % TYPES.length]);
  }

  private void updatePoweredState(final IBlockState state, final World world, final BlockPos pos) {
    if (!world.isRemote) {
      final boolean powered = world.isBlockPowered(pos);
      if (powered != state.getValue(POWERED)) {
        world.setBlockState(pos, state.withProperty(POWERED, powered), 2);
      }
    }
  }

  public enum Type implements IStringSerializable {
    LAPIS(0.3F, 1.5F), OBSIDIAN(2.0F, 2000.0F), QUARTZ(0.5F, 3.0F);

    private final float hardness;
    private final float resistance;

    Type(final float hardness, final float resistance) {
      this.hardness = hardness;
      this.resistance = (resistance * 3.0F) / 15.0F;
    }

    @Override
    public String getName() {
      return this.name().toLowerCase(Locale.ROOT);
    }
  }
}
