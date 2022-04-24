package BusinessLayer;

import java.time.LocalDateTime;

public class Employee
{
    private int id;
    private String name;
    private BusinessLayer.BankAccountDetails bankAccountDetails;
    private double salary;
    private LocalDateTime workStartingDate;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bankAccountDetails=" + bankAccountDetails +
                ", salary=" + salary +
                ", workStartingDate=" + workStartingDate +
                ", workingConditions=" + workingConditions +
                '}';
    }

    private WorkingConditions workingConditions;

    protected Employee(int id, String name, BankAccountDetails bankAccountDetails, double salary, LocalDateTime workStartingDate, WorkingConditions workingConditions)
    {
        this.id = id;
        this.name = name;
        this.bankAccountDetails = bankAccountDetails;
        this.salary = salary;
        this.workStartingDate = workStartingDate;
        this.workingConditions = workingConditions;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public BankAccountDetails getBankAccountDetails()
    {
        return bankAccountDetails;
    }

    public double getSalary()
    {
        return salary;
    }

    public LocalDateTime getWorkStartingDate()
    {
        return workStartingDate;
    }

    public WorkingConditions getWorkingConditions()
    {
        return workingConditions;
    }

    protected void setId(int id)
    {
        this.id = id;
    }

    protected void setName(String name)
    {
        this.name = name;
    }

    protected void setBankAccountDetails(BankAccountDetails bankAccountDetails)
    {
        this.bankAccountDetails = bankAccountDetails;
    }

    protected void setSalary(double salary)
    {
        this.salary = salary;
    }

    protected void setWorkStartingDate(LocalDateTime workStartingDate)
    {
        this.workStartingDate = workStartingDate;
    }
}
