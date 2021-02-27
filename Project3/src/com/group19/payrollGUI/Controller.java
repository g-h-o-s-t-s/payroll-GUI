package com.group19.payrollGUI;

import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Client class, creates GUI object to handle user interaction.
 * @author Sagnik Mukherjee, Michael Choe
 */
@SuppressWarnings("WeakerAccess")
public class Controller
{
    public Label statusMessage;
    public HBox CenterHBox;
    public Pane TitlePane;
    public MenuBar MenuBar;
    public Menu MenuFile;
    public MenuItem Export;
    public MenuItem Import;
    public MenuItem Quit;
    public Menu MenuEdit;
    public AnchorPane ContentAnchorPane;
    public BorderPane PrimaryBorderPane;
    public HBox BottomHBox;
    public MenuItem About;
    public Menu MenuHelp;
    public MenuItem UnselectAll;
    public MenuItem SelectAll;
    public MenuItem Delete;
    public MenuItem Paste;
    public MenuItem Copy;
    public MenuItem Cut;
    public Label TitleLabel;
    public Button AddPartTime;
    public Button AddFullTime;
    public Button AddManagement;
    public Button Calculate;
    public Button SetHours;
    public Button Remove;
    public Button Print;


    public void sayAddPartTime() {
        statusMessage.setText(Consts.ADDEDPT);
    }

    public void sayAddFullTime() {
        statusMessage.setText(Consts.ADDEDFT);
    }

    public void sayAddManagement() {
        statusMessage.setText(Consts.ADDEDMA);
    }

    public void sayRemove() {
        statusMessage.setText(Consts.REMOVED);
    }

    public void sayCalculate() {
        statusMessage.setText(Consts.CALCULATED);
    }

    public void saySetHours() {
        statusMessage.setText(Consts.SETHOURS);
    }

    public void sayClearText() {
        statusMessage.setText(Consts.BLANK);
    }

    public void sayPrint() {
        statusMessage.setText((Consts.PRINT_HEADER));
    }

    /**
     * Driver method to run Kiosk commands.
     * Methods should be matched to buttons.
     */
    public void run()
    {
        printout(Consts.STARTUP);
        Company company = new Company();
        Scanner scn = new Scanner(System.in);
        String input;
        String[] inputs;
        boolean loop = true;

        while (loop && scn.hasNextLine())
        {
            input = scn.nextLine();
            if (input.equals(""))
                continue;
            if (input.equals(Consts.QUIT)) {
                loop = false;
                continue;
            }

            inputs = input.split(Consts.DELIMITER);
            String command = inputs[Consts.SPLITONE];
            switch (command) {
                case Consts.ADDPARTTIME:
                    addPartTime(inputs, company);
                    break;
                case Consts.ADDFULLTIME:
                    addFullTime(inputs, company);
                    break;
                case Consts.ADDFULLROLE:
                    addFullRole(inputs, company);
                    break;
                case Consts.REMOVE:
                    removeEmployee(inputs, company);
                    break;
                case Consts.CALCULATE:
                    calculate(inputs, company);
                    break;
                case Consts.SET:
                    setHours(inputs, company);
                    break;
                case Consts.PRINTALL:
                    if (company.isEmpty())
                        printout(Consts.ISEMPTY);
                    else {
                        System.out.println(Consts.PRINT_HEADER);
                        company.print();
                    }
                    break;
                case Consts.PRINTHIRED:
                    if (company.isEmpty())
                        printout(Consts.ISEMPTY);
                    else {
                        System.out.println(Consts.PRINTDATE_HEADER);
                        company.printByDate();
                    }
                    break;
                case Consts.PRINTDEPART:
                    if (company.isEmpty())
                        printout(Consts.ISEMPTY);
                    else {
                        System.out.println(Consts.PRINTDEP_HEADER);
                        company.printByDepartment();
                    }
                    break;

                default:
                    printout("Command '"
                            + command + "' not supported!");
                    break;
            }
        }
        printout(Consts.SHUTDOWN);
    }

