package emord.javafx.ribbon;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BasicRibbonTest extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void addProgramButton(Ribbon ribbon) {
		RibbonProgramButton programButton = new RibbonProgramButton("File");
		
		RibbonContextMenu menu = new RibbonContextMenu(ribbon);
		
		menu.getContextMenu().getItems().add(new MenuItem("Whatever"));
		
		programButton.setRibbonMenu(menu);
		
		ribbon.setProgramButton(programButton);
	}
	
	String contextualGroup1 = "Test Tab Group";
	String contextualGroup2 = "Another Group";
	
	private RibbonTab createTab1() {
		RibbonGroup group1 = new RibbonGroup("Test Group 1");
		
		group1.getNodes().addAll(
				new RibbonButton("Sample text", loadIcon(null)) /*new Label("Whatever")*/,
				new RibbonButton("Do something", loadIcon(null)));
		
		return new RibbonTab("I am running", group1);
	}
	
	public ImageView loadIcon(Image image) {
		ImageView imageView = new ImageView();
		imageView.setImage(image);
		double dimensions = 2.6666666666666666666666666666667 * javafx.scene.text.Font.getDefault().getSize();
		imageView.setFitWidth(dimensions);
		imageView.setFitHeight(dimensions);
		
		return imageView;
	}

	private RibbonTab createTab2() {
		RibbonGroup group1 = new RibbonGroup("Test Group 1");
		group1.getNodes().addAll(
				new RibbonButton("Do nothing", loadIcon(null)));
		
		RibbonGroup group2 = new RibbonGroup("Test Group 1");
		group2.getNodes().addAll(
				new RibbonButton("Sample text", loadIcon(null)),
				new RibbonButton("Sample text", loadIcon(null)),
				new RibbonButton("moar text", loadIcon(null)));
		
		return new RibbonTab("out of ideas", group1, group2);
	}

	private RibbonTab createTab3() {
		RibbonGroup group1 = new RibbonGroup("Test Group 1");
		group1.getNodes().addAll(
				new RibbonButton("delet this", loadIcon(null)),
				new RibbonButton("(don't actually)", loadIcon(null)));
		
		RibbonGroup group2 = new RibbonGroup("Test Group 1");
		group2.getNodes().addAll(
				new RibbonButton("beep", loadIcon(null)),
				new RibbonButton("boop", loadIcon(null)),
				new RibbonButton("empty string", loadIcon(null)));
		
		RibbonTab tab = new RibbonTab("for tab names", group1, group2);
		tab.setContextualGroupName(contextualGroup1);
		return tab;
	}

	private RibbonTab createTab4() {
		RibbonGroup group1 = new RibbonGroup("Test Group 1");
		group1.getNodes().add(
				new RibbonButton("beep boop", loadIcon(null)));
		
		RibbonGroup group2 = new RibbonGroup("Test Group 1");
		group2.getNodes().addAll(
				new RibbonButton("we're off to", loadIcon(null)),
				new RibbonButton("see the", loadIcon(null)),
				new RibbonButton("ribbon, the", loadIcon(null)),
				new RibbonButton("wonderful ribbon", loadIcon(null)),
				new RibbonButton("of javafx", loadIcon(null)));
		
		RibbonTab tab = new RibbonTab("so please", group1, group2);
		tab.setContextualGroupName(contextualGroup1);
		return tab;
	}

	private RibbonTab createTab5() {
		RibbonGroup group1 = new RibbonGroup("Test Group 1");
		RibbonMenuButton menuButton = new RibbonMenuButton("menu items", loadIcon(null));
		menuButton.getItems().addAll(new MenuItem("actually"), new MenuItem("I"), new MenuItem("kinda"), new MenuItem("ran"), new MenuItem("out"), new MenuItem("of"), new MenuItem("those"), new MenuItem("a"), new MenuItem("while"), new MenuItem("ago"));
		group1.getNodes().add(menuButton);
		
		RibbonGroup group2 = new RibbonGroup("Test Group 1");
		group2.getNodes().addAll(
				new RibbonButton("boneless ribbon", loadIcon(null)),
				new RibbonButton("bone-in ribbon", loadIcon(null))
				/*new Button("Example3"),
				new Button("Example4")*/);
		
		RibbonTab tab = new RibbonTab("send help", group1, group2);
		tab.setContextualGroupName(contextualGroup2);
		return tab;
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		RibbonWindow main = new RibbonWindow();
		
		// A pane with a gradient used to differenciate it from the ribbon
		Pane pane = new Pane();
		pane.setBackground(new Background(new BackgroundFill(
				new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
						new Stop(0, Color.BLUE),
						new Stop(1, Color.ANTIQUEWHITE)),
				null,
				null
				)));
		Ribbon ribbon = main.getRibbon();
		
		ribbon.setContextualGroupColor(contextualGroup1, javafx.scene.paint.Color.rgb(255, 0, 255));
		ribbon.setContextualGroupColor(contextualGroup2, javafx.scene.paint.Color.rgb(0, 255, 0));
		
		RibbonTab tab1 = createTab1();
		RibbonTab tab2 = createTab2();
		RibbonTab tab3 = createTab3();
		RibbonTab tab4 = createTab4();
		RibbonTab tab5 = createTab5();
		
		//ribbon.setContentHeight(115);
		ribbon.setContentHeight(7.4166666666666666666666666666667 * javafx.scene.text.Font.getDefault().getSize());
		ribbon.getTabs().addAll(tab1, tab2, tab3, tab4, tab5);
		
		addProgramButton(ribbon);
		
		main.setContent(pane);
		
		Scene scene = new Scene(main, 1000, 700);
		
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);
		stage.show();
	}
}
