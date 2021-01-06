package dev.sapphic.torches;

import dev.sapphic.torches.block.LampBlock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Torches.NAMESPACE, useMetadata = true,
  // Not supported in metadata file schema
  acceptedMinecraftVersions = "[1.12,1.13)")
public final class Torches {
  public static final String NAMESPACE = "justenoughtorches";

  public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(NAMESPACE) {
    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslationKey() {
      return "item_group." + NAMESPACE + ".label";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack createIcon() {
      return new ItemStack(TorchItems.STONE_TORCH);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(final NonNullList<ItemStack> stacks) {
      stacks.add(new ItemStack(TorchItems.STONE_TORCH));
      stacks.add(new ItemStack(TorchItems.NETHERRACK_TORCH));
      stacks.add(new ItemStack(TorchItems.PRISMARINE_TORCH));
      stacks.add(new ItemStack(TorchItems.OBSIDIAN_TORCH));
      stacks.add(new ItemStack(TorchItems.GOLD_TORCH));
      for (final LampBlock.Type type : LampBlock.Type.values()) {
        stacks.add(new ItemStack(TorchItems.LAMP, 1, type.ordinal()));
      }
      stacks.add(new ItemStack(TorchItems.STONE_STICK));
      stacks.add(new ItemStack(TorchItems.NETHERRACK_STICK));
      stacks.add(new ItemStack(TorchItems.PRISMARINE_STICK));
      stacks.add(new ItemStack(TorchItems.OBSIDIAN_STICK));
      stacks.add(new ItemStack(TorchItems.GOLD_STICK));
    }
  };

  public Torches() {
    MinecraftForge.EVENT_BUS.register(TorchBlocks.class);
    MinecraftForge.EVENT_BUS.register(TorchItems.class);
    if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
      MinecraftForge.EVENT_BUS.register(TorchClient.class);
    }
    FMLCommonHandler.instance().getDataFixer().init(NAMESPACE, 0)
      .registerFix(FixTypes.ITEM_INSTANCE, TorchItems.dataFixer());
  }
}
