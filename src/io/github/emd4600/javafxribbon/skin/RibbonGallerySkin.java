package io.github.emd4600.javafxribbon.skin;

import java.util.function.Consumer;

import io.github.emd4600.javafxribbon.RibbonGallery;
import io.github.emd4600.javafxribbon.RibbonGalleryItem;
import io.github.emd4600.javafxribbon.RibbonGallery.GalleryItemDisplay;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;

public class RibbonGallerySkin extends SkinBase<RibbonGallery> {
	
	private final BorderPane mainPane = new BorderPane();
	private final VBox buttonsPane = new VBox();
	
	private StackPane upButton;
	private StackPane downButton;
	private StackPane popupButton;
	
	private final Popup popup = new Popup();
	
	private final GalleryPane itemsPane = new GalleryPane();
	private final GalleryPane popupPane = new GalleryPane();
	private final Pane popupMainPane = new Pane();

	public RibbonGallerySkin(RibbonGallery control) {
		super(control);
		
		buttonsPane.getStyleClass().add("gallery-buttons-pane");
		createButtons(buttonsPane);
		
		itemsPane.getStyleClass().add("items-pane");
		itemsPane.columnCountProperty().bind(control.columnCountProperty());
		itemsPane.displayedRowProperty().bind(control.displayedRowProperty());
		itemsPane.setPrefHeight(Double.MAX_VALUE);
		itemsPane.setMinWidth(Control.USE_PREF_SIZE);
		
		popupPane.getStyleClass().add("items-pane");
		popupPane.columnCountProperty().bind(control.columnCountProperty());
		popupPane.setPrefHeight(Control.USE_COMPUTED_SIZE);
		popupPane.setMinWidth(Control.USE_PREF_SIZE);
		popupPane.setMinHeight(Control.USE_PREF_SIZE);
		popupPane.setExpanded(true);
		
		mainPane.setCenter(itemsPane);
		mainPane.setRight(buttonsPane);
		
		getChildren().add(mainPane);
		
		generateItems();
		
		control.getItems().addListener((ListChangeListener.Change<? extends RibbonGalleryItem> c) -> {
			generateItems();
		});
		
		control.displayedRowProperty().addListener((obs, oldValue, newValue) -> {
			changeDisplayRow(newValue.intValue());
		});
		
		control.showingPopupProperty().addListener((obs, oldValue, newValue) -> {
			showPopup(newValue);
		});
		
		itemsPane.visibleRowsProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue.intValue() != 0) updateButtons();
		});
		
		
		popupMainPane.getChildren().add(popupPane);
		
		popupMainPane.getStylesheets().addAll(control.getRibbon().getRibbonWindow().getUserAgentStylesheet());
		popup.getContent().add(popupMainPane);
		
		Platform.runLater(() -> {
			control.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
				if (!popupMainPane.contains(popupMainPane.screenToLocal(event.getX(), event.getY()))) {
					control.setShowingPopup(false);
				}
			});
		});
	}
	
	private void showPopup(boolean show) {
		if (show) {
			popupMainPane.getStyleClass().setAll(getSkinnable().getStyleClass());
			popupMainPane.getStyleClass().add("ribbon-gallery-popup");
			
			popupMainPane.setPrefWidth(getSkinnable().getWidth());
			
			Point2D coords = getSkinnable().localToScreen(0, 0);
			popup.show(getSkinnable(), coords.getX(), coords.getY());
		} else {
			popup.hide();
		}
	}
	
	private Button createItem(RibbonGalleryItem item) {
		Button label = new Button(item.getText());
		label.setGraphic(item.getGraphic());
		label.setMinWidth(Control.USE_PREF_SIZE);
		label.getStyleClass().add("gallery-item");
		label.setTextAlignment(TextAlignment.LEFT);
		label.setAlignment(Pos.CENTER_LEFT);
		
		if (item.getGraphic() != null) {
			if (getSkinnable().getDisplayPriority() == GalleryItemDisplay.GRAPHIC_PRIORITY) {
				label.setContentDisplay(ContentDisplay.CENTER);
			} else {
				label.setContentDisplay(ContentDisplay.LEFT);
			}
		}
		
		if (item.getDescription() != null) {
			label.setTooltip(new Tooltip(item.getDescription()));
		}
		
		label.setOnAction(event -> {
			if (getSkinnable().isShowingPopup()) {
				getSkinnable().setShowingPopup(false);
			}
			
			Consumer<RibbonGalleryItem> action = getSkinnable().getOnItemAction();
			if (action != null) action.accept(item);
		});
		
		return label;
	}
	
	private void generateItems() {
		for (RibbonGalleryItem item : getSkinnable().getItems()) {
			itemsPane.getChildren().add(createItem(item));
			popupPane.getChildren().add(createItem(item));
		}
	}

	private void createButtons(VBox buttonsPane) {
		upButton = new StackPane();
		upButton.getStyleClass().addAll("gallery-button");
		upButton.setOnMouseReleased(event -> {
			if (!upButton.isDisabled()) getSkinnable().setDisplayedRow(getSkinnable().getDisplayedRow() - itemsPane.getVisibleRows());
		});
		
		downButton = new StackPane();
		downButton.getStyleClass().addAll("gallery-button");
		downButton.setOnMouseReleased(event -> {
			if (!downButton.isDisabled()) getSkinnable().setDisplayedRow(getSkinnable().getDisplayedRow() + itemsPane.getVisibleRows());
		});
		
		popupButton = new StackPane();
		popupButton.getStyleClass().addAll("gallery-button");
		popupButton.setOnMouseReleased(event -> {
			if (!popupButton.isDisabled()) getSkinnable().setShowingPopup(true);
		});
		
		Region upButtonGraphic = new Region();
        upButtonGraphic.getStyleClass().setAll("up-button-graphic");
        upButtonGraphic.setFocusTraversable(false);
        upButtonGraphic.setMaxHeight(Region.USE_PREF_SIZE);
        upButtonGraphic.setMaxWidth(Region.USE_PREF_SIZE);
        
        Region downButtonGraphic = new Region();
        downButtonGraphic.getStyleClass().setAll("down-button-graphic");
        downButtonGraphic.setFocusTraversable(false);
        downButtonGraphic.setMaxWidth(Region.USE_PREF_SIZE);
        downButtonGraphic.setMaxHeight(Region.USE_PREF_SIZE);

        Region popupButtonGraphic = new Region();
        popupButtonGraphic.getStyleClass().setAll("popup-button-graphic");
        popupButtonGraphic.setFocusTraversable(false);
        popupButtonGraphic.setMaxWidth(Region.USE_PREF_SIZE);
        popupButtonGraphic.setMaxHeight(Region.USE_PREF_SIZE);
        
        upButton.getChildren().add(upButtonGraphic);
        downButton.getChildren().add(downButtonGraphic);
        popupButton.getChildren().add(popupButtonGraphic);
        
        buttonsPane.getChildren().addAll(upButton, downButton, popupButton);
	}
	
	private void updateButtons() {
		int numVisibleRows = itemsPane.getVisibleRows();
		int numRows = itemsPane.getTotalRows();
    	int currentRow = getSkinnable().getDisplayedRow();
    	setDisable(upButton, currentRow == 0);
    	setDisable(downButton, currentRow >= numRows - numVisibleRows);
    	setDisable(popupButton, numVisibleRows == numRows);
    }
    
    private static void setDisable(Node element, boolean isDisabled) {
    	element.setDisable(isDisabled);
    	if (isDisabled) {
    		element.getStyleClass().add("disabled");
    	} else {
    		element.getStyleClass().remove("disabled");
    	}
    }
    
    private void changeDisplayRow(int newRow) {
    	updateButtons();
    }
 
}
