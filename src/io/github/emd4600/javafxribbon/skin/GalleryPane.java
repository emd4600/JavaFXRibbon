package io.github.emd4600.javafxribbon.skin;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class GalleryPane extends Pane {
	
	private final IntegerProperty displayedRow = new SimpleIntegerProperty(0){
		@Override public void invalidated() {
            requestLayout();
        }
	};
	private final IntegerProperty columnCount = new SimpleIntegerProperty(4) {
		@Override public void invalidated() {
            requestLayout();
        }
	};
	private final ReadOnlyIntegerWrapper numVisibleRows = new ReadOnlyIntegerWrapper(0){
		@Override public void invalidated() {
            requestLayout();
        }
	};
	private final BooleanProperty expanded = new SimpleBooleanProperty(false){
		@Override public void invalidated() {
            requestLayout();
        }
	};
	
	private int numRows;
	private int numColumns;
	private double tileWidth;
	private double tileHeight;
	private boolean paramsCalculated;
	
	public void recalculateParameters() {
		Insets insets = getInsets() == null ? Insets.EMPTY : getInsets();
		
		double width = getWidth() - insets.getLeft() - insets.getRight();
		double height = getHeight() - insets.getTop() - insets.getBottom();
		tileWidth = getMaxChildrenWidth();
		tileHeight = getMaxChildrenHeight();
		
		if (width == 0 || height == 0 || tileWidth == 0 || tileHeight == 0) {
			paramsCalculated = false;
			return;
		}
		
		numColumns = (int) (width / tileWidth);
		numRows = (int) Math.ceil(getChildren().size() / (double)numColumns);
		numVisibleRows.set((int) (height / tileHeight));
		
		tileWidth = width / numColumns;
		tileHeight = height / numVisibleRows.get();
		
		paramsCalculated = true;
	}

	@Override protected void layoutChildren() {
		recalculateParameters();
		
		if (!paramsCalculated) return;
		
		int currentRow = displayedRow.get();
		
		int size = getChildren().size();
		for (int i = 0; i < size; ++i) {
			int row = i / numColumns;
			int column = i % numColumns;
			Node node = getChildren().get(i);
			
			if (row >= currentRow && row < currentRow + numVisibleRows.get()) {
				node.relocate(column * tileWidth, (row - currentRow) * tileHeight);
				node.resize(tileWidth, tileHeight);
			}
			else {
				node.resize(0, 0);
			}
		}
	}
	
	public int getTotalRows() {
		return numRows;
	}
	
	public int getVisibleRows() {
		return numVisibleRows.get();
	}
	
	public ReadOnlyIntegerProperty visibleRowsProperty() {
		return numVisibleRows.getReadOnlyProperty();
	}
	
	public int getVisibleColumns() {
		return numColumns;
	}
	
	public final int getDisplayedRow() {
		return displayedRow.get();
	}
	
	public final void setDisplayedRow(int value) {
		displayedRow.set(value);
	}
	
	public final IntegerProperty displayedRowProperty() {
		return displayedRow;
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
	
	public final void setExpanded(boolean value) {
		expanded.set(value);
	}
	
	@Override protected double computePrefWidth(double height) {
		double value = getInsets().getLeft() + getMaxChildrenWidth() * columnCount.get() + getInsets().getRight();
		return value;
	}
	
	@Override protected double computePrefHeight(double width) {
		double maxHeight = getMaxChildrenHeight();
		if (expanded.get()) {
			maxHeight *= Math.ceil(getChildren().size() / (double)columnCount.get());
		}
		return getInsets().getTop() + maxHeight + getInsets().getBottom();
	}
	
	private double getMaxChildrenHeight() {
		double maxHeight = 0;
		for (Node node : getChildren()) {
			double height = node.prefHeight(-1);
			if (height > maxHeight) maxHeight = height;
		}
		return maxHeight;
	}
	
	private double getMaxChildrenWidth() {
		double maxWidth = 0;
		for (Node node : getChildren()) {
			double width = node.prefWidth(-1);
			if (width > maxWidth) maxWidth = width;
		}
		return maxWidth;
	}
}
