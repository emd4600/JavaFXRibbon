package emord.javafx.ribbon.skin;

import emord.javafx.ribbon.RibbonButton.ControlSize;
import emord.javafx.ribbon.TextUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;

public class ButtonTextHelper {
	
	private static Label createLabel(String text) {
		Label label = new Label(text);
		label.setTextOverrun(OverrunStyle.CLIP);
		return label;
	}

	public static void layoutButton(Labeled labeled, Pane container, boolean showArrow) {
		/*container.getChildren().clear();
		
		String text = labeled.getText();
		Node graphic = labeled.getGraphic();
		
		// Separate the text in words
		String[] words = text.split(" ");
		
		double spaceWidth = TextUtils.computeTextWidth(labeled.getFont(), " ", 0.0d);
		
		// Get how much pixels each word uses horizontally
		double[] widths = new double[words.length];
		for (int i = 0; i < words.length; i++) {
			widths[i] = TextUtils.computeTextWidth(labeled.getFont(), words[i], 0.0d);
		}
		
		// The button must be able to fit the biggest word, so that will be the minimum width
		double minimumWidth = 0;
		for (int i = 0; i < words.length; i++) {
			if (widths[i] > minimumWidth) {
				minimumWidth = widths[i];
			}
		}
		
		// The button must also be able to fit the graphic
		if (graphic != null) {
			// The graphic width is expected to be set as the ribbon button preferred width
			double width = labeled.getPrefWidth();
			
			// Might not work, so take it directly from the ImageView if possible
			if (width == -1.0 && graphic instanceof ImageView) {
				width = ((ImageView) graphic).getFitWidth();
			}
			
			minimumWidth = Math.max(minimumWidth, width);
			
			container.getChildren().add(graphic);
		}
		
		if (!text.trim().isEmpty() && words.length > 0) {
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(words[0]);
			double currentLength = widths[0];
			
			for (int i = 1; i < words.length; i++) {
				if (currentLength + spaceWidth + widths[i] > minimumWidth) {
					// We can't fit a new word, so save the previous line and start a new one
					container.getChildren().add(createLabel(sb.toString()));
					
					// Reset
					sb.setLength(0);
					currentLength = 0;
				}
				else {
					// We can fit it
					sb.append(" ");
					currentLength += spaceWidth;
				}
				
				sb.append(words[i]);
				currentLength += widths[i];
			}
			
			container.getChildren().add(createLabel(sb.toString()));
			
			if (showArrow) {
				Label arrowLabel = (Label) (container.getChildren().get(container.getChildren().size() - 1));
				
				if (words.length == 1) {
					arrowLabel.setContentDisplay(ContentDisplay.BOTTOM);
				}
				else {
					arrowLabel.setContentDisplay(ContentDisplay.RIGHT);
				}
				
				SVGPath arrow = new SVGPath();
				arrow.setContent("M 0 0 h 7 l -3.5 4 z");
				arrowLabel.setGraphic(arrow);
			}
		}
		
		Insets insets = labeled.getInsets();
		
		labeled.setMinWidth(minimumWidth + 10 + insets.getLeft() + insets.getRight());
		labeled.setPrefWidth(minimumWidth + 10 + insets.getLeft() + insets.getRight());
		*/
		layoutButton(labeled, container, showArrow, ControlSize.LARGE);
	}

