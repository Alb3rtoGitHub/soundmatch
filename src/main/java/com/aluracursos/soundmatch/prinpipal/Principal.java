package com.aluracursos.soundmatch.prinpipal;

import com.aluracursos.soundmatch.model.Artista;
import com.aluracursos.soundmatch.model.Cancion;
import com.aluracursos.soundmatch.model.TipoArtista;
import com.aluracursos.soundmatch.repository.ArtistaRepository;
import com.aluracursos.soundmatch.repository.CancionRepository;

import java.util.*;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ArtistaRepository repositorio;
    private CancionRepository repositorioCancion;

    public Principal(ArtistaRepository repository, CancionRepository cancionRepository) {
        this.repositorio = repository;
        this.repositorioCancion = cancionRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                    ***** SOUNDMATCH MORE MUSIC! *****
                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    1. Registrar Artista
                    2. Registrar Canción
                    3. Listar Artistas
                    4. Listar Canciones
                    5. Buscar Artistas por Nombre
                    6. Buscar Canciones por Artista
                    7. Buscar Canción por Título
                    8. Buscar datos de un Artista
                    
                    0. Salir
                    """;

            System.out.println(menu);
            System.out.print("Opción: ");
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    registarArtista();
                    break;
                case 2:
                    registarCancion();
                    break;
                case 3:
                    listarArtistas();
                    break;
                case 4:
                    listarCanciones();
                    break;
                case 5:
                    buscarArtistaPorNombre();
                    break;
                case 6:
                    buscarCancionesPorArtista();
                    break;
                case 7:
                    buscarCancionPorTitulo();
                    break;
//                case 8:
//                    buscarDatosDelArtista();
//                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción no válida");

            }
        }
    }

    private void registarArtista() {
        var registrarNuevoArtista = "S";

        while (registrarNuevoArtista.equalsIgnoreCase("s")) {
            TipoArtista tipoArtista = null;

            System.out.println("""
                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    REGISTRAR ARTISTA
                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~""");
            System.out.print("Ingrese el nombre del Artista: ");
            var nuevo = teclado.nextLine();

            while (tipoArtista == null) {
                System.out.print("Ingrese el tipo de artista [Solista, Duo, Banda]: ");
                var tipo = teclado.nextLine();
                try {
                    tipoArtista = TipoArtista.valueOf(tipo.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Tipo de Artista no válido. Por favor, ingrese uno de los siguientes: Solista, Duo, Banda.");
                }
            }

            Artista artista = new Artista(nuevo, tipoArtista);

            repositorio.save(artista);

            System.out.print("¿Desea registrar nuevo Artista? [S/N]: ");
            registrarNuevoArtista = teclado.nextLine();
        }
    }

    private void registarCancion() {
        var registrarNuevaCancion = "S";

        while (registrarNuevaCancion.equalsIgnoreCase("s")) {
            listarArtistas();

            System.out.println("""
                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    REGISTRAR CANCIÓN
                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~""");
            System.out.print("Ingrese el nombre del Artista que interpreta la canción: ");
            var nombreArtista = teclado.nextLine();

            Optional<Artista> artista = repositorio.findByNombreContainingIgnoreCase(nombreArtista);

            if (artista.isPresent()) {
                System.out.print("Ingrese el Título de la canción: ");
                var nombreCancion = teclado.nextLine();
                Cancion cancion = new Cancion(nombreCancion, artista.get());
                artista.get().getCanciones().add(cancion);
                repositorio.save(artista.get());
            } else {
                System.out.println("Artista No Encontrado.");
            }
            System.out.print("¿Desea registrar otra Canción? [S/N]: ");
            registrarNuevaCancion = teclado.nextLine();
        }
    }

    private void listarArtistas() {
        List<Artista> artistas = repositorio.findAll();
        System.out.println("""
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                LISTA DE ARTISTAS
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~""");
        artistas.forEach(a -> System.out.println(a));
    }

    private void listarCanciones() {
        List<Artista> artistas = repositorio.findAll();
        System.out.println("""
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                LISTA DE CANCIONES
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~""");
        // Canciones ordenadas alfabéticamente dentro de cada artista
//        artistas.forEach(a -> a.getCanciones().stream()
//                .sorted(Comparator.comparing(Cancion::getTitulo))
//                .forEach(System.out::println));

        // Todas las canciones ordenadas alfabéticamente
        List<Cancion> todasCanciones = artistas.stream()
                .flatMap(a -> a.getCanciones().stream())
                .sorted(Comparator.comparing(Cancion::getTitulo))
                .toList();
        todasCanciones.forEach(System.out::println);
    }

    private void buscarArtistaPorNombre() {
        System.out.println("""
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                BUSCAR ARTISTA POR NOMBRE
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~""");
        System.out.print("Ingrese el nombre del Artista que desea buscar: ");
        var nombre = teclado.nextLine();

        List<Artista> artistas = repositorio.findByNombreContainsIgnoreCase(nombre);

        System.out.println("""
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                ARTISTA POR NOMBRE
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~""");

        if (artistas.isEmpty()) {
            System.out.println("No se encontraron artistas con ese nombre.");
        } else {
            // Mostrar los artistas encontrados
            System.out.println("Artistas encontrados:");
            artistas.forEach(System.out::println);
        }
    }

    private void buscarCancionesPorArtista() {
        System.out.println("""
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                BUSCAR CANCIONES POR ARTISTA
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~""");
        System.out.print("Ingrese el nombre del artista cuyas canciones desea buscar: ");
        var nombre = teclado.nextLine();
        List<Cancion> canciones = repositorio.buscaCancionesPorArtista(nombre);
        System.out.println("""
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                CANCIONES POR ARTISTA
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~""");
        canciones.forEach(System.out::println);
    }

    private void buscarCancionPorTitulo() {

        System.out.println("""
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                BUSCAR CANCIÓN POR TÍTULO
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~""");
        System.out.print("Ingrese el título de la canción que desea buscar: ");
        String titulo = teclado.nextLine();

        // Usando el metodo de derived query
        List<Cancion> canciones = repositorioCancion.findByTituloContainsIgnoreCase(titulo);

        // O si estás usando JPQL o Native Query
//        List<Cancion> canciones = repositorio.buscarPorTitulo(titulo);

        System.out.println("""
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                CANCIONES POR TÍTULO
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~""");
        if (canciones.isEmpty()) {
            System.out.println("No se encontraron canciones con el Título: " + titulo);
        } else {
            System.out.println("Canciones encontradas:");
            canciones.forEach(System.out::println);
        }
    }

    // Para utilizar ChatGPT es necesario una API-KEY y tener crédito en la cuenta.
    //    private void buscarDatosDelArtista() {
//        System.out.print("¿Sobre que Artista desea buscar datos?: ");
//        var nombre = teclado.nextLine();
//        var respuesta = ConsultaChatGPT.obtenerInformacion(nombre);
//        System.out.println(respuesta.trim());
//    }
}
