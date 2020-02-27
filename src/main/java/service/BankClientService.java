package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }

    public boolean validateClient(String name, String password) {
        //этот метод проверяет, есть ли указанный клиент в бд. если да - вернуть true. и не добавлять!
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

    public BankClient getClientByName(String name) {
        return null;
    }

    public List<BankClient> getAllClient() {
        ArrayList<BankClient> bk = new ArrayList<>();
        BankClientDAO dao = getBankClientDAO();
        try {
            bk = (ArrayList<BankClient>) dao.getAllBankClient();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bk;
    }

    public boolean deleteClient(String name) {
        return false;
    }

    public boolean addClient(BankClient client) throws DBException {
        //return false;
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.addClient(client);  //падаем тут.
            return true;
        }
        catch (SQLException e) {
            System.out.println("сорян, посоны. скьюель ексцепсен!");
            System.out.println(e);
            return false;
        }

    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) {
        return false;
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
            System.out.println("Can't get class. No driver found");
            e.printStackTrace();
            //return;
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
         //   System.out.println("I try get connection!");
            return connection;
        }
        catch (SQLException e) {
            System.out.println("Can't get connection. Incorrect URL");
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
