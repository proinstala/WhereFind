package io.proinstala.wherefind.shared.dtos;

/**
 * Registro de tipo ResponseDTO utilizado para representar una respuesta del servicio.
 *
 * @param isError Indicador de error (1 si es un error, 0 si no lo es)
 * @param isUrl   Indicador de URL (1 si la respuesta contiene una URL, 0 si no lo hace)
 * @param result  Resultado de la operación
 * @param user    Información del usuario relacionada con la respuesta
 */
public record ResponseDTO(int isError, int isUrl, String result, Object user) {

}
