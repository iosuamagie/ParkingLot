package org.example.parkinglot.servlets.users;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.parkinglot.common.UserDto;
import org.example.parkinglot.ejb.UserBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"WRITE_USERS"}))
@WebServlet(name = "EditUser", value = "/EditUser")
public class EditUser extends HttpServlet {

    @Inject
    UserBean userBean;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Luăm ID-ul din URL
        Long userId = Long.parseLong(request.getParameter("id"));

        // 2. Găsim user-ul ca să pre-completăm formularul
        UserDto user = userBean.findById(userId);
        request.setAttribute("user", user);

        // 3. Trimitem lista de grupuri posibile

        request.getRequestDispatcher("/WEB-INF/pages/users/editUser.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId = Long.parseLong(request.getParameter("user_id"));
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String[] groups = request.getParameterValues("user_groups");

        userBean.updateUser(userId, username, email, password, groups != null ? Arrays.asList(groups) : List.of());

        response.sendRedirect(request.getContextPath() + "/Users");
    }
}