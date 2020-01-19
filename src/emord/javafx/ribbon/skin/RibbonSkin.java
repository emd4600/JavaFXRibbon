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

import emord.javafx.ribbon.Ribbon;
import emord.javafx.ribbon.Ribbon.RibbonState;
import emord.javafx.ribbon.RibbonButton;
import emord.javafx.ribbon.RibbonGroup;
import emord.javafx.ribbon.RibbonMenu;
import emord.javafx.ribbon.RibbonProgramButton;
import emord.javafx.ribbon.RibbonTab;
import javafx.collections.ListChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * The skin implementation of a {@link Ribbon}. It is basically composed of a {@link BorderPane}:
 * <li>The <code>TOP</code> region is used to display the tab selection buttons.
 * <li>The <code>CENTER</code> region is used to display the tab content.
 * When the ribbon is hidden, the center region is set to null.
 */
public class RibbonSkin extends SkinBase<Ribbon> {
	
	private final BorderPane pane = new BorderPane();
	private final BorderPane tabsBorderPane = new BorderPane();
	/** The pane that displays the tab selection buttons. */
	// No spacing between tabs
	private final HBox tabsPane = new HBox(0);
	private Pane tabsPaneOutline;
	
	private final BorderPane contentPane = new BorderPane();
	private final HBox groupsContainer = new HBox();
	
	private ImageView expandImage;
	private ImageView minimizeImage;
	
	private final Button expandButton = new Button();

	public RibbonSkin(Ribbon control) {
		super(control);
		construct();
	}
	
	private void handleRibbonState(RibbonState state) {
		if (state == RibbonState.DROPDOWN) {
			getSkinnable().getRibbonWindow().getChildren().add(contentPane);
			
			contentPane.setTranslateY(tabsPane.getBoundsInParent().getMaxY());
		}
		else {
			// Reset
			getSkinnable().getRibbonWindow().getChildren().remove(contentPane);
			contentPane.setTranslateY(0);
			
			if (state == RibbonState.EXPANDED) {
				pane.setCenter(contentPane);
				updateContentHeight(getSkinnable().getContentHeight());
			}
			else if (state == RibbonState.MINIMIZED) {
				pane.setCenter(null);
			}
		}
		
		if (state == RibbonState.EXPANDED)
			expandButton.setGraphic(minimizeImage);
		else
			expandButton.setGraphic(expandImage);
	}
	
	private void constructTabsPane() {
		tabsPane.getChildren().clear();
		
		createProgramButton();
		
		for (RibbonTab tab : getSkinnable().getTabs()) {
			createTabHeader(tab);
		}
		
		tabsPane.getChildren().add(tabsPaneOutline);
	}

