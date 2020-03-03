package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
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
            isClientTrue = dao.validateClient(sender_name, sender_password);
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
        String connectionString = "jdbc:sqlite:/home/namor/test_sqlite.sql";
        String driverName = "org.sqlite.JDBC";

        try {
            Class.forName(driverName);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
            return connection;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
