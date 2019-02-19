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

import emord.javafx.ribbon.RibbonTab;
import emord.javafx.ribbon.Ribbon.RibbonState;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;

class RibbonHeader extends Label {
	
	private static final PseudoClass SELECTED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("selected");
	
	private RibbonTab tab;
	
	private final BooleanProperty selected;

	public RibbonHeader(RibbonTab tab) {
		this.tab = tab;
		
		graphicProperty().bind(tab.graphicProperty());
		textProperty().bind(tab.textProperty());
		
		getStyleClass().setAll(tab.getStyleClass());
		
		selected = new BooleanPropertyBase() {
			
			@Override
			public void invalidated() {
				pseudoClassStateChanged(SELECTED_PSEUDOCLASS_STATE, get());
			}

			@Override
			public Object getBean() {
				return RibbonHeader.this;
			}

			@Override
			public String getName() {
				return "selected";
			}
			
		};
		
		selected.bind(tab.selectedProperty());
		
		// Width and height settings
		minWidthProperty().bind(tab.getRibbon().tabMinWidthProperty());
		maxWidthProperty().bind(tab.getRibbon().tabMaxWidthProperty());
		
		minHeightProperty().bind(tab.getRibbon().tabMinHeightProperty());
		maxHeightProperty().bind(tab.getRibbon().tabMaxHeightProperty());
		
		// We don't want the selected appearance when the ribbon is minimized
		tab.getRibbon().ribbonStateProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue == RibbonState.MINIMIZED) {
				pseudoClassStateChanged(SELECTED_PSEUDOCLASS_STATE, false);
			}
			else {
				pseudoClassStateChanged(SELECTED_PSEUDOCLASS_STATE, selected.get());
			}
		});
	}

	public final RibbonTab getTab() {
		return tab;
	}
}
