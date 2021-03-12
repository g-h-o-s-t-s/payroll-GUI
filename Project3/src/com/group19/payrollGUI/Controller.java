package com.group19.payrollGUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static com.group19.payrollGUI.Consts.*;

/**
 * Client class, creates GUI object to handle user interaction.
 * @author Sagnik Mukherjee, Michael Choe
 */
@SuppressWarnings({"WeakerAccess", "FieldMayBeFinal"})
public class Controller
{
    /* -- Data Fields. */
    //General Usage.
    private File fileHandle;
    private Company company = new Company();
    private String[] inputs = {""};

    //User Input Form Nodes.
    @FXML private TextField name;
    @FXML private DatePicker date;
    @FXML private TextField annualSalary;
    @FXML private TextField hoursWorked;
    @FXML private TextField hourlyRate;
    @FXML private ToggleGroup department;
    @FXML private RadioButton dep1, dep2, dep3;
    @FXML private ToggleGroup type;
    @FXML private RadioButton type1, type2, type3;
    @FXML private ToggleGroup admin;
    @FXML private RadioButton admin1, admin2, admin3;
    @FXML private Button reset;

    //Menu Bar Nodes.
    @FXML private MenuItem exportFile;
    @FXML private MenuItem importFile;
    @FXML private MenuItem quit;

    //Database-related Function Nodes.
    @FXML private Button add;
    @FXML private Button calculate;
    @FXML private Button setHours;
    @FXML private Button remove;
    @FXML private SplitMenuButton print;
    @FXML private MenuItem printDate;
    @FXML private MenuItem printDep;

    //TextArea Node for "console output" as program runs.
    @FXML private TextArea statusMessage;

    /**
     * A sort of "constructor" class for the Controller.
     * Initializes all the Action Events and MouseClicked Events associated
     * with MenuItems and Buttons.
     */
    public void initialize() {
        //MenuBar > MenuItem Events
        importFile.setOnAction(event -> this.handleImport());
        exportFile.setOnAction(event -> this.handleExport());
        quit.setOnAction(event -> this.exitProgram());

        //Button Events
        //Note: having the inputs[] parameter means both manual input and
        //input read from file can be supported
        add.setOnMouseClicked(event -> this.handleAdd());
        remove.setOnMouseClicked(event -> this.removeEmployee());
        calculate.setOnMouseClicked(event -> this.calculate());
        setHours.setOnMouseClicked(event -> this.setHours());
        reset.setOnMouseClicked(event -> this.resetForm());

        //Input Form Radio Buttons, Setting "User Data" values
        dep1.setUserData(CS);
        dep2.setUserData(IT);
        dep3.setUserData(ECE);
        type1.setUserData(ADDPARTTIME);
        type2.setUserData(ADDFULLTIME);
        type3.setUserData(ADDFULLROLE);
        admin1.setUserData(MA_CODE);
        admin2.setUserData(DH_CODE);
        admin3.setUserData(DI_CODE);

        //Disable irrelevant Input areas as needed.
        type1.setOnMouseClicked(event -> this.enablePartTime());
        type2.setOnMouseClicked(event -> this.enableFullTime());
        type3.setOnMouseClicked(event -> this.enableManagement());

        //Printing, SplitMenuButton > MenuItem Events
        print.setOnMouseClicked(event -> this.printAll());
        printDate.setOnAction(event -> this.printByDate());
        printDep.setOnAction(event -> this.printByDepartment());

        //Reset Input Form's data.
        reset.setOnMouseClicked(event -> resetForm());
    }

    /**
     * Handler to disable input areas not relevant to PartTime.
     */
    private void enablePartTime() {
        hourlyRate.setDisable(false);
        hoursWorked.setDisable(false);
        annualSalary.setDisable(true);
        admin1.setDisable(true);
        admin2.setDisable(true);
        admin3.setDisable(true);
    }

    /**
     * Handler to disable input areas not relevant to FullTime.
     */
    private void enableFullTime() {
        annualSalary.setDisable(false);
        hourlyRate.setDisable(true);
        hoursWorked.setDisable(true);
        admin1.setDisable(true);
        admin2.setDisable(true);
        admin3.setDisable(true);
    }

    /**
     * Handler to disable input areas not relevant to Management.
     */
    private void enableManagement() {
        annualSalary.setDisable(false);
        admin1.setDisable(false);
        admin2.setDisable(false);
        admin3.setDisable(false);
        hourlyRate.setDisable(true);
        hoursWorked.setDisable(true);
    }

    /**
     * Handler to reset form input.
     */
    private void resetForm() {
        name.clear();
        department.selectToggle(null);
        date.setValue(null);
        type.selectToggle(null);
        annualSalary.clear();
        hoursWorked.clear();
        hourlyRate.clear();
        admin.selectToggle(null);
    }

