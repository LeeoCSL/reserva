package rafael.reserva;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import rafael.reserva.modelos.Restaurante;
import rafael.reserva.utils.DialogFactory;
import rafael.reserva.utils.ToastUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRegister extends AppCompatActivity {

    @BindView(R.id.tb)
    Toolbar mToolbar;

    String restaurantEmail;
    String restaurantPassword;
    String restaurantPasswordConfirm;
    String restaurantName;
    String restaurantAddress;
    String restaurantPhone;

    EditText mRestaurantName;

    EditText mRestaurantAddress;

    EditText mRestaurantEmail;

    EditText mRestaurantPhone;

    EditText mRestaurantPassword;

    EditText mRestaurantPasswordConfirm;




    private FirebaseAuth mFirebaseAuth;

    private static final int GALLERY_PICK = 1;


    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_register);


        mFirebaseAuth = FirebaseAuth.getInstance();

        ButterKnife.bind(this);

        mRestaurantName = findViewById(R.id.et_restaurant_name);
        mRestaurantAddress = findViewById(R.id.et_restaurant_address);
        mRestaurantEmail = findViewById(R.id.et_restaurant_email);
        mRestaurantPhone = findViewById(R.id.et_restaurant_phone);
        mRestaurantPassword = findViewById(R.id.et_restaurant_password);
        mRestaurantPasswordConfirm = findViewById(R.id.et_restaurant_password_confirm);
        //seta voltar da toolbar V

        setSupportActionBar(mToolbar);

    }

    public void handlerRequest(View v) {
        validateDataFromEditText();
    }

    private String editTextToString(EditText editText) {
        return editText.getText().toString().trim();
    }

    public void validateDataFromEditText() {
        restaurantName = editTextToString(mRestaurantName);
        restaurantAddress = editTextToString(mRestaurantAddress);
        restaurantPhone = editTextToString(mRestaurantPhone);
        restaurantPassword = editTextToString(mRestaurantPassword);
        restaurantPasswordConfirm = editTextToString(mRestaurantPasswordConfirm);
        restaurantEmail = editTextToString(mRestaurantEmail);

        if (android.text.TextUtils.isEmpty(restaurantName)) {
            mRestaurantName.setError("Nome do restaurante em branco.");
            mRestaurantName.requestFocus();
            return;
        }
        if (android.text.TextUtils.isEmpty(restaurantEmail)) {
            mRestaurantEmail.setError("Email do restaurante em branco.");
            mRestaurantEmail.requestFocus();
            return;
        }

        if (android.text.TextUtils.isEmpty(restaurantAddress)) {
            mRestaurantAddress.setError("Endereço do restaurante em branco.");
            mRestaurantAddress.requestFocus();
            return;
        }


        if (android.text.TextUtils.isEmpty(restaurantPhone)) {
            mRestaurantPhone.setError("Telefone do restaurante em branco.");
            mRestaurantPhone.requestFocus();
            return;
        }

        if (android.text.TextUtils.isEmpty(restaurantPassword)) {
            mRestaurantPassword.setError("Senha em branco.");
            mRestaurantPassword.requestFocus();
            return;
        }
        if (android.text.TextUtils.isEmpty(restaurantPasswordConfirm)) {
            mRestaurantPasswordConfirm.setError("Confirmaçao de senha em branco.");
            mRestaurantPasswordConfirm.requestFocus();
            return;
        }
        if (restaurantPassword.length() < 6) {
            mRestaurantPassword.setError("Sua senha deve ter no mínimo 6 caracteres.");
            mRestaurantPassword.requestFocus();
            return;
        }


                            loginWithEmailAndPassword();



    }

    private void loginWithEmailAndPassword() {

        DialogFactory.createDialog(this, getString(R.string.enter), getString(R.string.validating_information));

        mFirebaseAuth.createUserWithEmailAndPassword(restaurantEmail, restaurantPassword)
                .addOnSuccessListener(RestaurantRegister.this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String userID = mFirebaseAuth.getCurrentUser().getUid();

                        if (mUri != null) {
                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(mUri).build();
                            authResult.getUser().updateProfile(request);
                        }


                        Restaurante restaurant = new Restaurante();
                        restaurant.setKey(userID);
                        restaurant.setId(userID);
                        restaurant.setName(restaurantName);
                        restaurant.setEmail(restaurantEmail);
                        restaurant.setAddress(restaurantAddress);

                        Call<Restaurante> call = new RetrofitInit(RestaurantRegister.this).getRestauranteService().register(restaurant);
                        call.enqueue(new Callback<Restaurante>() {
                            @Override
                            public void onResponse(Call<Restaurante> call, Response<Restaurante> response) {
                                DialogFactory.destroyDialog();
                                Intent intent = new Intent(RestaurantRegister.this, Main2ActivityRestaurant.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Restaurante> call, Throwable t) {
                                DialogFactory.destroyDialog();
                                AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantRegister.this);
                                builder.setTitle("Erro")
                                        .setMessage(t.getMessage())
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(RestaurantRegister.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        DialogFactory.destroyDialog();

                        ToastUtils.makeText(RestaurantRegister.this, e.getMessage());

                        if (e.getClass() == FirebaseAuthUserCollisionException.class) {
                            ToastUtils.makeText(RestaurantRegister.this, getString(R.string.email_already_used_with_facebook_account));
                        }
                    }
                });
    }



}
