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
import java.util.List;
import java.util.ArrayList;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleGroup;


public class Exercise3 extends Application {

    private Job job;
    private TextField nameText;
    private Button resetButton;
    private Spinner<Integer> fromNoteSpinner;
    private Spinner<Integer> toNoteSpinner;
    private Button updateButton;
    private CheckBox useDefaultTImesCheckBox;
    private List<RadioButton> intervalButtons;
    private boolean useDefaultTimes;
    

    

    @Override

    public void start(Stage stage) {

        this.resetButton = new Button("Click to reset");
        this.resetButton.setOnAction((event) -> {
            this.nameText.clear();
            this.nameText.setText(this.job.getName());
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


        List<String> intervalButtonNames = new ArrayList<String>();
        intervalButtonNames.add("One");
        intervalButtonNames.add("Three");
        intervalButtonNames.add("Six");
        intervalButtonNames.add("Twelve");

        ToggleGroup intervalButtonGroup = new ToggleGroup();
        this.intervalButtons = new ArrayList<RadioButton>();

        Job.Interval[] intervalValues = Job.Interval.values(); //Interval inside the Job class
        int buttonIndex = 0;
        
        for (Job.Interval value : intervalValues) {
            RadioButton rb = new RadioButton(intervalButtonNames.get(buttonIndex));
            rb.setToggleGroup(intervalButtonGroup);
            rb.setOnAction(this::handleIntervalButtonAction);
            this.intervalButtons.add(rb);

            buttonIndex++;

            //for loop gives references to all four radio buttons in this field
        }

        this.intervalButtons.get(0).setSelected(true);

        HBox intervalBox = new HBox(
            this.intervalButtons.get(0),
            this.intervalButtons.get(1),
            this.intervalButtons.get(2),
            this.intervalButtons.get(3)
        );
        intervalBox.setSpacing(20);
        TitledPane intervalPane = new TitledPane("Interval", intervalBox);

        this.useDefaultTImesCheckBox = new CheckBox("Use default times");
        this.useDefaultTImesCheckBox.setOnAction(this::handleTimeCheckBox);


        this.updateButton = new Button("Update Job");
        this.updateButton.setOnAction(this::handleUpdateButton);
        boolean canUpdate = this.job.getFromNote() <= this.job.getToNote();
        this.updateButton.setDisable(!canUpdate);
        
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        //pane.setGridLinesVisible(true);
        

        pane.add(new Label("From Note: " ), 0, 0);
        pane.add(this.fromNoteSpinner, 1, 0);

        pane.add(new Label("To Note:"), 0, 1);
        pane.add(this.toNoteSpinner, 1, 1);

        pane.add(new Label("Job name:"), 0, 2); 
        pane.add(this.nameText, 1, 2); 

        pane.setVgap(5);
        pane.add(this.updateButton, 1, 3, 2, 1);
        GridPane.setHalignment(this.updateButton, HPos.LEFT);
        pane.add(this.resetButton, 1, 5, 2, 1);
        GridPane.setHalignment(this.resetButton, HPos.LEFT);

        pane.add(intervalPane, 0, 6, 2, 1);


        

        stage.setTitle("Note Spinners");
        Scene scene = new Scene(pane, 500,400);
        stage.setScene(scene);
        stage.show();

    }

    public void handleIntervalButtonAction(ActionEvent event){
        if (this.intervalButtons.get(0).isSelected()) {
            this.job.setInterval(Job.Interval.ONE);
        }
        if (this.intervalButtons.get(1).isSelected()) {
            this.job.setInterval(Job.Interval.THREE);
        }
        if (this.intervalButtons.get(2).isSelected()) {
            this.job.setInterval(Job.Interval.SIX);
        }
        if (this.intervalButtons.get(3).isSelected()) {
            this.job.setInterval(Job.Interval.TWELVE);
        }

        //System.out.println(this.job);
    }

    public void handleTimeCheckBox(ActionEvent event) {
        this.useDefaultTimes = this.useDefaultTImesCheckBox.isSelected(); 
        //elikkäs, if useDefaultTimesCheckbox(boolean) == true -> useDefaultTimes = true, could be if/else too
        System.out.println("Use default times = " + this.useDefaultTimes); 

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
            //this.nameText.clear();         
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
