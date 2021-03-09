package util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DateTypeAdapter extends TypeAdapter<Date> {

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");

	@Override
	public void write(JsonWriter out, Date value) throws IOException {
		if (value != null) {
			out.value(simpleDateFormat.format(value));
		} else
			out.nullValue();

	}

	@Override
	public Date read(JsonReader in) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
