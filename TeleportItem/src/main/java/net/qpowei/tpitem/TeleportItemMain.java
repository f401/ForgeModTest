package net.qpowei.tpitem;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
		ItemRegistry.BLOCK_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static ServerWorld getWorldFromRes(MinecraftServer server, ResourceLocation world) {
		RegistryKey<World> dimension = RegistryKey.create(Registry.DIMENSION_REGISTRY, world);
		return server.getLevel(dimension);
	}

	public static void teleportPlayer(ServerPlayerEntity entity, BlockPos pos, ResourceLocation world) {
		ServerWorld serverWorld = getWorldFromRes(entity.getServer(), world);
		if (serverWorld == null) {
			LOGGER.error("Invaid Dimension: {}. Exiting", world);
			throw new NullPointerException("Invaid Dimension: " + world);
		}
		teleportPlayer(entity, pos, serverWorld);
	}

	public static void teleportPlayer(ServerPlayerEntity player, BlockPos pos, ServerWorld targetWorld) {
		Objects.requireNonNull(player, "`player` mustn't be null.");
		Objects.requireNonNull(pos, "`pos` mustn't be null.");
		Objects.requireNonNull(targetWorld, "`world` mustn't be null.");

		float yaw = player.xRot, pitch = player.yRot;

		ChunkPos chkPos = new ChunkPos(pos);
		targetWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chkPos, 1, player.getId());

		player.stopRiding();
		if (player.isSleeping()) {
			player.stopSleepInBed(true, true);
		}

		player.teleportTo(targetWorld, pos.getX(), pos.getY(), pos.getZ(), yaw, pitch);
	}

	@SubscribeEvent
	public static void onServerStarting() {}
}
