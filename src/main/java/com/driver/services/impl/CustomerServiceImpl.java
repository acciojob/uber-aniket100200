package com.driver.services.impl;

import com.driver.model.TripBooking;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;
import com.driver.model.TripStatus;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer)
	{
		//Save the customer in database
		//let's save the customer to database
		customerRepository2.save(customer);

	}

	@Override
	public void deleteCustomer(Integer customerId)
	{
		// Delete customer without using deleteById function
		customerRepository2.deleteById(customerId);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception
	{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		Customer customer=customerRepository2.findById(customerId).get();
		if(customer==null)return null;
		//customer is found..
		List<Driver>drivers=driverRepository2.findAll();
		int driverId=0;
		Driver driver1=null;
		for(Driver driver:drivers)
		{
			if(driver.getCab().getAvailable())
			{
				if(driverId==0 || driver.getDriverId()<driverId)
				{
					driverId=driver.getDriverId();
					driver1=driver;
				}
			}
		}

		if(driverId==0)throw new Exception("No cab available!");

		//means you have the driver..
		//you have that driver..
		Driver driver=driver1;

		//exception is done..
		TripBooking tripBooking=new TripBooking();
		//let's set the driver..
		tripBooking.setCustomer(customer);
		tripBooking.setDriver(driver);
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setDistanceInKm(distanceInKm);
		tripBooking.setStatus(TripStatus.CONFIRMED);
		//all done..

	tripBooking =tripBookingRepository2.save(tripBooking);

	//firstly save to override the same method..
		//let's have the bidirection mapping..
		driver.getTripBookingList().add(tripBooking);
		driver.getCab().setAvailable(false);

		//customer side..
		customer.getTripBookingList().add(tripBooking);

		driverRepository2.save(driver);
		customerRepository2.save(customer);


		return tripBooking;

	}

	private int getBill(int distanceInKm, int perKmRate)
	{
		return distanceInKm*perKmRate;
	}

	@Override
	public void cancelTrip(Integer tripId)
	{
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly

		TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();

		tripBooking.setStatus(TripStatus.CANCELED);

		//get the cab..
		Driver driver=tripBooking.getDriver();

		driver.getCab().setAvailable(true);

		//we have also the customer
		tripBooking.setBill(0);

		//just save the booking..
		Customer customer=tripBooking.getCustomer();

		customerRepository2.save(customer);
		driverRepository2.save(driver);

		tripBookingRepository2.delete(tripBooking);

	}

	@Override
	public void completeTrip(Integer tripId)
	{
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();

		//set the status and bill
		tripBooking.setStatus(TripStatus.COMPLETED);
		tripBooking.setBill(getBill(tripBooking.getDistanceInKm(),tripBooking.getDriver().getCab().getPerKmRate()));

		//aur kuch..
		Driver driver=tripBooking.getDriver();
		driver.getCab().setAvailable(true);

		Customer customer=tripBooking.getCustomer();

		//just save the customer and driver..

		driverRepository2.save(driver);
		customerRepository2.save(customer);

		tripBookingRepository2.delete(tripBooking);

	}
}
