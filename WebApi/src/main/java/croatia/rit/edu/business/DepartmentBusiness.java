package croatia.rit.edu.business;

import companydata.Department;
import companydata.Employee;
import companydata.DataLayer;

import java.util.List;

public class DepartmentBusiness {

    private DataLayer dataLayer = null;

    public DepartmentBusiness() {
        this.dataLayer = new DataLayer("jp3447");
    }

    // Get a department by ID
    public Department getDepartment(String company, int deptId) throws Exception {
        // Validate department ID and company
        if (company == null || company.isEmpty()) {
            throw new IllegalArgumentException("Company ID must be provided.");
        }
        if (deptId <= 0) {
            throw new IllegalArgumentException("Department ID must be a positive integer.");
        }
        return dataLayer.getDepartment(company, deptId);
    }

    // Get all departments for a company
    public List<Department> getAllDepartments(String company) throws Exception {
        //validate input
        if (company == null || company.isEmpty()) {
            throw new IllegalArgumentException("Company ID must be provided.");
        }
        return dataLayer.getAllDepartment(company);
    }

    // Update an existing department
    public Department updateDepartment(Department department) throws Exception {
        // Validate inputs
        if (department.getCompany() == null || department.getCompany().isEmpty()) {
            throw new IllegalArgumentException("Company ID must be provided.");
        }
        if (department.getDeptNo() == null || department.getDeptNo().isEmpty()) {
            throw new IllegalArgumentException("Department number must be provided.");
        }
        if (department.getId() <= 0) {
            throw new IllegalArgumentException("Valid Department ID must be provided.");
        }

        // dept_no must unique
        List<Department> allDepartments = getAllDepartments(department.getCompany());
        for (Department existing : allDepartments) {
            if (existing.getDeptNo().equals(department.getDeptNo())) {
                throw new IllegalArgumentException("dept_no must be unique among all companies");
            }
        }

        // dept_id exists
        Department existingDepartment = getDepartment(department.getCompany(), department.getId());
        if (existingDepartment == null) {
            throw new IllegalArgumentException("dept_id must be an existing record number for a department");
        }

        // Update department
        dataLayer.updateDepartment(department);
        return department;
    }

    // Insert a new department
    public Department insertDepartment(Department department) throws Exception {
        // Validate inputs
        if (department.getCompany() == null || department.getCompany().isEmpty()) {
            throw new IllegalArgumentException("Company ID must be provided.");
        }
        if (department.getDeptNo() == null || department.getDeptNo().isEmpty()) {
            throw new IllegalArgumentException("Department number must be provided.");
        }

        // dept_no is unique 
        List<Department> allDepartments = getAllDepartments(department.getCompany());
        for (Department existing : allDepartments) {
            if (existing.getDeptNo().equals(department.getDeptNo())) {
                throw new IllegalArgumentException("dept_no must be unique among all companies");
            }
        }

        // Insert department
        return dataLayer.insertDepartment(department);
    }

    // Delete a department
    public int deleteDepartment(String company, int deptId) throws Exception {
        // Validate company and department ID
        if (company == null || company.isEmpty()) {
            throw new IllegalArgumentException("Company ID must be provided.");
        }
        if (deptId <= 0) {
            throw new IllegalArgumentException("Valid Department ID must be provided.");
        }

        //check if there are associated employees
        List<Employee> employees = dataLayer.getAllEmployee(company);
        boolean hasEmployees = false;
        for (Employee emp : employees) {
            if (emp.getDeptId() == deptId) {
                hasEmployees = true;
                break;
            }
        }

        if (hasEmployees) {
            throw new IllegalArgumentException("Cannot delete department with associated employees.");
        }

        // Delete department
        return dataLayer.deleteDepartment(company, deptId);
    }
}
