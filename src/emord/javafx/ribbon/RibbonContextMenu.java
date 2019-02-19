/****************************************************************************
* Copyright (C) 2018 Eric Mor
*
* This file is part of JavaFXRibbon.
*
* JavaFXRibbon is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
****************************************************************************/

package emord.javafx.ribbon;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;

/**
 * An implementation of ribbon menu that shows a context menu, similar to the one used in standard menus.
 */
public class RibbonContextMenu extends RibbonMenu {
	
	public RibbonContextMenu(Ribbon ribbon) {
		super(ribbon);
	}

	private final ContextMenu menu = new ContextMenu();
	
	public final ContextMenu getContextMenu() {
		return menu;
	}

	@Override
	public void show(Node caller, double screenX, double screenY) {
		menu.show(caller, screenX, screenY);
	}

	@Override
	public void hide() {
		menu.hide();
	}

	@Override
	public boolean isShowing() {
		return menu.isShowing();
	}

	@Override
	public boolean contains(double screenX, double screenY) {
		if (screenX < menu.getX() || screenY < menu.getY()) {
			return false;
		}
		else if (screenX > menu.getX() + menu.getWidth() || screenY > menu.getY() + menu.getHeight() ) {
			return false;
		}
		else {
			return true;
		}
	}

}
