package examen.actividades.service;

import examen.actividades.model.Actividad;
import examen.actividades.model.ActividadPresencial;
import examen.actividades.model.ActividadVirtual;
import examen.actividades.model.TipoActividad;
import examen.actividades.repository.Repositorio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActividadService {

    private List<Actividad> actividades;

    private Repositorio<Actividad> repositorio;

    public ActividadService(
            Repositorio<Actividad> repositorio) {

        this.repositorio = repositorio;
        this.actividades = new ArrayList<>();
    }

    public void registrarActividad(
            String codigo,
            String nombre,
            TipoActividad tipo,
            double tarifaBase,
            int cupoTotal) {

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un tipo"
            );
        }

        if (buscarPorCodigo(codigo) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una actividad con ese codigo"
            );
        }

        Actividad actividad;

        if (tipo == TipoActividad.PRESENCIAL) {

            actividad = new ActividadPresencial(
                    codigo,
                    nombre,
                    tarifaBase,
                    cupoTotal,
                    0
            );

        } else {

            actividad = new ActividadVirtual(
                    codigo,
                    nombre,
                    tarifaBase,
                    cupoTotal,
                    0
            );
        }

        actividades.add(actividad);
    }

    public Actividad buscarPorCodigo(String codigo) {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe indicar un codigo"
            );
        }

        String codigoBuscado = codigo.trim();

        for (Actividad actividad : actividades) {

            if (actividad.getCodigo()
                    .equalsIgnoreCase(codigoBuscado)) {

                return actividad;
            }
        }

        return null;
    }

    public List<Actividad> listarActividades() {
        return new ArrayList<>(actividades);
    }

    public void inscribir(String codigo) {

        Actividad actividad =
                buscarPorCodigo(codigo);

        if (actividad == null) {

            throw new IllegalArgumentException(
                    "Actividad no encontrada"
            );
        }

        actividad.inscribir();
    }

    public void cargarDatos() throws IOException {

        List<Actividad> cargadas =
                repositorio.cargarTodos();

        actividades = new ArrayList<>(cargadas);
    }

    public void guardarDatos() throws IOException {
        repositorio.guardarTodos(actividades);
    }
}