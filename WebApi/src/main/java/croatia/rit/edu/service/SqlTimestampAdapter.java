package croatia.rit.edu.service;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class SqlTimestampAdapter extends TypeAdapter<java.sql.Timestamp> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void write(JsonWriter out, java.sql.Timestamp value) throws IOException {
        out.value(value == null ? null : dateFormat.format(value));
    }

    @Override
    public java.sql.Timestamp read(JsonReader in) throws IOException {
        try {
            String date = in.nextString();
            return date == null ? null : new java.sql.Timestamp(dateFormat.parse(date).getTime());
        } catch (Exception e) {
            throw new IOException("Failed to parse timestamp", e);
        }
    }
}
