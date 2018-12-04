package rafael.reserva;

import android.content.Context;
import android.support.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class RetrofitInit {

    private Retrofit retrofit;

    public RetrofitInit(Context activity) {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();

//                Log.i("CHAMOU INTERCEPTOR", "SIM");



                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        client.addInterceptor(interceptor);

        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.249.2:3000")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public UsuarioService getUsuarioService() {
        return retrofit.create(UsuarioService.class);
    }
    public RestauranteService getRestauranteService() {
        return retrofit.create(RestauranteService.class);
    }

}

