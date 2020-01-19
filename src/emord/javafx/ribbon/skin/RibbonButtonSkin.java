package emord.javafx.ribbon.skin;

import emord.javafx.ribbon.RibbonButton;
import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;

public class RibbonButtonSkin extends SkinBase<RibbonButton> {
	
	private Pane container;

	public RibbonButtonSkin(RibbonButton control) {
		super(control);
		construct();
	}

	private void construct() {
		if (getSkinnable().getControlSize() == RibbonButton.ControlSize.LARGE) {
			container = new VBox();
			((VBox)container).setAlignment(Pos.TOP_CENTER);
		}
		else {
			container = new HBox();
			((HBox)container).setAlignment(Pos.CENTER_LEFT);
		}
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
		getSkinnable().controlSizeProperty().addListener((obs, oldValue, newValue) -> {
			update();
		});
		
		update();
	}
	
	private void update() {
		ButtonTextHelper.layoutButton(getSkinnable(), container, false, getSkinnable().getControlSize());
		
		container.setOnMouseClicked((event) -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				getSkinnable().fire();
			}
		});
	}
}
