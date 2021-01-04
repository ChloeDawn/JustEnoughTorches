package dev.sapphic.torches;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.checkerframework.checker.nullness.qual.Nullable;

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

  static <T extends IForgeRegistryEntry<T>> void remap(final Mapping<T> mapping, final IForgeRegistry<T> registry) {
    if ("jetorches".equals(mapping.key.getNamespace())) {
      final @Nullable T value = registry.getValue(new ResourceLocation(NAMESPACE, mapping.key.getPath()));
      if (value == null) {
        throw new IllegalStateException(mapping.key.toString());
      }
      mapping.remap(value);
    }
  }
}
