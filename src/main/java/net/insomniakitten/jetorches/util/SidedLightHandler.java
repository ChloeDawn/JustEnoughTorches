package net.insomniakitten.jetorches.util;

import net.insomniakitten.jetorches.JETorches;
import net.insomniakitten.jetorches.JETorchesConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.OptionalInt;

public final class SidedLightHandler {

    @SidedProxy(modId = JETorches.ID)
    private static Impl instance;

    private SidedLightHandler() {}

    public static OptionalInt getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return instance.apply(state, world, pos);
    }

    private static abstract class Impl {
        public abstract OptionalInt apply(IBlockState state, IBlockAccess world, BlockPos pos);
    }

    @SideOnly(Side.CLIENT)
    public static final class ClientProxy extends Impl {
        private final boolean isMirageLoaded = Loader.isModLoaded("mirage");

        @Override
        public OptionalInt apply(IBlockState state, IBlockAccess world, BlockPos pos) {
            if (isMirageLoaded && JETorchesConfig.coloredLighting) {
                Minecraft mc = FMLClientHandler.instance().getClient();
                if (mc.world != null && !mc.world.isRemote) {
                    mc.world.setLightFor(EnumSkyBlock.BLOCK, pos, 0);
                } else return OptionalInt.of(0);
            }
            return OptionalInt.empty();
        }
    }

    @SideOnly(Side.SERVER)
    public static final class ServerProxy extends Impl {
        @Override
        public OptionalInt apply(IBlockState state, IBlockAccess world, BlockPos pos) {
            return OptionalInt.empty();
        }
    }

}
