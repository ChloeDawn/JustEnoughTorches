package dev.sapphic.torches.item;

import dev.sapphic.torches.block.LampBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;

public class LampItem extends ItemMultiTexture {
  private static final LampBlock.Type[] TYPES = LampBlock.Type.values();

  public LampItem(final Block block) {
    super(block, block, LampItem::getType);
  }

  private static String getType(final ItemStack stack) {
    return TYPES[stack.getMetadata() % TYPES.length].getName();
  }
}
