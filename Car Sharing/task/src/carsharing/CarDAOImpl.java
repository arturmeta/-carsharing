package carsharing;

import java.util.List;

class CarDAOImpl implements CarDAO {
    SqlManager sqlManager;

    public CarDAOImpl(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    @Override
    public List<Car> getALlCarByCompany(int companyId) {
        String sql = "SELECT * FROM car WHERE company_id = '" + companyId +
                "'\n ORDER BY name";
        return sqlManager.getCars(sql);
    }

    @Override
    public List<Car> getAllFreeCarByCompany(int companyId) {
        String sql = "SELECT * FROM car ca\n" +
                "LEFT JOIN customer cu ON ca.id = cu.rented_car_id\n" +
                "WHERE ca.company_id = ? AND cu.rented_car_id IS NULL";
        return sqlManager.getCarsById(sql, companyId);
    }

    @Override
    public void addCar(Car car) {
        if (car.getId() == 0) {
            String sql = "INSERT INTO car(name, company_id)\n" +
                    "VALUES(?, ?)";
            sqlManager.insertCar(sql, car.getName(), car.getCompanyId());
        }
    }

    @Override
    public Car getCarById(int carId) {
        String sql = "SELECT * FROM car WHERE id = ?;";
        return sqlManager.getCar(sql, carId);
    }
}
