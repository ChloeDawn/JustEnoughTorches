package net.insomniakitten.jetorches.util;

import net.insomniakitten.jetorches.JETorches;
import net.insomniakitten.jetorches.JETorchesConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class SidedLightHandler {

    @SidedProxy(modId = JETorches.ID)
    private static Impl instance;

    private SidedLightHandler() {}

    public static int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos, int delegate) {
        return instance.apply(state, world, pos, delegate);
    }

    private static abstract class Impl {
        public abstract int apply(IBlockState state, IBlockAccess world, BlockPos pos, int delegate);
    }

    @SideOnly(Side.CLIENT)
    public static final class ClientProxy extends Impl {
        private final boolean isMirageLoaded = Loader.isModLoaded("mirage");

        @Override
        public int apply(IBlockState state, IBlockAccess world, BlockPos pos, int delegate) {
            if (isMirageLoaded && JETorchesConfig.coloredLighting) {
                Minecraft mc = FMLClientHandler.instance().getClient();
                if (mc.world != null && mc.world.isRemote) {
                    return 0;
                }
            }
            return delegate;
        }
    }

    @SideOnly(Side.SERVER)
    public static final class ServerProxy extends Impl {
        @Override
        public int apply(IBlockState state, IBlockAccess world, BlockPos pos, int delegate) {
            return delegate;
        }
    }

}
