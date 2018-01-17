package net.insomniakitten.jetorches.item;

import net.insomniakitten.jetorches.block.BlockLamp;
import net.insomniakitten.jetorches.util.IModelled;
import net.insomniakitten.jetorches.util.IOreDict;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;

public final class ItemLamp extends ItemBlock implements IModelled, IOreDict {

    public ItemLamp(BlockLamp block) {
        super(block);
        //noinspection ConstantConditions
        setRegistryName(block.getRegistryName());
    }

    @Override
    public String getOreName() {
        return ((BlockLamp) block).getLamp().getOreName();
    }

    @Override
    public ModelResourceLocation getModelResourceLocation() {
        return new ModelResourceLocation(getRegistryName(), "powered=false");
    }

}
