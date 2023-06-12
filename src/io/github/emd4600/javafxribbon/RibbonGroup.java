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

import io.github.emd4600.javafxribbon.skin.RibbonGroupSkin;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.Skin;
import javafx.scene.text.TextAlignment;

public class RibbonGroup extends Labeled {
	
	public static final String DEFAULT_STYLE_CLASS = "ribbon-group";
	
	private static final double DEFAULT_SPACING = 5.0;
	
	public RibbonGroup() {
		this(null);
	}
	
	public RibbonGroup(String title, Node ... nodes) {
		setText(title);
		
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		
		getNodes().addAll(nodes);
		
		setTextAlignment(TextAlignment.CENTER);
	}
	
	private final ObservableList<Node> nodes = FXCollections.observableArrayList();
	
	public final ObservableList<Node> getNodes() {
		return nodes;
	}
	
	/**
	 * The amount of horizontal space between each child in the ribbon group.
	 */
	private final DoubleProperty spacing = new SimpleDoubleProperty(this, "spacing", DEFAULT_SPACING);
	
	/**
	 * The amount of horizontal space between each child in the ribbon group.
	 * @return Spacing property
	 */
	public final DoubleProperty spacingProperty() {
		return spacing;
	}
	
	/**
	 * Gets the value of the property spacing.
	 * @return Spacing
	 */
	public final double getSpacing() {
		return spacing.get();
	}
	
	/**
	 * Sets the value of the property spacing.
	 * @param spacing Spacing
	 */
	public void setSpacing(double spacing) {
		this.spacing.set(spacing);
	}
	
	/** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new RibbonGroupSkin(this);
    }
}
