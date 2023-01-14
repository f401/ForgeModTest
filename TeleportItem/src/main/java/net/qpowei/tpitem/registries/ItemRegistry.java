package net.qpowei.tpitem.registries;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.qpowei.tpitem.TeleportItem;

public class ItemRegistry {
	public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, TeleportItem.MOD_ID);

}
