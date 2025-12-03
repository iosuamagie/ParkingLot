package org.example.parkinglot.servlets;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.parkinglot.common.CarDto;
import org.example.parkinglot.common.UserDto;
import org.example.parkinglot.ejb.CarsBean;
import org.example.parkinglot.ejb.UserBean;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "EditCar", value = "/EditCar")
public class EditCar extends HttpServlet {

    @Inject
    CarsBean carsBean;

    @Inject
    UserBean userBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Luăm lista de useri pentru dropdown
        List<UserDto> users = userBean.findAllUsers();
        request.setAttribute("users", users);

        // 2. Luăm ID-ul mașinii din URL (?id=...)
        Long carId = Long.parseLong(request.getParameter("id"));

        // 3. Găsim mașina pe care vrem să o edităm
        CarDto car = carsBean.findById(carId);
        request.setAttribute("car", car);

        // 4. Trimitem totul la pagina JSP
        request.getRequestDispatcher("/WEB-INF/pages/editCar.jsp").forward(request, response);
    }

    // În org.example.parkinglot.servlets.EditCar

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Preluăm datele din formular
        String licensePlate = request.getParameter("license_plate");
        String parkingSpot = request.getParameter("parking_spot");
        Long userId = Long.parseLong(request.getParameter("owner_id"));
        Long carId = Long.parseLong(request.getParameter("car_id")); // ID-ul din input-ul hidden

        // 2. Apelăm metoda creată mai sus în Bean
        carsBean.updateCar(carId, licensePlate, parkingSpot, userId);

        // 3. Redirectăm la pagina principală
        response.sendRedirect(request.getContextPath() + "/Cars");
    }
}