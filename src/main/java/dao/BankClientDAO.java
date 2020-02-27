package dao;

//import com.sun.deploy.util.SessionState;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import model.BankClient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BankClientDAO {
    private String Start = "'";
    private String Final = "', ";

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        System.out.println("Я вО всех клиентах!");
        Statement statement = connection.createStatement();
        statement.execute("SELECT * FROM bank_client");
        ResultSet result = statement.getResultSet();
        result.next();

        ArrayList<BankClient> fromReturn = new ArrayList<BankClient>((Collection<? extends BankClient>) result);
        return fromReturn;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        //ЯННП, но, видимо, проверка пары логинъ=-пароль.
        Statement statement = connection.createStatement();
        statement.execute("select * from bank_client where name = '" + name + "';");    // + "AND password = '" + password + "';");
        ResultSet resultSet = statement.getResultSet();
        //statement.close();
        return resultSet.next(); //если что-то ессть, верну тру. т.е. после этого делать ничего не надо будет.
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws Exception {
        //оч странно. войд. а типа возвращать результат - успешно добавили, не успешно - не надо?
        Statement statement = connection.createStatement();
        statement.execute("UPDATE bank_client SET money = money + '" + transactValue + "'" + "WHERE name = '" + name + "'");
        statement.close();
    }

    public BankClient getClientById(long id) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where id = '" + id + "'");
        ResultSet result = stmt.getResultSet();
        result.next();  //это по факту позиционирование на 1 строку
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
        //return null;
        Statement statement = connection.createStatement();
        statement.execute("SELECT * from bank_client WHERE name = '" + name + "'");
        ResultSet result = statement.getResultSet();
        result.next();
        long tmpId = result.getLong("id");
        String tmpName = result.getString("name");
        String tmpPassword = result.getString("password");
        Long tmpMoney = result.getLong("money");

        BankClient bankClient = new BankClient(tmpId, tmpName, tmpPassword, tmpMoney);
        return bankClient;
    }

    public void addClient(BankClient client) throws SQLException {
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
