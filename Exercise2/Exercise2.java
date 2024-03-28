import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;

public class Exercise2 extends Application {


    private TextField nameText;
    private Button clickButton;

    private Spinner<Integer> fromNoteSpinner;
    private Spinner<Integer> toNoteSpinner;
    private Button updateButton;
    

    private Job job;

    @Override

    public void start(Stage stage) {

        this.clickButton = new Button("Click to reset");
        this.clickButton.setOnAction((event) -> {

            this.nameText.setText("");
            this.fromNoteSpinner.getValueFactory().setValue(this.job.getFromNote());
            this.toNoteSpinner.getValueFactory().setValue(this.job.getToNote());

        });

        this.nameText = new TextField();
        this.nameText.setOnAction(this::processName);
        this.job = new Job();
        IntegerSpinnerValueFactory fromNoteValueFactory = 
            new IntegerSpinnerValueFactory(0, 127, this.job.getFromNote());
        this.fromNoteSpinner = new Spinner<Integer>(fromNoteValueFactory);

        this.fromNoteSpinner.valueProperty().addListener((spinner, oldValue, newValue) -> {
            //System.out.println("From Note: old" + oldValue + " new " + newValue);
            this.setUpdateButtonState();

        });


        IntegerSpinnerValueFactory toNoteValueFactory = 
        new IntegerSpinnerValueFactory(0, 127, this.job.getToNote());
        this.toNoteSpinner = new Spinner<Integer>(toNoteValueFactory);

        this.toNoteSpinner.valueProperty().addListener((spinner, oldValue, newValue) -> {
            //System.out.println("To Note: old" + oldValue + " new " + newValue);
            this.setUpdateButtonState();
        });

        this.updateButton = new Button("Update Job");
        this.updateButton.setOnAction(this::handleUpdateButton);
        boolean canUpdate = this.job.getFromNote() <= this.job.getToNote();
        this.updateButton.setDisable(!canUpdate);

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);

        

        pane.add(new Label("From Note: " ), 0, 0);
        pane.add(this.fromNoteSpinner, 1, 0);

        pane.add(new Label("To Note:"), 0, 1);
        pane.add(this.toNoteSpinner, 1, 1);

        pane.add(this.updateButton, 0, 2, 2, 1);
        GridPane.setHalignment(this.updateButton, HPos.CENTER);



        pane.add(new Label("Job:"), 0, 3); 
        pane.add(this.nameText, 1, 3); 

        pane.add(this.clickButton, 0, 5, 2, 1);
        GridPane.setHalignment(this.clickButton, HPos.CENTER);
        

        stage.setTitle("Note Spinners");
        Scene scene = new Scene(pane, 300,200);
        stage.setScene(scene);
        stage.show();

    }

    private void setUpdateButtonState() {
        int fromNote = this.fromNoteSpinner.getValue();
        int toNote = this.toNoteSpinner.getValue();
        this.updateButton.setDisable(fromNote > toNote);

    }



    public void handleUpdateButton(ActionEvent event){
        int fromNote = this.fromNoteSpinner.getValue();
        int toNote = this.toNoteSpinner.getValue();
        //spinner limits value 0 - 127, no need to sanitize further´

        this.job.setFromNote(fromNote);
        this.job.setToNote(toNote);
        System.out.println(job);
    }

    public void processName(ActionEvent event) {

        String text = this.nameText.getText();

        if(!text.isEmpty()) {
            this.job = new Job(text);
                     
            this.fromNoteSpinner.getValueFactory().setValue(this.job.getFromNote());
            this.toNoteSpinner.getValueFactory().setValue(this.job.getToNote());
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
