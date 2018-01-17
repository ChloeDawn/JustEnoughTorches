package net.insomniakitten.jetorches;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = JETorches.ID, name = JETorches.ID)
@Mod.EventBusSubscriber(modid = JETorches.ID)
public final class JETorchesConfig {

    @Config.Name("underwater_torches")
    @Config.Comment("If true, the prismarine torch will not break when placed in water.")
    @Config.RequiresMcRestart
    public static boolean prismarineUnderwater = true;

    @Config.Name("colored_lighting")
    @Config.Comment("If true, torches will produce colored lighting when Mirage is present.")
    public static boolean coloredLighting = true;

    private JETorchesConfig() {}

    @SubscribeEvent
    protected static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (JETorches.ID.equals(event.getModID())) {
            ConfigManager.sync(JETorches.ID, Config.Type.INSTANCE);
        }
    }

}
