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

import javafx.scene.Node;

/**
 * <p>A menu that is opened from the ribbon. This is used in the program button or in {@link RibbonCustomMenuButton}.</p>
 * <p>The user must provide an implementation for this class, as any node can be used. For most cases, a popup-like menu is enough;
 * for that, you can use the {@link RibbonContextMenu} class.</p>
 * <p>The show and hide methods can be used from anywhere in the code, as they don't need to notify the ribbon in any way. The 
 * menu is hidden automatically, however, on this occasions:</p>
 * <ul>
 * <li>When the user clicks on the button again.
 * <li>When the user clicks outside the menu node. 
 * </ul>
 */
public abstract class RibbonMenu {
	
	protected Ribbon ribbon;
	
	public RibbonMenu(Ribbon ribbon) {
		this.ribbon = ribbon;
	}
	
	void setRibbon(Ribbon ribbon) {
		this.ribbon = ribbon;
	}
	
	public Ribbon getRibbon() {
		return ribbon;
	}

	/**
	 * Called when this menu is required to show in the specified coordinates.
	 * After this method is called, the {@link #isShowing()} method must return <code>true</code>.
	 * @param caller The Ribbon or RibbonCustomMenuButton that uses this menu.
	 * @param screenX The X coordinate, in screen-space.
	 * @param screenY The Y coordinate, in screen-space.
	 */
	public abstract void show(Node caller, double screenX, double screenY);
	
	/**
	 * Called when this method should be hidden.
	 * After this method is called, the {@link #isShowing()} method must return <code>true</code>.
	 */
	public abstract void hide();
	
	/**
	 * Returns whether the menu is currently showing or not.
	 * @return True if the menu is showing, false if it is not showing.
	 */
	public abstract boolean isShowing();
	
	/**
	 * Returns whether the scene coordinates are contained in this menu.
	 * @param screenX The X coordinate, in screen-space.
	 * @param screenY The Y coordinate, in screen-space.
	 * @return True if the point is inside the menu, false otherwise.
	 */
	public abstract boolean contains(double screenX, double screenY);

}
