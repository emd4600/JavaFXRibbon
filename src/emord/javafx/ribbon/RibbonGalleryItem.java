package emord.javafx.ribbon;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public class RibbonGalleryItem {

	private final StringProperty text = new SimpleStringProperty();
	private final StringProperty description = new SimpleStringProperty();
	private final ObjectProperty<Node> graphic = new SimpleObjectProperty<>();
	private Object userData;
	
	public final StringProperty textProperty() {
		return text;
	}
	
	public final String getText() {
		return text.get();
	}
	
	public final void setText(String value) {
		text.set(value);
	}
	
	public final StringProperty descriptionProperty() {
		return description;
	}
	
	public final String getDescription() {
		return description.get();
	}
	
	public final void setDescription(String value) {
		description.set(value);
	}
	
	
	public final ObjectProperty<Node> graphicProperty() {
		return graphic;
	}
	
	public final Node getGraphic() {
		return graphic.get();
	}
	
	public final void setGraphic(Node value) {
		graphic.set(value);
	}
	
	public Object getUserData() {
		return userData;
	}
	
	public void setUserData(Object value) {
		userData = value;
	}
}
