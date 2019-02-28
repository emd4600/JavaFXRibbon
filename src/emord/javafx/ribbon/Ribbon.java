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

import java.util.HashMap;
import java.util.Map;

import com.sun.javafx.css.converters.SizeConverter;

import emord.javafx.ribbon.skin.RibbonSkin;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.Control;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;

public class Ribbon extends Control {
	
	public static enum RibbonState {
		EXPANDED,
		MINIMIZED,
		DROPDOWN
	}
	
	public static final String DEFAULT_STYLE_CLASS = "ribbon";
	
	public static final double NO_FIXED_HEIGHT = -1.0;
	
    private static final double DEFAULT_TAB_MIN_WIDTH = 0;

    private static final double DEFAULT_TAB_MAX_WIDTH = Double.MAX_VALUE;

    private static final double DEFAULT_TAB_MIN_HEIGHT = 0;

    private static final double DEFAULT_TAB_MAX_HEIGHT = Double.MAX_VALUE;
    
    public Map<String, Color> contextualGroupHeadersMap = new HashMap<String, Color>(); 
    
    public Ribbon() {
    	getStyleClass().setAll(DEFAULT_STYLE_CLASS);
    	setSelectionModel(new RibbonSelectionModel(this));
    	
    	tabs.addListener((ListChangeListener<RibbonTab>) c -> {
    		while (c.next()) {
    			for (RibbonTab tab : c.getRemoved()) {
    				if (tab != null && !getTabs().contains(tab)) {
    					tab.setRibbon(null);
    				}
    			}
    			
    			for (RibbonTab tab : c.getAddedSubList()) {
    				tab.setRibbon(this);
    			}
    		}
    	});
    	
    	setPrefWidth(Double.MAX_VALUE);
    	
//    	ribbonStateProperty().addListener((obs, oldValue, newValue) -> {
//    		System.out.println(newValue.toString());
//    		
//    		this.requestLayout();
//    	});
    }
    
    
    private ReadOnlyObjectWrapper<RibbonWindow> ribbonWindow;
    
    final void setRibbonWindow(RibbonWindow value) {
    	ribbonWindowPropertyImpl().set(value);
    }
    
    /**
     * <p>A reference to the RibbonWindow where this ribbon is contained.</p>
     */
    public final RibbonWindow getRibbonWindow() {
    	return ribbonWindow == null ? null : ribbonWindow.get();
    }
    
    /**
     * The RibbonWindow that contains this ribbon.
     */
    public final ReadOnlyObjectProperty<RibbonWindow> ribbonProperty() {
        return ribbonWindowPropertyImpl().getReadOnlyProperty();
    }
    
    private ReadOnlyObjectWrapper<RibbonWindow> ribbonWindowPropertyImpl() {
        if (ribbonWindow == null) {
        	ribbonWindow = new ReadOnlyObjectWrapper<RibbonWindow>(this, "ribbonWindow");
        }
        return ribbonWindow;
    }
    
    private ObjectProperty<RibbonProgramButton> programButton;
    
    /**
     * An optional button that is placed before the tab headers and displays a menu.
     */
    public final ObjectProperty<RibbonProgramButton> programButtonProperty() {
    	if (programButton == null) {
    		programButton = new SimpleObjectProperty<RibbonProgramButton>(this, "programButton");
    	}
    	return programButton;
    }
    
    /**
     * Gets an optional button that is placed before the tab headers and displays a menu.
     * @return
     */
    public final RibbonProgramButton getProgramButton() {
    	return programButton == null ? null : programButton.get(); 
    }
    
    /**
     * Sets an optional button that is placed before the tab headers and displays a menu.
     * @param value
     */
    public final void setProgramButton(RibbonProgramButton value) {
    	programButtonProperty().set(value);
    }
    
    private DoubleProperty contentHeight;
    
