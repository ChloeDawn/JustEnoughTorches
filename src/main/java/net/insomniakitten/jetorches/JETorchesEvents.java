package net.insomniakitten.jetorches;

import net.insomniakitten.jetorches.block.BlockTorch;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = JETorches.ID, value = Side.CLIENT)
public final class JETorchesEvents {

    private JETorchesEvents() {}

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onViewRenderFOVModifier(EntityViewRenderEvent.FOVModifier event) {
        Block block = event.getState().getBlock();
        if (block instanceof BlockTorch && ((BlockTorch) block).getVariant().canWorkUnderwater()) {
            if (event.getEntity().isInsideOfMaterial(Material.WATER)) {
                event.setFOV(event.getFOV() * 60.0F / 70.0F);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onViewRenderFogDensity(EntityViewRenderEvent.FogDensity event) {
        Block block = event.getState().getBlock();
        if (block instanceof BlockTorch && ((BlockTorch) block).getVariant().canWorkUnderwater()) {
            if (event.getEntity().isInsideOfMaterial(Material.WATER)) {
                event.setCanceled(true);
                GlStateManager.setFog(GlStateManager.FogMode.EXP);
                event.setDensity(0.115F);
            }
        }
    }

}
