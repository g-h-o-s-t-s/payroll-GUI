/**
 * Object class which represents those in management, is of type Employee.
 @author Sagnik Mukherjee, Michael Choe
 */
public class Management extends Fulltime
{
    //object fields
    private final double bonusPay;
    private final int code;

    /**
     * Default constructor for Management.
     */
    public Management()
    {
        super();
        bonusPay = Consts.MANAGER_BONUS;
        code = Consts.MA_CODE;
    }

    /**
     * Parameterized constructor for Management.
     * @param p Profile which contains personal Management data
     */
    public Management(Profile p, double s, int c)
    {
        super(p, s);
        code = c;
        if (code == Consts.MA_CODE)
            bonusPay = Consts.MANAGER_BONUS;
        else if (code == Consts.DH_CODE)
            bonusPay = Consts.DEPHEAD_BONUS;
        else if (code == Consts.DI_CODE)
            bonusPay = Consts.DIRECTOR_BONUS;
        else {
            Profile invalid = new Profile();
            super.setProfile(invalid);
            bonusPay = Consts.ZERO;
        }
    }

    /**
     * Getter for annualSalary of this Management object.
     * @return super.annualSalary instance variable value
     */
    public double getAnnualSalary() {
        return super.getAnnualSalary();
    }

    /**
     * Setter for annualSalary of this Management object.
     * @param s value to set super.annualSalary to
     */
    public void setAnnualSalary(double s) {
        super.setAnnualSalary(s);
    }

    /**
     * Getter for periodEarnings of this Management object.
     * @return super.periodEarnings instance variable value
     */
    public double getPeriodEarnings() {
        return super.getPeriodEarnings();
    }

    /**
     * Setter for periodEarnings of this Management object.
     * @param pe value to set super.periodEarnings to
     */
    public void setPeriodEarnings(double pe) {
        super.setPeriodEarnings(pe);
    }

    /**
     * Returns a message formatting the contents of Management object.
     * @return String literal containing Management field values
     */
    @Override
    public String toString()
    {
        if (code == Consts.MA_CODE)
            return super.toString()
                    + Consts.MANAGER_MSG + Consts.df.format(bonusPay);
        else if (code == Consts.DH_CODE)
            return super.toString()
                    + Consts.DH_MSG + Consts.df.format(bonusPay);
        else if (code == Consts.DI_CODE)
            return super.toString()
                    + Consts.DIRECTOR_MSG + Consts.df.format(bonusPay);

        return super.toString();
    }

    /**
     * Compares all fields between two Management objects.
     * @param obj Object which must be of type Management
     * @return true if both objects have identical fields, false otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Management))
            return false;

        return super.equals(obj);
    }

    /**
     * Determines Management compensation based on data fields.
     */
    @Override
    public void calculatePayment()
    {
        double pe = getAnnualSalary() / Consts.PAYPERIODS + bonusPay;
        setPeriodEarnings(pe);
    }
}
