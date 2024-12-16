package croatia.rit.edu.service;

import croatia.rit.edu.business.EmployeeBusiness;
import companydata.Employee;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/employees")
public class EmployeesService {

    private EmployeeBusiness employeeBusiness = null;

    public EmployeesService() {
        this.employeeBusiness = new EmployeeBusiness();
    }

    // GET method for retrieving all employees of a company
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
}