    /**
     * An optional fixed height for the ribbon content pane. If the value is NO_FIXED_HEIGHT (-1),
     * the height is calculated with the ribbon groups.
     * @defaultValue NO_FIXED_HEIGHT
     */
    public final DoubleProperty contentHeightProperty() {
    	if (contentHeight == null) {
    		contentHeight = new SimpleDoubleProperty(this, "contentHeight", NO_FIXED_HEIGHT);
    	}
    	return contentHeight;
    }
    
    /**
     * Gets the optional fixed height for the ribbon content pane.
     * @return
     */
    public final double getContentHeight() {
    	return contentHeight == null ? NO_FIXED_HEIGHT : contentHeight.get();
    }
    
    /**
     * Sets the optional fixed height for the ribbon content pane.
     * @param value
     */
    public final void setContentHeight(double value) {
    	contentHeightProperty().set(value);
    }
    
    
    private ObjectProperty<SingleSelectionModel<RibbonTab>> selectionModel = new SimpleObjectProperty<SingleSelectionModel<RibbonTab>>(this, "selectionModel");

    /**
     * <p>Sets the model used for tab selection.  By changing the model you can alter
     * how the tabs are selected and which tabs are first or last.</p>
     */
    public final void setSelectionModel(SingleSelectionModel<RibbonTab> value) { selectionModel.set(value); }

    /**
     * <p>Gets the model used for tab selection.</p>
     */
    public final SingleSelectionModel<RibbonTab> getSelectionModel() { return selectionModel.get(); }

    /**
     * The selection model used for selecting tabs.
     */
    public final ObjectProperty<SingleSelectionModel<RibbonTab>> selectionModelProperty() { return selectionModel; }
	
	private final ObservableList<RibbonTab> tabs = FXCollections.observableArrayList();
	
	/**
     * <p>The tabs to display in this Ribbon. Changing this ObservableList will
     * immediately result in the Ribbon updating to display the new contents
     * of this ObservableList.</p>
     *
     * <p>If the tabs ObservableList changes, the selected tab will remain the previously
     * selected tab, if it remains within this ObservableList. If the previously
     * selected tab is no longer in the tabs ObservableList, the selected tab will
     * become the first tab in the ObservableList.</p>
     */
    public final ObservableList<RibbonTab> getTabs() {
        return tabs;
    }

    
    //// ALL THIS IS COPIED FROM TABPANE ////
    
    private DoubleProperty tabMinWidth;

    /**
     * <p>The minimum width of the tabs in the TabPane.  This can be used to limit
     * the length of text in tabs to prevent truncation.  Setting the min equal
     * to the max will fix the width of the tab.  By default the min equals to the max.
     *
     * This value can also be set via CSS using {@code -fx-tab-min-width}
     *
     * </p>
     */
    public final void setTabMinWidth(double value) {
        tabMinWidthProperty().setValue(value);
    }

    /**
     * The minimum width of the tabs in the TabPane.
     *
     * @return The minimum width of the tabs.
     */
    public final double getTabMinWidth() {
        return tabMinWidth == null ? DEFAULT_TAB_MIN_WIDTH : tabMinWidth.getValue();
    }

    /**
     * The minimum width of the tabs in the TabPane.
     */
    public final DoubleProperty tabMinWidthProperty() {
        if (tabMinWidth == null) {
            tabMinWidth = new StyleableDoubleProperty(DEFAULT_TAB_MIN_WIDTH) {

                @Override
                public CssMetaData<Ribbon,Number> getCssMetaData() {
                    return StyleableProperties.TAB_MIN_WIDTH;
                }

                @Override
                public Object getBean() {
                    return Ribbon.this;
                }

                @Override
                public String getName() {
                    return "tabMinWidth";
                }
            };
        }
        return tabMinWidth;
    }

    /**
     * <p>Specifies the maximum width of a tab.  This can be used to limit
     * the length of text in tabs.  If the tab text is longer than the maximum
     * width the text will be truncated.  Setting the max equal
     * to the min will fix the width of the tab.  By default the min equals to the max
     *
     * This value can also be set via CSS using {@code -fx-tab-max-width}.</p>
     */
    private DoubleProperty tabMaxWidth;
    public final void setTabMaxWidth(double value) {
        tabMaxWidthProperty().setValue(value);
    }

