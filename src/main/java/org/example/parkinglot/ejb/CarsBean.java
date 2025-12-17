package org.example.parkinglot.ejb;

import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.parkinglot.common.CarDto;
import org.example.parkinglot.common.CarPhotoDto;
import org.example.parkinglot.entities.Car;
import org.example.parkinglot.entities.CarPhoto;
import org.example.parkinglot.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class CarsBean {
    private static final Logger LOG = Logger.getLogger(CarsBean.class.getName());

    @PersistenceContext
    EntityManager entityManager;

    public List<CarDto> findAllCars() {
        LOG.info("findAllCars");
        try {
            TypedQuery<Car> typedQuery = entityManager.createQuery("SELECT c FROM Car c", Car.class);
            List<Car> cars = typedQuery.getResultList();
            return copyCarsToDto(cars);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    public void createCar(String licensePlate, String parkingSpot, Long userId) {
        LOG.info("createCar");

        Car car = new Car();
        car.setLicensePlate(licensePlate);
        car.setParkingSpot(parkingSpot);

        // Găsim user-ul (proprietarul) în baza de date
        User user = entityManager.find(User.class, userId);

        // Dacă user-ul există, facem legătura
        if (user != null) {
            user.getCars().add(car); // Adăugăm mașina la lista userului
            car.setOwner(user);      // Setăm userul ca proprietar al mașinii
        }

        entityManager.persist(car); // Salvăm mașina în DB
    }

    private List<CarDto> copyCarsToDto(List<Car> cars) {
        List<CarDto> dtoList = new ArrayList<>();

        for (Car car : cars) {
            // Verificare pentru a evita NullPointerException dacă nu există owner
            String ownerName = (car.getOwner() != null) ? car.getOwner().getUsername() : "No Owner";

            CarDto dto = new CarDto(
                    car.getId(),
                    car.getLicensePlate(),
                    car.getParkingSpot(),
                    ownerName
            );
            dtoList.add(dto);
        }
        return dtoList;
    }

    public CarDto findById(Long id) {
        // Căutăm entitatea în baza de date
        Car car = entityManager.find(Car.class, id);

        if (car == null) {
            return null;
        }

        // Convertim Entitatea în DTO (pentru a o trimite la JSP)
        String ownerName = (car.getOwner() != null) ? car.getOwner().getUsername() : "";
        return new CarDto(car.getId(), car.getLicensePlate(), car.getParkingSpot(), ownerName);
    }

    public void updateCar(Long carId, String licensePlate, String parkingSpot, Long userId) {
        LOG.info("updateCar");

        Car car = entityManager.find(Car.class, carId);
        car.setLicensePlate(licensePlate);
        car.setParkingSpot(parkingSpot);

        // 1. Scoatem mașina de la vechiul proprietar (dacă există)
        User oldUser = car.getOwner();
        if (oldUser != null) {
            oldUser.getCars().remove(car);
        }

        // 2. Adăugăm mașina la noul proprietar
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            user.getCars().add(car);
            car.setOwner(user);
        }
    }

    public void deleteCarsByIds(List<Long> carIds) {
        LOG.info("deleteCarsByIds");
        for (Long carId : carIds) {
            Car car = entityManager.find(Car.class, carId);
            entityManager.remove(car);
        }
    }

    public void addPhotoToCar(Long carId, String filename, String fileType, byte[] fileContent) {
        LOG.info("addPhotoToCar");
        CarPhoto photo = new CarPhoto();
        photo.setFilename(filename);
        photo.setFileType(fileType);
        photo.setFileContent(fileContent);
        Car car = entityManager.find(Car.class, carId);
        if (car.getPhoto() != null) {
            entityManager.remove(car.getPhoto());
        }
        car.setPhoto(photo);
        photo.setCar(car);
        entityManager.persist(photo);
    }
    public CarPhotoDto findPhotoByCarId(Long carId) {
        List<CarPhoto> photos = entityManager
                .createQuery("SELECT p FROM CarPhoto p where p.car.id = :id", CarPhoto.class)
                .setParameter("id", carId)
                .getResultList();
        if (photos.isEmpty()) {
            return null;
        }
        CarPhoto photo = photos.get(0); // the first element
        return new CarPhotoDto(photo.getId(), photo.getFilename(), photo.getFileType(),
                photo.getFileContent());
    }
    public long countCars() {
        try {
            return entityManager.createQuery("SELECT COUNT(c) FROM Car c", Long.class).getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }


}