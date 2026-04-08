package com.javaacademy.flat_rent;

import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.entity.Client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AblazzingFlatRentApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AblazzingFlatRentApplication.class, args);
//		Advert advert = context.getBean(Advert.class);
//		Apartment apartment = context.getBean(Apartment.class);
//		Booking booking = context.getBean(Booking.class);
//		Client client = context.getBean(Client.class);
	}
}
