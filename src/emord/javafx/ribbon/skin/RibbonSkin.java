/****************************************************************************
* Copyright (C) 2018 Eric Mor
*
* This file is part of JavaFXRibbon.
*
* JavaFXRibbon is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
****************************************************************************/

package emord.javafx.ribbon.skin;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.List;

import com.sun.glass.events.KeyEvent;
import com.sun.jna.Callback;
import com.sun.jna.CallbackReference;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.BaseTSD.LONG_PTR;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinUser.WNDCLASSEX;
import com.sun.jna.platform.win32.WinUser.WindowProc;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

import emord.javafx.ribbon.Ribbon;
import emord.javafx.ribbon.Ribbon.RibbonState;
import emord.javafx.ribbon.RibbonButton;
import emord.javafx.ribbon.RibbonGroup;
import emord.javafx.ribbon.RibbonMenu;
import emord.javafx.ribbon.RibbonProgramButton;
import emord.javafx.ribbon.RibbonTab;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The skin implementation of a {@link Ribbon}. It is basically composed of a {@link BorderPane}:
 * <li>The <code>TOP</code> region is used to display the tab selection buttons.
 * <li>The <code>CENTER</code> region is used to display the tab content.
 * When the ribbon is hidden, the center region is set to null.
 */
public class RibbonSkin extends SkinBase<Ribbon> {
	
	private final BorderPane pane = new BorderPane();
	/** The pane that displays the tab selection buttons. */
	// No spacing between tabs
	private final HBox tabsPane = new HBox(0);
	private Pane tabsPaneOutline;
	//private VBox tabStuffContainer;
	private BorderPane topStuffContainer;
	
	private final BorderPane contentPane = new BorderPane();
	private final HBox groupsContainer = new HBox();
	private final HBox contextualGroupsContainer = new HBox();
	
	private ImageView expandImage;
	private ImageView minimizeImage;
	
	private final Button expandButton = new Button();
	
	private final HBox captionButtonsPane = new HBox();
	private final Button minimizeButton = new Button();
	private final Button maximizeButton = new Button();
	private final Button closeButton = new Button();
	
	private double xWindowPos = 0;
	private double yWindowPos = 0;

	public RibbonSkin(Ribbon control) {
		super(control);
		construct();
	}
	
	private void handleRibbonState(RibbonState state) {
		if (state == RibbonState.DROPDOWN) {
			getSkinnable().getRibbonWindow().getChildren().add(contentPane);
			
			contentPane.setTranslateY(tabsPane.getBoundsInParent().getMaxY());
			contentPane.setMaxHeight(pane.getHeight());
			
			expandButton.setGraphic(expandImage);
		}
		else {
			// Reset
			getSkinnable().getRibbonWindow().getChildren().remove(contentPane);
			contentPane.setTranslateY(0);
			contentPane.setMaxHeight(Double.POSITIVE_INFINITY);
			
			if (state == RibbonState.EXPANDED) {
				pane.setCenter(contentPane);
				updateContentHeight(getSkinnable().getContentHeight());
				
				expandButton.setGraphic(minimizeImage);
			}
			else if (state == RibbonState.MINIMIZED) {
				pane.setCenter(null);
			}
		}
	}
	
	static final String CONTEXTUAL_HEADER_CLASS = "ribbon-contextual-group-header"; 
	
