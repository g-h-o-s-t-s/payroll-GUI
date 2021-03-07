package com.group19.payrollGUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static com.group19.payrollGUI.Consts.*;
/**
 * Client class, creates GUI object to handle user interaction.
 * @author Sagnik Mukherjee, Michael Choe
 */
@SuppressWarnings("WeakerAccess")
public class Controller
{
    @FXML public FileChooser fileChooser = new FileChooser();
    public AnchorPane contentAnchorPane;
    public BorderPane primaryBorderPane;
    public MenuBar menuBar;
    public Menu menuFile;
    @FXML private MenuItem exportFile;
    @FXML private MenuItem importFile;
    @FXML private MenuItem quit;
    public Pane titlePane;
    public Label titleLabel;
    public ButtonBar buttonBar;
    @FXML private Button addPartTime;
    @FXML private Button addFullTime;
    @FXML private Button addManagement;
    @FXML private Button calculate;
    @FXML private Button setHours;
    @FXML private Button remove;
    @FXML private Button print;
    @FXML private Button printDate;
    @FXML private Button printDep;
    @FXML private TextArea statusMessage;
    public Company company = new Company();
    public String[] inputs;

    /**
     * A sort of "constructor" class for the Controller.
     * Initializes all the Action Events and MouseClicked Events associated
     * with MenuItems and Buttons.
     */
    @FXML
    public void initialize() {
        //MenuItem Events
        importFile.setOnAction(event -> this.handleImport());
        exportFile.setOnAction(event -> this.handleExport());
        quit.setOnAction(event -> this.exitProgram());

        //Button Events
        addPartTime.setOnMouseClicked(event -> this.addPartTime());
        addFullTime.setOnMouseClicked(event -> this.addFullTime());
        addManagement.setOnMouseClicked(event -> this.addFullRole());
        remove.setOnMouseClicked(event -> this.removeEmployee());
        calculate.setOnMouseClicked(event -> this.calculate());
        setHours.setOnMouseClicked(event -> this.setHours());
        print.setOnMouseClicked(event -> this.printAll());
        printDate.setOnMouseClicked(event -> this.printByDate());
        printDep.setOnMouseClicked(event -> this.printByDepartment());
    }

    /**
     * Appends messages to the TextArea.
     * @param addon String literal to be appended to the TextArea.
     */
    public void appendText(String addon) {
        statusMessage.setText(statusMessage.getText() + "\n" + addon);
    }

    /**
     * Allows an alternative way to the "X" in the window corner
     * for exiting the GUI program.
     */
    public void exitProgram() {
        appendText(SHUTDOWN);
        Platform.exit();
    }

    /**
     * Handles the importing of a text file for use within GUI.
     */
    public void handleImport() {
        //allow user to choose only text files
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TXT files (*.txt)",
                        "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(null);

        //process file, if one was chosen at all
        try {
            if (selectedFile != null) {
                appendText((SELECTED + selectedFile.getName()));
                gatherInput(selectedFile);
            }
        } catch (NullPointerException ex) {
            appendText(ex.getMessage());
        }
    }

    /**
     * Handles the exporting of company contents to a text file.
     */
    public void handleExport() {
        //allows user to select their own save location
        File fileOut = fileChooser.showSaveDialog(null);

        try {
            //write company contents to output file
            FileOutputStream fOut = new FileOutputStream(fileOut);
            String data = this.toString();

            //converting string into byteStream for fileOutputStream use
            byte[] b = data.getBytes();
            fOut.write(b);
            fOut.close();
        } catch (IOException ex) {
            appendText(ex.getMessage());
        }
    }

