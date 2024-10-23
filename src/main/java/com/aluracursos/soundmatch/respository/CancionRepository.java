package com.aluracursos.soundmatch.respository;

import com.aluracursos.soundmatch.model.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CancionRepository extends JpaRepository<Cancion, Long> {
    List<Cancion> findByTituloContainsIgnoreCase(String titulo);
}