    /**
     * Helper method to execute "Add Part Time" client command.
     * @param inputs String[] reference pass of return value of split()
     * @param company Company, reference pass of company bag container
     */
    public void addPartTime(String[] inputs, Company company) {
        if (inputs.length == Consts.FIVEINPUTS) {
            try {
                Profile profile = inputBreakdown(inputs);

                double pay = Double.parseDouble(inputs[Consts.SPLITFIVE]);
                validatePayRate(pay);
                int hw = Consts.DEFAULTHOURS;

                Parttime addThis = new Parttime(profile, pay, hw);
                if (company.add(addThis))
                    printout(Consts.ADDED);
                else
                    printout(Consts.DUPLICATE);

            } catch (InputMismatchException | NumberFormatException ex) {
                printout(ex.getMessage());
            }
        }
        else
            printout(Consts.INVALID_INPUT);
    }

    /**
     * Helper method to execute "Add Full Time" client command.
     * @param inputs String[] reference pass of return value of split()
     * @param company Company, reference pass of company bag container
     */
    private void addFullTime(String[] inputs, Company company) {
        if (inputs.length == Consts.FIVEINPUTS) {
            try {
                Profile profile = inputBreakdown(inputs);

                double pay = Double.parseDouble(inputs[Consts.SPLITFIVE]);
                validateSalary(pay);

                Fulltime addThis = new Fulltime(profile, pay);
                if (company.add(addThis))
                    printout(Consts.ADDED);
                else
                    printout(Consts.DUPLICATE);

            } catch (InputMismatchException | NumberFormatException ex) {
                printout(ex.getMessage());
            }
        }
        else
            printout(Consts.INVALID_INPUT);
    }

    /**
     * Helper method to execute "Add Full Time Management" client command.
     * @param inputs String[] reference pass of return value of split()
     * @param company Company, reference pass of company bag container
     */
    private void addFullRole(String[] inputs, Company company)
    {
        if (inputs.length == Consts.SIXINPUTS) {
            try {
                Profile profile = inputBreakdown(inputs);

                double pay = Double.parseDouble(inputs[Consts.SPLITFIVE]);
                //handles -0.0, though this input is unlikely
                validateSalary(pay);

                int code = Integer.parseInt(inputs[Consts.SPLITSIX]);
                validateCode(code);


                Management addThis = new Management(profile, pay, code);
                if (company.add(addThis))
                    printout(Consts.ADDED);
                else
                    printout(Consts.DUPLICATE);
            } catch (InputMismatchException | NumberFormatException ex) {
                printout(ex.getMessage());
            }
        }
        else
            printout(Consts.INVALID_INPUT);
    }

    /**
     * Helper method to execute "Remove" client command.
     * @param inputs String[] reference pass of return value of split()
     * @param company Company, reference pass of company bag container
     */
    private void removeEmployee(String[] inputs, Company company)
    {
        if (company.isEmpty())
            printout(Consts.ISEMPTY);

        else if (inputs.length == Consts.FOURINPUTS) {
            try {
                Profile profile = inputBreakdown(inputs);

                Employee key = new Employee();
                key.setProfile(profile);

                if (company.remove(key))
                    printout(Consts.REMOVED);
                else
                    printout(Consts.NONEXISTENT);

            } catch (InputMismatchException | NumberFormatException ex) {
                printout(ex.getMessage());
            }
        }
        else
            printout(Consts.INVALID_INPUT);
    }

    /**
     * Helper method to execute "Calculate" client command.
     * @param inputs String[] reference pass of return value of split()
     * @param company Company, reference pass of company bag container
     */
    private void calculate(String[] inputs, Company company)
    {
        if (company.isEmpty())
            printout(Consts.ISEMPTY);

        else if (inputs.length == Consts.ONEINPUT) {
            company.processPayments();
            printout(Consts.CALCULATED);
        }
        else
            printout(Consts.INVALID_INPUT);
    }

