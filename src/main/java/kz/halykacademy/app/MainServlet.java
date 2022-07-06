package kz.halykacademy.app;

import kz.halykacademy.app.service.OrganizationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

import static kz.halykacademy.app.util.HttpResponse.outputResponse;

public class MainServlet extends HttpServlet {
    private OrganizationService organizationService;

    public MainServlet() {
        this.organizationService = new OrganizationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonResponse = organizationService.findAll();
        outputResponse(resp, jsonResponse, 200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        int status = HttpServletResponse.SC_OK;

        String response = this.organizationService.createOrganization(requestBody);
        if (response == null) {
            status = HttpServletResponse.SC_BAD_REQUEST;
        }
        outputResponse(resp, response, status);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        int status = HttpServletResponse.SC_OK;
        String responseMessage = "success";

        boolean isOrganizationDeleted = organizationService.deleteOrganization(id);
        if (!isOrganizationDeleted) {
            status = HttpServletResponse.SC_BAD_REQUEST;
            responseMessage = "error";
        }
        outputResponse(resp, responseMessage, status);
    }
}
