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

import java.io.InputStream;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class RibbonWindow extends StackPane {

	private final BorderPane pane = new BorderPane();
	private final Ribbon ribbon = new Ribbon();
	
	public RibbonWindow() {
		super();
		
		getChildren().add(pane);
		
		setAlignment(Pos.TOP_LEFT);
		
		ribbon.setRibbonWindow(this);
		pane.setTop(ribbon);
	}
	
	public Ribbon getRibbon() {
		return ribbon;
	}
	
	public void setContent(Node content) {
		pane.setCenter(content);
	}
	
	public Node getContent() {
		return pane.getCenter();
	}
	
	@Override
	public String getUserAgentStylesheet() {
		return getClass().getResource("resource/ribbonstyle.css").toExternalForm();
	}
	
	public InputStream getResource(String fileName) {
		return getClass().getResourceAsStream("/emd4600/javafx/ribbon/resource/" + fileName);
	}
}
