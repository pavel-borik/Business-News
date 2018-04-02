package cz.uhk.umte.borikpa1.businessnews.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceGenerator {
    private static final String API_BASE_URL = "https://api.iextrading.com/";
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Gson gson = new GsonBuilder()
     //       .registerTypeAdapter(new TypeToken<Map<String, Map<String, StockItem>>>(){}.getType(), new StockTypeAdapter())
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

//    private static class StockTypeAdapter implements JsonDeserializer<Map<String, Map<String, StockItem>>> {
//        @Override
//        public Map<String, Map<String, StockItem>> deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//            Map<String, Map<String, StockItem>> randomList = new HashMap<>();
//            JsonObject parentJsonObject = element.getAsJsonObject();
//            Map<String, StockItem> childMap;
//            for(Map.Entry<String, JsonElement> entry : parentJsonObject.entrySet()){
//                childMap = new HashMap<>();
//                for(Map.Entry<String, JsonElement> entry1 : entry.getValue().getAsJsonObject().entrySet()){
//                    childMap.put(entry1.getKey(), gson.fromJson(entry1.getValue(), StockItem.class));
//                }
//                randomList.put(entry.getKey(), childMap);
//            }
//            return randomList;
//        }
//    }
}
