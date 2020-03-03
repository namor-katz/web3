package servlet;

import com.sun.security.ntlm.Client;
import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", pageVariables));
        resp.setContentType("text/html; charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        String tmpName = req.getParameter("name");
        String tmpPassword = req.getParameter("password");
        String tmpMoney = req.getParameter("money");
        Long tmpMoney2 = Long.parseLong(tmpMoney);
        BankClient client = null;
        try {
            client = new BankClient(tmpName, tmpPassword, tmpMoney2);
        }
        catch (Exception e) {
            System.out.println("cjhzy");
        }

        try {
            String isName = null;
            isName = new BankClientService().getClientByName(tmpName).getName();
            if(isName.equals(tmpName)) {
                pageVariables.put("message", "Client not add");
            }
            else {
                new BankClientService().addClient(client);
                pageVariables.put("message", "Add client successful");
            }

        } catch (DBException e) {

            try {
                new BankClientService().addClient(client);
                pageVariables.put("message", "Add client successful");
            }
            catch (DBException ex) {
                ex.printStackTrace();
            }
        }

        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
        resp.setContentType("text/html; charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }


    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        pageVariables.put("pathInfo", request.getPathInfo());
        pageVariables.put("sessionId", request.getSession().getId());
        pageVariables.put("parameters", request.getParameterMap().toString());
        pageVariables.put("email", request.getParameter("email"));
        pageVariables.put("password", request.getParameter("password"));
        return pageVariables;
    }
}
