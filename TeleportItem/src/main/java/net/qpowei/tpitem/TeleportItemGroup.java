package net.qpowei.tpitem;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class TeleportItemGroup extends ItemGroup {

	public TeleportItemGroup() {
		super("Teleport Item");
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(Items.DIAMOND_SWORD.getItem());
	}

}
