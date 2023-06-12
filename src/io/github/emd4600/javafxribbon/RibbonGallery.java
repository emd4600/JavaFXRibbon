package io.github.emd4600.javafxribbon;

import java.util.function.Consumer;

import io.github.emd4600.javafxribbon.skin.RibbonGallerySkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class RibbonGallery extends Control {

	public static enum GalleryItemDisplay { GRAPHIC_PRIORITY, TEXT_PRIORITY }
	
	public static final String DEFAULT_STYLE_CLASS = "ribbon-gallery";
	
	private final ObjectProperty<GalleryItemDisplay> displayPriority = new SimpleObjectProperty<>(GalleryItemDisplay.TEXT_PRIORITY);
	private final Ribbon ribbon;
	private final ObservableList<RibbonGalleryItem> items = FXCollections.observableArrayList();
	private final IntegerProperty displayedRow = new SimpleIntegerProperty(0);
	private final BooleanProperty showingPopup = new SimpleBooleanProperty(false);
	private final IntegerProperty columnCount = new SimpleIntegerProperty(5);
	
	private Consumer<RibbonGalleryItem> onItemAction;
	
	public RibbonGallery(Ribbon ribbon) {
		super();
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		
		this.ribbon = ribbon;
	}
	
	public Ribbon getRibbon() {
		return ribbon;
	}
	
	public void setOnItemAction(Consumer<RibbonGalleryItem> action) {
		onItemAction = action;
	}
	
	public Consumer<RibbonGalleryItem> getOnItemAction() {
		return onItemAction;
	}
	
	public final ObservableList<RibbonGalleryItem> getItems() {
		return items;
	}
	
	public final ObjectProperty<GalleryItemDisplay> displayPriorityProperty() {
		return displayPriority;
	}
	
	public final GalleryItemDisplay getDisplayPriority() {
		return displayPriority.get();
	}
	
	public final void setDisplayPriority(GalleryItemDisplay value) {
		displayPriority.set(value);
	}
	
	public final IntegerProperty displayedRowProperty() {
		return displayedRow;
	}
	
	public final int getDisplayedRow() {
		return displayedRow.get();
	}
	
	public final void setDisplayedRow(int value) {
		displayedRow.set(value);
	}
	
	public final BooleanProperty showingPopupProperty() {
		return showingPopup;
	}
	
	public final boolean isShowingPopup() {
		return showingPopup.get();
	}
	
	public final void setShowingPopup(boolean value) {
		showingPopup.set(value);
	}
	
	public final IntegerProperty columnCountProperty() {
		return columnCount;
	}
	
	public final int getColumnCount() {
		return columnCount.get();
	}
	
	public final void setColumnCount(int value) {
		columnCount.set(value);
	}
	
	/** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new RibbonGallerySkin(this);
    }
}
