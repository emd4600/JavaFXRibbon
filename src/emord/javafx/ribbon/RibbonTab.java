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

import java.util.Collections;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;

/**
 * <p>RibbonTabs are placed within a {@link Ribbon}, where each tab represents a single 'page'.
 * <p>Tabs contain one or multiple {@link RibbonGroup}, which in turn contain UI controls.</p>
 * <p>When the user clicks
 * on a RibbonTab in the Ribbon the tab content becomes visible to the user.</p>
 */
public class RibbonTab implements Styleable {
	
	public static final String DEFAULT_STYLE_CLASS = "ribbon-tab";
	
	/**
	 * Constructs a new ribbon tab with no title.
	 */
	public RibbonTab() {
		this(null);
	}
	
	/**
	 * Constructs a new ribbon tab with the specified title and ribbon groups
	 * @param text The text that is displayed in the tab selection button.
	 * @param groups The ribbon groups that contain the controls.
	 */
	public RibbonTab(String text, RibbonGroup ... groups) {
		setText(text);
		getGroups().addAll(groups);
		getStyleClass().add(DEFAULT_STYLE_CLASS);
	}
	
	
	/**
	 * The title text that is displayed in the tab selection button.
	 */
	private final StringProperty text = new SimpleStringProperty();
	
	/**
	 * The title text that is displayed in the tab selection button.
	 */
	public final StringProperty textProperty() { return text; }
	
	/**
	 * Gets the title text that is displayed in the tab selection button.
	 */
	public final String getText() { return text.get(); }
	
	/**
	 * Sets the title text that is displayed in the tab selection button.
	 */
	public final void setText(String title) { this.text.set(title); }
	
	private ObjectProperty<Node> graphic;

    /**
     * <p>Sets the graphic to show in the tab to allow the user to differentiate
     * between the function of each tab. By default the graphic does not rotate
     * based on the TabPane.tabPosition value, but it can be set to rotate by
     * setting TabPane.rotateGraphic to true.</p>
     */
    public final void setGraphic(Node value) {
        graphicProperty().set(value);
    }

    /**
     * The graphic shown in the tab.
     *
     * @return The graphic shown in the tab.
     */
    public final Node getGraphic() {
        return graphic == null ? null : graphic.get();
    }
    
    /**
     * The graphic in the tab.
     *
     * @return The graphic in the tab.
     */
    public final ObjectProperty<Node> graphicProperty() {
        if (graphic == null) {
            graphic = new SimpleObjectProperty<Node>(this, "graphic");
        }
        return graphic;
    }

    private final ObservableList<RibbonGroup> groups = FXCollections.observableArrayList();
    
    /**
     * The groups that contain the controls of the tab.
     * @return
     */
    public final ObservableList<RibbonGroup> getGroups() {
    	return groups;
    }
    
    private ReadOnlyObjectWrapper<Ribbon> ribbon;
    
    final void setRibbon(Ribbon value) {
        ribbonPropertyImpl().set(value);
    }
    
    /**
     * <p>A reference to the Ribbon that contains this tab instance.</p>
     */
    public final Ribbon getRibbon() {
    	return ribbon == null ? null : ribbon.get();
    }
    
    /**
     * The Ribbon that contains this tab.
     */
    public final ReadOnlyObjectProperty<Ribbon> ribbonProperty() {
        return ribbonPropertyImpl().getReadOnlyProperty();
    }
    
    private ReadOnlyObjectWrapper<Ribbon> ribbonPropertyImpl() {
        if (ribbon == null) {
        	ribbon = new ReadOnlyObjectWrapper<Ribbon>(this, "ribbon");
        }
        return ribbon;
    }
    
    private BooleanProperty disable;

    /**
     * Sets the disabled state of this tab.
     *
     * @param value the state to set this tab
     *
     * @defaultValue false
     * @since JavaFX 2.2
     */
    public final void setDisable(boolean value) {
        disableProperty().set(value);
    }

