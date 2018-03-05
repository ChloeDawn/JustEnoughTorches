package net.insomniakitten.jetorches.item;

import net.insomniakitten.jetorches.JETorches;
import net.insomniakitten.jetorches.data.MaterialData;
import net.insomniakitten.jetorches.util.ModelSupplier;
import net.insomniakitten.jetorches.util.OreNameSupplier;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

import java.util.List;

public final class ItemMaterial extends Item implements ModelSupplier, OreNameSupplier {

    private final MaterialData data;

    public ItemMaterial(MaterialData data) {
        this.data = data;
        setRegistryName(JETorches.ID, data.getName());
        setUnlocalizedName(JETorches.ID + "." + data.getName());
        setCreativeTab(JETorches.TAB);
    }

    @Override
    public List<String> getOreNames() {
        return data.getOreNames();
    }

    @Override
    public ModelResourceLocation getModelResourceLocation() {
        return new ModelResourceLocation(getRegistryName(), "inventory");
    }

}
