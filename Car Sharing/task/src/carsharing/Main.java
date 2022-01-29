package carsharing;

public class Main {

    public static void main(String[] args) {
        String databaseFileName = "test";
        if (args.length == 2 && "-databaseFileName".equals(args[0])) {
            databaseFileName = args[1];
        }
        SqlManager sqlManager = new SqlManager(databaseFileName);
        sqlManager.init();
        CarsharingService carsharingService = new CarsharingService(sqlManager);
        carsharingService.chooseMenu();
        sqlManager.close();
    }


}