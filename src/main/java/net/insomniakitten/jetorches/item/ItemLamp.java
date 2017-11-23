package net.insomniakitten.jetorches.item;

import net.insomniakitten.jetorches.JETorches;
import net.insomniakitten.jetorches.type.LampType;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public final class ItemLamp extends ItemBlock {

    public ItemLamp(Block block) {
        super(block);
        setRegistryName(JETorches.ID, "lamp");
        setUnlocalizedName(JETorches.ID + ".lamp");
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        LampType type = LampType.getType(stack.getMetadata());
        return getUnlocalizedName() + "_" + type.getName();
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
