package emord.javafx.ribbon.skin;

import emord.javafx.ribbon.RibbonGroup;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.geometry.Orientation;

public class RibbonGroupSkin extends SkinBase<RibbonGroup> {

	public RibbonGroupSkin(RibbonGroup group) {
		super(group);
		construct();
	}

	private void construct() {
		RibbonGroup group = getSkinnable();
		
		BorderPane groupPane = new BorderPane();
		groupPane.getStyleClass().add("ribbon-group");
		
		//HBox hbox = new HBox();
		FlowPane hbox = new FlowPane();
		hbox.setOrientation(Orientation.VERTICAL);
		//hbox.setPrefWrapLength(400);
		//hbox.spacingProperty().bind(group.spacingProperty());
		
//		hbox.getChildren().addListener((ListChangeListener<Node>) c -> {
//			double totalHeight = 0;
//			
//			while (c.next()) {
//				for (Node n : c.getAddedSubList()) {
//					totalHeight += n.getBoundsInLocal().getHeight();
//				}
//			}
//			
//			System.out.println(totalHeight);
//			System.out.println(((Region) getSkinnable().getParent()).getMaxHeight());
//			double scale = totalHeight / ((Region) getSkinnable().getParent()).getMaxHeight();
//			hbox.setScaleX(scale);
//			hbox.setScaleY(scale);
//		});
		
//		hbox.heightProperty().addListener((obs, oldValue, newValue) -> {
//			System.out.println(newValue);
//			
//			double totalHeight = 0;
//			for (Node n : group.getNodes()) {
//				double height = n.getBoundsInLocal().getHeight();
//				if (height > totalHeight) totalHeight = height;
//			}
//			
//			System.out.println(totalHeight);
//			
//			double scale = newValue.doubleValue() / totalHeight;
////			hbox.setScaleX(scale);
////			hbox.setScaleY(scale);
//		});
		
		hbox.getChildren().addAll(group.getNodes());
		
		Label title = new Label();
		title.textProperty().bind(group.textProperty());
		title.alignmentProperty().bind(group.alignmentProperty());
		
		title.getStyleClass().add("ribbon-group-title");
		
		//groupPane.setStyle("-fx-background-color: red;");
		
		groupPane.setCenter(hbox);
		//groupPane.setTop(hbox);
		groupPane.setBottom(title);
		BorderPane.setAlignment(hbox, Pos.TOP_CENTER);
		
		groupPane.setMinHeight(getSkinnable().getPrefHeight());
		
		BorderPane.setAlignment(title, Pos.CENTER);
		
		getChildren().add(groupPane);
		hbox.prefWrapLengthProperty().bind(getSkinnable().heightProperty());//.bind(Bindings.add(0.0D, getSkinnable().heightProperty()));
		
		// Add listener
		group.getNodes().addListener((ListChangeListener<Node>) c -> {
			hbox.getChildren().setAll(c.getList());
			//hbox.prefWidth(hbox.USE_COMPUTED_SIZE);
			//group.prefWidth(group.USE_COMPUTED_SIZE);
			//getSkinnable().getPrefWidth(getSkinnable().USE_COMPUTED_SIZE);
		});
	}
}
