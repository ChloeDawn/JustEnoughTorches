package net.insomniakitten.jetorches.item;

import net.insomniakitten.jetorches.block.BlockTorch;
import net.insomniakitten.jetorches.util.IModelled;
import net.insomniakitten.jetorches.util.IOreDict;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;

public final class ItemTorch extends ItemBlock implements IModelled, IOreDict {

    public ItemTorch(BlockTorch block) {
        super(block);
        //noinspection ConstantConditions
        setRegistryName(block.getRegistryName());
    }

    @Override
    public String getOreName() {
        return ((BlockTorch) block).getTorchData().getOreName();
    }

    @Override
    public ModelResourceLocation getModelResourceLocation() {
        return new ModelResourceLocation(getRegistryName(), "inventory");
    }

}
