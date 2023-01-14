package net.qpowei.tpitem;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.qpowei.tpitem.registries.ItemRegistry;

@Mod(TeleportItem.MOD_ID)
public class TeleportItem {
	public static final String MOD_ID = "tpitem";

	public TeleportItem() {
		ItemRegistry.ITEM_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
