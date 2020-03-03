package servlet;

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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/transaction")
public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", pageVariables));
        resp.setContentType("text/html; charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        String sender = req.getParameter("senderName");
        String password = req.getParameter("senderPass");
        Long money = Long.parseLong(req.getParameter("count"));
        String acceptor = req.getParameter("nameTo");
        BankClient senderU = null;
        String result = "";
        boolean clientISExists;
        boolean successTransaction;
        try {
            senderU = bankClientService.getClientByName(sender);
            clientISExists = true;
        }
        catch (DBException e) {
            clientISExists = false;
            e.printStackTrace();
        }

        try {
            successTransaction =  bankClientService.sendMoneyToClient(senderU, acceptor, money);
            if (successTransaction == true) result = "The transaction was successful";
            else {
                result ="transaction rejected";
            }
        } catch (SQLException e) {
            result = "transaction rejected";
            e.printStackTrace();
        }

        pageVariables.put("message", result);
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

