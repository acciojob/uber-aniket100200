package com.driver.services.impl;

import com.driver.model.Cab;
import com.driver.repository.CabRepository;
import com.driver.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Driver;
import com.driver.repository.DriverRepository;

import java.util.ArrayList;

@Service
public class DriverServiceImpl implements DriverService {

	@Autowired
	DriverRepository driverRepository3;

	@Autowired
	CabRepository cabRepository3;

	@Override
	public void register(String mobile, String password)
	{
		//Save a driver in the database having given details and a cab with ratePerKm as 10 and availability as True by default.
		Driver driver=new Driver();
		driver.setMobile(mobile);
		driver.setPassword(password);

		Cab cab=new Cab();
		cab.setAvailable(true);
		cab.setPerKmRate(10);
		cab.setDriver(driver);

		//bidirectional mapping
		driver.setCab(cab);
		driver.setTripBookingList(new ArrayList<>());

		//just save the driver..

		driverRepository3.save(driver);

		//done registration..


	}

	@Override
	public void removeDriver(int driverId)
	{
		// Delete driver without using deleteById function
		Driver driver=driverRepository3.findById(driverId).get();
		if(driver==null)return;
		driverRepository3.delete(driver); //done deletion by driver object.
	}

	@Override
	public void updateStatus(int driverId)
	{
		//Set the status of respective car to unavailable
		Driver driver=driverRepository3.findById(driverId).get();
		if(driver==null)return;

		driver.getCab().setAvailable(false);

		driverRepository3.save(driver);
		//done the satus save
	}
}