    /**
     * The maximum width of the tabs in the TabPane.
     *
     * @return The maximum width of the tabs.
     */
    public final double getTabMaxWidth() {
        return tabMaxWidth == null ? DEFAULT_TAB_MAX_WIDTH : tabMaxWidth.getValue();
    }

    /**
     * The maximum width of the tabs in the TabPane.
     */
    public final DoubleProperty tabMaxWidthProperty() {
        if (tabMaxWidth == null) {
            tabMaxWidth = new StyleableDoubleProperty(DEFAULT_TAB_MAX_WIDTH) {

                @Override
                public CssMetaData<Ribbon,Number> getCssMetaData() {
                    return StyleableProperties.TAB_MAX_WIDTH;
                }

                @Override
                public Object getBean() {
                    return Ribbon.this;
                }

                @Override
                public String getName() {
                    return "tabMaxWidth";
                }
            };
        }
        return tabMaxWidth;
    }

    private DoubleProperty tabMinHeight;

    /**
     * <p>The minimum height of the tabs in the TabPane.  This can be used to limit
     * the height in tabs. Setting the min equal to the max will fix the height
     * of the tab.  By default the min equals to the max.
     *
     * This value can also be set via CSS using {@code -fx-tab-min-height}
     * </p>
     */
    public final void setTabMinHeight(double value) {
        tabMinHeightProperty().setValue(value);
    }

    /**
     * The minimum height of the tabs in the TabPane.
     *
     * @return The minimum height of the tabs.
     */
    public final double getTabMinHeight() {
        return tabMinHeight == null ? DEFAULT_TAB_MIN_HEIGHT : tabMinHeight.getValue();
    }

    /**
     * The minimum height of the tab.
     */
    public final DoubleProperty tabMinHeightProperty() {
        if (tabMinHeight == null) {
            tabMinHeight = new StyleableDoubleProperty(DEFAULT_TAB_MIN_HEIGHT) {

                @Override
                public CssMetaData<Ribbon,Number> getCssMetaData() {
                    return StyleableProperties.TAB_MIN_HEIGHT;
                }

                @Override
                public Object getBean() {
                    return Ribbon.this;
                }

                @Override
                public String getName() {
                    return "tabMinHeight";
                }
            };
        }
        return tabMinHeight;
    }

    /**
     * <p>The maximum height if the tabs in the TabPane.  This can be used to limit
     * the height in tabs. Setting the max equal to the min will fix the height
     * of the tab.  By default the min equals to the max.
     *
     * This value can also be set via CSS using -fx-tab-max-height
     * </p>
     */
    private DoubleProperty tabMaxHeight;
    public final void setTabMaxHeight(double value) {
        tabMaxHeightProperty().setValue(value);
    }

    /**
     * The maximum height of the tabs in the TabPane.
     *
     * @return The maximum height of the tabs.
     */
    public final double getTabMaxHeight() {
        return tabMaxHeight == null ? DEFAULT_TAB_MAX_HEIGHT : tabMaxHeight.getValue();
    }

    /**
     * <p>The maximum height of the tabs in the TabPane.</p>
     */
    public final DoubleProperty tabMaxHeightProperty() {
        if (tabMaxHeight == null) {
            tabMaxHeight = new StyleableDoubleProperty(DEFAULT_TAB_MAX_HEIGHT) {

                @Override
                public CssMetaData<Ribbon,Number> getCssMetaData() {
                    return StyleableProperties.TAB_MAX_HEIGHT;
                }

                @Override
                public Object getBean() {
                    return Ribbon.this;
                }

                @Override
                public String getName() {
                    return "tabMaxHeight";
                }
            };
        }
        return tabMaxHeight;
    }
    
    
    private final ObjectProperty<RibbonState> ribbonState = new SimpleObjectProperty<RibbonState>(this, "ribbonState", RibbonState.EXPANDED);
    
    public final ObjectProperty<RibbonState> ribbonStateProperty() {
    	return ribbonState;
    }
    
    public final RibbonState getRibbonState() {
    	return ribbonState.get();
    }
    
