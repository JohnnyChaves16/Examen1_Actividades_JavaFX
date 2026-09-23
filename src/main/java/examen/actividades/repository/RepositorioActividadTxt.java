package examen.actividades.repository;

import examen.actividades.model.Actividad;
import examen.actividades.model.ActividadPresencial;
import examen.actividades.model.ActividadVirtual;
import examen.actividades.model.TipoActividad;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RepositorioActividadTxt
        implements Repositorio<Actividad> {

    private String rutaArchivo;

    public RepositorioActividadTxt(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public List<Actividad> cargarTodos() throws IOException {

        List<Actividad> actividades = new ArrayList<>();

        Path ruta = Path.of(rutaArchivo);

        if (!Files.exists(ruta)) {
            return actividades;
        }

        List<String> lineas = Files.readAllLines(ruta);

        for (String linea : lineas) {

            if (!linea.trim().isEmpty()) {
                actividades.add(convertirDesdeLinea(linea));
            }
        }

        return actividades;
    }

    @Override
    public void guardarTodos(List<Actividad> elementos)
            throws IOException {

        List<String> lineas = new ArrayList<>();

        for (Actividad actividad : elementos) {
            lineas.add(convertirALinea(actividad));
        }

        Files.write(Path.of(rutaArchivo), lineas);
    }

    private String convertirALinea(Actividad actividad) {

        return actividad.getTipo() + ";"
                + actividad.getCodigo() + ";"
                + actividad.getNombre() + ";"
                + actividad.getTarifaBase() + ";"
                + actividad.getCupoTotal() + ";"
                + actividad.getInscritos();
    }

    private Actividad convertirDesdeLinea(String linea) {

        String[] datos = linea.split(";");

        TipoActividad tipo =
                TipoActividad.valueOf(datos[0]);

        String codigo = datos[1];
        String nombre = datos[2];

        double tarifa =
                Double.parseDouble(datos[3]);

        int cupo =
                Integer.parseInt(datos[4]);

        int inscritos =
                Integer.parseInt(datos[5]);

        if (tipo == TipoActividad.PRESENCIAL) {

            return new ActividadPresencial(
                    codigo,
                    nombre,
                    tarifa,
                    cupo,
                    inscritos
            );

        } else {

            return new ActividadVirtual(
                    codigo,
                    nombre,
                    tarifa,
                    cupo,
                    inscritos
            );
        }
    }
}