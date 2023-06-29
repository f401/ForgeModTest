package net.qpowei.tpitem.registries;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.qpowei.tpitem.TeleportItemMain;
import net.qpowei.tpitem.blocks.MyBlock;
import net.qpowei.tpitem.items.TeleportItem;

public class ItemRegistry {
	public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS,
			TeleportItemMain.MOD_ID);
	public static final RegistryObject<TeleportItem> TELEPORYT_ITEM_OBJECT = ITEM_REGISTER.register("teleport_item",
			TeleportItem::new);

	public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS,
			TeleportItemMain.MOD_ID);

	public static final RegistryObject<MyBlock> MY_BLOCK = BLOCK_REGISTER.register("my_block", MyBlock::new);
}
