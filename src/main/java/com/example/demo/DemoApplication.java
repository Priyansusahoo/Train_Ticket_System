package com.example.demo;

import com.example.demo.model.Seat;
import com.example.demo.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private SeatRepository seatRepository;

	@Value("${numOfSeatInEachSec.number}")
	public int numOfSeatInEachSec;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String[] args) throws Exception {
		// Initialize seats for section A
		for (int i = 1; i <= numOfSeatInEachSec; i++) {
			seatRepository.save(new Seat(null, "A", String.valueOf(i), false));
		}
		// Initialize seats for section B
		for (int i = 1; i <= numOfSeatInEachSec; i++) {
			seatRepository.save(new Seat(null, "B", String.valueOf(i), false));
		}
	}
}
