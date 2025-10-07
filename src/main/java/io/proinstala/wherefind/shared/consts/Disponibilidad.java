
package io.proinstala.wherefind.shared.consts;


/**
 * Representa el estado de disponibilidad de un recurso u objeto.
 * <p>
 * Cada constante enum contiene un valor booleano asociado:
 * <ul>
 *   <li>{@link #DISPONIBLE} → {@code true}</li>
 *   <li>{@link #NO_DISPONIBLE} → {@code false}</li>
 * </ul>
 * </p>
 * 
 * Permite consultar tanto el valor booleano como una representación textual legible.
 * 
 * @author David
 */
public enum Disponibilidad {
    
    /** Estado que indica que el recurso no está disponible. */
    NO_DISPONIBLE(false),
    
    /** Estado que indica que el recurso está disponible. */
    DISPONIBLE(true);
    
    
    /** Valor booleano asociado a la disponibilidad. */
    private final boolean valor;

    
    /**
     * Constructor privado del enum.
     *
     * @param valor valor booleano que representa la disponibilidad
     */
    Disponibilidad(boolean valor) {
        this.valor = valor;
    }
    
    /**
     * Devuelve el valor booleano asociado al estado.
     *
     * @return {@code true} si está disponible, {@code false} en caso contrario
     */
    public boolean getValor() {
        return valor;
    }
    
    
    /**
     * Obtiene una instancia de {@link Disponibilidad} a partir de un valor booleano.
     *
     * @param valor valor booleano de la disponibilidad
     * @return {@link #DISPONIBLE} si el valor es {@code true},
     *         {@link #NO_DISPONIBLE} en caso contrario
     */
    public static Disponibilidad fromValue(boolean valor) {
        return valor ? DISPONIBLE : NO_DISPONIBLE;
    }
    
    /**
     * Devuelve una representación en texto legible del estado.
     *
     * @return "Disponible" si el valor es {@code true}, "No disponible" si es {@code false}
     */
    @Override
    public String toString() {
        return valor ? "Disponible" : "No disponible";
    }
}
