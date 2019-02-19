package emord.javafx.ribbon.skin;

import emord.javafx.ribbon.RibbonButton;
import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseButton;
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
