package com.group19.payrollGUI;

import static com.group19.payrollGUI.Consts.*;
/**
 * Object class which contains company-relevant information of an employee.
 @author Sagnik Mukherjee, Michael Choe
 */
@SuppressWarnings("FieldMayBeFinal")
public class Profile
{
    //object fields
    private String name; //employee's name in the form "lastname,firstname"
    private String department; //department code: CS, ECE, IT
    private Date dateHired;

    /**
     * Default constructor for Profile.
     */
    public Profile()
    {
        name = "";
        department = "";
        dateHired = new Date();
    }

    /**
     * Parameterized constructor for Profile.
     * @param n String which contains Employee name
     * @param d String which contains Employee's assigned department
     * @param dh Date representing the day Employee was hired
     */
    public Profile(String n, String d, Date dh)
    {
        name = n;
        department = d;
        dateHired = dh;
    }

    /**
     * Verifies that Profile contains valid fields.
     * String name must have two substrings and a comma, String department
     * must be "CS", "ECE", or "IT", and dateHired must be a valid Date.
     * @return true if Profile object contains valid fields, false otherwise
     */
    public boolean isValid()
    {
        return name.split(",").length == NAMES
                && (department.equals(CS)
                    || department.equals(ECE)
                    || department.equals(IT))
                && dateHired.isValid();
    }

    /**
     * Getter for name of this Employee object.
     * @return String name instance variable value
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for department title of this Employee object.
     * @return String department instance variable value
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Getter for Date of this Employee object.
     * @return Date dateHired instance variable value
     */
    public Date getDateHired() {
        return dateHired;
    }

    /**
     * Returns a message formatting the contents of Profile object.
     * @return String literal containing Profile field values
     */
    @Override
    public String toString()
    {
        return name + SEPARATOR + department
                + SEPARATOR + dateHired;
    }

    /**
     * Compares all three fields between two Profile objects.
     * @param obj Object which must be of type Profile
     * @return true if both objects have identical fields, false otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        //check if invoking object & param are of the same class
        if (!(obj instanceof Profile))
            return false;

        Profile that = (Profile) obj;
        return this.name.equals(that.name)
                && this.department.equals(that.department)
                && this.dateHired.compareTo(that.dateHired) == ZERO;
    }
}
