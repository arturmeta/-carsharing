package carsharing;

import java.util.List;

interface CarDAO {
    public List<Car> getALlCarByCompany(int companyId);

    public List<Car> getAllFreeCarByCompany(int companyId);

    public void addCar(Car car);
    public Car getCarById(int carId);
}
