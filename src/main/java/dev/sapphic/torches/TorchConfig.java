package dev.sapphic.torches;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Torches.NAMESPACE)
public final class TorchConfig {
  @Name("prismarineUnderwater")
  @Comment("If true, the prismarine torch will not break when placed in water.")
  public static boolean prismarineUnderwater = true;

  private TorchConfig() {
  }

  @SubscribeEvent
  public static void changed(final ConfigChangedEvent.OnConfigChangedEvent event) {
    if (Torches.NAMESPACE.equals(event.getModID())) {
      ConfigManager.sync(Torches.NAMESPACE, Config.Type.INSTANCE);
    }
  }
}
