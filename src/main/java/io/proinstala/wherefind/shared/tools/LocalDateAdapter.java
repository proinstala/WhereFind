
package io.proinstala.wherefind.shared.tools;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * {@code LocalDateAdapter} es un adaptador para la librería Gson que permite
 * la serialización y deserialización de objetos {@link LocalDate}.
 * <p>
 * Convierte un {@code LocalDate} a su representación en formato ISO-8601
 * (ejemplo: {@code "2025-09-01"}) y viceversa.
 * </p>
 *
 * <p>
 * Uso típico:
 * <pre>{@code
 * Gson gson = new GsonBuilder()
 *      .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
 *      .create();
 *
 * String json = gson.toJson(LocalDate.now()); // Serializa
 * LocalDate fecha = gson.fromJson("\"2025-09-01\"", LocalDate.class); // Deserializa
 * }</pre>
 * </p>
 *
 */
public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    
    /** Formato estándar ISO para fechas (YYYY-MM-DD). */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Serializa un objeto {@link LocalDate} a un {@link JsonElement}.
     *
     * @param date el objeto {@code LocalDate} a serializar (puede ser {@code null})
     * @param typeOfSrc el tipo de la fuente (no se usa en este caso)
     * @param context el contexto de serialización de Gson
     * @return un {@link JsonPrimitive} con la fecha en formato ISO-8601,
     *         o {@link JsonNull} si la fecha es {@code null}
     */
    @Override
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        return date == null ? JsonNull.INSTANCE : new JsonPrimitive(date.format(FORMATTER));
    }

    /**
     * Deserializa un {@link JsonElement} a un objeto {@link LocalDate}.
     *
     * @param json el elemento JSON a deserializar (puede ser {@code null} o {@link JsonNull})
     * @param typeOfT el tipo esperado de destino (no se usa en este caso)
     * @param context el contexto de deserialización de Gson
     * @return el objeto {@link LocalDate} resultante o {@code null} si el JSON es nulo
     * @throws JsonParseException si el formato del JSON no corresponde con el esperado (ISO-8601)
     */
    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return json == null || json.isJsonNull() ? null : LocalDate.parse(json.getAsString(), FORMATTER);
    }
    
}
