package net.qpowei.tpitem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.qpowei.tpitem.registries.ItemRegistry;

@Mod(TeleportItemMain.MOD_ID)
public class TeleportItemMain {
	public static final String MOD_ID = "tpitem";

	private static final Logger LOGGER = LogManager.getLogger();

	public TeleportItemMain() {
		LOGGER.info("TeleportItem Mod Start, Registry Items");
		ItemRegistry.ITEM_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
