package io.proinstala.wherefind.infraestructure.data;

public class GestionPersistencia {
    private IGestorPersistencia gestorPersistencia;

    public GestionPersistencia() {
        //TODO: Crear el gestor de persistencia
        this.gestorPersistencia = null;
    }

    public GestionPersistencia(IGestorPersistencia gestorPersistencia) {
        this.gestorPersistencia = gestorPersistencia;
    }

    public IGestorPersistencia getGestorPersistencia() {
        return gestorPersistencia;
    }
}
