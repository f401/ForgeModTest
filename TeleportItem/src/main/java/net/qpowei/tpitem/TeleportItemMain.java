package net.qpowei.tpitem;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.impl.TeleportCommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.qpowei.tpitem.registries.TPItemRegistry;

@Mod(TeleportItemMain.MOD_ID)
public class TeleportItemMain {
	public static final String MOD_ID = "tpitem";

	private static final Logger LOGGER = LogManager.getLogger();

	public TeleportItemMain() {
		LOGGER.info("TeleportItem Mod Start, Registry Items");
		TPItemRegistry.ITEM_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
		TPItemRegistry.BLOCK_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static ServerWorld getWorldFromRes(MinecraftServer server, ResourceLocation world) {
		RegistryKey<World> dimension = RegistryKey.create(Registry.DIMENSION_REGISTRY, world);
		return server.getLevel(dimension);
	}

	public static void teleportEntity(Entity entity, BlockPos pos, ResourceLocation world) {
		ServerWorld serverWorld = getWorldFromRes(entity.getServer(), world);
		if (serverWorld == null) {
			LOGGER.error("Invaid Dimension: {}. Exiting", world);
			throw new NullPointerException("Invaid Dimension: " + world);
		}
		teleportEntity(entity, pos, serverWorld);
	}

	/**
	 * Based on {@link net.minecraft.command.impl.TeleportCommand#performTeleport}
	 */
	private static void performTeleportPlayer(ServerPlayerEntity player, BlockPos pos,
			ServerWorld targetWorld) {
		ChunkPos chkPos = new ChunkPos(pos);
		targetWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chkPos, 1,
				player.getId());

		player.stopRiding();
		if (player.isSleeping()) {
			player.stopSleepInBed(true, true);
		}

		player.teleportTo(targetWorld, pos.getX(), pos.getY(), pos.getZ(), player.xRot,
				player.yRot);
	}

	/**
	 * Based on {@link net.minecraft.command.impl.TeleportCommand#performTeleport}
	 */
	private static void performTeleportEntity(Entity entity, BlockPos pos, ServerWorld targetWorld) {
		float f1 = MathHelper.wrapDegrees(entity.xRot),
				f = MathHelper.clamp(MathHelper.wrapDegrees(entity.yRot), -90.0f, 90.0f);
		if (entity.level == targetWorld) {
			entity.moveTo(pos.getX(), pos.getY(), pos.getZ(), f1, f);
			entity.setYHeadRot(f1);
		} else {
			entity.unRide();
			Entity oldEntity = entity;
			if ((oldEntity = entity.getType().create(targetWorld)) == null) {
				return;
			}
			entity.restoreFrom(oldEntity);
			entity.moveTo(pos.getX(), pos.getY(), pos.getZ(), f1, f);
			entity.setYHeadRot(f1);
			targetWorld.addFromAnotherDimension(entity);
		}

	}

	/**
	 * Teleport an 'entity' to 'pos' in 'world'.
	 * Based on {@link net.minecraft.command.impl.TeleportCommand#performTeleport}
	 * Also you can get 'targetworld' from 'ResourceLocation' in
	 * {@link TeleportItemMain#getWorldFromRes} or use @{link
	 * {@link TeleportItemMain#teleportEntity(Entity, BlockPos, ResourceLocation)}
	 * 
	 * @param entity      The entity you want to teleport. Can be player.
	 * @param pos         Target position.
	 * @param targetworld Target world.
	 * @throws NullPointerException When one of 'entity', 'pos' and 'targetworld' is
	 *                              null.
	 */
	public static void teleportEntity(Entity entity, BlockPos pos, ServerWorld targetWorld) {
		Objects.requireNonNull(entity, "`entity` mustn't be null.");
		Objects.requireNonNull(pos, "`pos` mustn't be null.");
		Objects.requireNonNull(targetWorld, "`world` mustn't be null.");

		if (entity instanceof ServerPlayerEntity) {
			performTeleportPlayer((ServerPlayerEntity) entity, pos, targetWorld);
		} else {
			performTeleportEntity(entity, pos, targetWorld);
		}
	}

	@SubscribeEvent
	public static void onServerStarting(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal(MOD_ID).then(
				Commands.literal("test").requires((cmd) -> cmd.hasPermission(0))
				.executes((ctx) -> {
					ctx.getSource().sendSuccess(new TranslationTextComponent("cmd.tpitem.hello"), true);
					return 1;
				})));
	}
}
