/**
 * Object class which represents an employed individual.
 @author Sagnik Mukherjee, Michael Choe
 */
@SuppressWarnings("WeakerAccess")
public class Employee {
    //object fields
    private Profile profile;
    private double periodEarnings;

    /**
     * Default constructor for Employee.
     */
    public Employee()
    {
        profile = new Profile();
        periodEarnings = Consts.ZERO;
    }

    /**
     * Parameterized constructor for Employee.
     * @param p Profile which contains personal Employee data
     */
    public Employee(Profile p, double pe)
    {
        profile = p;
        periodEarnings = pe;
    }

    /**
     * Getter for Profile field of this Employee object.
     * @return Profile instance variable value
     */
    public Profile getProfile()
    {
        return profile;
    }

    /**
     * Setter for Profile field of this Employee object.
     * @param p Profile instance variable value to be set
     */
    public void setProfile(Profile p)
    {
        profile = p;
    }

    /**
     * Getter for periodEarnings of this Employee object.
     * @return periodEarnings instance variable value
     */
    public double getPeriodEarnings() {
        return periodEarnings;
    }

    /**
     * Setter for periodEarnings of this Employee object.
     * @param pe value to set periodEarnings to
     */
    public void setPeriodEarnings(double pe) {
        periodEarnings = pe;
    }

    /**
     * Determines Employee compensation, reused in subclasses.
     */
    public void calculatePayment()
    {
        periodEarnings = Consts.ZERO;
    }

    /**
     * Returns a message formatting the contents of Employee object.
     * @return String literal containing Employee field values
     */
    @Override
    public String toString()
    {
        return profile.toString();
    }

    /**
     * Compares all fields between two Employee objects.
     * @param obj Object which must be of type Employee
     * @return true if both objects have identical fields, false otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        //check if invoking object & param are of the same class
        if (!(obj instanceof Employee)) //null if obj is not of type Employee
            return false;

        Employee that = (Employee) obj;
        return profile.equals(that.profile)
                && this.periodEarnings == that.getPeriodEarnings();
    }
}
