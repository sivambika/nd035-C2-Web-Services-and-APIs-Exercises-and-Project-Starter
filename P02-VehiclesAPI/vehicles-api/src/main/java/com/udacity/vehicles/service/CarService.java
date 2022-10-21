package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final CarRepository repository;

    private final MapsClient mapsClient;

    private final PriceClient priceClient;

    public CarService(CarRepository repository, MapsClient mapsClient, PriceClient priceClient) {
        this.repository = repository;
        this.mapsClient = mapsClient;
        this.priceClient = priceClient;
    }

    /**
     * Gathers a list of all vehicles
     *
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return repository.findAll()
                .stream()
                .map(car -> {
                    //Use the Pricing Web client to get the price based on the `id` input'.
                    car.setPrice(priceClient.getPrice(car.getId()));
                    //Use the Maps Web client to get the address for the vehicle.
                    car.setLocation(mapsClient.getAddress(car.getLocation()));
                    return car;
                }).collect(Collectors.toList());
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     *
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        /**
         *   Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         *
         */
        return repository.findById(id)
                .map(car -> {
                    //Use the Pricing Web client to get the price based on the `id` input'.
                    car.setPrice(priceClient.getPrice(car.getId()));
                    //Use the Maps Web client to get the address for the vehicle.
                    car.setLocation(mapsClient.getAddress(car.getLocation()));
                    return car;
                })
                .orElseThrow(CarNotFoundException::new);
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     *
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        carToBeUpdated.setCondition(car.getCondition());
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }

        return repository.save(car);
    }

    /**
     * Deletes a given car by ID
     *
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        /**
         *  Find the car by ID from the `repository` if it exists.
         *  If it does not exist, throw a CarNotFoundException
         */
        Car carToBeDeleted = repository.findById(id).orElseThrow(CarNotFoundException::new);

        /**
         *  Delete the car from the repository.
         */
        repository.delete(carToBeDeleted);
    }
}
