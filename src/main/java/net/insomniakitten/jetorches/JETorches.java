package net.insomniakitten.jetorches;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = JETorches.ID, name = JETorches.NAME, version = JETorches.VERSION, dependencies = JETorches.DEPENDENCIES)
public final class JETorches {

    public static final String ID = "jetorches";
    public static final String NAME = "Just Enough Torches";
    public static final String VERSION = "%VERSION%";
    public static final String DEPENDENCIES = "required-after:forge@[14.21.1.2387,);";

    public static final CreativeTabs CTAB = new CreativeTabs(ID) {
        @Override
        @SideOnly(Side.CLIENT)
        public String getTranslatedTabLabel() {
            return "tab." + ID + ".name";
        }

        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return new ItemStack(JETorchesRegistry.ITEM_TORCH, 1, 1);
        }
    };

}
