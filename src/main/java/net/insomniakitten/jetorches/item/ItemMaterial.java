package net.insomniakitten.jetorches.item;

import net.insomniakitten.jetorches.JETorches;
import net.insomniakitten.jetorches.type.MaterialType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class ItemMaterial extends Item {

    public ItemMaterial() {
        final String name = "material";
        setRegistryName(name);
        setUnlocalizedName(JETorches.ID + "." + name);
        setCreativeTab(JETorches.CTAB);
        setHasSubtypes(true);
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack stack) {
        String type = MaterialType.get(stack.getMetadata()).getName();
        return getUnlocalizedName() + "." + type;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
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
