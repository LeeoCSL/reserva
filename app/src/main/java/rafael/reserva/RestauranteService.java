package rafael.reserva;

import rafael.reserva.modelos.Restaurante;
import rafael.reserva.modelos.Usuario;
import retrofit2.Call;
import retrofit2.http.*;

public interface RestauranteService {

    @POST("restaurante")
    Call<Restaurante> register(@Body Restaurante restaurante);

    @PATCH("restaurante/{id}")
    Call<Restaurante> att(@Path("id") String id, @Body Restaurante restaurante);

    @GET("restaurante/{id}")
    Call<Restaurante> getRes(@Path("id") String id);

    @DELETE("user/{id}")
    Call<Restaurante> deleteUser(@Path("id") String id);
}
