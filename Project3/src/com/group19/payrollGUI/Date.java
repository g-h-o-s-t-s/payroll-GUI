package com.group19.payrollGUI;

import java.util.Calendar;
/**
 * The Date class contains fields/methods for an object of type Date.
 * Three fields make up the month, day, and year of a Date.
 * Methods included to check if a Date is valid (i.e., is not
 * earlier than 1900, and is not in the future).
 * Testbed main used to ensure isValid() is exhaustive as possible.
 @author Sagnik Mukherjee, Michael Choe
 */
@SuppressWarnings("WeakerAccess")
public class Date implements Comparable<Date>
{
    //object fields
    private int month;
    private int day;
    private int year;

    /**
     * Parameterized constructor.
     * Splits passed string and updates Date fields accordingly.
     * @param date String literal used to update Date field values
     */
    public Date(String date) //taking mm/dd/yyyy and create a Date object
    {
        String[] fields = date.split("/");
        try
        {
            month = Integer.parseInt(fields[0]);
            day = Integer.parseInt(fields[1]);
            year = Integer.parseInt(fields[2]);
        } catch (NumberFormatException ex)
        {
            //for inputs such as "3/ /2013" intentionally return invalid Date
            month = Consts.INVALID;
            day = Consts.INVALID;
            year = Consts.INVALID;
        }
    }

    /**
     * Default constructor.
     * Uses Calendar class to get current date, and
     * assigns values to fields accordingly.
     */
    public Date() {
        Calendar current = Calendar.getInstance();

        //months start from 0 in Calendar class, so increment by 1
        month = current.get(Calendar.MONTH) + 1;
        day = current.get(Calendar.DAY_OF_MONTH);
        year = current.get(Calendar.YEAR);
    }

    /**
     * Determines if Date contents follow the Gregorian Calendar.
     * The year may not be less than 1900, and the day must be
     * less than or equal to 28, 29, 30, or 31, depending on the month and
     * whether or not the Date contains a leap year, to be valid.
     * If the date could not be found on a printed calendar for
     * the given year, or is in the future, it is not a valid date.
     * @return true if date is valid, false otherwise
     */
    public boolean isValid() {
        //four base-cases: month/day/year out of bounds,
        //or the date is in the future
        if (this.year < Consts.YEARMIN)
            return false;

        if (this.month <= 0 || this.month > Calendar.DECEMBER + 1)
            return false;

        if (this.day <= 0 || this.day > Consts.DEFAULTMAXDAYS)
            return false;

        if (isInFuture(this.month, this.day, this.year))
            return false;

        //finally, if day is valid, overall Date is valid
        boolean isLeapYear = isLeap();
        return isDayValid(isLeapYear);
    }

    /**
     * Helper method for isValid(), checks if date is in the future.
     * If the date passed has not occurred yet (i.e., is greater
     * than the current date on the Gregorian Calendar),
     * this method returns true.
     * @param month int value for this.month
     * @param day int value for this.day
     * @param year int value for this.year
     * @return true if date is in the future, false otherwise
     */
    private boolean isInFuture(int month, int day, int year)
    {
        Date curr = new Date();

        return ((year > curr.year)
                || (year == curr.year && month > curr.month)
                || (year == curr.year && month == curr.month
                && day > curr.day));
    }

    /**
     * Helper method for isValid(), checks if this.year is leap year.
     * If the year is only divisible by 4, it is a leap year.
     * If it is divisible by 4, 100, and 400, it is a leap year.
     * By assuming it is not a leap year to begin with,
     * we can skip two else-cases.
     * @return true if leap year, false otherwise
     */
    private boolean isLeap() {
        boolean result = false;
        if (year % Consts.QUADRENNIAL == 0)
        {
            if (year % Consts.CENTURY == 0)
            {
                if (year % Consts.QUARTERCENTENNIAL == 0)
                    result = true;
                //else false
            }
            else
                result = true;
        }
        //else false
        return result;
    }

    /**
     * Helper method for isValid(), checks if this.day is valid.
     * Most months on the Gregorian calendar have either 30 or 31 days.
     * February is the exception, having 28 days, or 29 in a leap year.
     * Thus, we use a switch-case structure to validate if this.day
     * fits within the constraints, which depend on the month.
     * @param isLeapYear boolean from isLeap() method call in isValid()
     * @return true if day valid, false otherwise
     */
    private boolean isDayValid(boolean isLeapYear)
    {
        switch (this.month)
        {
            case (Calendar.APRIL + 1):
            case (Calendar.JUNE + 1):
            case (Calendar.SEPTEMBER + 1):
            case (Calendar.NOVEMBER + 1):
                if (this.day > Consts.ALTMAXDAYS) //months with 30 days
                    return false;
                break;

            case (Calendar.FEBRUARY + 1):
                if ((isLeapYear && this.day > Consts.FEBDAYSLEAP) //>29
                        || (!isLeapYear && this.day > Consts.FEBDAYS)) //>28
                    return false;
                break;

            default: //months with 31 days
                if (this.day > Consts.DEFAULTMAXDAYS)
                    return false;
                break;
        }
        return true;
    }

    /**
     * Compare passed Date (that) with invoking Date object (this).
     * @param that Date to be compared to by invoking object
     * @return -1 if this is less, 1 if this is greater, 0 if equal
     */
    @Override
    public int compareTo(Date that)
    {
        if ((this.year < that.year)
                || (this.year == that.year && this.month < that.month)
                || (this.year == that.year && this.month == that.month
                && this.day < that.day))
            return -1;
        else if ((this.year > that.year)
                || (this.month > that.month)
                || (this.day > that.day))
            return 1;

        return 0;
    }

    /**
     * Returns Date contents as string literal.
     * @return String literal of Book field values
     */
    @Override
    public String toString() {
        return String.format("%d/%d/%d", this.month,
                this.day, this.year);
    }
}
