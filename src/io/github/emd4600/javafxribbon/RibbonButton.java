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

import io.github.emd4600.javafxribbon.skin.RibbonButtonSkin;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.image.ImageView;

public class RibbonButton extends Button {
	
	public static final String DEFAULT_STYLE_CLASS = "ribbon-button";
	
	public RibbonButton() {
		this(null, null);
	}

	public RibbonButton(String text, Node graphic) {
		super(text, graphic);
		
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		
		graphicProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue instanceof ImageView) {
				setPrefWidth(((ImageView) newValue).getFitWidth());
			}
		});
		
		setPadding(new Insets(0));
	}
	
	 /** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new RibbonButtonSkin(this);
    }
}
