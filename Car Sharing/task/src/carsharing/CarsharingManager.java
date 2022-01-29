package carsharing;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

class CarsharingManager {
    CompanyDAO companyDAO;
    CarDAO carDAO;
    CustomerDAO customerDAO;

    CarsharingManager(SqlManager sqlManager) {
        companyDAO = new CompanyDAOImpl(sqlManager);
        carDAO = new CarDAOImpl(sqlManager);
        customerDAO = new CustomerDAOImpl(sqlManager);
    }


    public void chooseCompanyMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Company list\n" +
                    "2. Create a company\n" +
                    "0. Back\n");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    chooseCompany(null);
                    break;
                case "2":
                    addCompany();
                    break;
                case "0":
                    exit = true;
                    break;
            }
        }
    }

    public void chooseCustomer() {
        List<Customer> customers = customerDAO.getAllCustomer();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!\n");
            return;
        }
        System.out.println("Choose a customer:");
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < customers.size(); i++) {
            System.out.println(i + 1 + ". " + customers.get(i).getName());
        }
        System.out.println("0. Back");
        String numberOfCustomer = scanner.nextLine();
        System.out.println("");

        if ("0".equals(numberOfCustomer)) {
        } else {
            chooseCustomerMenu(customers.get(Integer.parseInt(numberOfCustomer) - 1));
        }
    }

    public void chooseCompany(Customer customer) {
        List<Company> companies = getCompanyList();
        Customer customerFound = null;
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!\n");
            return;
        }

        if (customer != null) {
            customerFound = customerDAO.getCustomerByName(customer.getName());

            if (customerFound.getRentedCarId() != null && customerFound.getRentedCarId() > 0) {
                System.out.println("You've already rented a car!\n");
                return;
            }
        }

        Map<Integer, Company> companyById = companies.stream()
                .collect(Collectors.toMap(Company::getId, Function.identity()));

        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose a company:");
        for (int i = 0; i < companies.size(); i++) {
            System.out.println(i + 1 + ". " + companies.get(i).getName());
        }

        System.out.println("0. Back\n");
        String numberOfCompany = scanner.nextLine();
        System.out.println("");
        if ("0".equals(numberOfCompany)) {
            return;
        } else if (customer == null) {
            chooseCarsMenu(companies.get(Integer.parseInt(numberOfCompany) - 1));
        } else {
            chooseCar(customerFound, companies.get(Integer.parseInt(numberOfCompany) - 1));
        }

    }

    public void chooseCar(Customer customer, Company company) {
        List<Car> freeCars = carDAO.getAllFreeCarByCompany(company.getId());
        if (freeCars.isEmpty()) {
            System.out.printf("No available cars in the '%s' company%n%n", company.getName());
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a car:\n");
        for (int i = 0; i < freeCars.size(); i++) {
            System.out.println(i + 1 + ". " + freeCars.get(i).getName());
        }

        System.out.println("0. Back\n");
        String numberOfCar = scanner.nextLine();
        System.out.println("");
        if ("0".equals(numberOfCar)) {
            return;
        }
        Car selectedCar = freeCars.get(Integer.parseInt(numberOfCar) - 1);
        customer.setRentedCarId(selectedCar.getId());
        customerDAO.updateCustomer(customer);
        System.out.printf("You rented '%s'%n%n", selectedCar.getName());

    }

    public void chooseCustomerMenu(Customer customer) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println(
                    "1. Rent a car\n" +
                            "2. Return a rented car\n" +
                            "3. My rented car\n" +
                            "0. Back");
            String action = scanner.nextLine();
            System.out.println("");
            switch (action) {
                case "1":
                    rentCar(customer);
                    break;
                case "2":
                    returnRentedCar(customer);
                    break;
                case "3":
                    getRentedCar(customer);
                    break;
                case "0":
                    exit = true;
                    break;
            }
        }
    }

    public void rentCar(Customer customer) {
        chooseCompany(customer);
    }

    public void returnRentedCar(Customer customer) {
        Customer customerFound = customerDAO.getCustomerByName(customer.getName());
        if (customerFound.getRentedCarId() == null || customerFound.getRentedCarId() < 1) {
            System.out.println("You didn't rent a car!\n");
            return;
        }

        customerFound.setRentedCarId(null);
        customerDAO.updateCustomer(customerFound);
        System.out.println("You've returned a rented car!\n");
    }

    public void chooseCarsMenu(Company company) {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("%n'%s' company%n", company.getName());
        boolean exit = false;
        while (!exit) {
            System.out.println(
                    "1. Car list\n" +
                            "2. Create a car\n" +
                            "0. Back\n");
            String action = scanner.nextLine();
            System.out.println("");
            switch (action) {
                case "1":
                    getAllCar(company);
                    break;
                case "2":
                    addCar(company);
                    break;
                case "0":
                    exit = true;
                    break;
            }
        }
    }

    public void getAllCar(Company company) {
        List<Car> cars = carDAO.getALlCarByCompany(company.getId());
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!\n");
            return;
        }

        for (int i = 0; i < cars.size(); i++) {
            System.out.println(i + 1 + ". " + cars.get(i).getName());
        }
        System.out.println("");
    }

    public void getRentedCar(Customer customer) {
        Customer customerFound = customerDAO.getCustomerByName(customer.getName());
        if (customerFound.getRentedCarId() == null || customerFound.getRentedCarId() < 1) {
            System.out.println("You didn't rent a car!\n");
            return;
        }
        Car carFound = carDAO.getCarById(customerFound.getRentedCarId());
        System.out.println("Your rented car:");
        System.out.println(carFound.getName());
        System.out.println("Company:");
        Company companyFound = companyDAO.getCompany(carFound.getCompanyId());
        System.out.println(companyFound.getName());
        System.out.println("");
    }

    public void addCar(Company company) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the car name:");
        String carName = scanner.nextLine();
        carDAO.addCar(new Car(carName, company.getId()));
        System.out.println("The car was added!");
    }

    public void addCustomer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the customer name:");
        String customerName = scanner.nextLine();
        customerDAO.addCustomer(new Customer(customerName));
        System.out.println("The customer was added!");
    }

    public void addCompany() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        companyDAO.addCompany(new Company(name));
        System.out.println("The company was created!\n");
    }

    public List<Company> getCompanyList() {
        return companyDAO.getAllCompany();
    }
}