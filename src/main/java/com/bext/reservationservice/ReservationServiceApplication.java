package com.bext.reservationservice;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@SpringBootApplication
public class ReservationServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}
}

@Component
@Log4j2
@RequiredArgsConstructor
class SamplaDataInitializr {
	private final ReservationRepository reservationRepository;

	@EventListener(ApplicationReadyEvent.class)
	public void go() {
        this.reservationRepository.deleteAll().subscribe();
		Flux<String> nombres = Flux.just("Hugo","Paco","Luis","Daisy","McPato","Beto","Guille","Juan");
		Flux<Reservation> reservations = nombres.map(nombre -> new Reservation( null, nombre));
		Flux<Reservation> saved = reservations.flatMap(this.reservationRepository::save);
        saved.subscribe(new Consumer<Reservation>() {
            @Override
            public void accept(Reservation reservation) {
                log.info( reservation);
            }
        });
	}
}

interface ReservationRepository extends ReactiveCrudRepository<Reservation, String> {
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
class Reservation {
	@Id
	private String id;
	private String nombre;
}