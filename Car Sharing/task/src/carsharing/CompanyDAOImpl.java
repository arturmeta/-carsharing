package carsharing;

import java.util.List;

class CompanyDAOImpl implements CompanyDAO {
    private SqlManager sqlManager;

    public CompanyDAOImpl(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    @Override
    public List<Company> getAllCompany() {
        String sql = "SELECT * FROM COMPANY ORDER BY id";
        return sqlManager.getCompany(sql);
    }

    @Override
    public void addCompany(Company company) {
        String sql = "INSERT INTO company (name)\n" +
                "VALUES(?);";
        if (company.getId() == 0) {
            sqlManager.insertCompany(sql, company.getName());
        }

    }

    @Override
    public Company getCompany(int company_id) {
        String sql = "SELECT * FROM company WHERE id = ?;";
        return sqlManager.getCompany(sql, company_id);
    }
}
