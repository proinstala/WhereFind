package io.proinstala.wherefind.shared.dtos;

/**
 * La clase CardDTO representa un objeto de transferencia de datos (DTO, Data Transfer Object)
 * que encapsula la información de una tarjeta (card) para ser utilizada en la interfaz de usuario
 * o para ser transferida entre capas de la aplicación.
 *
 * @param classIcon   El nombre de la clase CSS del icono que se mostrará en la tarjeta
 * @param titulo      El título de la tarjeta
 * @param descripcion Una breve descripción del contenido de la tarjeta
 * @param url         La URL asociada con la tarjeta
 */
public record CardDTO(String classIcon, String titulo, String descripcion, String url) {

}
