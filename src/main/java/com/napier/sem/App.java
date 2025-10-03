package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

/* Simple application to connect to a MySQL database.
 */
public class App {
    /* Connection to MySQL database. */
    private Connection con = null;

    /* Connect to the MySQL database. */
    public void connect() {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(10000);
                // Connect to database
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

    /* Disconnect from the MySQL database. */
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
     * Gets all the current employees and salaries.
     * @return A list of all employees and salaries, or null if there is an error.
     */
    public ArrayList<Employee> getAllSalaries() {
        try {
            Statement stmt = con.createStatement();
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries "
                            + "WHERE employees.emp_no = salaries.emp_no AND salaries.to_date = '9999-01-01' "
                            + "ORDER BY employees.emp_no ASC";

            ResultSet rset = stmt.executeQuery(strSelect);
            ArrayList<Employee> employees = new ArrayList<>();

            while (rset.next()) {
                String empId = rset.getString("employees.emp_no");
                String empName = rset.getString("employees.first_name") + " " + rset.getString("employees.last_name");

                Employee emp = new Employee(empId, empName, null, null);
                // Salary could be added to Employee if your class has a salary field.
                employees.add(emp);
            }
            return employees;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    /**
     * Get a Department object by its name.
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
     * Get an Employee object by employee number.
     */
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
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /**
     * Get an Employee object by first and last name.
     */
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
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /**
     * Print employees with simple formatting.
     */
    public void printSalaries(ArrayList<Employee> employees) {
        System.out.println(String.format("%-10s %-25s %-15s", "Emp No", "Name", "Department"));
        for (Employee emp : employees) {
            String deptName = (emp.getDept() != null) ? emp.getDept().getDept_name() : "N/A";
            System.out.println(String.format("%-10s %-25s %-15s",
                    emp.getEmp_no(),
                    emp.getEmp_name(),
                    deptName));
        }
    }

    public static void main(String[] args) {
        App a = new App();
        a.connect();

        // --- Test getDepartment ---
        Department sales = a.getDepartment("Sales");
        if (sales != null) {
            System.out.println("Department: " + sales.getDept_name());
            if (sales.getManager() != null) {
                System.out.println("Manager: " + sales.getManager().getEmp_name());
            }
        }

        // --- Test getEmployee by ID ---
        Employee e1 = a.getEmployee(10001);
        if (e1 != null) {
            System.out.println("Employee by ID: " + e1.getEmp_name()
                    + " Dept: " + (e1.getDept() != null ? e1.getDept().getDept_name() : "N/A")
                    + " Manager: " + (e1.getManager() != null ? e1.getManager().getEmp_name() : "N/A"));
        }

        // --- Test getEmployee by Name ---
        Employee e2 = a.getEmployee("Georgi", "Facello");
        if (e2 != null) {
            System.out.println("Employee by Name: " + e2.getEmp_name()
                    + " Dept: " + (e2.getDept() != null ? e2.getDept().getDept_name() : "N/A")
                    + " Manager: " + (e2.getManager() != null ? e2.getManager().getEmp_name() : "N/A"));
        }

        a.disconnect();
    }
}