	public static void layoutButton(Labeled labeled, Pane container, boolean showArrow, ControlSize size) {
		/*container.getChildren().clear();
		
		String text = labeled.getText();
		Node graphic = labeled.getGraphic();
		
		// Separate the text in words
		double minimumWidth = 0;
		String[] words = new String[0];
		double[] widths = new double[0];
		double spaceWidth = TextUtils.computeTextWidth(labeled.getFont(), " ", 0.0d);
		
		if (size == ControlSize.LARGE) {
			words = text.split(" ");

			// Get how much pixels each word uses horizontally
			widths = new double[words.length];
			for (int i = 0; i < words.length; i++) {
				widths[i] = TextUtils.computeTextWidth(labeled.getFont(), words[i], 0.0d);
			}

			// The button must be able to fit the biggest word, so that will be the minimum
			// width
			for (int i = 0; i < words.length; i++) {
				if (widths[i] > minimumWidth) {
					minimumWidth = widths[i];
				}
			}
		}
		else if (size == ControlSize.MEDIUM) {
			words = text.split(" ");

			// Get how much pixels each word uses horizontally
			widths = new double[words.length];
			for (int i = 0; i < words.length; i++) {
				widths[i] = TextUtils.computeTextWidth(labeled.getFont(), words[i], 0.0d);
				minimumWidth += widths[i];
			}
		}
			minimumWidth = spaceWidth;
		
		// The button must also be able to fit the graphic
		if (graphic != null) {
			// The graphic width is expected to be set as the ribbon button preferred width
			double width = labeled.getPrefWidth();
			
			// Might not work, so take it directly from the ImageView if possible
			if (width == -1.0 && graphic instanceof ImageView) {
				width = ((ImageView) graphic).getFitWidth();
			}
			
			minimumWidth = Math.max(minimumWidth, width);
			
			container.getChildren().add(graphic);
		}
		
		if (!text.trim().isEmpty() && words.length > 0) {
			if (size == ControlSize.LARGE) {
			StringBuilder sb = new StringBuilder();
			
			sb.append(words[0]);
			double currentLength = widths[0];
			
			for (int i = 1; i < words.length; i++) {
				if (currentLength + spaceWidth + widths[i] > minimumWidth) {
					// We can't fit a new word, so save the previous line and start a new one
					container.getChildren().add(createLabel(sb.toString()));
					
					// Reset
					sb.setLength(0);
					currentLength = 0;
				}
				else {
					// We can fit it
					sb.append(" ");
					currentLength += spaceWidth;
				}
				
				sb.append(words[i]);
				currentLength += widths[i];
			}
			
			container.getChildren().add(createLabel(sb.toString()));
			}
			else if (size == ControlSize.MEDIUM)
				container.getChildren().add(createLabel(text));	
		}
		
		if (showArrow) {
			Label arrowLabel = (Label) (container.getChildren().get(container.getChildren().size() - 1));
			
			if (words.length == 1) {
				arrowLabel.setContentDisplay(ContentDisplay.BOTTOM);
			}
			else {
				arrowLabel.setContentDisplay(ContentDisplay.RIGHT);
			}
			
			SVGPath arrow = new SVGPath();
			arrow.setContent("M 0 0 h 7 l -3.5 4 z");
			arrowLabel.setGraphic(arrow);
		}
		
		Insets insets = labeled.getInsets();
		double finalWidth = 0;
		if (size == ControlSize.LARGE) {
			finalWidth = minimumWidth + 10 + insets.getLeft() + insets.getRight();
			labeled.setMinWidth(finalWidth);
			labeled.setPrefWidth(finalWidth);
		}
		else {
			labeled.setMinWidth(labeled.USE_PREF_SIZE);
			labeled.setPrefWidth(labeled.USE_COMPUTED_SIZE);
			//finalWidth = minimumWidth + 10 + insets.getLeft() + insets.getRight(); //finalWidth = spaceWidth + minimumWidth + 10 + insets.getLeft() + insets.getRight();
			// + ((ImageView) graphic).getFitWidth()
		}*/
		if (size == ControlSize.LARGE) {

			container.getChildren().clear();

			String text = labeled.getText();
			Node graphic = labeled.getGraphic();

			// Separate the text in words
			String[] words = text.split(" ");

			double spaceWidth = TextUtils.computeTextWidth(labeled.getFont(), " ", 0.0d);

			// Get how much pixels each word uses horizontally
			double[] widths = new double[words.length];
			for (int i = 0; i < words.length; i++) {
				widths[i] = TextUtils.computeTextWidth(labeled.getFont(), words[i], 0.0d);
			}

			// The button must be able to fit the biggest word, so that will be the minimum width
			double minimumWidth = 0;
			for (int i = 0; i < words.length; i++) {
				if (widths[i] > minimumWidth) {
					minimumWidth = widths[i];
				}
			}

			// The button must also be able to fit the graphic
			if (graphic != null) {
				// The graphic width is expected to be set as the ribbon button preferred width
				double width = labeled.getPrefWidth();

				// Might not work, so take it directly from the ImageView if possible
				if (width == -1.0 && graphic instanceof ImageView) {
					width = ((ImageView) graphic).getFitWidth();
				}

				minimumWidth = Math.max(minimumWidth, width);

				container.getChildren().add(graphic);
			}

			if (!text.trim().isEmpty() && words.length > 0) {

				StringBuilder sb = new StringBuilder();

				sb.append(words[0]);
				double currentLength = widths[0];

				for (int i = 1; i < words.length; i++) {
					if (currentLength + spaceWidth + widths[i] > minimumWidth) {
						// We can't fit a new word, so save the previous line and start a new one
						container.getChildren().add(createLabel(sb.toString()));

						// Reset
						sb.setLength(0);
						currentLength = 0;
					}
					else {
						// We can fit it
						sb.append(" ");
						currentLength += spaceWidth;
					}

					sb.append(words[i]);
					currentLength += widths[i];
				}

				container.getChildren().add(createLabel(sb.toString()));

				if (showArrow) {
					Label arrowLabel = (Label) (container.getChildren().get(container.getChildren().size() - 1));

					if (words.length == 1) {
						arrowLabel.setContentDisplay(ContentDisplay.BOTTOM);
					}
					else {
						arrowLabel.setContentDisplay(ContentDisplay.RIGHT);
					}

					SVGPath arrow = new SVGPath();
					arrow.setContent("M 0 0 h 7 l -3.5 4 z");
					arrowLabel.setGraphic(arrow);
				}
			}

			Insets insets = labeled.getInsets();

			labeled.setMinWidth(minimumWidth + 10 + insets.getLeft() + insets.getRight());
			labeled.setPrefWidth(minimumWidth + 10 + insets.getLeft() + insets.getRight());
		}
		else {
			container.getChildren().clear();
			Node graphic = labeled.getGraphic();
			double finalWidth = 0;
			if (graphic != null) {
				container.getChildren().add(graphic);
				double graphicWidth = ((ImageView) graphic).getFitWidth();
				if (graphicWidth <= 0)
					graphicWidth = ((ImageView) graphic).getFitHeight();
				finalWidth += graphicWidth;
			}
		
			if (size == ControlSize.MEDIUM) {
				Label label = createLabel(labeled.getText());
				container.getChildren().add(label);
				finalWidth += TextUtils.computeTextWidth(labeled.getFont(), labeled.getText(), 0.0d);
			}
			
			if (showArrow) {
				Label arrowLabel = (Label) (container.getChildren().get(container.getChildren().size() - 1));
				arrowLabel.setContentDisplay(ContentDisplay.RIGHT);
				
				SVGPath arrow = new SVGPath();
				arrow.setContent("M 0 0 h 7 l -3.5 4 z");
				if (size == ControlSize.MEDIUM)
					arrowLabel.setGraphic(arrow);
				else
					container.getChildren().add(arrow);
				//finalWidth += arrowLabel.getWidth();
				finalWidth += arrow.prefWidth(labeled.USE_COMPUTED_SIZE);
			}
			
			Insets insets = labeled.getInsets();
			finalWidth += /*10 + */insets.getLeft() + insets.getRight();
			/*for (Node n : container.getChildren()) {
				finalWidth += n.prefWidth(labeled.USE_COMPUTED_SIZE);*/
			if (container.getChildren().size() > 1)
				finalWidth += ((HBox)container).getSpacing() * (container.getChildren().size() - 1);
			//finalWidth += n.prefWidth(labeled.USE_COMPUTED_SIZE);
			
			if (size == ControlSize.MEDIUM)
				labeled.setPrefWidth(finalWidth);
			else
				labeled.setPrefWidth(labeled.USE_COMPUTED_SIZE);
			
			labeled.setMinWidth(labeled.USE_PREF_SIZE);
		}
	}
}
