package org.example.parkinglot.servlets.users;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.parkinglot.ejb.UserBean;

import java.io.IOException;
import java.util.Arrays;

@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"WRITE_USERS"}))
@WebServlet(name = "AddUser", value = "/AddUser")
public class AddUser extends HttpServlet {

    @Inject
    UserBean userBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Trimitem lista de grupuri posibile către JSP pentru a popula checkbox-urile
        request.setAttribute("userGroups", new String[] {"READ_CARS", "WRITE_CARS", "READ_USERS", "WRITE_USERS"});

        request.getRequestDispatcher("/WEB-INF/pages/users/addUser.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String[] userGroups = request.getParameterValues("user_groups");

        // Dacă nu s-a selectat niciun grup, inițializăm array-ul gol pentru a evita NullPointerException
        if (userGroups == null) {
            userGroups = new String[0];
        }

        // Apelăm metoda de creare (aceasta va fi roșie până o implementezi în UserBean)
        userBean.createUser(username, email, password, Arrays.asList(userGroups));

        response.sendRedirect(request.getContextPath() + "/Users");
    }
}