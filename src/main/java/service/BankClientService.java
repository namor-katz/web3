package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;
import org.eclipse.jetty.util.Scanner;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
//import com.mysql.jdbc.Driver;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }

    public boolean validateClient(String name, String password) {
        BankClientDAO dao = getBankClientDAO();
        boolean result;
        try {
            result = dao.validateClient(name, password);
            return result;
        } catch (SQLException e) {
            return false;
        }
    }

    public BankClient getClientById(long id) throws DBException {
        try {
            return getBankClientDAO().getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public BankClient getClientByName(String name) throws DBException {
        try {
            return getBankClientDAO().getClientByName(name);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public List<BankClient> getAllClient() {
        LinkedList<BankClient> bk;
        BankClientDAO dao = getBankClientDAO();
        try {
            bk = (LinkedList<BankClient>) dao.getAllBankClient();
        } catch (SQLException e) {
            bk = null;
            e.printStackTrace();
        }
        return bk;
    }

    public boolean deleteClient(String name) {
        return false;
    }

    public boolean addClient(BankClient client) throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.addClient(client);
            return true;
        }
        catch (SQLException e) {
            System.out.println(e);
            return false;
        }

    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) throws SQLException {
        Long sum = sender.getMoney();
        Long diff_sender = sum - value;
        String sender_name = sender.getName();
        String sender_password = sender.getPassword();
        if (sum < value) {
            return false;
        }
        else {
            BankClientDAO dao = getBankClientDAO();
            boolean isClientTrue;
            //isClientTrue = dao.validateClient(sender_name, sender_password);
            isClientTrue = validateClient(sender_name, sender_password);
            System.out.println("я ис клиент и я " + isClientTrue + " " + sender_name + " " + sender_password);
            if(isClientTrue == false) return false;

            BankClient acceptor = dao.getClientByName(name);
            Long acceptor_sum = acceptor.getMoney();
            Long diff_acceptor = acceptor_sum + value;
            try {
                dao.updateClientsMoney(acceptor.getName(), acceptor.getPassword(), diff_acceptor);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                dao.updateClientsMoney(sender.getName(), sender.getPassword(), diff_sender);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void createTable() throws DBException{
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
//            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());
                DriverManager.registerDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance());
            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("db_example?").          //db name
                    append("user=root&").          //login
                    append("password=logrys7");       //password

            //System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
