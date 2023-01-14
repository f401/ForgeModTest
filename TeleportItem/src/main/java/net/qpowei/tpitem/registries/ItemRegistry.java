package net.qpowei.tpitem.registries;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.qpowei.tpitem.TeleportItemMain;

public class ItemRegistry {
	public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, TeleportItemMain.MOD_ID);

}
