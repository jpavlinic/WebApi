package croatia.rit.edu.service;

import croatia.rit.edu.business.TimecardBusiness;
import companydata.Timecard;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/timecards")
public class TimecardsService {

    private TimecardBusiness timecardBusiness;

    public TimecardsService() {
        this.timecardBusiness = new TimecardBusiness();
    }

    // GET method for retrieving all timecards for a specific employee
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTimecards(@QueryParam("emp_id") int empId) {
        try {
            List<Timecard> timecards = timecardBusiness.getAllTimecards(empId);
            if (timecards != null && !timecards.isEmpty()) {
                return Response.ok(timecards).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No timecards found for the employee.\"}")
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
