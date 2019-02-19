package emord.javafx.ribbon;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

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
	
	private RibbonTab createTab1() {
		RibbonGroup group1 = new RibbonGroup("Test Group 1");
		
		group1.getNodes().addAll(
				new Label("Whatever"),
				new Button("Example"));
		
		return new RibbonTab("Test", group1);
	}

	private RibbonTab createTab2() {
		RibbonGroup group1 = new RibbonGroup("Test Group 1");
		group1.getNodes().addAll(
				new Button("Example1"),
				new Button("Example2"));
		
		RibbonGroup group2 = new RibbonGroup("Test Group 1");
		group2.getNodes().addAll(
				new Button("Example3"),
				new Button("Example4"));
		
		return new RibbonTab("Another test", group1, group2);
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
		
		RibbonTab tab1 = createTab1();
		RibbonTab tab2 = createTab2();
		
		Ribbon ribbon = main.getRibbon();
		ribbon.setContentHeight(115);
		ribbon.getTabs().addAll(tab1, tab2);
		
		addProgramButton(ribbon);
		
		main.setContent(pane);
		
		Scene scene = new Scene(main, 1000, 700);
		
		stage.setScene(scene);
		stage.show();
	}
}
