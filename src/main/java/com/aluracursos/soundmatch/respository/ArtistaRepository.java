package com.aluracursos.soundmatch.respository;

import com.aluracursos.soundmatch.model.Artista;
import com.aluracursos.soundmatch.model.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {

    List<Artista> findAll();

    Optional<Artista> findByNombreContainingIgnoreCase(String nombre);

    List<Artista> findByNombreContainsIgnoreCase(String nombre);

    @Query("SELECT c FROM Artista a JOIN a.canciones c WHERE a.nombre ILIKE %:nombre%")
    List<Cancion> buscaCancionesPorArtista(String nombre);

    // Usando JPQL
//    @Query("SELECT c FROM Cancion c WHERE LOWER(c.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
//    List<Cancion> buscarPorTitulo(@Param("titulo") String titulo);

    // Usando Native Query
//    @Query(value = "SELECT * FROM canciones WHERE LOWER(titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))", nativeQuery = true)
//    List<Cancion> buscarPorTituloNativo(@Param("titulo") String titulo);
}