    /**
     * Helper method to execute "Set" client command.
     * @param inputs String[] reference pass of return value of split()
     * @param company Company, reference pass of company bag container
     */
    private void setHours(String[] inputs, Company company)
    {
        if (company.isEmpty())
            printout(Consts.ISEMPTY);

        else if (inputs.length == Consts.FIVEINPUTS) {
            try {
                Profile profile = inputBreakdown(inputs);

                int hoursToSet = Integer.parseInt(inputs[Consts.SPLITFIVE]);
                validateHours(hoursToSet);

                Parttime key = new Parttime();
                key.setProfile(profile);
                key.setHoursWorked(hoursToSet);

                if (company.setHours(key))
                    printout(Consts.SETHOURS);
                else
                    printout(Consts.NONEXISTENT);

            } catch (InputMismatchException | NumberFormatException ex) {
                printout(ex.getMessage());
            }
        }
        else
            printout(Consts.INVALID_INPUT);
    }

    /**
     * Helper method to process input substrings, namely the common ones
     * among the command-based helper methods. This code appears in five
     * other methods and was very redundant, so refactoring as a private
     * method proved to be necessary.
     * @param inputs String[], contains substrings of user input
     * @return Profile containing validated information from user input
     */
    private Profile inputBreakdown(String[] inputs)
    {
        String name = inputs[Consts.SPLITTWO];
        String department = inputs[Consts.SPLITTHREE];
        String dateStr = inputs[Consts.SPLITFOUR];
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
        if (name.split(",").length != Consts.NAMES)
            throw new InputMismatchException("'" + name + "'"
                    + Consts.INVALID_NAME);
        if (!(dep.equals(Consts.CS)
                || dep.equals(Consts.ECE)
                || dep.equals(Consts.IT)))
            throw new InputMismatchException("'" + dep + "'"
                    + Consts.INVALID_DEP);
        if (!date.isValid())
            throw new InputMismatchException(date.toString()
                    + Consts.INVALID_DATE);
    }

    /**
     * Helper method to validate salary before adding a Fulltime/Management.
     * @param pay double salary amount to be validated (positive value)
     */
    private void validateSalary(double pay) throws InputMismatchException
    {
        if (Double.compare(pay, Consts.ZERO) < 0)
            throw new InputMismatchException(Consts.INVALID_SALARY);
    }

    /**
     * Helper method to validate hourly pay rate before adding a Parttime.
     * @param pay double payRate amount to be validated (positive value)
     */
    private void validatePayRate(double pay) throws InputMismatchException
    {
        if (Double.compare(pay, Consts.ZERO) < 0)
            throw new InputMismatchException(Consts.INVALID_PAYRATE);
    }

    /**
     * Helper method to validate code before adding a Management.
     * @param code double salary amount to be validated (1, 2, or 3)
     */
    private void validateCode(int code) throws InputMismatchException
    {
        if (!(code >= Consts.MA_CODE && code <= Consts.DI_CODE))
            throw new InputMismatchException(Consts.INVALID_MGMT);
    }

    /**
     * Helper method to validate salary before adding a Fulltime/Management.
     * @param hoursToSet int hoursWorked to be validated (positive value)
     */
    private void validateHours(int hoursToSet) throws InputMismatchException
    {
        if (hoursToSet < 0)
            throw new InputMismatchException(Consts.INVALID_HOURS_A);
        if (hoursToSet > Consts.PARTTIME_MAX)
            throw new InputMismatchException(Consts.INVALID_HOURS_B);
    }

    /**
     * Helper method to print given string.
     * Reduces the repetitions of "System.out.println" in PayrollProcessing.
     * @param str String literal to be printed
     */
    private void printout(String str) {
        System.out.println(str);
    }
}
