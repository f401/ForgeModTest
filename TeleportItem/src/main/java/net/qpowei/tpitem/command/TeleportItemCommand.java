package net.qpowei.tpitem.command;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.qpowei.tpitem.Location;
import net.qpowei.tpitem.TeleportItemMain;
import net.qpowei.tpitem.TeleportItemWorldSavedData;

import com.google.common.base.MoreObjects;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import static net.qpowei.tpitem.TeleportItemMain.MOD_ID;

@Mod.EventBusSubscriber
public class TeleportItemCommand {
	public static final String CMD_NO_POINTS = "cmd." + MOD_ID + ".nopoints";
	public static final String CMD_LIST_HEADER = "cmd." + MOD_ID + ".listheaders";
	public static final String CMD_LIST_INDIMENSION = "cmd." + MOD_ID + ".indimension";
	public static final String CMD_LIST_POS = "cmd." + MOD_ID + ".pos";

	@SubscribeEvent
	public static void onServerStarting(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal(MOD_ID)
				.requires((cmd) -> cmd.hasPermission(0))
				.then(
						Commands.literal("list").executes(TeleportItemCommand::performList))
				.then(
						Commands.literal("add")
								.then(Commands.argument("name",
										StringArgumentType.string())
										.then(Commands.argument("pos",
												Vec3Argument.vec3())
												.then(Commands.argument(
														"dimension",
														DimensionArgument
																.dimension())
														.executes((ctx) -> performAdd(
																ctx,
																getPosition(ctx),
																DimensionArgument
																		.getDimension(ctx,
																				"dimension")))))
										.executes((ctx) -> performAdd(
												ctx,
												getPosition(ctx),
												null)))
								.executes((ctx) -> performAdd(ctx, null, null))

				)
				.then(
						Commands.literal("remove")
								.then(Commands.argument("name",
										LocationStringArgument.create()))
								.executes((ctx) -> {
									TeleportItemWorldSavedData.get(ctx)
											.remove(LocationStringArgument
													.getString(ctx, "name"));
									return 1;
								}))
				.then(
						Commands.literal("tp")
								.then(Commands.argument("name",
										LocationStringArgument.create())
										.then(Commands.argument("target",
												EntityArgument.entity()))
										.executes((ctx) -> performTeleport(ctx,
												EntityArgument
														.getEntity(ctx, "target"))))
								.executes((ctx) -> performTeleport(ctx, null))));
	}

	private static BlockPos toBlockPos(Vector3d vec) {
		return new BlockPos(vec.x, vec.y, vec.z);
	}

	private static BlockPos getPosition(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		return toBlockPos(Vec3Argument.getVec3(ctx, "pos"));
	}

	private static int performTeleport(CommandContext<CommandSource> ctx, Entity entity) {
		Location location = TeleportItemWorldSavedData.get(ctx)
				.get(LocationStringArgument.getString(ctx, "name"));
		TeleportItemMain.teleportEntity(
				MoreObjects.firstNonNull(entity, ctx.getSource().getEntity()),
				location.getPos(), location.getWorld(ctx.getSource().getServer()));
		return 1;
	}

	private static int performAdd(CommandContext<CommandSource> ctx, BlockPos pos, ServerWorld level)
			throws CommandSyntaxException {
		CommandSource src = ctx.getSource();
		TeleportItemWorldSavedData.get(src).put(StringArgumentType.getString(ctx, "name"),
				MoreObjects.firstNonNull(pos, src.getEntityOrException().blockPosition()),
				MoreObjects.firstNonNull(level, src.getLevel()));
		return 1;
	}

	private static int performList(CommandContext<CommandSource> ctx) {
		TeleportItemWorldSavedData worldSavedData = TeleportItemWorldSavedData
				.get(ctx.getSource().getLevel());
		CommandSource source = ctx.getSource();
		if (worldSavedData.pointsMap.isEmpty()) {
			source.sendFailure(new TranslationTextComponent(
					CMD_NO_POINTS));
			return 0;
		}
		worldSavedData.forEach((location) -> {
			source.sendSuccess(
					new TranslationTextComponent(CMD_LIST_HEADER)
							.append(new StringTextComponent("")
									.append(location.getKey())
									.withStyle(TextFormatting.GREEN))
							.append(": ")
							.append(new TranslationTextComponent(
									CMD_LIST_INDIMENSION))
							.append(new StringTextComponent("")
									.append(location.getValue()
											.getDimensionString())
									.withStyle(TextFormatting.WHITE))
							.append(" ")
							.append(new TranslationTextComponent(CMD_LIST_POS))
							.append(new StringTextComponent("")
									.append(location.getValue()
											.getPosString())
									.withStyle(TextFormatting.BLUE)),
					false);
		});
		return 1;
	}
}
