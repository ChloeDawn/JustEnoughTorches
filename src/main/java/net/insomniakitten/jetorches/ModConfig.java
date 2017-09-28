package net.insomniakitten.jetorches;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = JETorches.ID, name = JETorches.ID)
@Mod.EventBusSubscriber(modid = JETorches.ID)
public class ModConfig {

    @Config.Name("Prismarine Works Underwater")
    @Config.Comment("If true, the prismarine torch will not break when placed in water.")
    @Config.RequiresMcRestart
    public static boolean prismarineUnderwater = true;

    @SubscribeEvent
    protected static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (JETorches.ID.equals(event.getModID())) {
            ConfigManager.sync(JETorches.ID, Config.Type.INSTANCE);
        }
    }

}
