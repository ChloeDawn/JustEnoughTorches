package dev.sapphic.torches;

import dev.sapphic.torches.block.LampBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public final class TorchClient {
  private TorchClient() {
  }

  @SubscribeEvent
  public static void putModels(final ModelRegistryEvent event) {
    ModelLoader.setCustomStateMapper(TorchBlocks.LAMP, new StateMapperBase() {
      @Override
      protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
        final ResourceLocation id = Objects.requireNonNull(state.getBlock().getRegistryName());
        final String name = state.getValue(LampBlock.TYPE).getName() + '_' + id.getPath();
        final ResourceLocation path = new ResourceLocation(id.getNamespace(), name);
        return new ModelResourceLocation(path, "powered=" + state.getValue(LampBlock.POWERED));
      }
    });

    putModel(TorchItems.STONE_TORCH);
    putModel(TorchItems.NETHERRACK_TORCH);
    putModel(TorchItems.PRISMARINE_TORCH);
    putModel(TorchItems.OBSIDIAN_TORCH);
    putModel(TorchItems.GOLD_TORCH);
    putModel(TorchItems.LAMP, 0, "lapis");
    putModel(TorchItems.LAMP, 1, "obsidian");
    putModel(TorchItems.LAMP, 2, "quartz");
    putModel(TorchItems.STONE_STICK);
    putModel(TorchItems.NETHERRACK_STICK);
    putModel(TorchItems.PRISMARINE_STICK);
    putModel(TorchItems.OBSIDIAN_STICK);
    putModel(TorchItems.GOLD_STICK);
  }

  @SubscribeEvent
  public static void modifyFov(final EntityViewRenderEvent.FOVModifier event) {
    if (TorchConfig.prismarineUnderwater) {
      if (event.getState().getBlock() == TorchBlocks.PRISMARINE_TORCH) {
        if (event.getEntity().isInsideOfMaterial(Material.WATER)) {
          event.setFOV((event.getFOV() * 60.0F) / 70.0F);
        }
      }
    }
  }

  @SubscribeEvent
  public static void setupFog(final EntityViewRenderEvent.FogDensity event) {
    if (TorchConfig.prismarineUnderwater) {
      if (event.getState().getBlock() == TorchBlocks.PRISMARINE_TORCH) {
        if (event.getEntity().isInsideOfMaterial(Material.WATER)) {
          GlStateManager.setFog(GlStateManager.FogMode.EXP);
          event.setDensity(0.115F);
          event.setCanceled(true);
        }
      }
    }
  }

  private static void putModel(final Item item) {
    final ResourceLocation id = Objects.requireNonNull(item.getRegistryName());
    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(id, "inventory"));
  }

  private static void putModel(final Item item, final int metadata, final String variant) {
    final ResourceLocation id = Objects.requireNonNull(item.getRegistryName());
    final ResourceLocation path = new ResourceLocation(id.getNamespace(), variant + '_' + id.getPath());
    ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(path, "inventory"));
  }
}