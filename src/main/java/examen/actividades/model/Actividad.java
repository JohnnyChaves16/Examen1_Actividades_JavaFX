package examen.actividades.model;

public abstract class Actividad {

    private String codigo;
    private String nombre;
    private double tarifaBase;
    private int cupoTotal;
    private int inscritos;

    public Actividad(String codigo, String nombre,
                     double tarifaBase, int cupoTotal, int inscritos) {

        validarTexto(codigo, "El codigo es obligatorio");
        validarTexto(nombre, "El nombre es obligatorio");

        if (!Double.isFinite(tarifaBase) || tarifaBase <= 0) {
            throw new IllegalArgumentException(
                    "La tarifa debe ser mayor que cero"
            );
        }

        if (cupoTotal <= 0) {
            throw new IllegalArgumentException(
                    "El cupo total debe ser mayor que cero"
            );
        }

        if (inscritos < 0 || inscritos > cupoTotal) {
            throw new IllegalArgumentException(
                    "Cantidad de inscritos invalida"
            );
        }

        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.tarifaBase = tarifaBase;
        this.cupoTotal = cupoTotal;
        this.inscritos = inscritos;
    }

    private void validarTexto(String texto, String mensaje) {

        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }

        if (texto.contains(";")
                || texto.contains("\n")
                || texto.contains("\r")) {

            throw new IllegalArgumentException(
                    "El texto contiene caracteres no permitidos"
            );
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getTarifaBase() {
        return tarifaBase;
    }

    public int getCupoTotal() {
        return cupoTotal;
    }

    public int getInscritos() {
        return inscritos;
    }

    public int getCuposDisponibles() {
        return cupoTotal - inscritos;
    }

    public void inscribir() {

        if (inscritos >= cupoTotal) {
            throw new IllegalStateException(
                    "No hay cupos disponibles"
            );
        }

        inscritos++;
    }

    public abstract double calcularTarifaFinal();

    public abstract TipoActividad getTipo();
}
