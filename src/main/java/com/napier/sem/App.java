package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class App {
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                Thread.sleep(10000); // Wait for DB to start
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/employees?useSSL=false&allowPublicKeyRetrieval=true",
                        "root",
                        "example"
                );
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
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

    /**
     * Gets details of a department by its name, including its manager.
     * @param dept_name The department name
     * @return Department object or null if not found
     */
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
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get department details");
            return null;
        }
    }

    /**
     * Gets all employees (with salaries) belonging to a department.
     * @param dept Department object
     * @return List of Employees
     */
    public ArrayList<Employee> getSalariesByDepartment(Department dept) {
        if (dept == null) {
            System.out.println("Department is null, cannot get salaries.");
            return null;
        }

        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT e.emp_no, e.first_name, e.last_name, s.salary " +
                            "FROM employees e, salaries s, dept_emp de, departments d " +
                            "WHERE e.emp_no = s.emp_no " +
                            "AND e.emp_no = de.emp_no " +
                            "AND de.dept_no = d.dept_no " +
                            "AND s.to_date = '9999-01-01' " +
                            "AND d.dept_no = \"" + dept.getDept_no() + "\" " +
                            "ORDER BY e.emp_no ASC";

            ResultSet rset = stmt.executeQuery(strSelect);
            ArrayList<Employee> employees = new ArrayList<>();

            while (rset.next()) {
                Employee emp = new Employee(
                        rset.getString("emp_no"),
                        rset.getString("first_name") + " " + rset.getString("last_name"),
                        dept,
                        null
                );
                emp.setSalary(rset.getInt("salary"));
                employees.add(emp);
            }
            return employees;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salaries by department");
            return null;
        }
    }

    /**
     * Prints employee salary details in tabular format.
     */
    public void printSalaries(ArrayList<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            System.out.println("No employees to display.");
            return;
        }
        System.out.println(String.format("%-10s %-20s %-15s %-10s",
                "Emp No", "Name", "Department", "Salary"));
        for (Employee emp : employees) {
            String emp_string =
                    String.format("%-10s %-20s %-15s %-10d",
                            emp.getEmp_no(),
                            emp.getEmp_name(),
                            emp.getDept() != null ? emp.getDept().getDept_name() : "N/A",
                            emp.getSalary());
            System.out.println(emp_string);
        }
    }

    /**
     * Main entry point for testing features.
     */
    public static void main(String[] args) {
        App a = new App();
        a.connect();

        // Get Sales department
        Department sales = a.getDepartment("Sales");
        if (sales != null) {
            System.out.println("Department: " + sales.getDept_name());
            if (sales.getManager() != null) {
                System.out.println("Manager: " + sales.getManager().getEmp_name());
            }

            // Get salaries by department
            ArrayList<Employee> employees = a.getSalariesByDepartment(sales);
            a.printSalaries(employees);
            System.out.println("Total employees in Sales: " + employees.size());
        } else {
            System.out.println("Department not found.");
        }

        a.disconnect();
    }
}
