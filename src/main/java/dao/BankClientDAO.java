package dao;

//import com.sun.deploy.util.SessionState;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class BankClientDAO {
    private String Start = "'";
    private String Final = "', ";

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("SELECT id, name, password, money FROM bank_client");
        ResultSet result = statement.getResultSet();

        List AllUsers = new LinkedList();

        while (result.next()) {
            long id = result.getLong("id");
            String name = result.getString("name");
            String password = result.getString("password");
            Long money = result.getLong("money");
            BankClient bankClient = new BankClient(id, name, password, money);
            AllUsers.add(bankClient);
        }
        return AllUsers;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        String query = "SELECT * FROM bank_client WHERE name=? AND password=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next(); //если что-то ессть, верну тру.
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws Exception {
        String query = "UPDATE bank_client SET money=? WHERE name=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, transactValue);
        preparedStatement.setString(2, name);
        int rowsAffected = preparedStatement.executeUpdate();
    }

    public BankClient getClientById(long id) throws SQLException {
        //Statement stmt = connection.createStatement();
        String query = "SELECT * FROM bank_client WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, id);
        ResultSet result = preparedStatement.executeQuery();
        result.next();
        long idTmp = result.getInt("id");
        String nameTmp = result.getString("name");
        String passwordTmp = result.getString("password");
        Long moneyTmp = result.getLong("money");
        BankClient bankClient = new BankClient(idTmp, nameTmp, passwordTmp, moneyTmp);
        return bankClient;
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        //проверка на то, есть ли у клиента та сумма, которую он якобы хочет перевести.
        Statement statement = connection.createStatement();
        statement.execute("SELECT money FROM bank_client WHERE name = '" + name + "';");
        ResultSet result = statement.getResultSet();
        if (result.next() == false) return false;

        Long tmpMoney = result.getLong(1); //тут, может быть, не так всё. нужно посмотреть. ибо в примере они дёргают не отдельную колонку, а ВСЁ. нахуа?
        if (tmpMoney > expectedSum) {
            return true;
        }
        else {
            return false;
        }
    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_clien where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public BankClient getClientByName(String name) throws SQLException {
        String query = "SELECT * FROM bank_client WHERE name=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        System.out.println("щас упаду");
        preparedStatement.setString(1, name);
        ResultSet result = preparedStatement.executeQuery();//нет, тут. как будто не происходит подстановка.
        result.next();

        String tmpName = result.getString("name");
        String tmpPassword = result.getString("password");
        Long tmpMoney = result.getLong("money");

        BankClient bankClient = new BankClient(tmpName, tmpPassword, tmpMoney);
        result.close();
        return bankClient;
    }

    public void addClient(BankClient client) throws SQLException {
        String query = "INSERT INTO bank";
        String  nameT = client.getName();
        Statement statement = connection.createStatement();
            String tmpPassword = client.getPassword();
            Long tmpMoney = client.getMoney();
            StringBuilder addClient = new StringBuilder("INSERT INTO bank_client (name, password, money) VALUES (").append(Start).append(nameT).append(Final).append(Start).
                    append(tmpPassword).append(Final).append(tmpMoney).append(");");
            statement.execute(addClient.toString());
            statement.close();
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        //stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.execute("create table if not exists bank_client (ID INTEGER PRIMARY KEY AUTOINCREMENT, name varchar(256), password varchar(256), money bigint)");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }
}
