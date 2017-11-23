package net.insomniakitten.jetorches.item;

import net.insomniakitten.jetorches.JETorches;
import net.insomniakitten.jetorches.type.MaterialType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public final class ItemMaterial extends Item {

    public ItemMaterial() {
        setRegistryName(JETorches.ID, "material");
        setUnlocalizedName(JETorches.ID + ".material");
        setCreativeTab(JETorches.CTAB);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        MaterialType type = MaterialType.get(stack.getMetadata());
        return JETorches.ID + "." + type.getName();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (MaterialType material : MaterialType.values()) {
                items.add(new ItemStack(this, 1, material.getMetadata()));
            }
        }
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return MaterialType.get(stack.getMetadata()).getStackSize();
    }

}
