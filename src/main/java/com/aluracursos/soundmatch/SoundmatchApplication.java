package com.aluracursos.soundmatch;

import com.aluracursos.soundmatch.prinpipal.Principal;
import com.aluracursos.soundmatch.respository.ArtistaRepository;
import com.aluracursos.soundmatch.respository.CancionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SoundmatchApplication implements CommandLineRunner {
	@Autowired
	private ArtistaRepository repositorio;
	@Autowired
	private CancionRepository cancionRepository;

	public static void main(String[] args) {
		SpringApplication.run(SoundmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repositorio, cancionRepository);
		principal.muestraElMenu();
	}
}
