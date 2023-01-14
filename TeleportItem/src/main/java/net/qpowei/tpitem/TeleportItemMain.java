package net.qpowei.tpitem;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.qpowei.tpitem.registries.ItemRegistry;

@Mod(TeleportItemMain.MOD_ID)
public class TeleportItemMain {
	public static final String MOD_ID = "tpitem";

	public TeleportItemMain() {
		ItemRegistry.ITEM_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
