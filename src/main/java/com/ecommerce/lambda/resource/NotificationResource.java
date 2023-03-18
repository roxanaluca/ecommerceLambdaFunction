package com.ecommerce.lambda.resource;

import com.ecommerce.lambda.controller.NotificationController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NotificationResource {

    @Inject
    NotificationController notificationController;
    @GET
    @Path("/notification/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProblem(@Context HttpServletRequest context, @PathParam("id") String messageId) {
        try {
            notificationController.sendMessage(messageId);
            return Response.status(200).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }
}
