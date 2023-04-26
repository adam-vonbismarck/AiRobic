package edu.brown.cs.student.main.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Moshi.Builder;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import okio.BufferedSource;

public interface Handler {

  /** serializeMap() uses Moshi to create JSON files*/
  default String serialize(Map<String, Object> map) {
    Moshi moshi = new Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(type);
    return adapter.toJson(map);
  }

  /**
   *  uses Moshi to convert a BufferedSource to some generic class.
   */
  public static <T> T fromJsonGeneric(BufferedSource jsonStr, Class<T> generic) throws IOException {
    try {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(generic).fromJson(jsonStr);
    } catch (IOException e) {
      throw e;
    }
  }
}
