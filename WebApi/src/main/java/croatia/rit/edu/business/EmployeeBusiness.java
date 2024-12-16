package croatia.rit.edu.business;

import companydata.Employee;
import companydata.DataLayer;
import companydata.Department;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class EmployeeBusiness {

    private DataLayer dataLayer = null;

    public EmployeeBusiness() {
        this.dataLayer = new DataLayer("jp3447");
    }

    public Employee getEmployee(int empId) throws Exception {
        // Validate emp_id
        if (empId <= 0) {
            throw new IllegalArgumentException("Valid Employee ID must be provided.");
        }
        return dataLayer.getEmployee(empId);
    }

    public List<Employee> getAllEmployees(String company) throws Exception {
        if (company == null || company.isEmpty()) {
            throw new IllegalArgumentException("Company ID must be provided.");
        }
        return dataLayer.getAllEmployee(company);
    }

    public Employee insertEmployee(Employee employee) throws Exception {
        // Validate fields
        if (employee.getEmpName() == null || employee.getEmpName().isEmpty()) {
            throw new IllegalArgumentException("Employee name must be provided.");
        }
        if (employee.getEmpNo() == null || employee.getEmpNo().isEmpty()) {
            throw new IllegalArgumentException("Employee number must be provided.");
        }
        if (employee.getHireDate() == null) {
            throw new IllegalArgumentException("Hire date must be provided.");
        }
        if (employee.getDeptId() <= 0) {
            throw new IllegalArgumentException("Department ID must be valid.");
        }
        if (employee.getSalary() <= 0) {
            throw new IllegalArgumentException("Salary must be greater than 0.");
        }

        // Validate hire_date
        LocalDate hireDate = employee.getHireDate().toLocalDate();
        if (hireDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Hire date must be in the past or today.");
        }

        // Ensure hire_date is not a weekend
        DayOfWeek dayOfWeek = hireDate.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Hire date cannot be on a weekend.");
        }

        // Ensure dept_id exists
        if (!doesDepartmentExist(employee.getDeptId())) {
            throw new IllegalArgumentException("Department ID must exist.");
        }

        // Validate manager if provided
        if (employee.getMngId() != 0 && !doesManagerExist(employee.getMngId())) {
            throw new IllegalArgumentException("Manager ID must be valid.");
        }

        // Ensure emp_no is unique
        if (!isEmployeeNumberUnique(employee.getEmpNo())) {
            throw new IllegalArgumentException("Employee number must be unique.");
        }

        return dataLayer.insertEmployee(employee);
    }

    public Employee updateEmployee(Employee employee) throws Exception {
        // Validate fields
        if (employee.getEmpNo() == null || employee.getEmpNo().isEmpty()) {
            throw new IllegalArgumentException("Employee number must be provided.");
        }
        if (employee.getId() <= 0) {
            throw new IllegalArgumentException("Valid Employee ID must be provided.");
        }

        // Validate hire_date
        LocalDate hireDate = employee.getHireDate().toLocalDate();
        if (hireDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Hire date must be in the past or today.");
        }

        // Ensure hire_date is not a weekend
        DayOfWeek dayOfWeek = hireDate.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Hire date cannot be on a weekend.");
        }

        // Ensure dept_id exists
        if (!doesDepartmentExist(employee.getDeptId())) {
            throw new IllegalArgumentException("Department ID must exist.");
        }

        // Validate manager if provided
        if (employee.getMngId() != 0 && !doesManagerExist(employee.getMngId())) {
            throw new IllegalArgumentException("Manager ID must be valid.");
        }

        // Ensure emp_no is unique
        if (!isEmployeeNumberUnique(employee.getEmpNo())) {
            throw new IllegalArgumentException("Employee number must be unique.");
        }

        //emp_id exists
        Employee existingEmployee = getEmployee(employee.getId());
        if (existingEmployee == null) {
            throw new IllegalArgumentException("Employee does not exist.");
        }

        return dataLayer.updateEmployee(employee);
    }

    public int deleteEmployee(int empId) throws Exception {
        if (empId <= 0) {
            throw new IllegalArgumentException("Valid Employee ID must be provided.");
        }
        return dataLayer.deleteEmployee(empId);
    }

    public boolean doesDepartmentExist(int deptId) throws Exception {
        Department department = dataLayer.getDepartment("jp3447", deptId);
        return department != null;
    }


    public boolean doesManagerExist(int mngId) throws Exception {
        Employee manager = dataLayer.getEmployee(mngId);
        return manager != null;
    }

    public boolean isEmployeeNumberUnique(String empNo) throws Exception {
        List<Employee> employees = dataLayer.getAllEmployee("jp3447");
        for (Employee emp : employees) {
            if (emp.getEmpNo().equals(empNo)) {
                return false;
            }
        }
        return true;
    }
}
