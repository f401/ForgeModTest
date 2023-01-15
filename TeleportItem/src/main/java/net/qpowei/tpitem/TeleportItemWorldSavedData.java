package net.qpowei.tpitem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class TeleportItemWorldSavedData extends WorldSavedData {

	public static final String NAME = "TeleportItem_Localtions";
	public ConcurrentHashMap<String, Location> pointsMap;
	private static final Logger LOGGER = LogManager.getLogger();

	public TeleportItemWorldSavedData() {
		super(NAME);
		pointsMap = new ConcurrentHashMap<>();
	}

	public static TeleportItemWorldSavedData get(World worldIn) {
		if (!(worldIn instanceof ServerWorld)) {
			LOGGER.error("Attempted to get the data from a client world. This is wrong. World: {}", worldIn);
			return null;
		}
		ServerWorld overWorld = worldIn.getServer().getLevel(World.OVERWORLD);
		DimensionSavedDataManager savedDataManager = overWorld.getDataStorage();
		return savedDataManager.computeIfAbsent(TeleportItemWorldSavedData::new, NAME);
	}

	public void put(String name, BlockPos pos, RegistryKey<World> dimension) {
		put(name, new Location(pos, dimension));
	}

	public void put(String name, Location location) {
		LOGGER.info("Put {}: {}", name, location);
		pointsMap.put(name, location);
	}

	public Location get(String name) {
		return pointsMap.get(name);
	}

	@Override
	public void load(CompoundNBT root) {
		LOGGER.info("Loading Points");
		for (String name : root.getAllKeys()) {
			pointsMap.put(name, new Location(root.getCompound(name)));
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT pCompound) {
		LOGGER.info("Saving Points");
		for (Map.Entry<String, Location> entry : pointsMap.entrySet()) {
			pCompound.put(entry.getKey(), entry.getValue().serializeNBT());
		}
		return pCompound;
	}
}