    public final void setRibbonState(RibbonState state) {
    	ribbonState.set(state);
    }
    
    public void constructTabsPane() {
    	((RibbonSkin)getSkin()).constructTabsPane();
    }

    /** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new RibbonSkin(this);
    }
    
    /***************************************************************************
     *                                                                         *
     *                         Stylesheet Handling                             *
     *                                                                         *
     **************************************************************************/

    private static class StyleableProperties {
    	private static final CssMetaData<Ribbon, Number> TAB_MIN_WIDTH = 
    			new CssMetaData<Ribbon, Number>("-fx-tab-min-width",
    					SizeConverter.getInstance(), DEFAULT_TAB_MIN_WIDTH)  {

    		@Override
    		public boolean isSettable(Ribbon n) {
    			return n.tabMinWidth == null || !n.tabMinWidth.isBound();
    		}

    		@Override
    		public StyleableProperty<Number> getStyleableProperty(Ribbon n) {
    			return (StyleableProperty<Number>)(WritableValue<Number>)n.tabMinWidthProperty();
    		};
    	};
    	
    	private static final CssMetaData<Ribbon, Number> TAB_MAX_WIDTH = 
    			new CssMetaData<Ribbon, Number>("-fx-tab-max-width",
    					SizeConverter.getInstance(), DEFAULT_TAB_MAX_WIDTH)  {

    		@Override
    		public boolean isSettable(Ribbon n) {
    			return n.tabMaxWidth == null || !n.tabMaxWidth.isBound();
    		}

    		@Override
    		public StyleableProperty<Number> getStyleableProperty(Ribbon n) {
    			return (StyleableProperty<Number>)(WritableValue<Number>)n.tabMaxWidthProperty();
    		};
    	};
    	
    	private static final CssMetaData<Ribbon, Number> TAB_MIN_HEIGHT = 
    			new CssMetaData<Ribbon, Number>("-fx-tab-min-height",
    					SizeConverter.getInstance(), DEFAULT_TAB_MIN_HEIGHT)  {

    		@Override
    		public boolean isSettable(Ribbon n) {
    			return n.tabMinHeight == null || !n.tabMinHeight.isBound();
    		}

    		@Override
    		public StyleableProperty<Number> getStyleableProperty(Ribbon n) {
    			return (StyleableProperty<Number>)(WritableValue<Number>)n.tabMinHeightProperty();
    		};
    	};
    	
    	private static final CssMetaData<Ribbon, Number> TAB_MAX_HEIGHT = 
    			new CssMetaData<Ribbon, Number>("-fx-tab-max-height",
    					SizeConverter.getInstance(), DEFAULT_TAB_MAX_HEIGHT)  {

    		@Override
    		public boolean isSettable(Ribbon n) {
    			return n.tabMaxHeight == null || !n.tabMaxHeight.isBound();
    		}

    		@Override
    		public StyleableProperty<Number> getStyleableProperty(Ribbon n) {
    			return (StyleableProperty<Number>)(WritableValue<Number>)n.tabMaxHeightProperty();
    		};
    	};
    }
    

    /***************************************************************************
     *                                                                         *
     * Support classes                                                         *
     *                                                                         *
     **************************************************************************/

    static class RibbonSelectionModel extends SingleSelectionModel<RibbonTab> {
        private final Ribbon ribbon;

