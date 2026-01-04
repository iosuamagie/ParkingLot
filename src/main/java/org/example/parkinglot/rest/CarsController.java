package org.example.parkinglot.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam; // <-- Asigură-te că ai acest import
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.example.parkinglot.common.CarDto;
import org.example.parkinglot.ejb.CarsBean;

import java.util.List;

@Path("/cars")
@RolesAllowed("READ_CARS")
public class CarsController {

    @Inject
    CarsBean carsBean;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CarDto> findAll() {
        return carsBean.findAllCars();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CarDto findById(@PathParam("id") Long id) {
        return carsBean.findById(id);
    }
}