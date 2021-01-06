package dev.sapphic.torches;

import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = Torches.NAMESPACE, useMetadata = true,
  // Not supported in metadata file schema
  acceptedMinecraftVersions = "[1.12,1.13)")
public final class Torches {
  public static final String NAMESPACE = "justenoughtorches";

  static final String OLD_NAMESPACE = "jetorches";

  public Torches() {
    FMLCommonHandler.instance().getDataFixer().init(NAMESPACE, 0)
      .registerFix(FixTypes.ITEM_INSTANCE, TorchItems.dataFixer());
  }
}
