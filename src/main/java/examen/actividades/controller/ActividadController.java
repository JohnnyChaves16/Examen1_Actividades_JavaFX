package examen.actividades.controller;

import examen.actividades.model.Actividad;
import examen.actividades.model.TipoActividad;
import examen.actividades.repository.RepositorioActividadTxt;
import examen.actividades.service.ActividadService;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class ActividadController {

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTarifaBase;

    @FXML
    private TextField txtCupoTotal;

    @FXML
    private TextField txtCodigoConsulta;

    @FXML
    private ComboBox<TipoActividad> cmbTipo;

    @FXML
    private TextArea txaResultados;

    @FXML
    private Label lblMensaje;

    private ActividadService service;

    @FXML
    public void initialize() {
        iniciar();
    }

    public void iniciar() {

        service = new ActividadService(
                new RepositorioActividadTxt(
                        "actividades.txt"
                )
        );

        cmbTipo.getItems().setAll(
                TipoActividad.values()
        );

        cmbTipo.setValue(null);

        try {

            service.cargarDatos();
            mostrarTodas();

        } catch (IOException e) {

            lblMensaje.setText(
                    "Error al cargar: " + e.getMessage()
            );
        }
    }

    @FXML
    private void registrar() {

        try {

            double tarifa =
                    Double.parseDouble(
                            txtTarifaBase.getText().trim()
                    );

            int cupo =
                    Integer.parseInt(
                            txtCupoTotal.getText().trim()
                    );

            service.registrarActividad(
                    txtCodigo.getText(),
                    txtNombre.getText(),
                    cmbTipo.getValue(),
                    tarifa,
                    cupo
            );

            lblMensaje.setText(
                    "Actividad registrada correctamente"
            );

            mostrarTodas();

        } catch (NumberFormatException e) {

            lblMensaje.setText(
                    "Tarifa y cupo deben ser numeros validos"
            );

        } catch (IllegalArgumentException e) {

            lblMensaje.setText(e.getMessage());
        }
    }

    @FXML
    private void buscar() {

        try {

            Actividad actividad =
                    service.buscarPorCodigo(
                            txtCodigoConsulta.getText()
                    );

            if (actividad == null) {

                txaResultados.clear();

                lblMensaje.setText(
                        "Actividad no encontrada"
                );

            } else {

                txaResultados.setText(
                        formatoActividad(actividad)
                );

                lblMensaje.setText(
                        "Actividad encontrada"
                );
            }

        } catch (IllegalArgumentException e) {

            txaResultados.clear();
            lblMensaje.setText(e.getMessage());
        }
    }

    @FXML
    private void inscribir() {

        try {

            service.inscribir(
                    txtCodigoConsulta.getText()
            );

            lblMensaje.setText(
                    "Inscripcion realizada"
            );

            mostrarTodas();

        } catch (IllegalArgumentException |
                 IllegalStateException e) {

            lblMensaje.setText(e.getMessage());
        }
    }

    @FXML
    private void mostrarTodas() {

        List<Actividad> actividades =
                service.listarActividades();

        if (actividades.isEmpty()) {

            txaResultados.setText(
                    "No hay actividades registradas"
            );

            return;
        }

        StringBuilder resultado =
                new StringBuilder();

        for (Actividad actividad : actividades) {

            resultado.append(
                    formatoActividad(actividad)
            );

            resultado.append(
                    System.lineSeparator()
            );
        }

        txaResultados.setText(
                resultado.toString()
        );
    }

    private String formatoActividad(
            Actividad actividad) {

        return "Codigo: "
                + actividad.getCodigo()
                + "\nNombre: "
                + actividad.getNombre()
                + "\nTipo: "
                + actividad.getTipo()
                + "\nTarifa final: "
                + String.format(
                "%.2f",
                actividad.calcularTarifaFinal()
        )
                + "\nCupo total: "
                + actividad.getCupoTotal()
                + "\nInscritos: "
                + actividad.getInscritos()
                + "\nDisponibles: "
                + actividad.getCuposDisponibles()
                + "\n";
    }

    @FXML
    private void limpiar() {

        txtCodigo.clear();
        txtNombre.clear();
        txtTarifaBase.clear();
        txtCupoTotal.clear();
        txtCodigoConsulta.clear();

        cmbTipo.setValue(null);

        lblMensaje.setText("");
    }

    @FXML
    private void guardarDatos() {

        try {

            service.guardarDatos();

            lblMensaje.setText(
                    "Datos guardados correctamente"
            );

        } catch (IOException e) {

            lblMensaje.setText(
                    "Error al guardar: "
                            + e.getMessage()
            );
        }
    }
}