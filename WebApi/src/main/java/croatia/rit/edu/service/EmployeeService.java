package croatia.rit.edu.service;

import croatia.rit.edu.business.EmployeeBusiness;
import companydata.Employee;

import java.sql.Date;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("/")
public class EmployeeService {

    private EmployeeBusiness employeeBusiness = null;
    private Gson gson = null;

    public EmployeeService() {
        this.employeeBusiness = new EmployeeBusiness();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(java.sql.Date.class, new SqlDateAdapter())
                .create();
    }

    // GET method for retrieving a specific employee by emp_id
    @Path("/employee")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployee(@QueryParam("emp_id") int empId) {
        try {
            Employee employee = employeeBusiness.getEmployee(empId);
            if (employee != null) {
                return Response.ok(employee).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Employee not found.\"}")
                        .build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @Path("/employees")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEmployees(@QueryParam("company") String company) {
        try {
            List<Employee> employees = employeeBusiness.getAllEmployees(company);
            if (employees != null && !employees.isEmpty()) {
                return Response.ok(employees).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No employees found for the company '" + company + "'.\"}")
                        .build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    // POST method for creating a new employee
    @Path("/employee")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertEmployee(@FormParam("emp_name") String empName,
                                   @FormParam("emp_no") String empNo,
                                   @FormParam("hire_date") String hireDate,
                                   @FormParam("job") String job,
                                   @FormParam("salary") double salary,
                                   @FormParam("dept_id") int deptId,
                                   @FormParam("mng_id") int mngId) {
        try {
            Employee newEmployee = new Employee(empName, empNo, Date.valueOf(hireDate), job, salary, deptId, mngId);
            Employee insertedEmployee = employeeBusiness.insertEmployee(newEmployee);
            if (insertedEmployee == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"" + "validation failed" + "\"}")
                .build();
            }
            return Response.ok("{\"success\":" + gson.toJson(insertedEmployee) + "}").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    // PUT method for updating an employee
    @Path("/employee")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEmployee(String json) {
        try {
            Employee employee = gson.fromJson(json, Employee.class);
            Employee updatedEmployee = employeeBusiness.updateEmployee(employee);
            if (updatedEmployee == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"" + "validation failed" + "\"}")
                .build();
            }
            return Response.ok("{\"success\":" + gson.toJson(updatedEmployee) + "}").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    // DELETE method for deleting an employee
    @Path("/employee")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEmployee(@QueryParam("emp_id") int empId) {
        try {
            int rowsDeleted = employeeBusiness.deleteEmployee(empId);
            if (rowsDeleted > 0) {
                return Response.ok("{\"success\":\"Employee " + empId + " deleted.\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Employee not found.\"}")
                        .build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
