package croatia.rit.edu.service;
// Custom Gson Date Adapter for java.sql.Date
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class SqlDateAdapter extends TypeAdapter<java.sql.Date> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void write(JsonWriter out, java.sql.Date value) throws IOException {
        out.value(value == null ? null : dateFormat.format(value));
    }

    @Override
    public java.sql.Date read(JsonReader in) throws IOException {
        try {
            String date = in.nextString();
            return date == null ? null : new java.sql.Date(dateFormat.parse(date).getTime());
        } catch (Exception e) {
            throw new IOException("Failed to parse date", e);
        }
    }
}
