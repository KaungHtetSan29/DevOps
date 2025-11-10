package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    static App app;

    @BeforeAll
    static void init() {
        app = new App();
    }

    // 🧩 Test 1: Employee is null
    @Test
    void displayEmployee_NullEmployee() {
        String output = app.displayEmployeeString(null);
        assertEquals("No employee information provided.", output);
    }


    // 🧩 Test 2: Valid Employee with Department
    @Test
    void displayEmployee_ValidEmployee()
    {
        Department dept = new Department("D001", "Engineering", null);
        Employee emp = new Employee("E001", "Kevin Chalmers", dept, null);
        emp.setSalary(55000);

        app.displayEmployee(emp);
        // Expected: prints details for Kevin Chalmers, department Engineering
    }

    // 🧩 Test 3: Employee with Missing Department
    @Test
    void displayEmployee_NoDepartment()
    {
        Employee emp = new Employee("E002", "John Doe", null, null);
        emp.setSalary(42000);

        app.displayEmployee(emp);
        // Expected: prints "N/A" for department instead of error
    }
}
