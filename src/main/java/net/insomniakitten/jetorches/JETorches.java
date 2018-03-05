package net.insomniakitten.jetorches;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = JETorches.ID, name = JETorches.NAME, version = JETorches.VERSION)
public final class JETorches {

    public static final String ID = "jetorches";
    public static final String NAME = "Just Enough Torches";
    public static final String VERSION = "%VERSION%";

    @GameRegistry.ItemStackHolder(ID + ":torch_stone")
    public static final ItemStack ICON = ItemStack.EMPTY;

    public static final CreativeTabs TAB = new CreativeTabs(ID) {
        @Override
        @SideOnly(Side.CLIENT)
        public String getTranslatedTabLabel() {
            return "item_group." + ID + ".label";
        }

        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return ICON;
        }
    };

}
