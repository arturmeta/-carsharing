package carsharing;

import java.util.List;

interface CustomerDAO {
    public List<Customer> getAllCustomer();
    public void addCustomer(Customer customer);

    public Customer getCustomerByName(String name);

    public void updateCustomer(Customer customer);
}