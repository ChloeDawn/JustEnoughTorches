package net.insomniakitten.jetorches.item;

import net.insomniakitten.jetorches.JETorches;
import net.insomniakitten.jetorches.ModRegistry;
import net.insomniakitten.jetorches.type.TorchType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTorch extends Item {

    public ItemTorch() {
        setRegistryName("torch");
        setUnlocalizedName(JETorches.ID + ".torch");
        setCreativeTab(JETorches.CTAB);
        setHasSubtypes(true);
    }

    @Override
    public EnumActionResult onItemUse(
            EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY,
            float hitZ) {
        if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
            pos = pos.offset(facing);
        }

        ItemStack stack = player.getHeldItem(hand);
        Block torch = ModRegistry.BLOCK_TORCHES.get(stack.getMetadata());
        int meta = getMetadata(stack.getMetadata());
        IBlockState state = torch.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, player, hand);

        boolean canPlayerEdit = player.canPlayerEdit(pos, facing, stack);
        boolean mayPlace = world.mayPlace(torch, pos, false, facing, null);

        if (!stack.isEmpty() && canPlayerEdit && mayPlace) {
            if (placeBlockAt(stack, player, world, pos, facing, hitX, hitY, hitZ, state)) {
                IBlockState stateAt = world.getBlockState(pos);
                SoundType soundtype = stateAt.getBlock().getSoundType(stateAt, world, pos, player);

                world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
                        (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                stack.shrink(1);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String name = getUnlocalizedName().replace("item.", "tile.");
        String type = TorchType.getType(stack.getMetadata()).getName();
        return name + "_" + type;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (TorchType torch : TorchType.values()) {
                items.add(new ItemStack(this, 1, torch.getMetadata()));
            }
        }
    }

    public boolean placeBlockAt(
            ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
            float hitZ, IBlockState state) {
        if (world.setBlockState(pos, state, 11)) {
            IBlockState stateAt = world.getBlockState(pos);
            Block torch = ModRegistry.BLOCK_TORCHES.get(stack.getMetadata());
            if (torch.equals(stateAt.getBlock())) {
                torch.onBlockPlacedBy(world, pos, stateAt, player, stack);
                if (player instanceof EntityPlayerMP) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }
            }
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(
            World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
        if (world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
            side = EnumFacing.UP;
        } else {
            pos = pos.offset(side);
        }
        Block torch = ModRegistry.BLOCK_TORCHES.get(stack.getMetadata());
        return world.mayPlace(torch, pos, false, side, null);
    }

}
