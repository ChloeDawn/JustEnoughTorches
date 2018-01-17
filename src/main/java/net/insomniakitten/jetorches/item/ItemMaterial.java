package net.insomniakitten.jetorches.item;

import net.insomniakitten.jetorches.JETorches;
import net.insomniakitten.jetorches.data.MaterialData;
import net.insomniakitten.jetorches.util.IModelled;
import net.insomniakitten.jetorches.util.IOreDict;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class ItemMaterial extends Item implements IModelled, IOreDict {

    private final MaterialData data;

    public ItemMaterial(MaterialData data) {
        this.data = data;
        setRegistryName(JETorches.ID, data.getName());
        setUnlocalizedName(JETorches.ID + "." + data.getName());
        setCreativeTab(JETorches.TAB);
    }

    @Override
    public String getOreName() {
        return data.getOreName();
    }

    @Override
    public ModelResourceLocation getModelResourceLocation() {
        return new ModelResourceLocation(getRegistryName(), "inventory");
    }

}
