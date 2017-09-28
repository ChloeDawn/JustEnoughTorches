package net.insomniakitten.jetorches;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = JETorches.ID,
     name = JETorches.NAME,
     version = JETorches.VERSION,
     acceptedMinecraftVersions = JETorches.MC_VERSIONS,
     dependencies = JETorches.DEPENDENCIES)
public class JETorches {

    public static final String ID = "jetorches";
    public static final String NAME = "Just Enough Torches";
    public static final String VERSION = "%VERSION%";
    public static final String MC_VERSIONS = "[1.12,1.13)";
    public static final String DEPENDENCIES = "required-after:forge@[14.21.1.2387,);";

    public static final CreativeTabs CTAB = new CreativeTabs(ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModRegistry.ITEM_TORCH, 1, 1);
        }
    };

}
