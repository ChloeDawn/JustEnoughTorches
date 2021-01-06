package dev.sapphic.torches;

import com.google.common.collect.ImmutableMap;
import dev.sapphic.torches.block.LampBlock;
import dev.sapphic.torches.block.SubmersibleTorchBlock;
import dev.sapphic.torches.block.TorchBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Torches.NAMESPACE)
@EventBusSubscriber(modid = Torches.NAMESPACE)
public final class TorchBlocks {
  public static final Block STONE_TORCH = new TorchBlock(EnumParticleTypes.FLAME)
    .setSoundType(SoundType.STONE).setLightLevel(14 / 15.0F).setCreativeTab(CreativeTabs.DECORATIONS);

  public static final Block NETHERRACK_TORCH = new TorchBlock(EnumParticleTypes.SMOKE_NORMAL)
    .setSoundType(SoundType.STONE).setLightLevel(10 / 15.0F).setCreativeTab(CreativeTabs.DECORATIONS);

  public static final Block PRISMARINE_TORCH = new SubmersibleTorchBlock(EnumParticleTypes.WATER_DROP)
    .setSoundType(SoundType.GLASS).setLightLevel(15 / 15.0F).setCreativeTab(CreativeTabs.DECORATIONS);

  public static final Block OBSIDIAN_TORCH = new TorchBlock(EnumParticleTypes.FLAME)
    .setSoundType(SoundType.STONE).setLightLevel(13 / 15.0F).setCreativeTab(CreativeTabs.DECORATIONS)
    .setHardness(0.2F).setResistance(3000.0F);

  public static final Block GOLD_TORCH = new TorchBlock(EnumParticleTypes.FLAME)
    .setSoundType(SoundType.METAL).setLightLevel(14 / 15.0F).setCreativeTab(CreativeTabs.DECORATIONS)
    .setResistance(30.0F);

  public static final Block LAMP = new LampBlock().setCreativeTab(CreativeTabs.REDSTONE);

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
    final ImmutableMap<String, Block> blocks = ImmutableMap.<String, Block>builder()
      .put("torch_stone", STONE_TORCH)
      .put("torch_nether", NETHERRACK_TORCH)
      .put("torch_prismarine", PRISMARINE_TORCH)
      .put("torch_obsidian", OBSIDIAN_TORCH)
      .put("torch_golden", GOLD_TORCH)
      .put("lamp", LAMP)
      .build();
    for (final Mapping<Block> mapping : event.getAllMappings()) {
      if (Torches.OLD_NAMESPACE.equals(mapping.key.getNamespace())) {
        mapping.remap(blocks.get(mapping.key.getPath()));
      }
    }
  }

  private static void register(final IForgeRegistry<Block> registry, final String name, final Block block) {
    block.setRegistryName(new ResourceLocation(Torches.NAMESPACE, name));
    block.setTranslationKey(Torches.NAMESPACE + '.' + name);
    registry.register(block);
  }
}
