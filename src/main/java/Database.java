
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {

    private static Database instance;
    private Connection connection;
    private Statement statement;

    private Database(){
        connection = null;
        statement = null;
    }

    public static Database getInstance(){
        if(instance == null){
            instance = new Database();
        }
        return instance;
    }

    public void initialize(){
        try{
            Class.forName("org.sqlite.JDBC"); //default
            connection = DriverManager.getConnection("jdbc:sqlite:AdminDatabase.db"); //filename
            statement = connection.createStatement();

            ResultSet rs;
            rs = statement.executeQuery("SELECT * FROM sqlite_master where type = 'table' and name = 'inventory'");
            if(!rs.next()){
                statement.execute("CREATE TABLE inventory" + "("
                        + "Date CHAR(50) NOT NULL"
                        + ")");
                System.out.println("Successfully made a table named inventory.");
            }
            rs.close();

            rs = statement.executeQuery("SELECT * FROM sqlite_master where type = 'table' and name = 'prods'");
            if(!rs.next()){
                statement.execute("CREATE TABLE prods" + "("
                        + "ID INT PRIMARY KEY,"
                        + "Date CHAR(50) NOT NULL,"
                        + "Product CHAR(50) NOT NULL,"
                        + "Price INT NOT NULL,"
                        + "Quantity INT NOT NULL,"
                        + "BasePrice INT NOT NULL"
                        + ")");
                System.out.println("Successfully made a table named prods.");
            }



        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void date_db (String date){
        try{
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO inventory VALUES('"+date+"')");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void append_db(String ID,String Date, String Product, String Price, String  Quantity, String Basep){
        try{
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO prods(ID,Date,Product,Price,Quantity,BasePrice)"+"VALUES('"+ID+"','"+Date+"','"+Product+"','"+Price+"','"+Quantity+"','"+Basep+"')");

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public ResultSet getResult(String query){ //getter para sa frame.java
        try{
            return statement.executeQuery(query);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet setResult(String query){
        try{
            statement.executeUpdate(query);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
