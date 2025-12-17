package org.example.parkinglot.servlets;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.parkinglot.common.CarPhotoDto;
import org.example.parkinglot.ejb.CarsBean;

import java.io.IOException;

@WebServlet(name = "CarPhotos", value = "/CarPhotos")
public class CarPhotos extends HttpServlet {

    @Inject
    CarsBean carsBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // În imagine apare Integer, dar dacă ID-urile tale sunt Long, folosește Long.parseLong
        Integer carId = Integer.parseInt(request.getParameter("id"));

        CarPhotoDto photo = carsBean.findPhotoByCarId(Long.valueOf(carId));

        if (photo != null) {
            // Setează tipul fișierului (ex: image/png, image/jpeg)
            response.setContentType(photo.getFileType());

            // Setează lungimea conținutului
            response.setContentLength(photo.getFileContent().length);

            // Scrie bytes-ii imaginii direct în răspuns
            response.getOutputStream().write(photo.getFileContent());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // Error 404
        }
    }
}