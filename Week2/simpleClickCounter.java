import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClickCounter extends Application {
    private int count;

    private Text countText;
    private Button clickButton;
    private Button resetButton;

    //inner class can reach the private fields of the enclosing class
    private class ResetButtonHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event){
            System.out.println("In private class ResetButtonHandler.handle");
            count = 0;
            countText.setText("Clicks: " + count);
        }

    }

    @Override
    public void start(Stage stage) {
        this.count = 0;

        this.countText = new Text("Clicks: " + count);

        this.clickButton = new Button("Click Me");
        this.clickButton.setOnAction(this::handleClickButton);

        this.resetButton = new Button("Reset");

        //2 ways to do Reset here
        this.resetButton.setOnAction(new ResetButtonHandler());
        //this.resetButton.setOnAction(this::handleResetButton);

        FlowPane pane= new FlowPane(
        this.clickButton,
        this.countText,
        this.resetButton
        );
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(20);
        
        Scene scene = new Scene(pane, 400, 200);
        stage.setScene(scene);
        stage.show();

    }

    public void handleClickButton(ActionEvent event)
    {
        System.out.println("In handleClickButton");
        this.count ++;
        this.countText.setText("Clicks: " + this.count);
    }

    public void handleResetButton(ActionEvent event)
    {
        System.out.println("In handleResetButton");
        this.count = 0;
        this.countText.setText("Clicks: " + this.count);
    }

    public static void main(String[] args) {
        launch();
    }

}

//javac --module-path "C:\Program Files\Java\javafx-sdk-22\lib" --add-modules javafx.controls,javafx.fxml ClickCounter.java
//java --module-path "C:\Program Files\Java\javafx-sdk-22\lib" --add-modules javafx.controls ClickCounter