    /**
     * Driver method to process input and invoke GUI commands.
     * Methods should be matched to buttons.
     * @param file text File which is imported from user storage
     */
    public void gatherInput(File file) {
        String input;
        boolean loop = true;
        Scanner scn = null;
        try {
            scn = new Scanner(file);
        } catch (FileNotFoundException | NullPointerException ex) {
            appendText(ex.getMessage());
            loop = false;
        }

        while (loop)
        {
            if (!scn.hasNextLine()) break;
            input = scn.nextLine();
            if (input.equals(""))
                continue;
            if (input.equals(QUIT)) {
                loop = false;
                continue;
            }

            inputs = input.split(DELIMITER);
            String command = inputs[SPLITONE];
            switch (command) {
                case ADDPARTTIME -> addPartTime();
                case ADDFULLTIME -> addFullTime();
                case ADDFULLROLE -> addFullRole();
                case REMOVE -> removeEmployee();
                case CALCULATE -> calculate();
                case SET -> setHours();
                case PRINTALL -> printAll();
                case PRINTHIRED -> printByDate();
                case PRINTDEPART -> printByDepartment();

                default -> appendText("Command '"
                            + command + "' not supported!" + "\n");
            }
        }
    }

    /**
     * Helper method to execute "Add Part Time" client command.
     */
    @FXML
    public void addPartTime() {
        if (inputs.length == FIVEINPUTS) {
            try {
                Profile profile = inputBreakdown(inputs);

                double pay = Double.parseDouble(inputs[SPLITFIVE]);
                validatePayRate(pay);

                Parttime addThis = new Parttime(profile, pay, DEFAULTHOURS);
                if (company.add(addThis))
                    appendText(ADDEDPT);
                else
                    appendText(DUPLICATE);

            } catch (InputMismatchException | NumberFormatException ex) {
                appendText(ex.getMessage());
            }
        }
        else
            appendText(INVALID_INPUT);
    }

    /**
     * Helper method to execute "Add Full Time" client command.
     */
    @FXML
    private void addFullTime() {
        if (inputs.length == FIVEINPUTS) {
            try {
                Profile profile = inputBreakdown(inputs);

                double pay = Double.parseDouble(inputs[SPLITFIVE]);
                validateSalary(pay);

                Fulltime addThis = new Fulltime(profile, pay);
                if (company.add(addThis))
                    appendText(ADDEDFT);
                else
                    appendText(DUPLICATE);

            } catch (InputMismatchException | NumberFormatException ex) {
                appendText(ex.getMessage());
            }
        }
        else
            appendText(INVALID_INPUT);
    }

    /**
     * Helper method to execute "Add Full Time Management" client command.
     */
    @FXML
    private void addFullRole() {
        if (inputs.length == SIXINPUTS) {
            try {
                Profile profile = inputBreakdown(inputs);

                double pay = Double.parseDouble(inputs[SPLITFIVE]);
                //handles -0.0, though this input is unlikely
                validateSalary(pay);

                int code = Integer.parseInt(inputs[SPLITSIX]);
                validateCode(code);


                Management addThis = new Management(profile, pay, code);
                if (company.add(addThis))
                    appendText(ADDEDMA);
                else
                    appendText(DUPLICATE);
            } catch (InputMismatchException | NumberFormatException ex) {
                appendText(ex.getMessage());
            }
        }
        else
            appendText(INVALID_INPUT);
    }

    /**
     * Helper method to execute "Remove" client command.
     */
    @FXML
    private void removeEmployee() {
        if (company.isEmpty())
            appendText(ISEMPTY);

        else if (inputs.length == FOURINPUTS) {
            try {
                Profile profile = inputBreakdown(inputs);

                Employee key = new Employee();
                key.setProfile(profile);

                if (company.remove(key))
                    appendText(REMOVED);
                else
                    appendText(NONEXISTENT);

            } catch (InputMismatchException | NumberFormatException ex) {
                appendText(ex.getMessage());
            }
        }
        else
            appendText(INVALID_INPUT);
    }

    /**
     * Helper method to execute "Calculate" client command.
     */
    @FXML
    private void calculate() {
        if (company.isEmpty())
            appendText(ISEMPTY);

        else if (inputs.length == ONEINPUT) {
            company.processPayments();
            appendText(CALCULATED);
        }
        else
            appendText(INVALID_INPUT);
    }

