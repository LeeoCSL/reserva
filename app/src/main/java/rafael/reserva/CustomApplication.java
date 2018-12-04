package rafael.reserva;

import android.app.Application;
import android.content.Context;
import rafael.reserva.modelos.Reserva;
import rafael.reserva.modelos.Restaurante;
import rafael.reserva.modelos.Usuario;

import java.util.ArrayList;
import java.util.List;


public class CustomApplication extends Application {

    public static Usuario currentUser = new Usuario();
    public static Restaurante currentRestaurante = new Restaurante();
    public static List<Reserva> reservas = new ArrayList<>();
    public static String idRestaurante = "cTB2iakhbueFUBSLWCN5K8lx1c72";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }



    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public static void setCurrentUser(Usuario currentUser) {
        CustomApplication.currentUser = currentUser;
    }

    public static void setCurrentRestaurante(Restaurante currentRestaurante) {
        CustomApplication.currentRestaurante = currentRestaurante;
    }

    public static List<Reserva> getReserva() {
        return reservas;
    }

    public static void setReserva(List<Reserva> reserva) {
        CustomApplication.reservas = reserva;
    }

    public static void addReserva(Reserva reserva) {
        CustomApplication.reservas.add(reserva);
    }

    public static String getIdRestaurante() {
        return idRestaurante;
    }

    public void destroySession() {
        currentUser = null;
    }
}
