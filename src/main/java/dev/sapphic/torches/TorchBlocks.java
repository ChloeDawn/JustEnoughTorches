package dev.sapphic.torches;

import com.google.common.collect.ImmutableMap;
import dev.sapphic.torches.block.LampBlock;
import dev.sapphic.torches.block.PrismarineTorchBlock;
import dev.sapphic.torches.block.TorchBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

@ObjectHolder(Torches.NAMESPACE)
public final class TorchBlocks {
  public static final Block STONE_TORCH = new TorchBlock(EnumParticleTypes.FLAME)
    .setSoundType(SoundType.STONE).setLightLevel(14 / 15.0F);

  public static final Block NETHERRACK_TORCH = new TorchBlock(EnumParticleTypes.SMOKE_NORMAL)
    .setSoundType(SoundType.STONE).setLightLevel(10 / 15.0F);

  public static final Block PRISMARINE_TORCH = new PrismarineTorchBlock(EnumParticleTypes.WATER_DROP)
    .setSoundType(SoundType.GLASS).setLightLevel(15 / 15.0F);

  public static final Block OBSIDIAN_TORCH = new TorchBlock(EnumParticleTypes.FLAME)
    .setSoundType(SoundType.STONE).setHardness(0.2F).setResistance(3000.0F).setLightLevel(13 / 15.0F);

  public static final Block GOLD_TORCH = new TorchBlock(EnumParticleTypes.FLAME)
    .setSoundType(SoundType.METAL).setResistance(30.0F).setLightLevel(14 / 15.0F);

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
    final ImmutableMap<String, String> names = ImmutableMap.<String, String>builder()
      .put("torch_stone", "stone_torch")
      .put("torch_nether", "netherrack_torch")
      .put("torch_prismarine", "prismarine_torch")
      .put("torch_obsidian", "obsidian_torch")
      .put("torch_golden", "gold_torch")
      .put("lamp", "lamp")
      .build();
    for (final Mapping<Block> mapping : event.getAllMappings()) {
      if ("jetorches".equals(mapping.key.getNamespace())) {
        final String oldName = mapping.key.getPath();
        final String newName = Objects.requireNonNull(names.get(oldName), oldName);
        final ResourceLocation id = new ResourceLocation(Torches.NAMESPACE, newName);
        mapping.remap(Objects.requireNonNull(event.getRegistry().getValue(id), id::toString));
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
