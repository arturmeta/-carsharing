package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class SqlManager {
    private Connection connection;
    private final String databaseFileName;

    public SqlManager(String databaseFileName) {
        connection = getConnection(databaseFileName);
        this.databaseFileName = databaseFileName;
    }

    public void init() {
        String companyTable = "CREATE TABLE IF NOT EXISTS company" +
                "(id INTEGER AUTO_INCREMENT PRIMARY KEY,\n" +
                "name VARCHAR(255) UNIQUE NOT NULL\n" +
                ")";

        String carTable = "CREATE TABLE IF NOT EXISTS car" +
                "(id INTEGER AUTO_INCREMENT PRIMARY KEY,\n" +
                "name VARCHAR(255) UNIQUE NOT NULL,\n" +
                "company_id INTEGER NOT NULL,\n" +
                "CONSTRAINT fk_company FOREIGN KEY (company_id)\n" +
                "REFERENCES company(id)" +
                ")";
        String customerTable = "CREATE TABLE IF NOT EXISTS customer" +
                "(id INTEGER AUTO_INCREMENT PRIMARY KEY,\n" +
                "name VARCHAR(255) UNIQUE NOT NULL,\n" +
                "rented_car_id INTEGER,\n" +
                "CONSTRAINT fk_car FOREIGN KEY (rented_car_id)\n" +
                "REFERENCES car(id)\n" +
                "ON DELETE SET NULL\n" +
                " ON UPDATE CASCADE" +
                ")";


        createTable(companyTable);
        createTable(carTable);
        createTable(customerTable);


    }

    public Connection getConnection(String dataBaseFileName) {
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection("jdbc:h2:./src/carsharing/db/" + dataBaseFileName, "", "");
        } catch (SQLException | ClassNotFoundException sqlException) {
            System.out.println("can not connect to H2 ");
            sqlException.printStackTrace();
            return null;
        }
    }

    public void tryConnection() {
        if (connection == null) {
            connection = getConnection(databaseFileName);
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void insertCompany(String sql, String name) {
        tryConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void insertCar(String sql, String name, int id) {
        tryConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void insertCustomer(String sql, String name) {
        tryConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void updateCustomer(String sql, Customer customer) {
        tryConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (customer.getRentedCarId() == null) {
                statement.setString(1, null);
            } else {
                statement.setInt(1, customer.getRentedCarId());
            }
            statement.setInt(2, customer.getId());
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public List<Company> getCompany(String sql) {
        tryConnection();
        List<Company> companies = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                companies.add(new Company(resultSet.getInt("id"), resultSet.getString("name")));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return companies;
    }

    public Company getCompany(String sql, int companyId) {
        tryConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, companyId);
            boolean hasResult = statement.execute();
            if (hasResult) {
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                return new Company(resultSet.getInt("id"), resultSet.getString("name"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public Customer getCustomer(String sql, String name) {
        tryConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            boolean hasResult = statement.execute();
            if (hasResult) {
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                return new Customer(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getInt("rented_car_id"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public Car getCar(String sql, int id) {
        tryConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            boolean hasResult = statement.execute();
            if (hasResult) {
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                return new Car(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getInt("company_id"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public List<Car> getCars(String sql) {
        tryConnection();
        List<Car> cars = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                cars.add(new Car(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getInt("company_id")));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return cars;
    }

    public List<Car> getCarsById(String sql, int company_id) {
        tryConnection();
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, company_id);
            boolean hasResult = statement.execute();
            if (!hasResult) {
                return cars;
            }

            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                cars.add(new Car(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getInt("company_id")));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return cars;
    }

    public List<Customer> getCustomers(String sql) {
        tryConnection();
        List<Customer> customers = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                customers.add(new Customer(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getInt("rented_car_id")));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return customers;
    }


    public void createTable(String sql) {
        tryConnection();
        try (Statement statement = connection.createStatement();) {
            connection.setAutoCommit(true);
            statement.executeUpdate(sql);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

}
