package ch.telegraphstudios.TSMenuBar;

public class TSMenuBarApplicationItem extends TSMenuBarItem {

	public TSMenuBarApplicationItem(String title) {
		super(title);
		this.setFont(TSMenuBarItem.MENU_BAR_ITEM_FONT_BOLD);
		
		//Set the size again because the super constructor doesn't use the bold font.
		this.setSize(GraphicsUtils.getStringSize(this.name, this.getFont()).width + (MENU_BAR_ITEM_PADDING * 2), TSMenuBar.MENU_BAR_HEIGHT);
	}

}
