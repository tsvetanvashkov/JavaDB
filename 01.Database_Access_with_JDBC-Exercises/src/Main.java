import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public class Main {

    private static final String CONNECTION_STRING =
            "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "minions_db";
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Connection connection;

    public static void main(String[] args) throws SQLException, IOException {

        connection = getConnection();

        System.out.println("Enter exercises number:");
        int exNum = Integer.parseInt(reader.readLine());
        switch (exNum) {
            case 2 -> getVillainsNames();
            case 3 -> getMinionNames();
            case 4 -> addMinion();
            case 5 -> changeTownNames();
            case 6 -> removeVillain();
            case 7 -> printAllMinionNames();
            case 8 -> increaseMinionsAge();
            case 9 -> increaseAgeStoredProcedure();
        }



    }

    private static void removeVillain() throws IOException, SQLException {
        System.out.println("Enter villain id:");
        int villainId = Integer.parseInt(reader.readLine());
        String villainName = "";
        try{
            villainName = findNameById("villains", villainId);
        }catch (SQLException e){
            System.out.println("No such villain was found\n");
            return;
        }
        int affectedEntities = deleteMinionsByVillainId(villainId);
        deleteVillainById(villainId);

        System.out.printf("%s was deleted\n" +
                "%d minions released\n", villainName, affectedEntities);
    }

    private static void deleteVillainById(int villainId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `villains`" +
                "WHERE `id` = ?;");
        preparedStatement.setInt(1, villainId);
        preparedStatement.executeUpdate();
    }

    private static int deleteMinionsByVillainId(int villainId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `minions_villains`" +
                "WHERE `villain_id` = ?;");
        preparedStatement.setInt(1, villainId);
        return preparedStatement.executeUpdate();
    }

    private static void increaseMinionsAge() throws IOException, SQLException {
        System.out.println("Enter minion IDs:");
        int[] minionId = Arrays.stream(reader.readLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();
        for (int id : minionId) {
            lowerNameAndIncreaseAge(id);
        }

        PreparedStatement preparedStatementAll = connection.prepareStatement("SELECT `id`, `name`, `age`" +
                "FROM `minions`;");
        ResultSet resultSet = preparedStatementAll.executeQuery();
        while(resultSet.next()){
            System.out.printf("%d %s %d\n", resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("age"));
        }

    }

    private static void lowerNameAndIncreaseAge(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `minions` " +
                "SET `age` = `age` + 1 , `name` = LOWER(`name`) " +
                "WHERE `id` = ?;");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    private static void increaseAgeStoredProcedure() throws IOException, SQLException {
        System.out.println("Enter minion id:");
        int minionId = Integer.parseInt(reader.readLine());

        CallableStatement callableStatement = connection.prepareCall("CALL usp_get_older(?);");
        callableStatement.setInt(1, minionId);
        int affected = callableStatement.executeUpdate();

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT `name`, `age`" +
                "FROM `minions`;");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            System.out.printf("%s %d\n", resultSet.getString("name"),
                    resultSet.getInt("age"));
        }
    }

    private static void printAllMinionNames() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT `name` " +
                "FROM `minions`;");
        ResultSet resultSet = preparedStatement.executeQuery();

        List<String> allMinionsNames = new ArrayList<>();
        while (resultSet.next()){
            allMinionsNames.add(resultSet.getString(1));
        }
        int start = 0;
        int end = allMinionsNames.size() - 1;

        for (int i = 0; i < allMinionsNames.size(); i++){
            System.out.println(i % 2 == 0
                    ? allMinionsNames.get(start++)
                    : allMinionsNames.get(end--));
        }

    }

    private static void changeTownNames() throws IOException, SQLException {
        System.out.println("Enter country name:");
        String countryName = reader.readLine();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `towns` " +
                "SET `name` = UPPER(`name`) " +
                "WHERE `country` = ?;");
        preparedStatement.setString(1, countryName);

        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows == 0){
            System.out.println("No town names were affected.");
            return;
        }
        System.out.printf("%d town names were affected.\n", affectedRows);
        PreparedStatement preparedStatementTowns = connection.prepareStatement("SELECT `name` " +
                "FROM `towns` " +
                "WHERE country = ?;");
        preparedStatementTowns.setString(1, countryName);
        ResultSet resultSetTowns = preparedStatementTowns.executeQuery();
        List<String> affectedTowns = new ArrayList<>();
        while(resultSetTowns.next()){
            affectedTowns.add(resultSetTowns.getString("name"));
        }
        System.out.println(affectedTowns.toString());
    }

    private static void addMinion() throws IOException, SQLException {

        System.out.println("Minion: ");
        String[] input =  reader.readLine().split("\\s+");
        String minionName = input[0];
        int minionAge = Integer.parseInt(input[1]);
        String minionTown = input[2];
        System.out.println("Villain: ");
        String villainName = reader.readLine();

        boolean townIsInDatabase = checkDatabaseForTown(minionTown);
        boolean villainIsInDatabase = checkDatabaseForVillain(villainName);
        if (!townIsInDatabase) {
            addTownToDatabase(minionName, minionAge, minionTown);
            System.out.printf("Town %s was added to the database.\n", minionTown);
        }
        if(!villainIsInDatabase){
            addVillainInDatabase(villainName);
            System.out.printf("Villain %s was added to the database.\n", villainName);
        }
        int villainId = getVillainsId(villainName);
        int minionId = getMinionId(minionName);
        addMinionToVillain(villainId, minionId);
        System.out.printf("Successfully added %s to be minion of %s\n", minionName, villainName);

    }

    private static void addMinionToVillain(int villainId, int minionId) throws SQLException {
        PreparedStatement preparedStatementMV = connection.prepareStatement("INSERT INTO `minions_villains`" +
                "VALUES (?, ?);");
        preparedStatementMV.setInt(1, minionId);
        preparedStatementMV.setInt(2, villainId);
        preparedStatementMV.executeUpdate();
    }

    private static int getMinionId(String minionName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT `id`" +
                "FROM `minions`" +
                "WHERE `name` = ?;");
        preparedStatement.setString(1, minionName);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    private static void addVillainInDatabase(String villainName) throws SQLException {
        PreparedStatement preparedStatementVillain = connection.prepareStatement("INSERT INTO `villains` (`name`, `evilness_factor`) " +
                "VALUES (?, 'evil');");
        preparedStatementVillain.setString(1, villainName);
        preparedStatementVillain.executeUpdate();
    }

    private static int getVillainsId(String villainName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT `id`" +
                "FROM `villains`" +
                "WHERE `name` = ?;");
        preparedStatement.setString(1, villainName);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    private static void addTownToDatabase(String minionName, int minionAge, String minionTown) throws SQLException {
        PreparedStatement preparedStatementTown = connection.prepareStatement("INSERT INTO `towns` (`name`, `country`)" +
                "VALUES (?, null);");
        preparedStatementTown.setString(1, minionTown);
        preparedStatementTown.executeUpdate();
        int townId = getTownId(minionTown);
        PreparedStatement preparedStatementMinions = connection.prepareStatement("INSERT INTO `minions` (`name`, `age`,`town_id`)" +
                "VALUES (?, ?, ?);");
        preparedStatementMinions.setString(1, minionName);
        preparedStatementMinions.setInt(2, minionAge);
        preparedStatementMinions.setInt(3, townId);
        preparedStatementMinions.executeUpdate();

    }

    private static int getTownId(String minionTown) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT `id`" +
                "FROM `towns`" +
                "WHERE `name` = ?;");
        preparedStatement.setString(1, minionTown);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    private static boolean checkDatabaseForVillain(String villain) {
        try {
            getVillainsId(villain);
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    private static boolean checkDatabaseForTown(String minionTown) {
        try {
            getTownId(minionTown);
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    private static void getMinionNames() throws IOException {
        System.out.println("Enter villain id:");
        int villainId = Integer.parseInt(reader.readLine());

        try{
            String villainName = findNameById("villains", villainId);
            System.out.printf("Villain: %s\n", villainName);
            Set<String> allMinionsByVillainId =getAllMinionsByVillainId(villainId);
            allMinionsByVillainId.forEach(System.out::println);
        } catch (SQLException exception){
            System.out.printf("No villain with ID %d exists in the database.\n", villainId);
        }

    }

    private static Set<String> getAllMinionsByVillainId(int villainId) throws SQLException {
        Set<String> result = new LinkedHashSet<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT " +
                "`m`.`name`, " +
                "`m`.`age` " +
                "FROM `minions` AS `m` " +
                "JOIN `minions_villains` AS `mv` " +
                "ON `m`.`id` = `mv`.`minion_id` " +
                "WHERE `mv`.`villain_id` = ?;");
        preparedStatement.setInt(1, villainId);
        ResultSet resultSet= preparedStatement.executeQuery();
        int count = 0;
        while(resultSet.next()){
            result.add(String.format("%d. %s %s", ++count,
                    resultSet.getString("name"),
                    resultSet.getString("age")));
        }
        return result;

    }

    private static String findNameById(String villains, int villainId) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT `name` " +
                "FROM `villains` " +
                "WHERE `id` = ?;");
        preparedStatement.setInt(1, villainId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString("name");
    }

    private static void getVillainsNames() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT  " +
                "`v`.`name`, " +
                "COUNT(distinct `mv`.`minion_id`) AS `number_of_minions` " +
                "FROM `villains` AS `v` " +
                "JOIN `minions_villains` AS `mv` " +
                "ON `v`.`id` = `mv`.`villain_id` " +
                "GROUP BY `v`.`name` " +
                "HAVING `number_of_minions` > ?; ");

        preparedStatement.setInt(1, 15);
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()){
            System.out.printf("%s %s\n", resultSet.getString(1),
                    resultSet.getString(2));
        }
    }

    private static Connection getConnection() throws IOException, SQLException {

        System.out.println("Enter user:");
        String user = reader.readLine().equals("") ? "root" : reader.readLine();
        System.out.println("Enter password:");
        String password = reader.readLine().equals("") ? "123" : reader.readLine();

        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        Connection connection = DriverManager.getConnection(CONNECTION_STRING + DB_NAME, properties);
        return connection;
    }

}
