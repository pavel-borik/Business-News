package cz.uhk.umte.borikpa1.businessnews.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.uhk.umte.borikpa1.businessnews.model.StockItem;
import cz.uhk.umte.borikpa1.businessnews.model.StockLogo;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceGenerator {
    private static final String API_BASE_URL = "https://api.iextrading.com/1.0/";
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<List<StockItem>>(){}.getType(), new StockItemTypeAdapter())
            .registerTypeAdapter(new TypeToken<List<StockLogo>>(){}.getType(), new StockLogoTypeAdapter())
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setDateFormat(DateFormat.LONG)
            .setPrettyPrinting()
            .setVersion(1.0)
            .create();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(
                            GsonConverterFactory.create(gson)
                    );

    private static Retrofit retrofit = builder.client(httpClient.build()).build();

    public static <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

    private static class StockItemTypeAdapter implements JsonDeserializer<List<StockItem>> {
        @Override
        public List<StockItem> deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject parentJsonObject = element.getAsJsonObject();
            List<StockItem> stockItemList = new ArrayList<>();
            for(Map.Entry<String, JsonElement> entry : parentJsonObject.entrySet()) {
                for(Map.Entry<String, JsonElement> entry1 : entry.getValue().getAsJsonObject().entrySet()) {
                    StockItem s = gson.fromJson(entry1.getValue(),StockItem.class);
                    stockItemList.add(s);
                }
            }
            return stockItemList;
        }
    }

    private static class StockLogoTypeAdapter implements JsonDeserializer<List<StockLogo>> {
        @Override
        public List<StockLogo> deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject parentJsonObject = element.getAsJsonObject();
            List<StockLogo> stockLogoList = new ArrayList<>();
            for(Map.Entry<String, JsonElement> entry : parentJsonObject.entrySet()) {
                for(Map.Entry<String, JsonElement> entry1 : entry.getValue().getAsJsonObject().entrySet()) {
                    StockLogo s = gson.fromJson(entry1.getValue(),StockLogo.class);
                    stockLogoList.add(s);
                }
            }
            return stockLogoList;
        }
    }
}
