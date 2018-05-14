/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wirtualnaszkola;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;


/**
 *
 * @author paulo
 */
public class FXMLDocumentController implements Initializable {
    //<editor-fold defaultstate="collapsed" desc="FXML">
    @FXML private MenuBar menubar;
    @FXML private BorderPane bpContainer;
    @FXML private Label lSchoolName;
    @FXML private ListView lvClasses;
    @FXML private ListView lvStudents;
    @FXML private StackPane spDetails;
    @FXML private TabPane tpBottomInfos;
    @FXML private MenuItem miAddClass;
    @FXML private MenuItem miAddStudent;
    @FXML private MenuItem miSetProfile;
    @FXML private MenuItem miSetTeacher;
    @FXML private MenuItem miDane;
    @FXML private Menu miClass;
    @FXML private Menu miSchool;
    @FXML private GridPane gpClass;
    @FXML private GridPane gpStudent;
    @FXML private TextField tfMath;
    @FXML private TextField tfPolish;
    @FXML private TextField tfPhysics;
    @FXML private TextField tfEnglish;
    @FXML private TextField tfChemistry;
    @FXML private TextField tfBiology;
    @FXML private TextField tfPE;
    //student details
    @FXML private Label lName;
    @FXML private Label lSurname;
    @FXML private Label lPESEL;
    @FXML private Label lAverage;
    //class details
    @FXML private Label lNoStudents;
    @FXML private Label lClassName;
    @FXML private Label lClassAverage;
    @FXML private Label lProfile;
    @FXML private Label lTeacher;
    //</editor-fold>
    
    private Szkola school;
    private String clas;
    private String student;
    protected ObservableList<Klasa> listClasses;
    protected ObservableList<Uczen> listStudents;
    
    //<editor-fold defaultstate="" desc="Menu Items">
    //New school
    @FXML
    private void newschool() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setHeaderText(null);
        dialog.setContentText("School name");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            school = new Szkola(name);
            lSchoolName.setText(school.getName());
            
