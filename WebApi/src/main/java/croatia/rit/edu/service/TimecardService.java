package croatia.rit.edu.service;

import croatia.rit.edu.business.TimecardBusiness;
import companydata.Timecard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.Timestamp;
import java.util.List;

@Path("/")
public class TimecardService {

    private TimecardBusiness timecardBusiness;
    private Gson gson;

    public TimecardService() {
        this.timecardBusiness = new TimecardBusiness();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, new SqlTimestampAdapter())
                .create();
    }

    // GET method for retrieving a specific timecard by timecard_id
    @Path("/timecard")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimecard(@QueryParam("timecard_id") int timecardId) {
        try {
            Timecard timecard = timecardBusiness.getTimecard(timecardId);
            if (timecard != null) {
                return Response.ok(timecard).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Timecard not found.\"}")
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

    @Path("/timecards")
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

    // POST method for creating a new timecard
    @Path("/timecard")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertTimecard(@FormParam("emp_id") int empId,
                                   @FormParam("start_time") String startTime,
                                   @FormParam("end_time") String endTime) {
        try {
            Timestamp startTimestamp = Timestamp.valueOf(startTime);
            Timestamp endTimestamp = Timestamp.valueOf(endTime);

            Timecard newTimecard = new Timecard(startTimestamp, endTimestamp, empId);
            Timecard insertedTimecard = timecardBusiness.insertTimecard(newTimecard);
            if (insertedTimecard == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"" + "validation failed" + "\"}")
                .build();
            }

            return Response.ok("{\"success\":" + gson.toJson(insertedTimecard) + "}").build();
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

    // PUT method for updating a timecard
    @Path("/timecard")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTimecard(String json) {
        try {
            Timecard timecard = gson.fromJson(json, Timecard.class);
            Timecard updatedTimecard = timecardBusiness.updateTimecard(timecard);
            if (updatedTimecard == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"" + "validation failed" + "\"}")
                .build();
            }
            return Response.ok("{\"success\":" + gson.toJson(updatedTimecard) + "}").build();
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

    // DELETE method for deleting a timecard
    @Path("/timecard")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTimecard(@QueryParam("timecard_id") int timecardId) {
        try {
            int rowsDeleted = timecardBusiness.deleteTimecard(timecardId);
            if (rowsDeleted > 0) {
                return Response.ok("{\"success\":\"Timecard " + timecardId + " deleted.\"}").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Timecard not found.\"}")
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