	private void construct() {
		
		
		tabsBorderPane.setCenter(tabsPane);
		pane.setTop(tabsBorderPane);
		
		contentPane.setCenter(groupsContainer);
		contentPane.getStyleClass().add("ribbon-content-area");
		groupsContainer.maxHeightProperty().bind(contentPane.maxHeightProperty());
		
		groupsContainer.setAlignment(Pos.CENTER_LEFT);
		
		updateContentHeight(getSkinnable().getContentHeight());
		
		handleRibbonState(getSkinnable().getRibbonState());
		
		tabsPaneOutline = new Pane();
		tabsPaneOutline.getStyleClass().add("ribbon-tab-header-outline");
		
		HBox.setHgrow(tabsPaneOutline, Priority.SOMETIMES);
		
		constructTabsPane();
		
		setContent(getSkinnable().getSelectionModel().getSelectedItem());
		
		getChildren().add(pane);
		
		// -- Expand Button -- //
		
		expandImage = new ImageView(new Image(getSkinnable().getRibbonWindow().getResource("ribbon-expand.png")));
		minimizeImage = new ImageView(new Image(getSkinnable().getRibbonWindow().getResource("ribbon-minimize.png")));
		
		expandButton.getStyleClass().add("ribbon-minimize-button");
		expandButton.setOnAction((event) -> {
			if (getSkinnable().getRibbonState() == RibbonState.EXPANDED) {
				getSkinnable().setRibbonState(RibbonState.MINIMIZED);
			}
			else/* if (getSkinnable().getRibbonState() == RibbonState.DROPDOWN)*/ {
				getSkinnable().setRibbonState(RibbonState.EXPANDED);
			}
		});
		
		expandButton.setGraphic(minimizeImage);
		//contentPane.setRight(expandButton);
		tabsBorderPane.setRight(expandButton);
		BorderPane.setAlignment(expandButton, Pos.TOP_RIGHT);
		
		// -- Update Listeners -- //
		
		getSkinnable().getTabs().addListener((ListChangeListener.Change<? extends RibbonTab> c) -> {
			constructTabsPane();
		});
		
		getSkinnable().getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				setContent(newValue);
			}
		});
		
		getSkinnable().contentHeightProperty().addListener((obs, oldValue, newValue) -> {
			updateContentHeight(newValue.doubleValue());
		});
		
		getSkinnable().ribbonStateProperty().addListener((obs, oldValue, newValue) -> {
			handleRibbonState(newValue);
		});
		
		getSkinnable().setOnScroll((ScrollEvent event) -> {
			if ((event.getDeltaY() < 0) && (getSkinnable().getSelectionModel().getSelectedIndex() < getSkinnable().getTabs().size() - 1)) {
				getSkinnable().getSelectionModel().select(getSkinnable().getSelectionModel().getSelectedIndex() + 1);
			}
			else if ((event.getDeltaY() > 0) && (getSkinnable().getSelectionModel().getSelectedIndex() > 0)) {
				getSkinnable().getSelectionModel().select(getSkinnable().getSelectionModel().getSelectedIndex() - 1);
			}
		});
		
		getSkinnable().getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, (event) -> {
			// If ribbon is in dropdown and the user clicked outside the content/tab headers/program button,
			// minimize the ribbon again
			if (getSkinnable().getRibbonState() == RibbonState.DROPDOWN) {
				
				// If the click was on the content pane, do nothing
				if (contentPane.contains(contentPane.sceneToLocal(event.getSceneX(), event.getSceneY()))) {
					return;
				}
				
				// If the click was on a tab header or program button, do nothing
				for (Node node : tabsPane.getChildren()) {
					if (node.contains(node.sceneToLocal(event.getSceneX(), event.getSceneY()))
							// Clicking on the outline panel DOES minimize the ribbon again
							&& node != tabsPaneOutline) {
						return;
					}
				}
				
				// If we have arrived here, minimize the ribbon
				getSkinnable().setRibbonState(RibbonState.MINIMIZED);
			}
		});
	}
	
	private void createProgramButton() {
		if (getSkinnable().getProgramButton() != null) {
			RibbonProgramButton button = getSkinnable().getProgramButton();
			
			button.setOnAction((event) -> {
				if (button.getRibbonMenu().isShowing()) {
					button.getRibbonMenu().hide();
					button.setSelected(false);
				}
				else {
					Bounds rect = button.localToScreen(button.getBoundsInLocal());
					
					button.getRibbonMenu().show(getSkinnable(), rect.getMinX(), rect.getMaxY());
					button.setSelected(true);
				}
			});
			
			button.sceneProperty().addListener((obs, oldValue, newValue) -> {
				if (newValue != null) {
					newValue.addEventFilter(MouseEvent.MOUSE_PRESSED, (event) -> {
						RibbonMenu menu = button.getRibbonMenu();
						if (menu.isShowing() && !menu.contains(event.getScreenX(), event.getScreenY())) {
							menu.hide();
							
							// Also ensure we deselect the button 
							button.setSelected(false);
							
							// If the click was on the program button, it will open the menu again
							// in the action event; consume the click to avoid that
							if (button.contains(button.screenToLocal(event.getScreenX(), event.getScreenY()))) {
								event.consume();
							}
						};

					});
				}
			});
			
			tabsPane.getChildren().add(button);
		}
	}
	
	private void updateContentHeight(double newValue) {
		if (newValue == Ribbon.NO_FIXED_HEIGHT) {
			contentPane.setPrefHeight(Ribbon.USE_COMPUTED_SIZE);
			contentPane.setMaxHeight(Ribbon.USE_COMPUTED_SIZE);
			contentPane.setMinHeight(Ribbon.USE_COMPUTED_SIZE);
		}
		else {
			contentPane.setPrefHeight(newValue);
			contentPane.setMaxHeight(newValue);
			contentPane.setMinHeight(newValue);
		}
	}
	
	private void createTabHeader(RibbonTab tab) {
		RibbonHeader button = new RibbonHeader(tab);
		tabsPane.getChildren().add(button);
		
		button.setOnMouseClicked((event) -> {
			
			if (event.getButton() == MouseButton.PRIMARY) {
				RibbonState state = getSkinnable().getRibbonState();
				
				if (state == RibbonState.EXPANDED) {
					// Two possibilities:
					// 1. Double-click on the selected tab: minimize the ribbon
					if (tab.isSelected() && event.getClickCount() == 2) {
						getSkinnable().setRibbonState(RibbonState.MINIMIZED);
					}
					// 2. Just clicked on another tab, select it
					else {
						select(tab);
					}
				}
				else if (state == RibbonState.MINIMIZED) {
					// Two possibilities:
					// 1. Double-click on any tab: set it to expanded again, and select that tab
					if (event.getClickCount() == 2) {
						select(tab);
						getSkinnable().setRibbonState(RibbonState.EXPANDED);
					}
					// 2. Single-click on any tab: select it and display as drop-down
					else {
						select(tab);
						getSkinnable().setRibbonState(RibbonState.DROPDOWN);
					}
				}
				else if (state == RibbonState.DROPDOWN) {
					// Two possibilities:
					// 1. Double-click on any tab: set it to expanded again, and select that tab
					if (event.getClickCount() == 2) {
						select(tab);
						getSkinnable().setRibbonState(RibbonState.EXPANDED);
					}
					// 2. Single-click on any tab: select it, but keep as drop-down
					else {
						select(tab);
					}
				}
			} 
		});
		
	}
	
	private void select(RibbonTab tab) {
		tab.getRibbon().getSelectionModel().select(tab);
	}
	
	private void setContent(RibbonTab tab) {
		groupsContainer.getChildren().clear();
		
		for (RibbonGroup group : tab.getGroups()) {
			//group.minHeightProperty().bind(getSkinnable().contentHeightProperty());
			group.prefHeightProperty().bind(getSkinnable().contentHeightProperty());
			//group.maxHeightProperty().bind(getSkinnable().contentHeightProperty());
			
			groupsContainer.getChildren().add(group);
			
			Separator separator = new Separator(Orientation.VERTICAL);
			separator.getStyleClass().add("ribbon-group-separator");
			separator.setMaxHeight(getSkinnable().getContentHeight() - 6);
			groupsContainer.getChildren().add(separator);
		}
	}
}