            miSchool.setDisable(false);
            miDane.setDisable(false);
        });
        
    }
    
    //Close application
    @FXML
    private void close() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close application");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }
    
    //About
    @FXML
    private void about() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Wirtualna Szkola v0.1.0");
        alert.setContentText("Paulo Gliwa \n15015 \npaulo.gliwa@gmail.com");

        alert.showAndWait();
    }
    
    //Add new class to the school
    @FXML
    private void addclass() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setHeaderText(null);
        dialog.setContentText("Class name");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if(!school.addClass(new Klasa(name))) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle(null);
                alert.setHeaderText(null);
                alert.setContentText("Error occured while adding new class");
                alert.showAndWait();
            } else {
                listClasses = FXCollections.observableArrayList(school.listClasses());
                lvClasses.setItems(listClasses);
            }
        });
    }
    
    //Add new student to the class
    @FXML
    private void miAddStudent() {
        Dialog<Uczen> dialog = new Dialog<>();
        dialog.setTitle(null);
        dialog.setHeaderText("Add student");

        // Set the button types
        ButtonType add = new ButtonType("Add", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(add, ButtonType.CANCEL);

        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField pesel = new TextField();
        pesel.setPromptText("PESEL");
        TextField name = new TextField();
        name.setPromptText("FirstName");
        TextField surname = new TextField();
        surname.setPromptText("LastName");
        Label info = new Label(" ");

        grid.add(new Label("PESEL:"), 0, 0);
        grid.add(pesel, 1, 0);
        grid.add(new Label("First name:"), 0, 1);
        grid.add(name, 1, 1);
        grid.add(new Label("Last name:"), 0, 2);
        grid.add(surname, 1, 2);
        grid.add(info, 0, 3, 2, 1);

        // Disable add button
        Node addButton = dialog.getDialogPane().lookupButton(add);
        addButton.setDisable(true);

        // Check if PESEL is correct and if student exists
        pesel.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.trim().isEmpty() || newValue.length() != 11 || school.givemeClass(clas).getStudent(newValue) != null) {
                pesel.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                addButton.setDisable(true);
                info.setText("Incorrect PESEL value!");
            }
            else {
                pesel.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                addButton.setDisable(false);
                info.setText(" ");
            }
            
            if(school.givemeClass(clas).getStudent(newValue) != null) {
                info.setText("Student with that PESEL already exists!");
            }
        });

        dialog.getDialogPane().setContent(grid);

        // Focus PESEL field
        Platform.runLater(() -> pesel.requestFocus());

        // Convert the result to Uczen
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == add) {
                return new Uczen(pesel.getText(), name.getText(), surname.getText());
            }
            return null;
        });

        Optional<Uczen> result = dialog.showAndWait();

        result.ifPresent(addresult -> {
            school.givemeClass(clas).dodaj(new Uczen(addresult.getPesel(), addresult.getImie(), addresult.getNazwisko()));
            updateStudentsLV();
        });
    }
    
    //Set teacher's name
    @FXML
    private void miSetTeacher() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setHeaderText(null);
        dialog.setContentText("Teacher's name");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            school.givemeClass(clas).setWychowawca(name);
            lTeacher.setText("Teacher: " + name);
        });
    }
    
    //Set class profile name
    @FXML
    private void miSetProfile() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setHeaderText(null);
        dialog.setContentText("Profile");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            school.givemeClass(clas).setProfil(name);
            lProfile.setText("Profil: " + name);
        });
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="przykladowe dane">
    @FXML
    private void wypelnij_danymi() {
        Random rand = new Random();
        
        Klasa klasa1 = new Klasa("1A");
        klasa1.setProfil("Matematyczno-Informatyczny");
        klasa1.setWychowawca("Krystyna Kowalska");
        List<String> im = Arrays.asList("Paulo", "Gabrysia", "Damian", "Mariusz", "Lukasz", "Dawid", "Ala", "Leonardo");
        List<String> na = Arrays.asList("Gliwa", "Bielan", "Broda", "Cyganski", "Lisewski", "Lewandowski", "Gaul", "Gliwa");
        int z = 0;
        for(String imie : im) {
            String pesel = Integer.toString(rand.nextInt(99999)+100000)+Integer.toString(rand.nextInt(9999)+10000);
            klasa1.dodaj(new Uczen(pesel,imie,na.get(z)));
            z++;
            for(Przedmiot prz : Przedmiot.values()) {
                int ile = rand.nextInt(9);
                for(int i = 0; i < ile; i++) {
                    klasa1.dodaj(pesel, prz, rand.nextInt(2)+5);
                }
            }
        }
        
        Klasa klasa2 = new Klasa("1B");
        klasa2.setProfil("Humanistyczny");
        klasa2.setWychowawca("Anna Adamiak");
        im = Arrays.asList("Patryk", "Bibiana", "Agnieszka", "Dominika", "Krzysztof", "Damian", "Karolina");
        na = Arrays.asList("Balana", "Sajda", "Pawowicz", "Wisniewska", "Gromadzki", "Zela", "Dratkiewicz");
        z = 0;
        for(String imie : im) {
            String pesel = Integer.toString(rand.nextInt(99999)+100000)+Integer.toString(rand.nextInt(9999)+10000);
            klasa2.dodaj(new Uczen(pesel,imie,na.get(z)));
            z++;
            for(Przedmiot prz : Przedmiot.values()) {
                int ile = rand.nextInt(9);
                for(int i = 0; i < ile; i++) {
                    klasa2.dodaj(pesel, prz, rand.nextInt(5)+1);
                }
            }
        }
        
        school.addClass(klasa1);
        school.addClass(klasa2);
        miDane.setDisable(true);
        lSchoolName.setText("wypelnione");
        updateClassesLV();
        updateStudentsLV();
    }
    //</editor-fold>
    
    //</editor-fold>
    
    @FXML
    private void focusClassDetails() {
        if(clas != null) {
            //show and enable class details pane
            gpClass.setVisible(true);
            gpClass.setDisable(false);
            //hide and disable student details pane
            gpStudent.setVisible(false);
            gpStudent.setDisable(true);
            updateClassInfo();
        }
    }
    
    @FXML
    private void focusStudentDetails() {
        if(clas != null && school.givemeClass(clas).getStudentsCount() > 0) {
            //hide and disable class details pane
            gpClass.setVisible(false);
            gpClass.setDisable(true);
            //show and enable student details pane
            gpStudent.setVisible(true);
            gpStudent.setDisable(false);
            updateStudentInfo();
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Grade buttons">
    @FXML
    private void addMath() {
        addGrade(Przedmiot.Matematyka);
    }
    
    @FXML
    private void addPolish() {
        addGrade(Przedmiot.Polski);
    }
    
    @FXML
    private void addPhysics() {
        addGrade(Przedmiot.Fizyka);
    }
    
    @FXML
    private void addEnglish() {
        addGrade(Przedmiot.Angielski);
    }
    
    @FXML
    private void addChemistry() {
        addGrade(Przedmiot.Chemia);
    }
    
    @FXML
    private void addBiology() {
        addGrade(Przedmiot.Biologia);
    }
    
    @FXML
    private void addPE() {
        addGrade(Przedmiot.WF);
    }
    
    @FXML
    private void remPE() {
        remGrade(Przedmiot.WF);
    }
    
    @FXML
    private void remBiology() {
        remGrade(Przedmiot.Biologia);
    }
    
    @FXML
    private void remChemistry() {
        remGrade(Przedmiot.Chemia);
    }
    
    @FXML
    private void remEnglish() {
        remGrade(Przedmiot.Angielski);
    }
    
    @FXML
    private void remPhysics() {
        remGrade(Przedmiot.Fizyka);
    }
    
    @FXML
    private void remPolish() {
        remGrade(Przedmiot.Polski);
    }
    
    @FXML
    private void remMath() {
        remGrade(Przedmiot.Matematyka);
    }
    //</editor-fold>
    
    //add grade
    private void addGrade(Przedmiot p) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setContentText(null);
        dialog.setHeaderText("Add grade");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(grade -> {
            school.givemeClass(clas).getStudent(student).dodaj(p, Integer.parseInt(grade));
            updateGrades();
            updateStudentInfo();
        });
    }
    
    //remove grade
    private void remGrade(Przedmiot p) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setContentText(null);
        dialog.setHeaderText("Remove grade");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(grade -> {
            school.givemeClass(clas).usun(student, p, Integer.parseInt(grade));
            updateGrades();
            updateStudentInfo();
        });
    }
    
    //Update text fields with grades for single student
    private void updateGrades() {
        if(clas != null && student != null) {
        tfMath.setText(school.givemeClass(clas).getStudent(student).oceny(Przedmiot.Matematyka));
        if(school.givemeClass(clas).srednia(student, Przedmiot.Matematyka) < 2.0){
            tfMath.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            tfMath.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        tfPolish.setText(school.givemeClass(clas).getStudent(student).oceny(Przedmiot.Polski));
        if(school.givemeClass(clas).srednia(student, Przedmiot.Polski) < 2.0){
            tfPolish.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            tfPolish.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        tfPhysics.setText(school.givemeClass(clas).getStudent(student).oceny(Przedmiot.Fizyka));
        if(school.givemeClass(clas).srednia(student, Przedmiot.Fizyka) < 2.0){
            tfPhysics.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            tfPhysics.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        tfEnglish.setText(school.givemeClass(clas).getStudent(student).oceny(Przedmiot.Angielski));
        if(school.givemeClass(clas).srednia(student, Przedmiot.Angielski) < 2.0){
            tfEnglish.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            tfEnglish.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        tfChemistry.setText(school.givemeClass(clas).getStudent(student).oceny(Przedmiot.Chemia));
        if(school.givemeClass(clas).srednia(student, Przedmiot.Chemia) < 2.0){
            tfChemistry.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            tfChemistry.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        tfBiology.setText(school.givemeClass(clas).getStudent(student).oceny(Przedmiot.Biologia));
        if(school.givemeClass(clas).srednia(student, Przedmiot.Biologia) < 2.0){
            tfBiology.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            tfBiology.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        tfPE.setText(school.givemeClass(clas).getStudent(student).oceny(Przedmiot.WF));
        if(school.givemeClass(clas).srednia(student, Przedmiot.WF) < 2.0){
            tfPE.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            tfPE.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        }
    }
    
    //Update student info page (name, average and such)
    private void updateStudentInfo() {
        lName.setText(school.givemeClass(clas).getStudent(student).getImie());
        lSurname.setText(school.givemeClass(clas).getStudent(student).getNazwisko());
        lPESEL.setText(school.givemeClass(clas).getStudent(student).getPesel());
        if(Double.toString(school.givemeClass(clas).srednia(student)).equalsIgnoreCase("nan")) {
            lAverage.setText("-");
        } else {
            lAverage.setText("Avg: " + Double.toString(school.givemeClass(clas).srednia(student)));
        }
    }
    
    //Update class info page (name, average, students count)
    private void updateClassInfo() {
        lTeacher.setText("Teacher: " + school.givemeClass(clas).getTeacher());
        lProfile.setText("Profile: " + school.givemeClass(clas).getProfile());
        lClassName.setText("Class: " + school.givemeClass(clas).getName());
        lNoStudents.setText("Students count: " + Integer.toString(school.givemeClass(clas).getStudentsCount()));
        if(school.givemeClass(clas).srednia() > 0) {
            lClassAverage.setText("Class average: " + Double.toString(school.givemeClass(clas).srednia()));
        } else {
            lClassAverage.setText("Class average: -");
        }
        
    }
    
    //Update classes ListView
    private void updateClassesLV() {
        listClasses = FXCollections.observableArrayList(school.listClasses());
        lvClasses.setItems(listClasses);
    }
    
    //Update students ListView
    private void updateStudentsLV() {
        listStudents = FXCollections.observableArrayList(school.givemeClass(clas).listStudents());
        lvStudents.setItems(listStudents);
    }
    
    //listview custom cells
    private void bindmylist() {
        lvStudents.setCellFactory(new Callback<ListView<Uczen>, ListCell<Uczen>>() {
            @Override
            public ListCell<Uczen> call(ListView<Uczen> param) {
                final ListCell<Uczen> cell = new ListCell<Uczen>() {
                    @Override
                    public void updateItem(Uczen item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getNazwisko() + " " + item.getImie());
                            //SimpleStringProperty str = new SimpleStringProperty(item.getNazwisko() + " " + item.getImie());
                            //textProperty().bind(str);
                        }
                    }
                };
                return cell;
            }
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //custom ListView for Classes
        lvClasses.setCellFactory(new Callback<ListView<Klasa>, ListCell<Klasa>>() {
            @Override
            public ListCell<Klasa> call(ListView<Klasa> param) {
                final ListCell<Klasa> cell = new ListCell<Klasa>() {
                    @Override
                    public void updateItem(Klasa item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName());
                        }
                    }
                };
                return cell;
            }
        });
        
        //Class list selection
        lvClasses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Klasa>() {
            @Override
            public void changed(ObservableValue<? extends Klasa> observable, Klasa oldValue, Klasa newValue) {
                if(newValue != null) {
                    clas = newValue.getName();
                    lSchoolName.setText(school.getName() + " -> " + newValue.getName() + " \"" + newValue.getProfile() +"\"");
                }
                miClass.setDisable(false);
                updateClassInfo();
                //list students in second listview (if there are any)
                if(newValue.getStudentsCount() > 0) {
                    listStudents = FXCollections.observableArrayList(newValue.listStudents());
                    lvStudents.setItems(listStudents);
                    bindmylist();
                }
                else
                {
                    lvStudents.setItems(null);
                    bindmylist();
                }
                
            }
            
        });
        
        //Student list selection
        lvStudents.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Uczen>() {
            @Override
            public void changed(ObservableValue<? extends Uczen> observable, Uczen oldValue, Uczen newValue) {
                student = school.givemeClass(clas).getStudent(newValue.getPesel()).toString();
                student = newValue.getPesel();
                lSchoolName.setText(school.getName() + " -> " + clas + " -> " + newValue);
                updateStudentInfo();
                updateGrades();
            }
            
        });
    }    
    
}
