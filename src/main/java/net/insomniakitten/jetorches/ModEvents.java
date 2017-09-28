package net.insomniakitten.jetorches;

import net.insomniakitten.jetorches.block.BlockTorch;
import net.insomniakitten.jetorches.type.TorchType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = JETorches.ID, value = Side.CLIENT)
public class ModEvents {

    @SubscribeEvent
    protected static void onViewRender(EntityViewRenderEvent.FOVModifier event) {
        if ((event.getState().getBlock() instanceof BlockTorch)) {
            BlockTorch torch = (BlockTorch) event.getState().getBlock();
            boolean isPrismarine = torch.getType().equals(TorchType.PRISMARINE);
            boolean inWater = event.getEntity().isInsideOfMaterial(Material.WATER);
            if (isPrismarine && ModConfig.prismarineUnderwater && inWater) {
                event.setFOV(event.getFOV() * 60.0F / 70.0F);
            }
        }
    }

    @SubscribeEvent
    protected static void onFogRender(EntityViewRenderEvent.FogDensity event) {
        boolean inWater = event.getEntity().isInsideOfMaterial(Material.WATER);
        if (event.getState().getBlock() instanceof BlockTorch && inWater) {
            event.setCanceled(true);
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            event.setDensity(0.0115F);
        }
    }

}
