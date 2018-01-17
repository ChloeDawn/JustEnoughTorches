package net.insomniakitten.jetorches;

import net.insomniakitten.jetorches.block.BlockLamp;
import net.insomniakitten.jetorches.block.BlockTorch;
import net.insomniakitten.jetorches.data.LampData;
import net.insomniakitten.jetorches.data.MaterialData;
import net.insomniakitten.jetorches.data.TorchData;
import net.insomniakitten.jetorches.item.ItemLamp;
import net.insomniakitten.jetorches.item.ItemMaterial;
import net.insomniakitten.jetorches.item.ItemTorch;
import net.insomniakitten.jetorches.util.IModelled;
import net.insomniakitten.jetorches.util.IOreDict;
import net.insomniakitten.jetorches.util.RegistryHolder;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber(modid = JETorches.ID)
public final class JETorchesRegistry {

    private static final RegistryHolder<Block> TORCHES = new RegistryHolder<>();
    private static final RegistryHolder<Block> LAMPS = new RegistryHolder<>();

    private static final RegistryHolder<Item> ITEMS = new RegistryHolder<>();

    private JETorchesRegistry() {}

    @SubscribeEvent
    protected static void onBlockRegistry(RegistryEvent.Register<Block> event) {
        RegistryHolder<Block>.Registry torches = TORCHES.begin(event);
        TorchData.forEach(t -> torches.register(new BlockTorch(t)));
        RegistryHolder<Block>.Registry lamps = LAMPS.begin(event);
        LampData.forEach(l -> lamps.register(new BlockLamp(l)));
    }

    @SubscribeEvent
    protected static void onItemRegistry(RegistryEvent.Register<Item> event) {
        RegistryHolder<Item>.Registry items = ITEMS.begin(event);
        MaterialData.forEach(m -> items.register(new ItemMaterial(m)));
        TORCHES.entries().forEach(t -> items.register(new ItemTorch((BlockTorch) t)));
        LAMPS.entries().forEach(l -> items.register(new ItemLamp((BlockLamp) l)));
        ITEMS.entries().forEach(i -> {
            if (i instanceof IOreDict) {
                OreDictionary.registerOre(((IOreDict) i).getOreName(), i);
            }
        });
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    protected static void onModelRegistry(ModelRegistryEvent event) {
        ITEMS.entries().forEach(i -> {
            if (i instanceof IModelled) {
                ModelResourceLocation mrl = ((IModelled) i).getModelResourceLocation();
                ModelLoader.setCustomModelResourceLocation(i, 0, mrl);
            }
        });
    }

}