    /**
     * Appends messages to the TextArea.
     * @param addon String literal to be appended to the TextArea.
     */
    public void appendText(String addon) {
        statusMessage.setText(statusMessage.getText() + "\n" + addon);

        //autoscroll cursor to bottom of text, un-highlight
        statusMessage.selectPositionCaret(statusMessage.getLength());
        statusMessage.deselect();
    }

    /**
     * Allows an alternative way to the "X" in the window corner
     * for exiting the GUI program.
     */
    public void exitProgram() {
        Platform.exit();
    }

    /**
     * Handles the importing of a text file for use within GUI.
     */
    public void handleImport() {
        FileChooser fileChooser = new FileChooser();
        //allow user to choose only text files
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TXT files (*.txt)",
                        "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileHandle = fileChooser.showOpenDialog(null);

        //process file, if one was chosen at all
        try {
            appendText((SELECTED + fileHandle.getName()));
            gatherInput();
        } catch (Exception ex) {
            throwAlert(ex.getMessage());
        }
    }

    /**
     * Handles the exporting of company contents to a text file.
     */
    public void handleExport() {
        appendText(company.exportDatabase());
    }

    /**
     * Intermediate handler for Add Button Event.
     */
    private void handleAdd() {
        try {
            String typeStr =
                    type.getSelectedToggle().getUserData().toString();
            if (typeStr.equals(ADDPARTTIME))
                addPartTime();
            else if (typeStr.equals(ADDFULLTIME))
                addFullTime();
            else
                addFullRole();
        } catch (NullPointerException ex) {
            throwAlert(ex.getMessage());
        }
    }

    /**
     * Driver method to process input and invoke GUI commands.
     * Methods should be matched to buttons.
     */
    public void gatherInput() {
        String input;
        Scanner scn = null;
        try {
            scn = new Scanner(fileHandle);
        } catch (FileNotFoundException | NullPointerException ex) {
            throwAlert(ex.getMessage());
        }

        //assert to avoid possible null pointer exception
        while (true) {
            assert scn != null;
            if (!scn.hasNextLine()) break;
            input = scn.nextLine();
            if (input.equals(""))
                continue;

            inputs = input.split(DELIMITER);
            String command = inputs[SPLITONE];
            switch (command) {
                //imported file only contains employees
                //to be added
                case ADDPARTTIME -> addPartTime();
                case ADDFULLTIME -> addFullTime();
                case ADDFULLROLE -> addFullRole();

                default -> appendText("Command '"
                        + command + "' not supported!" + "\n");
            }
        }

        scn.close();
        fileHandle = null;
    }

    /**
     * Converts manual user input from textFields and
     * radioButtons to a String[] for inputs, a data field of Controller().
     */
    private void setInputs() {
        try {
            //expected: "P" "F" or "M"
            String typeStr =
                    type.getSelectedToggle().getUserData().toString();

            if (typeStr.equals(ADDPARTTIME)) {
                setCommonInputs(typeStr, hourlyRate);
            }
            else if (typeStr.equals(ADDFULLTIME)) {
                setCommonInputs(typeStr, annualSalary);
            }
            else {
                String nameStr = name.getText();
                String depStr = department.getSelectedToggle()
                        .getUserData().toString();
                String dateStr = date.getValue().toString();
                dateStr = dateFormat(dateStr);
                String salaryStr = annualSalary.getText();
                String adminStr =
                        admin.getSelectedToggle().getUserData().toString();
                inputs = new String[]{typeStr, nameStr,
                        depStr, dateStr, salaryStr, adminStr};
            }
        } catch (Exception ex) {
            throwAlert(ex.getMessage());
        }
    }

    /**
     * Intermediate helper, processes textField/radioButton inputs for
     * Part-time and Full-time. They basically share input fields, just
     * two different forms of salaries (annual vs hourly).
     * @param typeStr "P" or "F," for the type of employee to be added
     * @param salary TextField value for hourlyRate, or for annualSalary
     */
    private void setCommonInputs(String typeStr, TextField salary) {
        String nameStr = name.getText();
        String depStr = department.getSelectedToggle()
                .getUserData().toString();
        String dateStr = date.getValue().toString();
        dateStr = dateFormat(dateStr);
        String rateStr = salary.getText();
        inputs = new String[]{typeStr, nameStr, depStr, dateStr, rateStr};
    }

    /**
     * Formats the date toString value returned by DatePicker Node.
     * Converts string from YYYY-MM-DD to MM-DD-YYYY.
     * @param dateStr String of date to be formatted
     * @return formatted String value of date
     */
    private String dateFormat(String dateStr)
    {
        String result = "";
        String[] parts = dateStr.split("-");
        if (parts.length == DATE_PARTS) {
            result = parts[SPLITTWO] + "/"
                    + parts[SPLITTHREE] + "/" + parts[SPLITONE];
        }
        return result;
    }

