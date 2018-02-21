package net.insomniakitten.jetorches.util;

import net.insomniakitten.jetorches.JETorches;
import net.insomniakitten.jetorches.JETorchesConfig;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.SidedProxy;

import java.util.Optional;

public abstract class LightProxy {

    @SidedProxy(modId = JETorches.ID)
    private static LightProxy instance;

    private LightProxy() {}

    public static LightProxy getInstance() {
        return instance;
    }

    public abstract Optional<Integer> getActualLightValue();

    public static final class ClientProxy extends LightProxy {
        private Boolean mirageLoaded = null;

        @Override
        public Optional<Integer> getActualLightValue() {
            if (JETorchesConfig.coloredLighting) {
                if (mirageLoaded == null) {
                    mirageLoaded = Loader.isModLoaded("mirage");
                }
                if (mirageLoaded) {
                    return Optional.of(0);
                }
            }
            return Optional.empty();
        }
    }

    public static final class ServerProxy extends LightProxy {
        @Override
        public Optional<Integer> getActualLightValue() {
            return Optional.empty();
        }
    }

}
