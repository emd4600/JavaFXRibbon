package io.github.emd4600.javafxribbon.skin;

import io.github.emd4600.javafxribbon.RibbonMenuButton;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.VBox;

public class RibbonMenuButtonSkin extends SkinBase<RibbonMenuButton> {
	
	private static final PseudoClass PSEUDO_CLASS_SHOWING = PseudoClass.getPseudoClass("showing");

	
	private VBox container;
	private final ContextMenu contextMenu = new ContextMenu();

	public RibbonMenuButtonSkin(RibbonMenuButton control) {
		super(control);
		construct();
	}

	private void construct() {
		container = new VBox();
		container.setAlignment(Pos.TOP_CENTER);
		getChildren().add(container);
		
		getSkinnable().textProperty().addListener((obs, oldValue, newValue) -> {
			update();
		});
		getSkinnable().graphicProperty().addListener((obs, oldValue, newValue) -> {
			update();
		});
		getSkinnable().styleProperty().addListener((obs, oldValue, newValue) -> {
			update();
		});
		
		update();
		
		RibbonMenuButton button = getSkinnable();
		
		button.showingProperty().addListener((obs, oldValue, newValue) -> {if (newValue) {
				Bounds rect = button.localToScreen(button.getBoundsInLocal());
				
				contextMenu.show(button, rect.getMinX(), rect.getMaxY());
			}
			else {
				contextMenu.hide();
			}
		});
		
		contextMenu.showingProperty().addListener((obs, oldValue, newValue) -> {
			// Keep it synchronized
			if (!newValue) {
				button.hide();
				
			}
			
			button.pseudoClassStateChanged(PSEUDO_CLASS_SHOWING, newValue);
		});
		
		button.setOnMouseClicked((event) -> {
			if (contextMenu.isShowing()) {
				button.hide();
			}
			else {
				button.show();
			}
		});
		
		// Allow items updating
		button.getItems().addListener(new ListChangeListener<MenuItem>() {

			@Override
			public void onChanged(Change<? extends MenuItem> c) {
				while (c.next()) {
					contextMenu.getItems().removeAll(c.getRemoved());
					contextMenu.getItems().addAll(c.getFrom(), c.getAddedSubList());
				}
			}
		});
		
		contextMenu.getItems().setAll(button.getItems());
	}
	
	private void update() {
		ButtonTextHelper.layoutButton(getSkinnable(), container, true);
	}
}
