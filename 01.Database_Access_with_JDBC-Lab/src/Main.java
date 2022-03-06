import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

public class Main {

    private static final String CONNECTION_STRING =
            "jdbc:mysql://localhost:3306/";
    private static final String TABLE_NAME = "soft_uni";

    public static void main(String[] args) throws SQLException, IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter user:");
        String user = reader.readLine();
        System.out.println("Enter password:");
        String password = reader.readLine();

        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);

        Connection connection = DriverManager.getConnection(CONNECTION_STRING + TABLE_NAME, properties);
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery("SELECT * FROM employees");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employees WHERE employee_id < ?;");

        System.out.println("Enter max id");
        int maxId = Integer.parseInt(reader.readLine());
        preparedStatement.setInt(1, maxId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()){
            System.out.printf("%s %s %s\n", resultSet.getString("first_name"),
                resultSet.getString("last_name"), resultSet.getString("job_title"));
        }

    }

}