    /**
     * Helper method to execute "Set" client command.
     */
    @FXML
    private void setHours() {
        if (company.isEmpty())
            appendText(ISEMPTY);

        else if (inputs.length == FIVEINPUTS) {
            try {
                Profile profile = inputBreakdown(inputs);

                int hoursToSet = Integer.parseInt(inputs[SPLITFIVE]);
                validateHours(hoursToSet);

                Parttime key = new Parttime();
                key.setProfile(profile);
                key.setHoursWorked(hoursToSet);

                if (company.setHours(key))
                    appendText(SETHOURS);
                else
                    appendText(NONEXISTENT);

            } catch (InputMismatchException | NumberFormatException ex) {
                appendText(ex.getMessage());
            }
        }
        else
            appendText(INVALID_INPUT);
    }

    /**
     * Handles printAll onMouseClicked action.
     */
    @FXML
    private void printAll() {
        if (company.isEmpty())
            appendText(ISEMPTY);

        else {
            appendText(PRINT_HEADER);
            appendText(company.print());
        }
    }

    /**
     * Handles printByDate onMouseClicked action.
     */
    @FXML
    private void printByDate() {
        if (company.isEmpty())
            appendText(ISEMPTY);

        else {
            appendText(PRINTDATE_HEADER);
            appendText(company.printByDate());
        }
    }

    /**
     * Handles printByDepartment onMouseClicked action.
     */
    @FXML
    private void printByDepartment() {
        if (company.isEmpty())
            appendText(ISEMPTY);

        else {
            appendText(PRINTDEP_HEADER);
            appendText(company.printByDepartment());
        }
    }

    /**
     * Helper method to process input substrings, namely the common ones
     * among the command-based helper methods. This code appears in five
     * other methods and was very redundant, so refactoring as a private
     * method proved to be necessary.
     */
    private Profile inputBreakdown(String[] inputs)
    {
        String name = inputs[SPLITTWO];
        String department = inputs[SPLITTHREE];
        String dateStr = inputs[SPLITFOUR];
        Date date = new Date(dateStr);
        validateSharedInput(name, department, date);

        return new Profile(name, department, date);
    }

    /**
     * Helper method to validate common input among command-based helpers.
     * @param name String Employee name to be validated (last,first)
     * @param dep String of department code to be validated (CS, ECE, or IT)
     * @param date Date object to be validated, hiring date
     */
    private void validateSharedInput(String name, String dep, Date date)
            throws InputMismatchException
    {
        if (name.split(" ").length != NAMES)
            throw new InputMismatchException("'" + name + "'"
                    + INVALID_NAME);
        if (!(dep.equals(CS)
                || dep.equals(ECE)
                || dep.equals(IT)))
            throw new InputMismatchException("'" + dep + "'"
                    + INVALID_DEP);
        if (!date.isValid())
            throw new InputMismatchException(date.toString()
                    + INVALID_DATE);
    }

    /**
     * Helper method to validate salary before adding a Fulltime/Management.
     * @param pay double salary amount to be validated (positive value)
     */
    private void validateSalary(double pay) throws InputMismatchException
    {
        if (Double.compare(pay, ZERO) < 0)
            throw new InputMismatchException(INVALID_SALARY);
    }

    /**
     * Helper method to validate hourly pay rate before adding a Parttime.
     * @param pay double payRate amount to be validated (positive value)
     */
    private void validatePayRate(double pay) throws InputMismatchException
    {
        if (Double.compare(pay, ZERO) < 0)
            throw new InputMismatchException(INVALID_PAYRATE);
    }

    /**
     * Helper method to validate code before adding a Management.
     * @param code double salary amount to be validated (1, 2, or 3)
     */
    private void validateCode(int code) throws InputMismatchException
    {
        if (!(code >= MA_CODE && code <= DI_CODE))
            throw new InputMismatchException(INVALID_MGMT);
    }

    /**
     * Helper method to validate salary before adding a Fulltime/Management.
     * @param hoursToSet int hoursWorked to be validated (positive value)
     */
    private void validateHours(int hoursToSet) throws InputMismatchException
    {
        if (hoursToSet < 0)
            throw new InputMismatchException(INVALID_HOURS_A);
        if (hoursToSet > PARTTIME_MAX)
            throw new InputMismatchException(INVALID_HOURS_B);
    }
}
