import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OrderViewModel {

    OrderViewModel() {

    }

    OrderViewModel(int id, String status ) {
        setStatus(status);
        setId(id);
    }

    private IntegerProperty id = new SimpleIntegerProperty(this, "id", 0);
    private StringProperty status = new SimpleStringProperty(this,"status","");

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
