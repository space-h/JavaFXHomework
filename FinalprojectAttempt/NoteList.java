import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NoteList extends Application {

    //GUI
    private ListView<String> noteListView;

    //DATA
    private Job job;

    @Override
    public void start(Stage stage) {
        this.job = new Job();
        List<Integer> velocities = new ArrayList<Integer>();
        velocities.add(60);
        velocities.add(80);
        velocities.add(100);
        this.job.setSpecificVelocities(velocities);
        System.out.println(this.job);

        List<String> noteStrings = new ArrayList<String>();
        for (int note : this.job.getNotes()) {
            for (int velocity : this.job.getVelocities()){
                String ns = String.format("note %d at vel %d (dur %d ms + decay %d ms)",
                note, 
                velocity,
                this.job.getNoteDuration(),
                this.job.getNoteDecay()
                );
                System.out.println(ns);
                noteStrings.add(ns);
            }
        }
        System.out.println("Total number of notes = " + noteStrings.size());
        ObservableList<String> noteList = FXCollections.observableArrayList();
        noteList.addAll(noteStrings);
        this.noteListView = new ListView<String>(noteList);
        this.noteListView.setMinWidth(100);
        this.noteListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.noteListView.getSelectionModel().select(0);
        this.noteListView.getSelectionModel().selectedItemProperty().addListener(
            this::processListSelection
        );

        Button button = new Button("Read Selected Indices");
        button.setOnAction(event -> {
            //You have to parametrize the generic types, so add <Interger>
            ObservableList<Integer> selectedIndices = 
            this.noteListView.getSelectionModel().getSelectedIndices();

            for (Integer index : selectedIndices){
                System.out.println("index = " + index + " (" + index.getClass() + ")");
            }
        });

        VBox box = new VBox(this.noteListView, button);

        stage.setTitle("Note List");
        Scene scene = new Scene(box, 300, 200);
        stage.setScene(scene);
        stage.show();

    }

    public void processListSelection(
        ObservableValue<? extends String> val,
        String oldValue,
        String newValue) {
        int index = this.noteListView.getSelectionModel().getSelectedIndex();
        System.out.println("Selected Item #" + index + " = " + newValue);
        }


    public static void main(String[] args) {
        launch();
    }

}
