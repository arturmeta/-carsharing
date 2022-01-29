package carsharing;

import java.util.List;

interface CompanyDAO {
    public List<Company> getAllCompany();

    public void addCompany(Company company);

    public Company getCompany(int company_id);
}
