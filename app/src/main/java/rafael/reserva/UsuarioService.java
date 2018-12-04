package rafael.reserva;

import rafael.reserva.modelos.Usuario;
import retrofit2.Call;
import retrofit2.http.*;

public interface UsuarioService {

    @POST("usuario")
    Call<Usuario> register(@Body Usuario user);

    @PATCH("usuario/{id}")
    Call<Usuario> att(@Path("id") String id, @Body Usuario user);

    @GET("usuario/{id}")
    Call<Usuario> getUser(@Path("id") String id);

    @DELETE("user/{id}")
    Call<Usuario> deleteUser(@Path("id") String id);
}