	public void constructTabsPane() {
		tabsPane.getChildren().clear();
		contextualGroupsContainer.getChildren().clear();
		
		//groupsContainer.setMinWidth(HBox.USE_PREF_SIZE);
		
		RibbonProgramButton btn = createProgramButton();
		Button programButtonDummy = new Button();
		//programButtonDummy.set
		//programButtonDummy.prefWidthProperty().bind(btn.widthProperty());
		programButtonDummy.setOpacity(0);
		//programButtonDummy.setMaxHeight(0);
		//programButtonDummy.setMinHeight(0);
		//programButtonDummy.setPrefHeight(0);
		programButtonDummy.getStyleClass().setAll(CONTEXTUAL_HEADER_CLASS);
		contextualGroupsContainer.getChildren().add(programButtonDummy);
		
		RibbonTab prevTab = null;
		Button prevGroupHeader = null;
		
		for (RibbonTab tab : getSkinnable().getTabs()) {
			RibbonHeader head = createTabHeader(tab);
			
			/*tabsPane.applyCss();
			tabsPane.layout();

			contextualGroupsContainer.applyCss();
			contextualGroupsContainer.layout();*/
			
			if (((prevTab != null) && (prevTab.getContextualGroupName() != tab.getContextualGroupName())) | (prevTab == null)) {
				Button groupHeader = new Button();
				
				groupHeader.getStyleClass().setAll(CONTEXTUAL_HEADER_CLASS);
				groupHeader.setText(tab.getContextualGroupName());
				groupHeader.setPrefWidth(0);
				groupHeader.setMinWidth(0);
				groupHeader.setMinHeight(0);
				
				if ((tab.getContextualGroupName() == RibbonTab.DEFAULT_GROUP_NAME)) {
					groupHeader.setOpacity(0);
					groupHeader.setMaxHeight(0);
					groupHeader.setPrefHeight(0);
				}
				else {
					javafx.scene.paint.Color groupColor = getSkinnable().getContextualGroupColor(tab.getContextualGroupName());
					if (groupColor != null) {
						String red = Double.toString(groupColor.getRed() * 255);
						if (red.contains("."))
							red = red.substring(0, red.indexOf("."));
						String green = Double.toString(groupColor.getGreen() * 255);
						if (green.contains("."))
							green = green.substring(0, green.indexOf("."));
						String blue = Double.toString(groupColor.getBlue() * 255);
						if (blue.contains("."))
							blue = blue.substring(0, blue.indexOf("."));
						String colorString = "-fx-ribbon-contextual-color: rgb(" + red + ", " + green + ", " + blue + ");";
						//System.out.println(colorString);
						groupHeader.setStyle(colorString);
					}
					else
						groupHeader.setStyle("-fx-ribbon-contextual-color: rgb(128, 128, 128);");
				}
				
				contextualGroupsContainer.getChildren().add(groupHeader);
			}
				/*else {
					Button groupHeader = new Button();
					groupHeader.setTextAlignment(TextAlignment.CENTER);

					groupHeader.getStyleClass().setAll(CONTEXTUAL_HEADER_CLASS);
					
					if ((tab.getContextualGroupName() == RibbonTab.DEFAULT_GROUP_NAME)) {
						groupHeader.setOpacity(0.1);
						groupHeader.setMaxHeight(0);
						groupHeader.setMinHeight(0);
						groupHeader.setPrefHeight(0);
					}
					
					contextualGroupsContainer.getChildren().add(groupHeader);
				}
			}*/
			
			prevTab = tab;
		}
		
		tabsPane.getChildren().add(tabsPaneOutline);
		tabsPane.getChildren().add(expandButton);
		
		Platform.runLater(new Runnable() {
            @Override public void run() {
            	RibbonTab prevTab = null;
        		int groupIndex = 0;
            	//boolean hasProgramButton = tabsPane.getChildren().get(0) instanceof RibbonProgramButton;
            	//System.out.println("CHILDREN SIZE: " + contextualGroupsContainer.getChildren().size());
            	for (Node item : tabsPane.getChildren()) {
            		if (item instanceof RibbonProgramButton) {
            			RibbonProgramButton btn = (RibbonProgramButton)item;
            			Button groupHeader = (Button)contextualGroupsContainer.getChildren().get(groupIndex);
            			groupHeader.setPrefWidth(groupHeader.getPrefWidth() + btn.getWidth());
            			//groupHeader.setMinWidth(groupHeader.getWidth() + btn.getWidth());
            			//groupHeader.setMaxWidth(groupHeader.getWidth() + btn.getWidth());
            			groupIndex++;
            		}
            		else if (item instanceof RibbonHeader) {
            			RibbonHeader tab = (RibbonHeader)item;
            			if ((prevTab != null) && (prevTab.getContextualGroupName() != tab.getTab().getContextualGroupName())) {
	    					//System.out.println("CONTEXTUAL GROUP NAME: " + tab.getTab().getContextualGroupName());
	        				groupIndex++;
	        			}
            			//System.out.println(tab.getTab().getText() + ": " + tab.getWidth());
            			Button groupHeader = (Button)contextualGroupsContainer.getChildren().get(groupIndex);
            			
	        			groupHeader.setPrefWidth(groupHeader.getPrefWidth() + tab.getWidth());
	        			//groupHeader.setMinWidth(groupHeader.getWidth() + tab.getWidth());
	        			//groupHeader.setMaxWidth(groupHeader.getWidth() + tab.getWidth());
	        			
	        			prevTab = tab.getTab();
            		}
            		
            		//System.out.println("groupIndex: " + groupIndex);
        		}
            }
        });
	}
	
