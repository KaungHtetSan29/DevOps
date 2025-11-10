package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

/* Simple application to connect to a MySQL database and fetch employee info. */
public class App {
    private Connection con = null;

    /* Default connect using default host/db and 10s delay */
    public void connect() {
        connect("db:3306", 10000);
    }

    /* Connect with custom location and delay */
    public void connect(String location, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        boolean shouldWait = false;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                if (shouldWait) {
                    // Wait a bit for db to start
                    Thread.sleep(delay);
                }

                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());

                // Let's wait before attempting to reconnect
                shouldWait = true;
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /* Disconnect from the database */
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Disconnected from database");
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /* Get a Department object by name */
    public Department getDepartment(String dept_name) {
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT d.dept_no, d.dept_name, e.emp_no, CONCAT(e.first_name, ' ', e.last_name) AS emp_name " +
                            "FROM departments d " +
                            "LEFT JOIN dept_manager dm ON d.dept_no = dm.dept_no " +
                            "LEFT JOIN employees e ON dm.emp_no = e.emp_no " +
                            "WHERE d.dept_name = \"" + dept_name + "\" " +
                            "AND (dm.to_date = '9999-01-01' OR dm.to_date IS NULL)";

            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next()) {
                Department dept = new Department(
                        rset.getString("dept_no"),
                        rset.getString("dept_name"),
                        null
                );

                String managerId = rset.getString("emp_no");
                String managerName = rset.getString("emp_name");
                if (managerId != null) {
                    Employee manager = new Employee(managerId, managerName, dept, null);
                    dept.setManager(manager);
                }
                return dept;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get department details");
        }
        return null;
    }

    /* Get an Employee by ID */
    public Employee getEmployee(int ID) {
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT e.emp_no, CONCAT(e.first_name, ' ', e.last_name) AS emp_name, " +
                            "d.dept_no, d.dept_name, " +
                            "m.emp_no AS mgr_no, CONCAT(m.first_name, ' ', m.last_name) AS mgr_name " +
                            "FROM employees e " +
                            "LEFT JOIN dept_emp de ON e.emp_no = de.emp_no " +
                            "LEFT JOIN departments d ON de.dept_no = d.dept_no " +
                            "LEFT JOIN dept_manager dm ON d.dept_no = dm.dept_no " +
                            "LEFT JOIN employees m ON dm.emp_no = m.emp_no " +
                            "WHERE e.emp_no = " + ID + " " +
                            "AND (de.to_date = '9999-01-01' OR de.to_date IS NULL) " +
                            "AND (dm.to_date = '9999-01-01' OR dm.to_date IS NULL)";

            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next()) {
                Department dept = new Department(
                        rset.getString("dept_no"),
                        rset.getString("dept_name"),
                        null
                );

                Employee manager = null;
                String mgrId = rset.getString("mgr_no");
                String mgrName = rset.getString("mgr_name");
                if (mgrId != null) {
                    manager = new Employee(mgrId, mgrName, dept, null);
                    dept.setManager(manager);
                }

                return new Employee(
                        rset.getString("emp_no"),
                        rset.getString("emp_name"),
                        dept,
                        manager
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
        }
        return null;
    }

    /* Get an Employee by first and last name */
    public Employee getEmployee(String firstName, String lastName) {
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT e.emp_no, CONCAT(e.first_name, ' ', e.last_name) AS emp_name, " +
                            "d.dept_no, d.dept_name, " +
                            "m.emp_no AS mgr_no, CONCAT(m.first_name, ' ', m.last_name) AS mgr_name " +
                            "FROM employees e " +
                            "LEFT JOIN dept_emp de ON e.emp_no = de.emp_no " +
                            "LEFT JOIN departments d ON de.dept_no = d.dept_no " +
                            "LEFT JOIN dept_manager dm ON d.dept_no = dm.dept_no " +
                            "LEFT JOIN employees m ON dm.emp_no = m.emp_no " +
                            "WHERE e.first_name = \"" + firstName + "\" " +
                            "AND e.last_name = \"" + lastName + "\" " +
                            "AND (de.to_date = '9999-01-01' OR de.to_date IS NULL) " +
                            "AND (dm.to_date = '9999-01-01' OR dm.to_date IS NULL)";

            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next()) {
                Department dept = new Department(
                        rset.getString("dept_no"),
                        rset.getString("dept_name"),
                        null
                );

                Employee manager = null;
                String mgrId = rset.getString("mgr_no");
                String mgrName = rset.getString("mgr_name");
                if (mgrId != null) {
                    manager = new Employee(mgrId, mgrName, dept, null);
                    dept.setManager(manager);
                }

                return new Employee(
                        rset.getString("emp_no"),
                        rset.getString("emp_name"),
                        dept,
                        manager
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
        }
        return null;
    }

    /* Get all employees with salaries in a given department */
    public ArrayList<Employee> getSalariesByDepartment(Department dept) {
        if (dept == null) return null;

        ArrayList<Employee> employees = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT e.emp_no, CONCAT(e.first_name, ' ', e.last_name) AS emp_name, s.salary " +
                            "FROM employees e " +
                            "JOIN dept_emp de ON e.emp_no = de.emp_no " +
                            "JOIN salaries s ON e.emp_no = s.emp_no " +
                            "WHERE de.dept_no = '" + dept.getDept_no() + "' " +
                            "AND de.to_date = '9999-01-01' " +
                            "AND s.to_date = '9999-01-01' " +
                            "ORDER BY e.emp_no ASC";

            ResultSet rset = stmt.executeQuery(strSelect);
            while (rset.next()) {
                String empId = rset.getString("emp_no");
                String empName = rset.getString("emp_name");
                int salary = rset.getInt("salary");

                Employee emp = new Employee(empId, empName, dept, null);
                emp.setSalary(salary); // assuming Employee class has setSalary()
                employees.add(emp);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salaries by department");
        }
        return employees;
    }

    /* Print salaries nicely */
    public void printSalaries(ArrayList<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            System.out.println("No employees to display.");
            return;
        }

        System.out.println(String.format("%-10s %-25s %-20s %-10s", "Emp No", "Name", "Department", "Salary"));
        for (Employee emp : employees) {
            if (emp == null) continue;
            String deptName = (emp.getDept() != null) ? emp.getDept().getDept_name() : "N/A";
            String empString = String.format("%-10s %-25s %-20s %-10d",
                    emp.getEmp_no(),
                    emp.getEmp_name(),
                    deptName,
                    emp.getSalary());
            System.out.println(empString);
        }
    }

    public String displayEmployeeString(Employee emp) {
        if (emp == null) return "No employee information provided.";
        return String.format("ID: %s, Name: %s, Department: %s, Salary: %d",
                emp.getEmp_no(),
                emp.getEmp_name(),
                emp.getDept() != null ? emp.getDept().getDept_name() : "N/A",
                emp.getSalary());
    }



    public void displayEmployee(Employee emp) {
        System.out.println(displayEmployeeString(emp));
    }

    /* Main method with command-line args support */
    public static void main(String[] args) {
        App a = new App();

        if(args.length < 1){
            a.connect("localhost:33060", 30000);
        }else{
            a.connect(args[0], Integer.parseInt(args[1]));
        }

        Department dept = a.getDepartment("Development");
        ArrayList<Employee> employees = a.getSalariesByDepartment(dept);


        // Print salary report
        a.printSalaries(employees);

        // Disconnect from database
        a.disconnect();
    }
}
