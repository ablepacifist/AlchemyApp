package alchemy.srsys.application;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import alchemy.srsys.logic.AlchemyApiService;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton Retrofit client for all Alchemy API calls.
 * Handles session cookies and HTTP logging.
 */
public class ApiClient {
    private static final String BASE_URL = "http://see-recover.gl.at.ply.gg:36567/";
    private static ApiClient instance;
    private final AlchemyApiService apiService;

    private ApiClient() {
        // cookie manager to persist JSESSIONID across requests
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        JavaNetCookieJar cookieJar = new JavaNetCookieJar(cookieManager);

        // logging interceptor for request/response debugging
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(AlchemyApiService.class);
    }

    /**
     * Returns the singleton instance of AlchemyApiService.
     * All network calls should use ApiClient.getService().
     */
    public static synchronized AlchemyApiService getService() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance.apiService;
    }
}
