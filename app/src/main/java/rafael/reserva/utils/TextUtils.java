package rafael.reserva.utils;

import android.util.Patterns;

public class TextUtils {

    public static boolean isEmailValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}