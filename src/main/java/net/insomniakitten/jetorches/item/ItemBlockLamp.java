package net.insomniakitten.jetorches.item;

import net.insomniakitten.jetorches.type.LampType;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemBlockLamp extends ItemBlock {

    public ItemBlockLamp(Block block) {
        super(block);
        setRegistryName(block.getRegistryName());
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String type = LampType.getType(stack.getMetadata()).getName();
        return getUnlocalizedName() + "_" + type;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (LampType lamp : LampType.values()) {
                items.add(new ItemStack(this, 1, lamp.getMetadata()));
            }
        }
    }

}
