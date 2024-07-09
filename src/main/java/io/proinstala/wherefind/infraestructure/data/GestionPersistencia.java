package io.proinstala.wherefind.infraestructure.data;

import io.proinstala.wherefind.infraestructure.data.mysql.GestionPersistenciaMySql;

public class GestionPersistencia {
    private IGestorPersistencia gestorPersistencia;

    public GestionPersistencia() {
        //TODO: Crear el gestor de persistencia
        this.gestorPersistencia = new GestionPersistenciaMySql();
    }

    public GestionPersistencia(IGestorPersistencia gestorPersistencia) {
        this.gestorPersistencia = gestorPersistencia;
    }

    public IGestorPersistencia getGestorPersistencia() {
        return gestorPersistencia;
    }
}