    /**
     * Returns {@code true} if this tab is disable.
     * @since JavaFX 2.2
     */
    public final boolean isDisable() { return disable == null ? false : disable.get(); }

    /**
     * Sets the disabled state of this tab. A disable tab is no longer interactive
     * or traversable, but the contents remain interactive.  A disable tab
     * can be selected using {@link TabPane#getSelectionModel()}.
     *
     * @defaultValue false
     * @since JavaFX 2.2
     */
    public final BooleanProperty disableProperty() {
        if (disable == null) {
            disable = new SimpleBooleanProperty(false, "disable");
        }
        return disable;
    }
    
    private ObjectProperty<ContextMenu> contextMenu;

    /**
     * <p>Specifies the context menu to show when the user right-clicks on the tab.
     * </p>
     */
    public final void setContextMenu(ContextMenu value) {
        contextMenuProperty().set(value);
    }

    /**
     * The context menu associated with the tab.
     * @return The context menu associated with the tab.
     */
    public final ContextMenu getContextMenu() {
        return contextMenu == null ? null : contextMenu.get();
    }

    /**
     * The context menu associated with the tab.
     */
    public final ObjectProperty<ContextMenu> contextMenuProperty() {
        if (contextMenu == null) {
            contextMenu = new SimpleObjectProperty<ContextMenu>(this, "contextMenu");
        }
        return contextMenu;
    }
    
    private ObjectProperty<Tooltip> tooltip;

    /**
     * <p>Specifies the tooltip to show when the user hovers over the tab.</p>
     */
    public final void setTooltip(Tooltip value) { tooltipProperty().setValue(value); }

    /**
     * The tooltip associated with this tab.
     * @return The tooltip associated with this tab.
     */
    public final Tooltip getTooltip() { return tooltip == null ? null : tooltip.getValue(); }

    /**
     * The tooltip associated with this tab.
     */
    public final ObjectProperty<Tooltip> tooltipProperty() {
        if (tooltip == null) {
            tooltip = new SimpleObjectProperty<Tooltip>(this, "tooltip");
        }
        return tooltip;
    }
    
    private ReadOnlyBooleanWrapper selected;

    protected final void setSelected(boolean value) {
        selectedPropertyImpl().set(value);
    }

    /**
     * <p>Represents whether this tab is the currently selected tab,
     * To change the selected Tab use {@code tabPane.getSelectionModel().select()}
     * </p>
     */
    public final boolean isSelected() {
        return selected == null ? false : selected.get();
    }

    /**
     * The currently selected tab.
     */
    public final ReadOnlyBooleanProperty selectedProperty() {
        return selectedPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyBooleanWrapper selectedPropertyImpl() {
        if (selected == null) {
            selected = new ReadOnlyBooleanWrapper(this, "selected");
        }
        return selected;
    }

    
    // STYLING
    
    private final ObservableList<String> styleClass = FXCollections.observableArrayList();
    
    /**
     * A list of String identifiers which can be used to logically group
     * Nodes, specifically for an external style engine. This variable is
     * analogous to the "class" attribute on an HTML element and, as such,
     * each element of the list is a style class to which this Node belongs.
     *
     * @see <a href="http://www.w3.org/TR/css3-selectors/#class-html">CSS3 class selectors</a>
     */
    @Override
    public ObservableList<String> getStyleClass() {
        return styleClass;
    }
    
	@Override
	public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
		return getClassCssMetaData();
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public ObservableSet<PseudoClass> getPseudoClassStates() {
		return FXCollections.emptyObservableSet();
	}

	@Override
	public String getStyle() {
		return null;
	}

	@Override
	public Styleable getStyleableParent() {
		return getRibbon();
	}

	@Override
	public String getTypeSelector() {
		return "RibbonTab";
	}
	
	/**
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     * @since JavaFX 8.0
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return Collections.emptyList();
    }
}