    /**
     * Helper method to execute "Add Part Time" client command.
     */
    public void addPartTime() {
        //Set inputs[] based off user form if no file was imported.
        if (fileHandle == null)
            setInputs();
        try {
            Profile profile = inputBreakdown(inputs);

            double pay = Double.parseDouble(inputs[SPLITFIVE]);
            validatePayRate(pay);

            Parttime addThis = new Parttime(profile, pay, DEFAULTHOURS);
            if (company.add(addThis))
                appendText(ADDEDPT);
            else
                appendText(DUPLICATE);

        } catch (Exception ex) {
            throwAlert(ex.getMessage());
        }
    }

    /**
     * Helper method to execute "Add Full Time" client command.
     */
    private void addFullTime() {
        if (fileHandle == null)
            setInputs();
        try {
            Profile profile = inputBreakdown(inputs);

            double pay = Double.parseDouble(inputs[SPLITFIVE]);
            validateSalary(pay);

            Fulltime addThis = new Fulltime(profile, pay);
            if (company.add(addThis))
                appendText(ADDEDFT);
            else
                appendText(DUPLICATE);

        } catch (Exception ex) {
            throwAlert(ex.getMessage());
        }

    }

    /**
     * Helper method to execute "Add Full Time Management" client command.
     */
    private void addFullRole() {
        if (fileHandle == null)
            setInputs();
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
        } catch (Exception ex) {
            throwAlert(ex.getMessage());
        }
    }

    /**
     * Helper method to execute "Remove" client command.
     */

    private void removeEmployee() {
        setInputs();
        if (company.isEmpty())
            appendText(ISEMPTY);

        else {
            try {
                Profile profile = inputBreakdown(inputs);

                Employee key = new Employee();
                key.setProfile(profile);

                if (company.remove(key))
                    appendText(REMOVED);
                else
                    appendText(NONEXISTENT);

            } catch (Exception ex) {
                throwAlert(ex.getMessage());
            }
        }
    }

    /**
     * Helper method to execute "Calculate" client command.
     */

    private void calculate() {
        if (company.isEmpty())
            appendText(ISEMPTY);
        else {
            company.processPayments();
            appendText(CALCULATED);
        }
    }

    /**
     * Helper method to execute "Set" client command.
     */

    private void setHours() {
        setInputs();
        if (company.isEmpty())
            appendText(ISEMPTY);

        else if (type.getSelectedToggle().getUserData().toString().
                equals(ADDPARTTIME)){
            try {
                Profile profile = inputBreakdown(inputs);

                int hoursToSet = Integer.parseInt(hoursWorked.getText());
                validateHours(hoursToSet);

                Parttime key = new Parttime();
                key.setProfile(profile);
                key.setHoursWorked(hoursToSet);

                if (company.setHours(key))
                    appendText(SETHOURS);
                else
                    appendText(NONEXISTENT);

            } catch (Exception ex) {
                throwAlert(ex.getMessage());
            }
        }
    }

    /**
     * Handles printAll onMouseClicked action.
     */

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
        try {
            String name = inputs[SPLITTWO];
            String department = inputs[SPLITTHREE];
            String dateStr = inputs[SPLITFOUR];
            Date date = new Date(dateStr);
            validateSharedInput(name, department, date);

            return new Profile(name, department, date);
        } catch (Exception ex) {
            throwAlert(ex.getMessage());
        }
        return new Profile();
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
     * Helper method to validate salary before adding a FullTime/Management.
     * @param pay double salary amount to be validated (positive value)
     */
    private void validateSalary(double pay) throws InputMismatchException
    {
        if (Double.compare(pay, ZERO) < 0)
            throw new InputMismatchException(INVALID_SALARY);
    }

    /**
     * Helper method to validate hourly pay rate before adding a PartTime.
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
     * Helper method to validate salary before adding a FullTime/Management.
     * @param hoursToSet int hoursWorked to be validated (positive value)
     */
    private void validateHours(int hoursToSet) throws InputMismatchException
    {
        if (hoursToSet < 0)
            throw new InputMismatchException(INVALID_HOURS_A);
        if (hoursToSet > PARTTIME_MAX)
            throw new InputMismatchException(INVALID_HOURS_B);
    }

    /**
     * Helper to open Alert Dialog box when exception occurs.
     * @param exceptionMsg String literal containing exception details
     */
    public static void throwAlert(String exceptionMsg)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exception Occurred");
        alert.setHeaderText("Exception Details.");
        alert.setContentText(exceptionMsg);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                alert.close();
            }
        });
    }
}
