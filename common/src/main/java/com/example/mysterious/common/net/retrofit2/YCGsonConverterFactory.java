package com.example.mysterious.common.net.retrofit2;

import com.example.mysterious.common.net.model.HttpResult;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by hanbo on 2018/3/12.
 */

public class YCGsonConverterFactory  extends Converter.Factory {
    public static YCGsonConverterFactory create() {
        return new YCGsonConverterFactory(new Gson());
    }


    private final Gson gson;

    private YCGsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new YCGsonResponseBodyConverter<>(gson, adapter,type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new YCGsonRequestBodyConverter<>(gson, adapter);
    }



    //请求的转换器
    static final class YCGsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private static final Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        YCGsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, value);
            jsonWriter.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }



    //响应的转换器
    static final class YCGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final TypeAdapter<T> adapter;
        Type mType;

        YCGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, Type type) {
            this.gson = gson;
            this.adapter = adapter;
            mType = type;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            return getResponseBody(mType,value);
        }

        //解析
        private T getResponseBody(Type type, ResponseBody value) throws ParseError{
            Class<?> rawType = getRawType(type);
            String jsonString = null;   //原始string，可能有可能没有，尽量获取了
            try {
                if(rawType==HttpResult.class){
                    return getYCResult(type, value);
                }else if(rawType==String.class){
                    jsonString = value.string();
                    return (T)jsonString;
                }else if(rawType==JSONObject.class){
                    jsonString = value.string();
                    return (T) new JSONObject(jsonString);
                }else if(rawType==JSONArray.class){
                    jsonString = value.string();
                    return (T) new JSONArray(jsonString);
                }else{
                    JsonReader jsonReader = gson.newJsonReader(value.charStream());
                    try {
                        return adapter.read(jsonReader);
                    } finally {
                        value.close();
                    }
                }
            } catch (Exception e) {
                throw new ParseError(YCAPI.SERVER_FAIL_DESC,e).setJsonString(jsonString);
            }
        }

        //type为原始type
        private T getYCResult(Type type, ResponseBody value) throws IOException, JSONException {
            String response = value.string();
            JSONObject jo = new JSONObject(response);
            int status = jo.getInt("status");
            String message = jo.optString("message");
            String dataString = jo.optString("data");

            HttpResult httpResult = new HttpResult();
            httpResult.status = status;
            httpResult.message = message;
            httpResult.originJsonString = response;

            if(httpResult.isSuccess()){
                if (!(type instanceof ParameterizedType)) {
                    throw new IllegalStateException("HttpResult need config fanxing");
                }
                Type dataType = getParameterUpperBound(0, (ParameterizedType) type);
                Class<?> rawDataType = getRawType(dataType);

                if(rawDataType==String.class){
                    httpResult.data = dataString;
                }else if(rawDataType==JSONObject.class){
                    httpResult.data = new JSONObject(dataString);
                }else if(rawDataType==JSONArray.class){
                    httpResult.data = new JSONArray(dataString);
                }else {
                    httpResult.data = gson.fromJson(dataString,dataType);
                }
                return (T) httpResult;

            }else{
                return (T) httpResult;
            }
        }
    }

    public static class ParseError extends IOException implements YCParseError{

        private String jsonString;

        public ParseError(String message, Throwable cause) {
            super(message, cause);
        }

        public ParseError setJsonString(String jsonString) {
            this.jsonString = jsonString;
            return this;
        }

        @Override
        public String getJsonString() {
            return jsonString;
        }
    }

}
