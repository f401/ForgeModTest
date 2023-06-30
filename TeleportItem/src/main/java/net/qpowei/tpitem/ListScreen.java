package net.qpowei.tpitem;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

public class ListScreen extends Screen {

	public static final String SCREEN_TITLE = "gui.tpitem.title"; 

	protected ListScreen() {
		super(new TranslationTextComponent(SCREEN_TITLE));
	}

	//TODO
	@Override
	protected void init() {
		super.init();

	}

}
