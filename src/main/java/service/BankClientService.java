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

    public BankClient getClientByName(String name) throws DBException {
        try {
            return getBankClientDAO().getClientByName(name);
        } catch (SQLException e) {
            System.out.println("что то пошло не так в getclientByName");
            throw new DBException(e);
        }
    }

    public List<BankClient> getAllClient() {
        //System.out.println("старт getAllClient of BankClientService");
        LinkedList<BankClient> bk;// = new ArrayList<>();
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
            System.out.println("сорян, посоны. скьюель ексцепсен!");
            System.out.println(e);
            return false;
        }

    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) throws SQLException {
        Long sum = sender.getMoney();//это переводим
        if (sum < value) {
            System.out.println("на счету недостаточно денег!");
            return false;
        }
        else {
            BankClientDAO dao = getBankClientDAO();
            BankClient acceptor = dao.getClientByName(name);
            Long acceptor_sum = acceptor.getMoney();
            Long diff_sender = sum - value;
            sender.setMoney(diff_sender);
            Long diff_acceptor = acceptor_sum + value;
            acceptor.setMoney(diff_acceptor);
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
