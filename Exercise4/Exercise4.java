import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Exercise4 extends Application {
    // Define the user interface components for controlling and displaying note timing.
    private Slider noteDurationSlider; // Controls the duration of the note in milliseconds.
    private Label noteDurationLabel; // Displays the current duration.
    private Slider noteDecaySlider; // Controls the decay time of the note.
    private Label noteDecayLabel; // Displays the current decay time.
    private Slider noteGapSlider; // Controls the gap time between notes.
    private Label noteGapLabel; // Displays the current gap time.
    private Canvas noteTimingCanvas; // A canvas to visually represent note duration, decay, and gap.

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
        
        
        for (int i = 0; i < intervalValues.length; i++) {
            RadioButton rb = new RadioButton(intervalButtonNames.get(i));
            rb.setToggleGroup(intervalButtonGroup);
            rb.setOnAction(this::handleIntervalButtonAction);
            this.intervalButtons.add(rb);
        }

        this.intervalButtons.get(0).setSelected(true);



        // Initialize labels and sliders with default values.
        noteDurationLabel = new Label("Duration: 100 ms");
        noteDurationSlider = new Slider(100, 5000, 100);
        noteDurationSlider.setPadding(new Insets(0, 50, 10, 50));
        noteDecayLabel = new Label("Decay: 100 ms");
        noteDecaySlider = new Slider(100, 4500, 100);
        noteDecaySlider.setPadding(new Insets(0, 50, 10, 50));
        noteGapLabel = new Label("Gap: 100 ms");
        noteGapSlider = new Slider(100, 4500, 100);
        noteGapSlider.setPadding(new Insets(0, 50, 10, 50));

        // Create the canvas for visual representation of note timing.
        noteTimingCanvas = new Canvas(400, 30);

        // Update the canvas to reflect the current values of the sliders.
        updateCanvas(
            (int) noteDurationSlider.getValue(),
            (int) noteDecaySlider.getValue(),
            (int) noteGapSlider.getValue()
        );

        // Attach listeners to sliders to update labels and canvas when values change.
        noteDurationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int duration = newValue.intValue();
            noteDurationLabel.setText(String.format("Duration: %d ms", duration));
            updateCanvas(duration, (int) noteDecaySlider.getValue(), (int) noteGapSlider.getValue());
        });

        noteDecaySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int decay = newValue.intValue();
            noteDecayLabel.setText(String.format("Decay: %d ms", decay));
            updateCanvas((int) noteDurationSlider.getValue(), decay, (int) noteGapSlider.getValue());
        });

        noteGapSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int gap = newValue.intValue();
            noteGapLabel.setText(String.format("Gap: %d ms", gap));
            updateCanvas((int) noteDurationSlider.getValue(), (int) noteDecaySlider.getValue(), gap);
        });

        
        // Create a layout container for the UI components.
        VBox noteControlBox = new VBox(
            noteDurationLabel,
            noteDurationSlider,
            noteDecayLabel,
            noteDecaySlider,
            noteGapLabel,
            noteGapSlider,
            noteTimingCanvas
        );
        noteControlBox.setAlignment(Pos.CENTER);
        noteControlBox.setSpacing(5);

        HBox intervalBox = new HBox(
            this.intervalButtons.get(0),
            this.intervalButtons.get(1),
            this.intervalButtons.get(2),
            this.intervalButtons.get(3)
            
            
        );
        intervalBox.setAlignment(Pos.CENTER);

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
        TitledPane intervalPane = new TitledPane("Interval", intervalBox);
        pane.add(intervalPane, 0, 6, 2, 1);

       
        
        

        VBox mainBox = new VBox(noteControlBox, intervalBox,pane);
        mainBox.setSpacing(5);

        // Configure and display the primary stage.
        stage.setTitle("Note Sliders");
        stage.setScene(new Scene(mainBox, 500, 600));
        stage.show();
    }

    // Updates the visual representation of the note timing based on the given parameters.
    private void updateCanvas(int duration, int decay, int gap) {
        GraphicsContext gc = noteTimingCanvas.getGraphicsContext2D(); // Get the graphics context for drawing on the canvas.
        gc.setFill(Color.LIGHTGRAY); // Set the background color.
        gc.fillRect(0, 0, noteTimingCanvas.getWidth(), noteTimingCanvas.getHeight()); // Clear the canvas.

        // Draw the note duration.
        gc.setFill(Color.BLUE);
        int durationWidth = duration / 6; // Scale the duration for visual representation.
        gc.fillRect(0, 0, durationWidth, 30); // Draw the duration segment.

        // Draw the note decay time.
        gc.setFill(Color.LIGHTBLUE);
        int decayWidth = decay / 6; // Scale the decay time for visual representation.
        gc.fillRect(durationWidth, 0, decayWidth, 30); // Draw the decay segment.

        // Draw the gap between notes.
        gc.setFill(Color.YELLOW);
        int gapWidth = gap / 6; // Scale the gap time for visual representation.
        gc.fillRect(durationWidth + decayWidth, 0, gapWidth, 30); // Draw the gap segment.
    }


    //Action eventit alkaa tästä
    
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
        //spinner limits value 0 - 127, no need to sanitize further

        int duration = (int) noteDurationSlider.getValue();
        int decay = (int) noteDecaySlider.getValue();
        int gap = (int) noteGapSlider.getValue();


        this.job.setFromNote(fromNote);
        this.job.setToNote(toNote);
        this.job.setNoteDecay(decay);
        this.job.setNoteDuration(duration);
        this.job.setNoteGap(gap);
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
        launch(); // Launch the JavaFX application.
    }
}
