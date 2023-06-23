package net.qpowei.tpitem.datagen;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.qpowei.tpitem.TeleportItemMain;
import net.qpowei.tpitem.registries.ItemRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {

	private static class MyItemProvider extends ItemModelProvider {

		private Logger logger = LogManager.getLogger();

		public MyItemProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
			super(gen, TeleportItemMain.MOD_ID, exFileHelper);

		}

		@Override
		protected void registerModels() {
			Collection<RegistryObject<Item>> items = ItemRegistry.ITEM_REGISTER.getEntries();
			items.forEach((item) -> {
				build(item.getId().getPath());
			});
		}

		protected final ItemModelBuilder build(String name) {
			logger.info("Registry: " + name);
			return withExistingParent(name,
					new ResourceLocation("item/generated"))
					.texture("layer0", modLoc("item/" + name));
		}

	}

	private static class MyBlockStateAndModelProvider extends BlockStateProvider {

		public MyBlockStateAndModelProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
			super(gen, TeleportItemMain.MOD_ID, exFileHelper);
		}

		@Override
		protected void registerStatesAndModels() {
		}
	}

	@SubscribeEvent
	public static void registerEvent(GatherDataEvent event) {
		event.getGenerator().addProvider(
				new MyItemProvider(event.getGenerator(), event.getExistingFileHelper()));
		event.getGenerator().addProvider(
				new MyBlockStateAndModelProvider(event.getGenerator(), event.getExistingFileHelper()));
	}
}
