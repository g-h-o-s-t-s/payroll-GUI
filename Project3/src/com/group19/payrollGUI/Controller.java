package com.group19.payrollGUI;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.lang.NumberFormatException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

/**
 * Client class, creates GUI object to handle user interaction.
 * @author Sagnik Mukherjee, Michael Choe
 */
@SuppressWarnings("WeakerAccess")
public class Controller
{
    public Label statusMessage;

    public void sayAddPartTime(ActionEvent actionEvent) {
        statusMessage.setText(Consts.ADDEDPT);
    }

    public void sayAddFullTime(ActionEvent actionEvent) {
        statusMessage.setText(Consts.ADDEDFT);
    }

    public void sayAddFullRole(ActionEvent actionEvent) {
        statusMessage.setText(Consts.ADDEDMA);
    }

    public void sayRemove(ActionEvent actionEvent) {
        statusMessage.setText(Consts.REMOVED);
    }

    public void sayCalculate(ActionEvent actionEvent) {
        statusMessage.setText(Consts.CALCULATED);
    }

    public void saySetHours(ActionEvent actionEvent) {
        statusMessage.setText(Consts.SETHOURS);
    }
}
