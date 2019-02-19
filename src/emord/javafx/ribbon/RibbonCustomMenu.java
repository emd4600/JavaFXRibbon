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

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * An implementation of ribbon menu that shows any node. The artificial menu is meant to be styled to look
 * like a context menu: using buttons within it will style them as a MenuItem.
 */
public class RibbonCustomMenu extends RibbonMenu {
	
	public static final String DEFAULT_STYLE_CLASS = "custom-ribbon-menu";
	
	private final Node node;
	private boolean isShowing;
	
	public RibbonCustomMenu(Ribbon ribbon) {
		super(ribbon);
		
		node = new VBox();
		setDefaultStyling();
	}
	
	public RibbonCustomMenu(Ribbon ribbon, Node node) {
		super(ribbon);
		
		this.node = node;
	}
	
	public void setDefaultStyling() {
		node.getStyleClass().add(DEFAULT_STYLE_CLASS);
		node.getStyleClass().add("context-menu");
	}
	
	public Node getNode() {
		return node;
	}

	@Override
	public void show(Node caller, double screenX, double screenY) {
		
		ribbon.getRibbonWindow().getChildren().add(node);
		StackPane.setAlignment(node, Pos.TOP_LEFT);
		
		Point2D coords = caller.screenToLocal(screenX, screenY);
		
		node.setTranslateX(coords.getX());
		node.setTranslateY(coords.getY());
		
		isShowing = true;
	}

	@Override
	public void hide() {
		if (ribbon != null && node != null) {
			ribbon.getRibbonWindow().getChildren().remove(node);
		}
		isShowing = false;
	}

	@Override
	public boolean isShowing() {
		return isShowing;
	}

	@Override
	public boolean contains(double screenX, double screenY) {
		return node != null && node.contains(node.screenToLocal(screenX, screenY));
	}

}
