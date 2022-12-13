import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class Main {

    public static void main(String args[]) throws SQLException, IOException, ClassNotFoundException {

        //Group 1/7 – The product p1 is deleted from Product and Stock.
        //



        // Load the MySQL driver
        Class.forName("org.postgresql.Driver");


        //Establishing connection
        /*
        parameter 1 = local postgres server connection url
        parameter 2 = user
        parameter 3 = password
        * */

        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cs623", "postgres", "admin");


        // For atomicity
        conn.setAutoCommit(false);

        // For  Isolation
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

        Statement statement = null;
        try {

            statement = conn.createStatement();


            System.out.println("\nDeleting p1 from Product table");
            statement.executeUpdate("DELETE FROM Product WHERE prodid = 'p1'");
            System.out.println("\nDeleted p1 from Product table and Stock table");

            System.out.println("After delete operation\n");
            ResultSet product = statement.executeQuery("SELECT * FROM Product");
            ResultSet stock = statement.executeQuery("SELECT * FROM Stock");

            //Print data in depot table (should show d1 as dd1 now)
            while(product.next()) {
                System.out.println("prodId: " + product.getString("prodid") + " pname: " + product.getString("pname") + " price: " + product.getInt("price"));
            }

            //Print data in stock table (should show d1 as dd1 now)
            while(stock.next()) {
                System.out.println("prodId: " + stock.getString("prodId") + " depId: " + stock.getString("depId") + " quantity: " + stock.getInt("quantity"));
            }



        } catch (SQLException err) {
            System.out.println("Error: "+err);

            // For atomicity
            conn.rollback();
            statement.close();
            conn.close();
            return;
        } // main

        System.out.println("\n-----Transaction successful and committing-----");
        conn.commit();
        statement.close();
        conn.close();
        System.out.println("\n-----Committed-----");
    }
}