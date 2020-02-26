package servlet;

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
        super.doPost(req, resp);
        //String tmpName = req.getParameter("name");
        //String tmpPassword = req.getParameter("password");
        //String tmpMoney = req.getParameter("money");
        //Long tmpMoney2 = Long.parseLong(tmpMoney);
        //new BankClientService().addClient(tmpName, tmpPassword, tmpMoney2);
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
