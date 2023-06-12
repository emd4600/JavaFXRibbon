package io.github.emd4600.javafxribbon;

import io.github.emd4600.javafxribbon.skin.RibbonCustomMenuButtonSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Skin;

public class RibbonCustomMenuButton extends Button {
	
	public static final String DEFAULT_STYLE_CLASS = "ribbon-button";
	
	public RibbonCustomMenuButton() {
		this(null, null);
	}

	public RibbonCustomMenuButton(String text, Node graphic) {
		this(text, graphic, null);
	}
	
	public RibbonCustomMenuButton(String text, Node graphic, RibbonMenu menu) {
		super(text, graphic);
		
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		
		setPadding(new Insets(0));
	}
	
	private final ObjectProperty<RibbonMenu> menu = new SimpleObjectProperty<RibbonMenu>(this, "menu");
	
	public final ObjectProperty<RibbonMenu> menuProperty() {
		return menu;
	}
	
	public final RibbonMenu getMenu() {
		return menu.get();
	}
	
	public final void setMenu(RibbonMenu value) {
		menu.set(value);
	}
	
	private static final PseudoClass PSEUDO_CLASS_SHOWING = PseudoClass.getPseudoClass("showing");
	
	/**
     * Indicates whether the {@link ContextMenu} is currently visible.
     */
    private ReadOnlyBooleanWrapper showing = new ReadOnlyBooleanWrapper(this, "showing", false) {
        @Override protected void invalidated() {
            pseudoClassStateChanged(PSEUDO_CLASS_SHOWING, get());
            super.invalidated();
        }
    };
    private void setShowing(boolean value) {
        // these events will not fire if the showing property is bound
        Event.fireEvent(this, value ? new Event(ComboBoxBase.ON_SHOWING) :
                new Event(ComboBoxBase.ON_HIDING));
        showing.set(value);
        Event.fireEvent(this, value ? new Event(ComboBoxBase.ON_SHOWN) :
                new Event(ComboBoxBase.ON_HIDDEN));
    }
    public final boolean isShowing() { return showing.get(); }
    public final ReadOnlyBooleanProperty showingProperty() { return showing.getReadOnlyProperty(); }
    
    /**
     * Shows the {@link ContextMenu}, assuming this MenuButton is not disabled.
     *
     * @see #isDisabled()
     * @see #isShowing()
     */
    public void show() {
        // TODO: isBound check is probably unnecessary here
        if (!isDisabled() && !showing.isBound()) {
            setShowing(true);
        }
    }

    /**
     * Hides the {@link ContextMenu}.
     *
     * @see #isShowing()
     */
    public void hide() {
        // TODO: isBound check is probably unnecessary here
        if (!showing.isBound()) {
            setShowing(false);
        }
    }
	
	 /** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new RibbonCustomMenuButtonSkin(this);
    }
}
