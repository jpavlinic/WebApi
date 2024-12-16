package croatia.rit.edu.service;

import croatia.rit.edu.business.DepartmentBusiness;
import companydata.Department;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/departments")
public class DepartmentsService {

    private DepartmentBusiness departmentBusiness = null;

    public DepartmentsService() {
        this.departmentBusiness = new DepartmentBusiness();
    }

    // GET method for retrieving all departments of a company
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDepartments(@QueryParam("company") String company) {
        try {
            List<Department> departments = departmentBusiness.getAllDepartments(company);
            if (departments != null && !departments.isEmpty()) {
                return Response.ok(departments).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No departments found for company '" + company + "'.\"}")
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
