package dev.sapphic.torches;

import dev.sapphic.torches.block.LampBlock;
import dev.sapphic.torches.block.PrismarineTorchBlock;
import dev.sapphic.torches.block.TorchBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

public final class TorchBlocks {
  public static final Block STONE_TORCH = new TorchBlock(EnumParticleTypes.FLAME).setSoundType(SoundType.STONE).setLightLevel(14 / 15.0F);
  public static final Block NETHERRACK_TORCH = new TorchBlock(EnumParticleTypes.SMOKE_NORMAL).setSoundType(SoundType.STONE).setLightLevel(10 / 15.0F);
  public static final Block PRISMARINE_TORCH = new PrismarineTorchBlock(EnumParticleTypes.WATER_DROP).setSoundType(SoundType.GLASS).setLightLevel(15 / 15.0F);
  public static final Block OBSIDIAN_TORCH = new TorchBlock(EnumParticleTypes.FLAME).setSoundType(SoundType.STONE).setHardness(0.2F).setResistance(3000.0F).setLightLevel(13 / 15.0F);
  public static final Block GOLD_TORCH = new TorchBlock(EnumParticleTypes.FLAME).setSoundType(SoundType.METAL).setResistance(30.0F).setLightLevel(14 / 15.0F);

  public static final Block LAMP = new LampBlock(); // LAPIS, OBSIDIAN, QUARTZ

  private TorchBlocks() {
  }

  @SubscribeEvent
  public static void registerAll(final RegistryEvent.Register<Block> event) {
    final IForgeRegistry<Block> registry = event.getRegistry();
    register(registry, "stone_torch", STONE_TORCH);
    register(registry, "netherrack_torch", NETHERRACK_TORCH);
    register(registry, "prismarine_torch", PRISMARINE_TORCH);
    register(registry, "obsidian_torch", OBSIDIAN_TORCH);
    register(registry, "gold_torch", GOLD_TORCH);

    register(registry, "lamp", LAMP);
  }

  @SubscribeEvent
  public static void remapAll(final RegistryEvent.MissingMappings<Block> event) {
    for (final RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getAllMappings()) {
      if ("jetorches".equals(mapping.key.getNamespace())) {
        final ResourceLocation key = new ResourceLocation(Torches.NAMESPACE, mapping.key.getPath());
        mapping.remap(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(key)));
      }
    }
  }

  private static void register(final IForgeRegistry<Block> registry, final String name, final Block block) {
    block.setRegistryName(new ResourceLocation(Torches.NAMESPACE, name));
    block.setTranslationKey(Torches.NAMESPACE + '.' + name);
    block.setCreativeTab(Torches.CREATIVE_TAB);
    registry.register(block);
  }
}
