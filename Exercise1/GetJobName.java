import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// will inherit the default lifecycle of the Application
public class GetJobName extends Application {

    //
    // User interface controls
    //
    private TextField nameText;
    private Text idText;
    private Button clickButton;

    //
    // Data model
    //
    private Job job;

    @Override
    public void start(Stage stage) {

        this.clickButton = new Button("Click here to reset");
        //Lambda function, a function defined in place
        this.clickButton.setOnAction((event) -> {

            System.out.println("Testpress");
            this.nameText.setText("");
            this.idText.setText(""); 

        });


        stage.setTitle("Get Job Name");

        
        this.nameText = new TextField();
        this.nameText.setOnAction(this::processName);

        
        
        Label idLabel = new Label("");
        Label JobName = new Label("Job: ");
        this.idText= new Text(" ");
        HBox idBox = new HBox(idLabel, this.idText);
        HBox jobNameBox = new HBox(JobName, this.nameText);

        FlowPane pane = new FlowPane(
            
            this.clickButton,
            jobNameBox,
            idBox
            );
        pane.setAlignment(Pos.BASELINE_CENTER);

        Scene scene = new Scene(pane, 250, 500);
        stage.setScene(scene);
        stage.show();
    }
    //This is the point where you sanitize the user input
    public void processName(ActionEvent event) {
        String text = this.nameText.getText();

        if(!text.isEmpty()) {
            this.job = new Job(text);

            this.idText.setText(job.getId().toString());
            System.out.println(job);
        }
        else {
            System.out.println("Input was empty");
        }

        //System.out.println("Job Name = " + this.nameText.getText());
    }

    public static void main(String[] args) {
        launch();
    }

}

//PS C:\Users\jonne\Documents\JavaFX\Week2> javac --module-path "C:\Program Files\Java\javafx-sdk-22\lib" --add-modules javafx.controls,javafx.fxml GetJobName.java
//PS C:\Users\jonne\Documents\JavaFX\Week2> java --module-path "C:\Program Files\Java\javafx-sdk-22\lib" --add-modules javafx.controls GetJobName

//For some reason using a variable for Path didnt work, so I had to type it out