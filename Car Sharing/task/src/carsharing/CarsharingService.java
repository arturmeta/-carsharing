package carsharing;

import java.util.Scanner;


public class CarsharingService {
        CarsharingManager carsharingManager;

        CarsharingService(SqlManager sqlManager) {
            carsharingManager = new CarsharingManager(sqlManager);
        }

        public void chooseMenu() {
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;
            while (!exit) {
                System.out.println("1. Log in as a manager\n" +
                        "2. Log in as a customer\n" +
                        "3. Create a customer" +
                        "0. Exit\n");
                String s = scanner.nextLine();
                switch (s) {
                    case "1":
                        carsharingManager.chooseCompanyMenu();
                        break;
                    case "2":
                        carsharingManager.chooseCustomer();
                        break;
                    case "3":
                        carsharingManager.addCustomer();
                        break;
                    case "0":
                        exit = true;
                        break;
                }
            }

        }
}
