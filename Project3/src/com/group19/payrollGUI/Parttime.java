package com.group19.payrollGUI;

import static com.group19.payrollGUI.Consts.*;
/**
 * Object class which represents a part-time worker, is of type Employee.
 @author Sagnik Mukherjee, Michael Choe
 */
public class Parttime extends Employee
{
    //object fields
    private final double hourlyPay;
    private int hoursWorked;

    /**
     * Default constructor for Parttime.
     */
    public Parttime()
    {
        super();
        hourlyPay = ZERO;
        hoursWorked = DEFAULTHOURS;
    }

    /**
     * Parameterized constructor for Parttime.
     * @param p Profile which contains personal Parttime data
     */
    public Parttime(Profile p, double hp, int hw)
    {
        super(p, ZERO);
        hourlyPay = hp;
        //intentionally invalidate Parttime object if max hours exceeded
        if (hw > PARTTIME_MAX || hw < 0) {
            Profile invalid = new Profile();
            super.setProfile(invalid);
        }
        hoursWorked = hw;
    }

    /**
     * Getter for periodEarnings of this Fulltime object.
     * @return super.periodEarnings instance variable value
     */
    public double getPeriodEarnings() {
        return super.getPeriodEarnings();
    }

    /**
     * Setter for periodEarnings of this Fulltime object.
     * @param pe value to set super.periodEarnings to
     */
    public void setPeriodEarnings(double pe) {
        super.setPeriodEarnings(pe);
    }

    /**
     * Setting the value for the hours worked by this Parttime.
     * @param hw value to set this.hoursWorked to
     */
    public void setHoursWorked(int hw)
    {
        hoursWorked = hw;
    }

    /**
     * Getter for number of hours worked by this Parttime
     * @return int value of hours worked
     */
    public int getHoursWorked() {
        return hoursWorked;
    }

    /**
     * Returns a message formatting the contents of Parttime object.
     * @return String literal containing Parttime field values
     */
    @Override
    public String toString()
    {
        return super.toString() + PAYCHECK_MSG
            + df.format(getPeriodEarnings())
            + SEPARATOR + PARTTIME
            + HOURLYPAY_MSG + df.format(hourlyPay)
            + HOURS_MSG + hoursWorked;
    }

    /**
     * Compares all fields between two Parttime objects.
     * @param obj Object which must be of type Parttime
     * @return true if both objects have identical fields, false otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Parttime))
            return false;

        return super.equals(obj);
    }

    /**
     * Determines Parttime compensation based on data fields.
     */
    @Override
    public void calculatePayment()
    {
        int overtime = hoursWorked % FULLHOURS;
        int regularTime = hoursWorked - overtime;
        double overtimeRate = OVERTIME_RATE * hourlyPay;

        double pe = (hourlyPay * regularTime) + (overtimeRate * overtime);
        setPeriodEarnings(pe);
    }
}
