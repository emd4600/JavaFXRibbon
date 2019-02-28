package emord.javafx.ribbon.skin;

import emord.javafx.ribbon.Ribbon;
import emord.javafx.ribbon.RibbonButton;
import emord.javafx.ribbon.RibbonGroup;
import javafx.application.Platform;
import javafx.css.Styleable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class RibbonButtonSkin extends SkinBase<RibbonButton> {
	
	private VBox container;

	public RibbonButtonSkin(RibbonButton control) {
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
		
		
		Platform.runLater(new Runnable() {
            @Override public void run() {
					ContextMenu menu = new ContextMenu();
					MenuItem addToQuickAccessMenuItem = new MenuItem();
					addToQuickAccessMenuItem.setOnAction((event) -> {
						Node potentialParent = getSkinnable().getParent();
						while (potentialParent != null) {
							potentialParent = potentialParent.getParent();
							if (potentialParent instanceof Ribbon)
								break;
						}

						if ((potentialParent != null) && (potentialParent instanceof Ribbon)) {
			            	final Ribbon ribbon = (Ribbon)potentialParent;
							
			            	if (getSkinnable().getOnMouseClicked() != null)
			            		ribbon.addToQuickAccess(getSkinnable().getOnMouseClicked(), getSkinnable().getGraphic());
			            	else if (getSkinnable().getOnAction() != null)
			            		ribbon.addToQuickAccess(getSkinnable().getOnAction(), getSkinnable().getGraphic());
						}
					});
					addToQuickAccessMenuItem.setText("Add to Quick Access Toolbar");
					menu.getItems().add(addToQuickAccessMenuItem);
					getSkinnable().setContextMenu(menu);
            }
		});
		
		update();
	}
	
	private void update() {
		ButtonTextHelper.layoutButton(getSkinnable(), container, false);
		
		container.setOnMouseClicked((event) -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				getSkinnable().fire();
			}
		});
	}
}
