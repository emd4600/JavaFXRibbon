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

package io.github.emd4600.javafxribbon;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;

/**
 * In a ribbon, the program button is placed next to the tab headers, but acts differently:
 * instead of opening a tab, it opens a menu.
 */
public class RibbonProgramButton extends ToggleButton {
	
	public static final String DEFAULT_STYLE_CLASS = "ribbon-program-button";

	public RibbonProgramButton(String text) {
		this(text, null);
	}

	public RibbonProgramButton(String text, Node graphic) {
		super(text, graphic);
		
		getStyleClass().add(DEFAULT_STYLE_CLASS);
	}
	
	
	private final ObjectProperty<RibbonMenu> ribbonMenu = new SimpleObjectProperty<RibbonMenu>(this, "ribbonMenu");
	
	/**
	 * The menu that is shown when clicking this program button.
	 * @return Ribbon menu property
	 */
	public final ObjectProperty<RibbonMenu> ribbonMenuProperty() {
		return ribbonMenu;
	}
	
	/**
	 * Gets the menu that is shown when clicking this program button.
	 * @return Ribbon menu
	 */
	public final RibbonMenu getRibbonMenu() {
		return ribbonMenu.get();
	}
	
	/**
	 * Sets the menu that is shown when clicking this program button.
	 * @param menu Ribbon menu
	 */
	public final void setRibbonMenu(RibbonMenu menu) {
		ribbonMenu.set(menu);
	}
}
