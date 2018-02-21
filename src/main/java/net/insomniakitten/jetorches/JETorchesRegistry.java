package net.insomniakitten.jetorches;

import net.insomniakitten.jetorches.block.BlockLamp;
import net.insomniakitten.jetorches.block.BlockTorch;
import net.insomniakitten.jetorches.color.ColoredLight;
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
import net.minecraftforge.fml.common.registry.GameRegistry;
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
        GameRegistry.registerTileEntity(ColoredLight.class, ColoredLight.ID);
        RegistryHolder<Block>.Registry torches = TORCHES.begin(event);
        for (TorchData torch : TorchData.VALUES) {
            torches.register(new BlockTorch(torch));
        }
        RegistryHolder<Block>.Registry lamps = LAMPS.begin(event);
        for (LampData lamp : LampData.VALUES) {
            lamps.register(new BlockLamp(lamp));
        }
    }

    @SubscribeEvent
    protected static void onItemRegistry(RegistryEvent.Register<Item> event) {
        RegistryHolder<Item>.Registry items = ITEMS.begin(event);
        for (MaterialData material : MaterialData.VALUES) {
            items.register(new ItemMaterial(material));
        }
        for (Block block : TORCHES.entries()) {
            items.register(new ItemTorch((BlockTorch) block));
        }
        for (Block block : LAMPS.entries()) {
            items.register(new ItemLamp((BlockLamp) block));
        }
        for (Item item : ITEMS.entries()) {
            if (item instanceof IOreDict) {
                OreDictionary.registerOre(((IOreDict) item).getOreName(), item);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    protected static void onModelRegistry(ModelRegistryEvent event) {
        for (Item item : ITEMS.entries()) {
            if (item instanceof IModelled) {
                ModelResourceLocation mrl = ((IModelled) item).getModelResourceLocation();
                ModelLoader.setCustomModelResourceLocation(item, 0, mrl);
            }
        }
    }

}
