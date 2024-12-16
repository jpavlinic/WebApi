package croatia.rit.edu.service;

import croatia.rit.edu.business.DepartmentBusiness;
import companydata.Department;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/")
public class DepartmentService {

    private DepartmentBusiness departmentBusiness = null;
    private Gson gson = null;

    public DepartmentService() {
        this.departmentBusiness = new DepartmentBusiness();
        this.gson = new Gson();
    }

    // GET method for retrieving a specific department by dept_id
    @Path("/department")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartment(@QueryParam("company") String company, @QueryParam("dept_id") int deptId) {
        try {
            Department department = departmentBusiness.getDepartment(company, deptId);
            if (department != null) {
                return Response.ok(department).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Department not found.\"}")
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

    @Path("/departments")
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

    // PUT method for updating a department
    @Path("/department")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDepartment(String json) {
        try {
            Department department = gson.fromJson(json, Department.class);
            Department updatedDepartment = departmentBusiness.updateDepartment(department);
            if (updatedDepartment == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"" + "validation failed" + "\"}")
                        .build();
            }
            return Response.ok("{\"success\":" + gson.toJson(updatedDepartment) + "}").build();
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

    // POST method for creating a new department
    @Path("/department")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertDepartment(@FormParam("company") String company, @FormParam("dept_name") String deptName,
            @FormParam("dept_no") String deptNo, @FormParam("location") String location) {
        try {
            Department newDepartment = new Department(company, deptName, deptNo, location);
            Department insertedDepartment = departmentBusiness.insertDepartment(newDepartment);
            if (insertedDepartment == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"" + "validation failed" + "\"}")
                        .build();
            }
            return Response.ok("{\"success\":" + gson.toJson(insertedDepartment) + "}").build();
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

    // DELETE method for deleting a department
    @Path("/department")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDepartment(@QueryParam("company") String company, @QueryParam("dept_id") int deptId) {
        try {
            int rowsDeleted = departmentBusiness.deleteDepartment(company, deptId);
            if (rowsDeleted > 0) {
                return Response.ok("{\"success\":\"Department " + deptId + " from " + company + " deleted.\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Department not found.\"}")
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
