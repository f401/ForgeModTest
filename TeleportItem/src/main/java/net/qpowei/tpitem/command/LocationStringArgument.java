package net.qpowei.tpitem.command;

import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.qpowei.tpitem.TeleportItemWorldSavedData;

public class LocationStringArgument implements ArgumentType<String> {

	public static final Logger LOGGER = LogManager.getLogger();

	public static LocationStringArgument create() {
		return new LocationStringArgument();
	}

	/** Based on {@link ISuggestionProvider#matchesSubStr(String, String)} */
	private static boolean matchStr(String input, String str) {
		for (int i = 0; !str.startsWith(input, i); ++i) {
			i = str.indexOf('_', i);
			if (i < 0) {
				return false;
			}
		}
		return true;
	}

	private static List<String> getSuggestionsList(CommandSource src) {
		TeleportItemWorldSavedData savedData = TeleportItemWorldSavedData.get(src);
		return savedData.pointsMap.keySet().stream().toList();
	}

	public static String getString(CommandContext<CommandSource> ctx, String name) {
		return ctx.getArgument(name, String.class);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context,
			SuggestionsBuilder builder) {
		String input = builder.getRemaining().toLowerCase();

		if (context.getSource() instanceof CommandSource) {
			CommandSource src = (CommandSource) context.getSource();
			for (String suggestion : getSuggestionsList(src)) {
				if (matchStr(input, suggestion)) {
					builder.suggest(suggestion);
				}
			}
			return builder.buildFuture();
		}
		LOGGER.error("Unknow source type {}", context.getSource().getClass().toString());
		return Suggestions.empty();
	}

	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		return reader.readQuotedString();
	}
}