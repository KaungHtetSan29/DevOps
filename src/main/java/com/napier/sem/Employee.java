package com.napier.sem;

public class Employee {
    private String emp_no;
    private String emp_name;
    private int salary;           // added salary
    private Employee manager;
    private Department dept;

    public Employee(String emp_no, String emp_name, Department dept, Employee manager) {
        this.emp_no = emp_no;
        this.emp_name = emp_name;
        this.dept = dept;
        this.manager = manager;
    }

    public String getEmp_no() {
        return emp_no;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Department getDept() {
        return dept;
    }

    public void setDept(Department dept) {
        this.dept = dept;
    }
}
