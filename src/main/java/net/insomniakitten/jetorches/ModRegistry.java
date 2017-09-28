package net.insomniakitten.jetorches;

import net.insomniakitten.jetorches.block.BlockLamp;
import net.insomniakitten.jetorches.block.BlockTorch;
import net.insomniakitten.jetorches.item.ItemBlockLamp;
import net.insomniakitten.jetorches.item.ItemMaterial;
import net.insomniakitten.jetorches.item.ItemTorch;
import net.insomniakitten.jetorches.type.LampType;
import net.insomniakitten.jetorches.type.MaterialType;
import net.insomniakitten.jetorches.type.TorchType;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = JETorches.ID)
public class ModRegistry {

    public static final List<BlockTorch> BLOCK_TORCHES = new ArrayList<>();
    public static final BlockLamp BLOCK_LAMP = new BlockLamp();

    public static final ItemTorch ITEM_TORCH = new ItemTorch();
    public static final ItemBlockLamp ITEM_LAMP = new ItemBlockLamp(BLOCK_LAMP);
    public static final ItemMaterial ITEM_MATERIAL = new ItemMaterial();

    public static final ItemStack ANY_TORCH = new ItemStack(ITEM_TORCH, 1, Short.MAX_VALUE);

    @SubscribeEvent
    protected static void onBlockRegistry(RegistryEvent.Register<Block> event) {
        Arrays.stream(TorchType.values()).map(BlockTorch::new).forEach(BLOCK_TORCHES::add);
        BLOCK_TORCHES.forEach(event.getRegistry()::register);
        event.getRegistry().register(BLOCK_LAMP);
    }

    @SubscribeEvent
    protected static void onItemRegistry(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ITEM_LAMP, ITEM_TORCH, ITEM_MATERIAL);
        OreDictionary.registerOre("torch", ANY_TORCH);
        for (TorchType torch : TorchType.values()) {
            ItemStack stack = new ItemStack(ITEM_TORCH, 1, torch.getMetadata());
            OreDictionary.registerOre(torch.getOreDict(), stack);
        }
        for (LampType lamp : LampType.values()) {
            ItemStack stack = new ItemStack(ITEM_LAMP, 1, lamp.getMetadata());
            OreDictionary.registerOre(lamp.getOreDict(), stack);
        }
        for (MaterialType material : MaterialType.values()) {
            ItemStack stack = new ItemStack(ITEM_MATERIAL, 1, material.getMetadata());
            OreDictionary.registerOre(material.getOreDict(), stack);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    protected static void onModelRegistry(ModelRegistryEvent event) {
        ModelLoader.setCustomStateMapper(BLOCK_LAMP, new BlockLamp.LampStateMapper());
        for (int i = 0; i < TorchType.values().length; ++i) {
            String type = TorchType.getType(i).getName();
            ResourceLocation rl = new ResourceLocation(JETorches.ID, "torch_" + type);
            ModelResourceLocation mrl = new ModelResourceLocation(rl, "inventory");
            ModelLoader.setCustomModelResourceLocation(ITEM_TORCH, i, mrl);
        }
        for (int i = 0; i < LampType.values().length; ++i) {
            String type = LampType.getType(i).getName();
            ResourceLocation rl = new ResourceLocation(JETorches.ID, "lamp_" + type);
            ModelResourceLocation mrl = new ModelResourceLocation(rl, "powered=false");
            ModelLoader.setCustomModelResourceLocation(ITEM_LAMP, i, mrl);
        }
        for (int i = 0; i < MaterialType.values().length; ++i) {
            String type = MaterialType.get(i).getName();
            ResourceLocation rl = ITEM_MATERIAL.getRegistryName();
            ModelResourceLocation mrl = new ModelResourceLocation(rl, "type=" + type);
            ModelLoader.setCustomModelResourceLocation(ITEM_MATERIAL, i, mrl);
        }
    }

}
