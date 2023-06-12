package io.github.emd4600.javafxribbon;

import io.github.emd4600.javafxribbon.skin.RibbonMenuButtonSkin;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Skin;

public class RibbonMenuButton extends MenuButton {
	
	public static final String DEFAULT_STYLE_CLASS = "ribbon-button";

	public RibbonMenuButton() {
		this(null, null);
	}
	
	public RibbonMenuButton(String text) {
		this(text, null);
	}
	
	public RibbonMenuButton(String text, Node graphic, MenuItem ... items) {
		super(text, graphic, items);
		
		getStyleClass().add(DEFAULT_STYLE_CLASS);
	}
	
	/** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new RibbonMenuButtonSkin(this);
    }
}