	private double maxWidth = 0;
	private double maxHeight = 0;

	private void construct() {
		//tabStuffContainer = new VBox();
		topStuffContainer = new BorderPane();
		topStuffContainer.getStyleClass().add("ribbon-header-area");
		
		//tabStuffContainer.setMinHeight(0);
		topStuffContainer.setBottom(tabsPane);
		topStuffContainer.setCenter(contextualGroupsContainer);
		topStuffContainer.setRight(captionButtonsPane);
		pane.setTop(topStuffContainer);
		
		contentPane.setCenter(groupsContainer);
		contentPane.getStyleClass().add("ribbon-content-area");
		groupsContainer.maxHeightProperty().bind(contentPane.maxHeightProperty());
		//contextualGroupsContainer.setMinHeight(0);
		groupsContainer.setAlignment(Pos.CENTER_LEFT);
		
		updateContentHeight(getSkinnable().getContentHeight());
		
		handleRibbonState(getSkinnable().getRibbonState());
		
		tabsPaneOutline = new Pane();
		tabsPaneOutline.getStyleClass().add("ribbon-tab-header-outline");
		
		tabsPane.getStyleClass().add("ribbon-tab-header-area");
		
		HBox.setHgrow(tabsPaneOutline, Priority.ALWAYS);
		
		constructTabsPane();
		
		setContent(getSkinnable().getSelectionModel().getSelectedItem());
		
		getChildren().add(pane);
		
		// -- Expand Button -- //
		
		expandImage = new ImageView(new Image(getSkinnable().getRibbonWindow().getResource("ribbon-expand.png")));
		minimizeImage = new ImageView(new Image(getSkinnable().getRibbonWindow().getResource("ribbon-minimize.png")));
		
		// Technically it's not a ribbon button, but we don't want the standard button background
		//expandButton.getStyleClass().add(RibbonButton.DEFAULT_STYLE_CLASS);
		expandButton.getStyleClass().add("ribbon-expand-collapse-button");
		//expandButton.setPadding(new Insets(0));
		expandButton.setOnAction((event) -> {
			if (getSkinnable().getRibbonState() == RibbonState.EXPANDED) {
				expandButton.setGraphic(expandImage);
				getSkinnable().setRibbonState(RibbonState.MINIMIZED);
			}
			else /*if (getSkinnable().getRibbonState() == RibbonState.DROPDOWN)*/ {
				expandButton.setGraphic(minimizeImage);
				getSkinnable().setRibbonState(RibbonState.EXPANDED);
			}
		});
		
		expandButton.setGraphic(minimizeImage);
		/*contentPane.setRight(expandButton);*/
		expandButton.setAlignment(Pos.TOP_RIGHT);
		//getSkinnable().getRibbonWindow().getChildren().add(expandButton);
		
		getSkinnable().getRibbonWindow().setAlignment(expandButton, Pos.TOP_RIGHT);
		/*BorderPane.setAlignment(expandButton, Pos.TOP_RIGHT);*/
		
		/*tabStuffContainer.getChildren().add(contextualGroupsContainer);
		tabStuffContainer.getChildren().add(tabsPane);*/
		
		// -- Do Titlebar things -- //
		
		String operatingSystem = System.getProperty("os.name");
		if (operatingSystem.toLowerCase().startsWith("windows")) {
			Stage window = (Stage)getSkinnable().getScene().getWindow();
			
			String oldTitle = window.getTitle();
			String newTitle = "RIBBON_WINDOW_RIBBON_WINDOW_RIBBON_WINDOW";
			
			window.setTitle(newTitle);
			HWND handle = User32.INSTANCE.FindWindow(null, newTitle);
			window.setTitle(oldTitle);
			
			//Remove native Titlebar
			//System.out.println("handle: " + handle);
			//int gwlStyle = User32.INSTANCE.GetWindowLong(handle, User32.INSTANCE.GWL_STYLE);
			//gwlStyle &= ~User32.INSTANCE.WS_CAPTION;
			//User32.INSTANCE.SetWindowLong(handle, User32.INSTANCE.GWL_STYLE, gwlStyle);
			
			int gwlStyle = User32.INSTANCE.GetWindowLong(handle, User32.INSTANCE.GWL_STYLE);
			gwlStyle |= User32.INSTANCE.WS_THICKFRAME;
			gwlStyle |= User32.INSTANCE.WS_MINIMIZEBOX;
			User32.INSTANCE.SetWindowLong(handle, User32.INSTANCE.GWL_STYLE, gwlStyle);
			
			maxWidth = window.getMaxWidth();
			maxHeight = window.getMaxHeight();
			
			window.heightProperty().addListener((obs, oldValue, newValue) -> {
				fixMaximizedWindow((Stage)getSkinnable().getScene().getWindow());
			});
			
			groupsContainer.setMinWidth(0);
			/*window.widthProperty().addListener((obs, oldValue, newValue) -> {
				fixMaximizedWindow(window);
			});*/
			
			contextualGroupsContainer.setOnMousePressed((event) -> {
				if (!window.isMaximized()) {
					xWindowPos = event.getSceneX();
					yWindowPos = event.getSceneY();
				}
			});
			
			contextualGroupsContainer.setOnMouseDragged((event) -> {
				if (!window.isMaximized()) {
					window.setX(event.getScreenX() - xWindowPos);
					window.setY(event.getScreenY() - yWindowPos);
				}
			});
			
			contextualGroupsContainer.setOnMouseClicked((event) -> {
				if (event.getClickCount() == 2) {
					window.setMaximized(!window.isMaximized());
				}
			});
			
			
			// r/therewasanattempt
			/*WindowProc callback = new WindowProc() {
				//@Override
				public LRESULT callback(HWND hWnd, int uMsg, WPARAM wParam, LPARAM lParam) {
					switch (uMsg) { //0x0081 = WM_CREATE //0x83 = WM_NCCALCSIZE //0x85 = WM_NCPAINT
					case 0x0081:
						return new LRESULT(1);
					case 0x83: {
						NCCALCSIZE_PARAMS params = new NCCALCSIZE_PARAMS(lParam.toPointer());
						params.rgrc[0] = params.rgrc[2];
						return new LRESULT(1);
					}
					default: return User32.INSTANCE.DefWindowProc(hWnd, uMsg, wParam, lParam); //new LRESULT(1);
					}
				}
			};
			
			
			WNDCLASSEX wClassEx = new WNDCLASSEX();
			wClassEx.clear();
			wClassEx.lpfnWndProc = callback;
			wClassEx.lpszClassName = "GlassWndClass-GlassWindowClass-2";
			
			System.out.println("SETWINDOWLONGPTR: " + User32.INSTANCE.SetWindowLongPtr(handle, WinUser.GWL_WNDPROC, CallbackReference.getFunctionPointer(callback)));
			*/

			
			/*IntByReference threadPointer = new IntByReference();
			int threadId = User32.INSTANCE.GetWindowThreadProcessId(handle, threadPointer);
			WinNT.HANDLE threadHandle = Kernel32.INSTANCE.OpenThread(0xFFFF, false, threadId);
			Thread thread = new Thread(); //(threadHandle.getPointer().getInt(0));*/
			//threadHandle.th
			
			//Thread thread = new Thread(threadId);
			
			
			//PointerByReference ptr = new PointerByReference().poi;
			//Pointer.createConstant(peer)
			//Pointer pointer = com.sun.jna.CallbackReference.getFunctionPointer(callback);
			//LONG_PTR ptr = new LONG_PTR(callback.hashCode());
			//System.out.println("AFTER SETWINDOWLONGPTR");
			//System.out.println("GETLASTERROR: " + Kernel32.INSTANCE.GetLastError());
			
			/*contextualGroupsContainer.setOnMousePressed((event) -> {
				//POINT point = new POINT();
				//point.x = (int)(window.getX() + 50);
				//point.y = (int)(window.getY() + 50);
				//new POINT(, )
				///User32.INSTANCE.PostMessage(handle, User32.INSTANCE.WM_SYSCOMMAND, new WPARAM(0xF010|0x0002), new LPARAM(Pointer.nativeValue(point.getPointer()))); //
				
				///WinUser.INPUT snapLeft = new WinUser.INPUT();
				///snapLeft.type = new DWORD(WinUser.INPUT.INPUT_KEYBOARD);
				///snapLeft.input.setType("ki");
				///snapLeft.input.ki.wScan = new WORD(0);
				///snapLeft.input.ki.time = new DWORD(0);
				///snapLeft.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR(0);
				///snapLeft.input.ki.wVk = new WORD(0x25 & 0x5B);
				///snapLeft.input.ki.dwFlags = new DWORD(0x0001 | 0x0002);
				
				///User32.INSTANCE.SendInput(new DWORD(1), (WinUser.INPUT[])snapLeft.toArray(1), snapLeft.size());
				
				//User32.INSTANCE.PostMessage(handle, User32.INSTANCE.WM_SYSCOMMAND, new WinDef.WPARAM(0xf012), new WinDef.LPARAM(0));
			});*/

			//minimizeButton.setText("-");
			minimizeButton.getStyleClass().add("minimize-button");
			minimizeButton.setOnMouseClicked((event) -> {
				window.setIconified(true);
			});
			captionButtonsPane.getChildren().add(minimizeButton);
			
			//maximizeButton.setText("[]");
			maximizeButton.getStyleClass().add("maximize-button");
			maximizeButton.setOnMouseClicked((event) -> {
				window.setMaximized(!window.isMaximized());
				/*if (window.isMaximized()) {
					window.setMaximized(false);
				}
				else {
					int style = User32.INSTANCE.GetWindowLong(handle, User32.INSTANCE.GWL_STYLE);
					
					style &= User32.INSTANCE.WS_CAPTION;
					User32.INSTANCE.SetWindowLong(handle, User32.INSTANCE.GWL_STYLE, style);
					window.setMaximized(true);
					
					double width = window.getWidth();
					window.setMaxHeight(width);
					double height = window.getHeight();
					window.setMaxHeight(height);
					style &= ~User32.INSTANCE.WS_CAPTION;
					User32.INSTANCE.SetWindowLong(handle, User32.INSTANCE.GWL_STYLE, style);
				}*/
			});
			captionButtonsPane.getChildren().add(maximizeButton);
			
			//closeButton.setText("X");
			closeButton.getStyleClass().add("close-button");
			closeButton.setOnMouseClicked((event) -> {
				window.close();
			});
			captionButtonsPane.getChildren().add(closeButton);
			
			//System.out.println("AFTER EVERYTHING");
		}
		
		// -- Update Listeners -- //
		
		getSkinnable().getTabs().addListener((ListChangeListener.Change<? extends RibbonTab> c) -> {
			constructTabsPane();
		});
		
		getSkinnable().getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				setContent(newValue);
			}
		});
		
		getSkinnable().contentHeightProperty().addListener((obs, oldValue, newValue) -> {
			updateContentHeight(newValue.doubleValue());
		});
		
		getSkinnable().ribbonStateProperty().addListener((obs, oldValue, newValue) -> {
			handleRibbonState(newValue);
		});
		
		getSkinnable().setOnScroll((ScrollEvent event) -> {
			if ((event.getDeltaY() < 0) && (getSkinnable().getSelectionModel().getSelectedIndex() < getSkinnable().getTabs().size() - 1)) {
				getSkinnable().getSelectionModel().select(getSkinnable().getSelectionModel().getSelectedIndex() + 1);
			}
			else if ((event.getDeltaY() > 0) && (getSkinnable().getSelectionModel().getSelectedIndex() > 0)) {
				getSkinnable().getSelectionModel().select(getSkinnable().getSelectionModel().getSelectedIndex() - 1);
			}
		});
		
		getSkinnable().getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, (event) -> {
			// If ribbon is in dropdown and the user clicked outside the content/tab headers/program button,
			// minimize the ribbon again
			if (getSkinnable().getRibbonState() == RibbonState.DROPDOWN) {
				
				// If the click was on the content pane, do nothing
				if (contentPane.contains(contentPane.sceneToLocal(event.getSceneX(), event.getSceneY()))) {
					return;
				}
				
				// If the click was on a tab header or program button, do nothing
				for (Node node : tabsPane.getChildren()) {
					if (node.contains(node.sceneToLocal(event.getSceneX(), event.getSceneY()))
							// Clicking on the outline panel DOES minimize the ribbon again
							&& node != tabsPaneOutline) {
						return;
					}
				}
				
				// If we have arrived here, minimize the ribbon
				getSkinnable().setRibbonState(RibbonState.MINIMIZED);
			}
		});
	}
	
	private void fixMaximizedWindow(Stage window) {
		if (window.isMaximized()) {
			/*int gwlMaxStyle = User32.INSTANCE.GetWindowLong(handle, User32.INSTANCE.GWL_STYLE);
			gwlMaxStyle |= ~User32.INSTANCE.WS_THICKFRAME;
			User32.INSTANCE.SetWindowLong(handle, User32.INSTANCE.GWL_STYLE, gwlMaxStyle);*/
			
			//maxWidth = window.getMaxWidth();
			//maxHeight = window.getMaxHeight();
			Rectangle workArea = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			//System.out.println("WORK AREA BOUNDS: " + workArea.getX() + ", " + workArea.getY() + ", " + workArea.getWidth() + ", " + workArea.getHeight());
			double winX = window.getX();
			double winY = window.getY();
			//System.out.println("WINDOW LOCATION: " + winX + ", " + winY);
			window.setMaxWidth(workArea.getWidth() - (winX * 2));
			window.setMaxHeight(workArea.getHeight() - (winY * 2));
			
			if (workArea.getX() > 0)
				window.setX(workArea.getX() + winX);
			if (workArea.getY() > 0)
				window.setY(workArea.getY() + winY);
		}
		else {
			window.setMaxWidth(Double.POSITIVE_INFINITY);
			window.setMaxHeight(Double.POSITIVE_INFINITY);
			
			/*int gwlMaxStyle = User32.INSTANCE.GetWindowLong(handle, User32.INSTANCE.GWL_STYLE);
			gwlMaxStyle |= User32.INSTANCE.WS_THICKFRAME;
			User32.INSTANCE.SetWindowLong(handle, User32.INSTANCE.GWL_STYLE, gwlMaxStyle);*/
		}
	}
	
	//https://www.programcreek.com/java-api-examples/?code=Guerra24/NanoUI/NanoUI-master/nanoui-core/src/main/java/net/luxvacuos/win32/DWMapiExt.java
	public class NCCALCSIZE_PARAMS extends Structure implements Structure.ByReference {
		public final List<String> FIELDS = createFieldsOrder("rgrc", "lppos");

		public RECT[] rgrc = new RECT[3];
		public WINDOWPOS lppos;

		public NCCALCSIZE_PARAMS(Pointer pointer) {
			super(pointer);
			read();
		}

		@Override
		protected List<String> getFieldOrder() {
			return FIELDS;
		}

	}
	
	public class WINDOWPOS extends Structure implements Structure.ByReference {
		public final List<String> FIELDS = createFieldsOrder("hwnd", "hwndInsertAfter", "x", "y", "cx", "cy",
				"flags");

		public WINDOWPOS(Pointer pointer) {
			super(pointer);
			read();
		}

		public HWND hwnd;
		public HWND hwndInsertAfter;
		public int x;
		public int y;
		public int cx;
		public int cy;
		public int flags;

		@Override
		protected List<String> getFieldOrder() {
			return FIELDS;
		}

	}

	
	private RibbonProgramButton createProgramButton() {
		if (getSkinnable().getProgramButton() != null) {
			RibbonProgramButton button = getSkinnable().getProgramButton();
			
			button.setOnAction((event) -> {
				if (button.getRibbonMenu().isShowing()) {
					button.getRibbonMenu().hide();
					button.setSelected(false);
				}
				else {
					Bounds rect = button.localToScreen(button.getBoundsInLocal());
					
					button.getRibbonMenu().show(getSkinnable(), rect.getMinX(), rect.getMaxY());
					button.setSelected(true);
				}
			});
			
			button.sceneProperty().addListener((obs, oldValue, newValue) -> {
				if (newValue != null) {
					newValue.addEventFilter(MouseEvent.MOUSE_PRESSED, (event) -> {
						RibbonMenu menu = button.getRibbonMenu();
						if (menu.isShowing() && !menu.contains(event.getScreenX(), event.getScreenY())) {
							menu.hide();
							
							// Also ensure we deselect the button 
							button.setSelected(false);
							
							// If the click was on the program button, it will open the menu again
							// in the action event; consume the click to avoid that
							if (button.contains(button.screenToLocal(event.getScreenX(), event.getScreenY()))) {
								event.consume();
							}
						};

					});
				}
			});
			
			tabsPane.getChildren().add(button);
			
			return button;
		}
		else
			return null;
	}
	
	private void updateContentHeight(double newValue) {
		if (newValue == Ribbon.NO_FIXED_HEIGHT) {
			contentPane.setPrefHeight(Ribbon.USE_COMPUTED_SIZE);
			contentPane.setMaxHeight(Ribbon.USE_COMPUTED_SIZE);
			contentPane.setMinHeight(Ribbon.USE_COMPUTED_SIZE);
		}
		else {
			contentPane.setPrefHeight(newValue);
			contentPane.setMaxHeight(newValue);
			contentPane.setMinHeight(newValue);
		}
	}
	
	private RibbonHeader createTabHeader(RibbonTab tab) {
		RibbonHeader button = new RibbonHeader(tab);
		tabsPane.getChildren().add(button);
		
		button.setOnMouseClicked((event) -> {
			
			if (event.getButton() == MouseButton.PRIMARY) {
				RibbonState state = getSkinnable().getRibbonState();
				
				if (state == RibbonState.EXPANDED) {
					// Two possibilities:
					// 1. Double-click on the selected tab: minimize the ribbon
					if (tab.isSelected() && event.getClickCount() == 2) {
						getSkinnable().setRibbonState(RibbonState.MINIMIZED);
					}
					// 2. Just clicked on another tab, select it
					else {
						select(tab);
					}
				}
				else if (state == RibbonState.MINIMIZED) {
					// Two possibilities:
					// 1. Double-click on any tab: set it to expanded again, and select that tab
					if (event.getClickCount() == 2) {
						getSkinnable().setRibbonState(RibbonState.EXPANDED);
						select(tab);
					}
					// 2. Single-click on any tab: select it and display as drop-down
					else {
						select(tab);
						getSkinnable().setRibbonState(RibbonState.DROPDOWN);
					}
				}
				else if (state == RibbonState.DROPDOWN) {
					// Two possibilities:
					// 1. Double-click on any tab: set it to expanded again, and select that tab
					if (event.getClickCount() == 2) {
						getSkinnable().setRibbonState(RibbonState.EXPANDED);
						select(tab);
					}
					// 2. Single-click on any tab: select it, but keep as drop-down
					else {
						select(tab);
					}
				}
			} 
		});
		
		return button;
	}
	
	private void select(RibbonTab tab) {
		tab.getRibbon().getSelectionModel().select(tab);
	}
	
	private void setContent(RibbonTab tab) {
		groupsContainer.getChildren().clear();
		
		for (RibbonGroup group : tab.getGroups()) {
			//group.minHeightProperty().bind(getSkinnable().contentHeightProperty());
			group.prefHeightProperty().bind(getSkinnable().contentHeightProperty());
			//group.maxHeightProperty().bind(getSkinnable().contentHeightProperty());
			
			groupsContainer.getChildren().add(group);
			
			Separator separator = new Separator(Orientation.VERTICAL);
			separator.getStyleClass().add("ribbon-group-separator");
			separator.setMaxHeight(getSkinnable().getContentHeight());
			groupsContainer.getChildren().add(separator);
		}
	}
}
