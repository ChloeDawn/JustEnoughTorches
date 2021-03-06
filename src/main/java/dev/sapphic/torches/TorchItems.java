package dev.sapphic.torches;

import dev.sapphic.torches.block.LampBlock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Torches.NAMESPACE)
@EventBusSubscriber(modid = Torches.NAMESPACE)
public final class TorchItems {
  public static final Item STONE_TORCH = new ItemBlock(TorchBlocks.STONE_TORCH);
  public static final Item NETHERRACK_TORCH = new ItemBlock(TorchBlocks.NETHERRACK_TORCH);
  public static final Item PRISMARINE_TORCH = new ItemBlock(TorchBlocks.PRISMARINE_TORCH);
  public static final Item OBSIDIAN_TORCH = new ItemBlock(TorchBlocks.OBSIDIAN_TORCH);
  public static final Item GOLD_TORCH = new ItemBlock(TorchBlocks.GOLD_TORCH);

  public static final Item LAMP = new ItemMultiTexture(TorchBlocks.LAMP, Blocks.AIR, LampBlock.Type::getName);

  public static final Item STONE_STICK = new Item().setCreativeTab(CreativeTabs.MATERIALS);
  public static final Item NETHERRACK_STICK = new Item().setCreativeTab(CreativeTabs.MATERIALS);
  public static final Item PRISMARINE_STICK = new Item().setCreativeTab(CreativeTabs.MATERIALS);
  public static final Item OBSIDIAN_STICK = new Item().setCreativeTab(CreativeTabs.MATERIALS);
  public static final Item GOLD_STICK = new Item().setCreativeTab(CreativeTabs.MATERIALS);

  private TorchItems() {
  }

  @SubscribeEvent
  public static void registerAll(final RegistryEvent.Register<Item> event) {
    final IForgeRegistry<Item> registry = event.getRegistry();
    register(registry, "stone_torch", STONE_TORCH);
    register(registry, "netherrack_torch", NETHERRACK_TORCH);
    register(registry, "prismarine_torch", PRISMARINE_TORCH);
    register(registry, "obsidian_torch", OBSIDIAN_TORCH);
    register(registry, "gold_torch", GOLD_TORCH);

    register(registry, "lamp", LAMP);

    register(registry, "stone_stick", STONE_STICK);
    register(registry, "netherrack_stick", NETHERRACK_STICK);
    register(registry, "prismarine_stick", PRISMARINE_STICK);
    register(registry, "obsidian_stick", OBSIDIAN_STICK);
    register(registry, "gold_stick", GOLD_STICK);

    OreDictionary.registerOre("torch", STONE_TORCH);
    OreDictionary.registerOre("torchStone", STONE_TORCH);
    OreDictionary.registerOre("torch", NETHERRACK_TORCH);
    OreDictionary.registerOre("torchNetherrack", NETHERRACK_TORCH);
    OreDictionary.registerOre("torch", PRISMARINE_TORCH);
    OreDictionary.registerOre("torchPrismarine", PRISMARINE_TORCH);
    OreDictionary.registerOre("torch", OBSIDIAN_TORCH);
    OreDictionary.registerOre("torchObsidian", OBSIDIAN_TORCH);
    OreDictionary.registerOre("torch", GOLD_TORCH);
    OreDictionary.registerOre("torchGold", GOLD_TORCH);

    OreDictionary.registerOre("stickStone", STONE_STICK);
    OreDictionary.registerOre("stickNetherrack", NETHERRACK_STICK);
    OreDictionary.registerOre("stickPrismarine", PRISMARINE_STICK);
    OreDictionary.registerOre("stickObsidian", OBSIDIAN_STICK);
    OreDictionary.registerOre("stickGold", GOLD_STICK);
  }

  @SubscribeEvent
  public static void remapAll(final RegistryEvent.MissingMappings<Item> event) {
    for (final Mapping<Item> mapping : event.getAllMappings()) {
      if (Torches.OLD_NAMESPACE.equals(mapping.key.getNamespace())) {
        if ("lamp".equals(mapping.key.getPath())) {
          mapping.remap(LAMP);
        } else {
          mapping.ignore();
        }
      }
    }
  }

  static IFixableData dataFixer() {
    return new IFixableData() {
      private final String[] torches = {
        "stone_torch", "netherrack_torch", "prismarine_torch", "obsidian_torch", "gold_torch"
      };

      private final String[] materials = {
        "stone_stick", "netherrack_stick", "prismarine_stick", "obsidian_stick", "gold_stick"
      };

      @Override
      public int getFixVersion() {
        return 0;
      }

      @Override
      public NBTTagCompound fixTagCompound(final NBTTagCompound tag) {
        final String id = tag.getString("id");
        final boolean isTorch = (Torches.OLD_NAMESPACE + ":torch").equals(id);
        if (isTorch || (Torches.OLD_NAMESPACE + ":material").equals(id)) {
          final String[] names = isTorch ? this.torches : this.materials;
          final int index = Math.min(tag.getShort("Damage"), names.length);
          tag.setString("id", Torches.NAMESPACE + ':' + names[index]);
          tag.setShort("Damage", (short) 0);
          return tag;
        }
        return tag;
      }
    };
  }

  private static void register(final IForgeRegistry<Item> registry, final String name, final Item item) {
    item.setRegistryName(new ResourceLocation(Torches.NAMESPACE, name));
    item.setTranslationKey(Torches.NAMESPACE + '.' + name);
    registry.register(item);
  }
}
