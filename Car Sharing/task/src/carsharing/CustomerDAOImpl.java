package carsharing;

import java.util.List;

class CustomerDAOImpl implements CustomerDAO {
    private SqlManager sqlManager;
    public CustomerDAOImpl(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    @Override
    public List<Customer> getAllCustomer() {
        String sql = "SELECT * FROM customer order by id";
        return sqlManager.getCustomers(sql);
    }

    @Override
    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO customer(name) VALUES(?);";
        sqlManager.insertCustomer(sql, customer.getName());
    }

    @Override
    public Customer getCustomerByName(String name) {
        String sql = "SELECT * FROM customer WHERE name = ?;";
        return sqlManager.getCustomer(sql, name);
    }

    @Override
    public void updateCustomer(Customer customer) {
        String sql = "UPDATE customer SET rented_car_id =?\n" +
                "WHERE id = ?;";
        sqlManager.updateCustomer(sql, customer);
    }
}