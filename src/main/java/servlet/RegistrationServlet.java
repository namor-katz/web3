package servlet;

import com.sun.security.ntlm.Client;
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
//        PageGenerator.getInstance().getPage("registrationPage.html", pageVariables);  //с пустым словарём нихуя не работает!

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
            System.out.println("в РегСервлет успешно создан новый клиент!");
//            request.setAttribute("message", message);
//            req.setAttribute("message", "Add client successful");
        } catch (Exception e) {
            System.out.println("cjhzy");
        }
        try {
            boolean sw =  new BankClientService().addClient(client); //планирую навелосипедить. если тут определённое имя - кидать ошибку ерр
            if(sw == true) {
                pageVariables.put("message", "Add client successful");
            } else {pageVariables.put("message", "Client not add");}

        } catch (DBException e) {
            pageVariables.put("message", "Client not add");
            e.printStackTrace();
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
//        String tmp0 = request.getParameter("value");  //example
        return pageVariables;
    }
}
