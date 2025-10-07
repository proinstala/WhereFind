
package io.proinstala.wherefind.shared.tools;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.proinstala.wherefind.shared.consts.Disponibilidad;
import java.io.IOException;


/**
 * Adaptador personalizado de Gson para (de)serializar {@link Disponibilidad}.
 * 
 * Serialización:
 * - DISPONIBLE → "Disponible"
 * - NO_DISPONIBLE → "No disponible"
 * 
 * Deserialización:
 * - "Disponible" o true  → DISPONIBLE
 * - "No disponible" o false → NO_DISPONIBLE
 */
public class DisponibilidadAdapter extends TypeAdapter<Disponibilidad> {
    
    @Override
    public void write(JsonWriter out, Disponibilidad value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.toString()); // Usa "Disponible" o "No disponible"
    }

    @Override
    public Disponibilidad read(JsonReader in) throws IOException {
    switch (in.peek()) {
        case NULL:
            in.nextNull();
            return null;

        case BOOLEAN:
            boolean boolValue = in.nextBoolean();
            return boolValue ? Disponibilidad.DISPONIBLE : Disponibilidad.NO_DISPONIBLE;

        case STRING:
            String jsonValue = in.nextString().trim().toLowerCase();
            switch (jsonValue) {
                case "disponible":
                case "true":
                    return Disponibilidad.DISPONIBLE;
                case "no disponible":
                case "false":
                    return Disponibilidad.NO_DISPONIBLE;
                default:
                    throw new IllegalArgumentException("Valor no válido para Disponibilidad: " + jsonValue);
            }

        default:
            throw new IllegalStateException("Tipo inesperado en JSON para Disponibilidad: " + in.peek());
    }
}
    
}
