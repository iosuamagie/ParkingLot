package org.example.parkinglot.servlets.users;

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
import org.example.parkinglot.ejb.UserBean; // Asigură-te că numele clasei EJB e corect (UserBean sau UsersBean)

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DeclareRoles({"READ_USERS", "WRITE_USERS", "INVOICING"})
@ServletSecurity(
        value = @HttpConstraint(rolesAllowed = {"READ_USERS"}),
        httpMethodConstraints = {@HttpMethodConstraint(value = "POST", rolesAllowed = {"WRITE_USERS", "INVOICING"})}
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

        if (request.isUserInRole("INVOICING")) {
            // 1. Trimitem ID-urile selectate anterior pentru a bifa checkbox-urile
            request.setAttribute("activeUserIds", invoiceBean.getUserIds());

            // 2. Trimitem numele utilizatorilor selectați (cerința: "Do not set the attribute with the names...")
            if (!invoiceBean.getUserIds().isEmpty()) {
                Collection<String> usernames = userBean.findUsernamesByUserIds(invoiceBean.getUserIds());
                request.setAttribute("invoices", usernames);
            }
        }

        request.getRequestDispatcher("/WEB-INF/pages/users/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!request.isUserInRole("INVOICING")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String[] userIdsAsString = request.getParameterValues("user_ids");

        // Folosim un Set pentru a evita duplicatele și pentru că InvoiceBean folosește probabil Set<Long>
        Set<Long> userIds = new HashSet<>();

        if (userIdsAsString != null) {
            for (String userIdAsString : userIdsAsString) {
                userIds.add(Long.parseLong(userIdAsString));
            }
        }
        // Dacă userIdsAsString e null (niciun checkbox bifat), lista 'userIds' e goală,
        // iar setUserIds va goli selecția din sesiune (ceea ce e corect).
        invoiceBean.setUserIds(userIds);

        response.sendRedirect(request.getContextPath() + "/Users");
    }
}