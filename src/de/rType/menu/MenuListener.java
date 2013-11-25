package de.rType.menu;

/**
 * 
 * @author Jo
 * 
 */
public abstract class MenuListener {
	
	public Menu menu;

	public MenuListener() {
		
	}
	
	public void addToMenu(Menu menu){
		this.menu = menu;
	}

	protected static final String START_GAME = "start";
	protected static final String PAUSE_GAME = "pause";

	public void performMenuItem(MenuItem item) {
		String command = item.getEvent();
		if (command != null && !command.isEmpty()) {
			if (command == MenuItemKeys.NEW_GAME) {
				newGame();
			} else if (command == MenuItemKeys.RESUME) {
				resumeGame();
			} else if (command.equals(MenuItemKeys.OPTIONS)) {
				showOptions();
			} else if (command.equals(MenuItemKeys.EXIT)) {
				exitGame();
			}
		}
	}

	public abstract void newGame();

	public abstract void resumeGame();

	public abstract void showOptions();

	public abstract void exitGame();

}
