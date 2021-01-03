package dev.sapphic.torches.item;

import dev.sapphic.torches.block.LampBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

public class LampItem extends ItemMultiTexture {
  private static final LampBlock.Type[] TYPES = LampBlock.Type.values();

  public LampItem(final Block block) {
    super(block, block, stack -> TYPES[stack.getMetadata() % TYPES.length].getName());
  }
}
