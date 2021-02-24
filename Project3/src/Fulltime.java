/**
 * Object class which represents a full-time worker, is of type Fulltime.
 @author Sagnik Mukherjee, Michael Choe
 */
public class Fulltime extends Employee
{
    // object fields
    private double annualSalary;

    /**
     * Default constructor for Fulltime.
     */
    public Fulltime()
    {
        super();
        annualSalary = Consts.ZERO;
    }

    /**
     * Parameterized constructor for Fulltime.
     * @param p Profile which contains personal Fulltime data
     */
    public Fulltime(Profile p, double s)
    {
        super(p, Consts.ZERO);
        annualSalary = s;
    }

    /**
     * Getter for annualSalary of this Fulltime object.
     * @return annualSalary instance variable value
     */
    public double getAnnualSalary() {
        return annualSalary;
    }

    /**
     * Setter for annualSalary of this Fulltime object.
     * @param s value to set annualSalary to
     */
    public void setAnnualSalary(double s) {
        annualSalary = s;
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
     * Returns a message formatting the contents of Fulltime object.
     * @return String literal containing Fulltime field values
     */
    @Override
    public String toString()
    {
        return super.toString()+ Consts.PAYCHECK_MSG
                + Consts.df.format(getPeriodEarnings())
                + Consts.SEPARATOR + Consts.FULLTIME
                + Consts.SALARY_MSG + Consts.df.format(annualSalary);
    }

    /**
     * Compares all fields between two Fulltime objects.
     * @param obj Object which must be of type Fulltime
     * @return true if both objects have identical fields, false otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Fulltime))
            return false;

        return super.equals(obj);
    }

    /**
     * Determines Fulltime compensation based on data fields.
     */
    @Override
    public void calculatePayment()
    {
        double pe = annualSalary / Consts.PAYPERIODS;
        setPeriodEarnings(pe);
    }
}
