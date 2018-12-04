package rafael.reserva;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.*;
import rafael.reserva.modelos.Restaurante;
import rafael.reserva.modelos.Usuario;
import rafael.reserva.utils.DialogFactory;
import rafael.reserva.utils.TextUtils;
import rafael.reserva.utils.ToastUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = LoginActivity.class.getCanonicalName();

    EditText mLoginEmail;

    EditText mLoginPassword;

    public static String id = "";

    private String email;
    private String password;

    private ProgressDialog mDialog;

    EditText input_email;

    private FirebaseAuth mFirebaseAuth;


    Date dt1, dt2;
    long dtf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        input_email = (EditText) findViewById(R.id.input_email);
mLoginEmail = findViewById(R.id.input_email);
        mLoginPassword = findViewById(R.id.input_password);
    }

    public void handlerUserLogin(View view) {
        validateEditText();
    }

    private void validateEditText() {

        email = mLoginEmail.getText().toString().trim();
        password = mLoginPassword.getText().toString().trim();

        if (!TextUtils.isEmailValid(email)) {
            mLoginEmail.setError(getString(R.string.invalid_email));
            return;
        }

        if (android.text.TextUtils.isEmpty(password)) {
            mLoginPassword.setError("o campo senha esta vazio");
            return;
        }
        loginWithEmailAndPassword();
    }

    private void loginWithEmailAndPassword() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Aguarde");
        dialog.setMessage(getString(R.string.validating_information));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        final String userID = authResult.getUser().getUid();
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                        id = user.getUid();
                        loginComUser();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        if (e.getClass() == FirebaseAuthUserCollisionException.class) {
                            ToastUtils.makeText(LoginActivity.this,
                                    getString(R.string.email_already_used_with_facebook_account));
                            return;
                        }
                        if (e.getClass() == FirebaseAuthInvalidUserException.class) {
                            ToastUtils.makeText(LoginActivity.this, "Email não cadastrado.");
                            return;
                        }
                        if (e.getClass() == FirebaseAuthInvalidCredentialsException.class) {
                            mLoginPassword.setError("Senha invalida");
                            return;
                        } else {
                            ToastUtils.makeText(LoginActivity.this, "Algo deu errado, tente novamente.");
                        }
                        Log.d(TAG, e.getMessage() + " Exception");
                    }
                });

    }

    private void loginComUser() {
        Call<Usuario> call = new RetrofitInit(LoginActivity.this).getUsuarioService().getUser(id);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.code() != 200){
//                    Toast.makeText(LoginActivity.this, "deu ruim", Toast.LENGTH_SHORT).show();
                    loginComRestaurante();
                }else {
//                    Toast.makeText(LoginActivity.this, "logou User", Toast.LENGTH_SHORT).show();
                    CustomApplication.setCurrentUser(response.body());
                    startActivity(new Intent(LoginActivity.this, Main2ActivityUser.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
//                Toast.makeText(LoginActivity.this, "deu ruim", Toast.LENGTH_SHORT).show();
                loginComRestaurante();
            }
        });
    }

    private void loginComRestaurante() {
        Call<Restaurante> call = new RetrofitInit(LoginActivity.this).getRestauranteService().getRes(id);
        call.enqueue(new Callback<Restaurante>() {
            @Override
            public void onResponse(Call<Restaurante> call, Response<Restaurante> response) {
                if(response.code() != 200){
//                    Toast.makeText(LoginActivity.this, "deu ruim", Toast.LENGTH_SHORT).show();
                    loginComRestaurante();
                }else {
//                    Toast.makeText(LoginActivity.this, "logou Res", Toast.LENGTH_SHORT).show();
                    CustomApplication.setCurrentRestaurante(response.body());
                    startActivity(new Intent(LoginActivity.this, Main2ActivityRestaurant.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Restaurante> call, Throwable t) {
//                Toast.makeText(LoginActivity.this, "deu ruim", Toast.LENGTH_SHORT).show();
                loginComRestaurante();
            }
        });
    }


    public void registrar(View v) {
        startActivity(new Intent(this, RegisterChoice.class));
    }

    public void forgetPassword(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Email");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        builder.setView(input);

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email = input.getText().toString().trim();

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("email_reset", email);
                editor.commit();

                if (!android.text.TextUtils.isEmpty(email)) {
                    DialogFactory.loadingDialog(LoginActivity.this);
                    resetPassword(email);
                    dialog.dismiss();
                } else {

                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void resetPassword(String email) {
        mFirebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        DialogFactory.hideLoadingDialog();
                        Toast.makeText(LoginActivity.this, "Acabamos de te enviar as instruções de como recuperar sua conta.", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                DialogFactory.hideLoadingDialog();
                if (e.getClass() == FirebaseAuthInvalidUserException.class) {

                    Toast.makeText(LoginActivity.this,
                            "Usuário não encontrado", Toast.LENGTH_LONG).show();

                    return;
                }
                if (e.getClass() == FirebaseException.class) {

                    Toast.makeText(LoginActivity.this,
                            "Email inválido", Toast.LENGTH_LONG).show();
                }
                e.printStackTrace();
            }
        });
    }
}