        public RibbonSelectionModel(final Ribbon t) {
            if (t == null) {
                throw new NullPointerException("TabPane can not be null");
            }
            this.ribbon = t;

            // watching for changes to the items list content
            final ListChangeListener<RibbonTab> itemsContentObserver = c -> {
                while (c.next()) {
                    for (RibbonTab tab : c.getRemoved()) {
                        if (tab != null && !ribbon.getTabs().contains(tab)) {
                            if (tab.isSelected()) {
                                tab.setSelected(false);
                                final int tabIndex = c.getFrom();

                                // we always try to select the nearest, non-disabled
                                // tab from the position of the closed tab.
                                findNearestAvailableTab(tabIndex, true);
                            }
                        }
                    }
                    if (c.wasAdded() || c.wasRemoved()) {
                        // The selected tab index can be out of sync with the list of tab if
                        // we add or remove tabs before the selected tab.
                        if (getSelectedIndex() != ribbon.getTabs().indexOf(getSelectedItem())) {
                            clearAndSelect(ribbon.getTabs().indexOf(getSelectedItem()));
                        }
                    }
                }
                if (getSelectedIndex() == -1 && getSelectedItem() == null && ribbon.getTabs().size() > 0) {
                    // we go looking for the first non-disabled tab, as opposed to
                    // just selecting the first tab (fix for RT-36908)
                    findNearestAvailableTab(0, true);
                } else if (ribbon.getTabs().isEmpty()) {
                    clearSelection();
                }
            };
            if (this.ribbon.getTabs() != null) {
                this.ribbon.getTabs().addListener(itemsContentObserver);
            }
        }

        // API Implementation
        @Override public void select(int index) {
            if (index < 0 || (getItemCount() > 0 && index >= getItemCount()) ||
                (index == getSelectedIndex() && getModelItem(index).isSelected())) {
                return;
            }

            // Unselect the old tab
            if (getSelectedIndex() >= 0 && getSelectedIndex() < ribbon.getTabs().size()) {
                ribbon.getTabs().get(getSelectedIndex()).setSelected(false);
            }

            setSelectedIndex(index);

            RibbonTab tab = getModelItem(index);
            if (tab != null) {
                setSelectedItem(tab);
            }

            // Select the new tab
            if (getSelectedIndex() >= 0 && getSelectedIndex() < ribbon.getTabs().size()) {
                ribbon.getTabs().get(getSelectedIndex()).setSelected(true);
            }

            /* Does this get all the change events */
            ribbon.notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
        }

        @Override public void select(RibbonTab tab) {
            final int itemCount = getItemCount();

            for (int i = 0; i < itemCount; i++) {
                final RibbonTab value = getModelItem(i);
                if (value != null && value.equals(tab)) {
                    select(i);
                    return;
                }
            }
            if (tab != null) {
                setSelectedItem(tab);
            }
        }

        @Override protected RibbonTab getModelItem(int index) {
            final ObservableList<RibbonTab> items = ribbon.getTabs();
            if (items == null) return null;
            if (index < 0 || index >= items.size()) return null;
            return items.get(index);
        }

        @Override protected int getItemCount() {
            final ObservableList<RibbonTab> items = ribbon.getTabs();
            return items == null ? 0 : items.size();
        }

        private RibbonTab findNearestAvailableTab(int tabIndex, boolean doSelect) {
            // we always try to select the nearest, non-disabled
            // tab from the position of the closed tab.
            final int tabCount = getItemCount();
            int i = 1;
            RibbonTab bestTab = null;
            while (true) {
                // look leftwards
                int downPos = tabIndex - i;
                if (downPos >= 0) {
                	RibbonTab _tab = getModelItem(downPos);
                    if (_tab != null && ! _tab.isDisable()) {
                        bestTab = _tab;
                        break;
                    }
                }

                // look rightwards. We subtract one as we need
                // to take into account that a tab has been removed
                // and if we don't do this we'll miss the tab
                // to the right of the tab (as it has moved into
                // the removed tabs position).
                int upPos = tabIndex + i - 1;
                if (upPos < tabCount) {
                	RibbonTab _tab = getModelItem(upPos);
                    if (_tab != null && ! _tab.isDisable()) {
                        bestTab = _tab;
                        break;
                    }
                }

                if (downPos < 0 && upPos >= tabCount) {
                    break;
                }
                i++;
            }

            if (doSelect && bestTab != null) {
                select(bestTab);
            }

            return bestTab;
        }
    }
    
    public void setContextualGroupColor(String groupName, Color groupColor) {
    	contextualGroupHeadersMap.put(groupName, groupColor);
    }
    
    public Color getContextualGroupColor(String groupName) {
    	return contextualGroupHeadersMap.get(groupName);
    }
}
