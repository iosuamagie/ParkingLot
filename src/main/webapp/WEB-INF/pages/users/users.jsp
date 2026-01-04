<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:pageTemplate pageTitle="Users">
    <form method="POST" action="${pageContext.request.contextPath}/Users">

        <h1>Users</h1>

        <c:if test="${pageContext.request.isUserInRole('WRITE_USERS')}">
            <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/AddUser">Add User</a>
        </c:if>
        <c:if test="${pageContext.request.isUserInRole('EDIT_USERS')}">
            <a href="${pageContext.request.contextPath}/EditUser?id=${user.id}" class="btn btn-secondary">Edit</a>
        </c:if>

        <c:if test="${pageContext.request.isUserInRole('INVOICING')}">
            <button class="btn btn-secondary btn-lg" type="submit">Invoice</button>
        </c:if>


        <div class="container text-center mt-3">
            <div class="row fw-bold border-bottom pb-2">
                <c:if test="${pageContext.request.isUserInRole('INVOICING')}">
                    <div class="col-1">
                        Select
                    </div>
                </c:if>

                <div class="col">
                    Username
                </div>
                <div class="col">
                    Email
                </div>
            </div>

            <c:forEach var="user" items="${users}">
                <div class="row border-bottom py-2 align-items-center">

                    <c:if test="${pageContext.request.isUserInRole('INVOICING')}">
                        <div class="col-1">
                            <c:set var="isChecked" value="" />
                            <c:if test="${activeUserIds.contains(user.id)}">
                                <c:set var="isChecked" value="checked" />
                            </c:if>

                            <input type="checkbox" name="user_ids" value="${user.id}" ${isChecked} />
                        </div>
                    </c:if>

                    <div class="col">
                            ${user.username}
                    </div>
                    <div class="col">
                            ${user.email}
                    </div>
                </div>
            </c:forEach>
        </div>
    </form>

    <c:if test="${not empty invoices and pageContext.request.isUserInRole('INVOICING')}">
        <h2 class="mt-4">Invoices Selection</h2>
        <ul class="list-group">
            <c:forEach var="username" items="${invoices}" varStatus="status">
                <li class="list-group-item">
                        ${status.index + 1}. ${username}
                </li>
            </c:forEach>
        </ul>
    </c:if>

</t:pageTemplate>