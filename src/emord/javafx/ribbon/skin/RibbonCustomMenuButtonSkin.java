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

package emord.javafx.ribbon.skin;

import emord.javafx.ribbon.RibbonCustomMenuButton;
import emord.javafx.ribbon.RibbonMenu;
import javafx.css.PseudoClass;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * The skin of a <code>MenuButton</code> that can display any item in its drop-down list. It also layouts the text in the same way
 * as a {@link RibbonButton}.
 */
public class RibbonCustomMenuButtonSkin extends SkinBase<RibbonCustomMenuButton> {
	
	private static final PseudoClass PSEUDO_CLASS_SHOWING = PseudoClass.getPseudoClass("showing");
	
	private VBox container;

	public RibbonCustomMenuButtonSkin(RibbonCustomMenuButton control) {
		super(control);
		construct();
	}

	private void construct() {
		container = new VBox();
		container.setAlignment(Pos.TOP_CENTER);
		getChildren().add(container);
		
		getSkinnable().textProperty().addListener((obs, oldValue, newValue) -> {
			update();
		});
		getSkinnable().graphicProperty().addListener((obs, oldValue, newValue) -> {
			update();
		});
		getSkinnable().styleProperty().addListener((obs, oldValue, newValue) -> {
			update();
		});
		
		update();
		
		RibbonCustomMenuButton button = getSkinnable();
		
		button.showingProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				Bounds rect = button.localToScreen(button.getBoundsInLocal());
			
				button.getMenu().show(button, rect.getMinX(), rect.getMaxY());
			}
			else {
				button.getMenu().hide();
			}
			
			button.pseudoClassStateChanged(PSEUDO_CLASS_SHOWING, newValue);
		});
		
		button.setOnAction((event) -> {
			if (button.isShowing()) {
				button.hide();
			}
			else {
				button.show();
			}
		});
		
		button.sceneProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				newValue.addEventFilter(MouseEvent.MOUSE_PRESSED, (event) -> {
					RibbonMenu menu = button.getMenu();
					if (button.isShowing() && !menu.contains(event.getScreenX(), event.getScreenY())) {
						button.hide();
						
						// If the click was on the program button, it will open the menu again
						// in the action event; consume the click to avoid that
						if (button.contains(button.screenToLocal(event.getScreenX(), event.getScreenY()))) {
							event.consume();
						}
					};

				});
			}
		});
	}
	
	private void update() {
		ButtonTextHelper.layoutButton(getSkinnable(), container, true);
	}
}
