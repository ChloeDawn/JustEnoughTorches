package dev.sapphic.torches;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
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
  };

  public Torches() {
    MinecraftForge.EVENT_BUS.register(TorchBlocks.class);
    MinecraftForge.EVENT_BUS.register(TorchItems.class);
    MinecraftForge.EVENT_BUS.register(TorchConfig.class);
    if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
      MinecraftForge.EVENT_BUS.register(TorchClient.class);
    }
    FMLCommonHandler.instance().getDataFixer().init(NAMESPACE, 0)
      .registerFix(FixTypes.ITEM_INSTANCE, TorchItems.dataFixer());
  }
}
