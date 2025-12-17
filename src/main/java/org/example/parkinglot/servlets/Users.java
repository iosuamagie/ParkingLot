package org.example.parkinglot.servlets;

import jakarta.annotation.security.DeclareRoles;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.HttpMethodConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.parkinglot.common.UserDto;
import org.example.parkinglot.ejb.InvoiceBean;
import org.example.parkinglot.ejb.UserBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DeclareRoles({"READ_USERS", "WRITE_USERS"})
@ServletSecurity(
        value = @HttpConstraint(rolesAllowed = {"READ_USERS"}),
        httpMethodConstraints = {@HttpMethodConstraint(value = "POST", rolesAllowed = {"WRITE_USERS"})}
)

@WebServlet(name = "Users", value = "/Users")
public class Users extends HttpServlet {
    @Inject
    UserBean userBean;

    @Inject
    InvoiceBean invoiceBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<UserDto> users = userBean.findAllUsers();
        request.setAttribute("users", users);

        // Verificăm dacă avem ID-uri salvate în sesiune (în invoiceBean)
        if (!invoiceBean.getUserIds().isEmpty()) {
            // Căutăm numele utilizatorilor pe baza ID-urilor
            Collection<String> usernames = userBean.findUsernamesByUserIds(invoiceBean.getUserIds());
            // Trimitem lista de nume către JSP sub atributul "invoices"
            request.setAttribute("invoices", usernames);
        }
        request.getRequestDispatcher("/WEB-INF/pages/users.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Preluăm valorile checkbox-urilor bifate (numele "user_ids" vine din JSP)
        String[] userIdsAsString = request.getParameterValues("user_ids");

        if (userIdsAsString != null) {
            List<Long> userIds = new ArrayList<>();

            for (String userIdAsString : userIdsAsString) {
                userIds.add(Long.parseLong(userIdAsString));
            }
            // Adăugăm lista de ID-uri în InvoiceBean
            invoiceBean.getUserIds().addAll(userIds);
        }

        response.sendRedirect(request.getContextPath() + "/Users");
    }
}
