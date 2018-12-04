package rafael.reserva;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
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
import de.hdodenhof.circleimageview.CircleImageView;
import rafael.reserva.modelos.Usuario;
import rafael.reserva.utils.DialogFactory;
import rafael.reserva.utils.ToastUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static rafael.reserva.utils.TextUtils.isEmailValid;

public class UserRegister extends AppCompatActivity {
    @BindView(R.id.tb)
    Toolbar mToolbar;

    @BindView(R.id.edt_nome)
    EditText mUserName;

    @BindView(R.id.edt_senha)
    EditText mUserPassword;

    @BindView(R.id.edt_tel)
    EditText mUserPhone;

    @BindView(R.id.edt_email)
    EditText mUserEmail;


    private static final int GALLERY_PICK = 1;

    private String userName;
    private String userPassword;
    private String userPhone;
    private String userEmail;

    private Spinner sp;
    private ImageView sexo;

    private String [] resSexo = new String[]{"Masculino", "Feminino"};

    private int[] resSexoImg = {R.drawable.ic_avatar_masc, R.drawable.ic_avatar_fem};

    private FirebaseAuth mFirebaseAuth;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);


        mUserName = findViewById(R.id.edt_nome);
        mUserPassword = findViewById(R.id.edt_senha);
        mUserPhone = findViewById(R.id.edt_tel);
        mUserEmail = findViewById(R.id.edt_email);
        //cria nó no database para clientes
        mFirebaseAuth = FirebaseAuth.getInstance();

        //seta voltar da toolbar V


        setSupportActionBar(mToolbar);

        //seta voltar da toolbar ^


        //alteração da imagem e spinner para sexo V
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, resSexo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        sp = (Spinner) findViewById(R.id.spinner_sexo);
        sp.setAdapter(adapter);


        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //alteração da imagem e spinner para sexo ^

    }

    public void handlerUserRegister(View v) {
        validateDataFromEditText();
    }



    public void validateDataFromEditText() {
        userEmail = mUserEmail.getText().toString().trim();
        userName = mUserName.getText().toString().trim();
        userPhone = mUserPhone.getText().toString().trim();
        userPassword = mUserPassword.getText().toString().trim();


        if (TextUtils.isEmpty(userName)) {
            mUserName.setError(getString(R.string.name_field_empty));
            return;
        }

        if (TextUtils.isEmpty(userPhone)) {
            mUserPhone.setError(getString(R.string.invalid_telephone));
            return;
        }

        if (!isEmailValid(userEmail)) {
            mUserEmail.setError(getString(R.string.invalid_email));
            return;
        }

        if (userPassword.length() < 6) {
            mUserPassword.setError(getString(R.string.your_password_needs_more_caracters));
            return;
        }


            loginWithEmailAndPassword();

    }

    private void loginWithEmailAndPassword() {

        DialogFactory.createDialog(this, getString(R.string.enter), getString(R.string.validating_information));

        mFirebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnSuccessListener(UserRegister.this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String userID = mFirebaseAuth.getCurrentUser().getUid();
                        Usuario customer = new Usuario();
                        customer.setKey(userID);
                        customer.setId(userID);
                        customer.setName(userName);
                        customer.setPhone(userPhone);
                        customer.setEmail(userEmail);
                        customer.setSexo(sp.getSelectedItem().toString());

                        Call<Usuario> call = new RetrofitInit(UserRegister.this).getUsuarioService().register(customer);
                        call.enqueue(new Callback<Usuario>() {
                            @Override
                            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                                DialogFactory.destroyDialog();
                                Intent intent = new Intent(UserRegister.this, Main2ActivityUser.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Usuario> call, Throwable t) {

                            }
                        });

//                        mDatabaseCustomers.child(userID).setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                DialogFactory.destroyDialog();
//                                Intent intent = new Intent(UserRegister.this, Main2ActivityUser.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                                finish();
//                            }
//                        });


                    }
                })
                .addOnFailureListener(UserRegister.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        DialogFactory.destroyDialog();

                        ToastUtils.makeText(UserRegister.this, e.getMessage());

                        if (e.getClass() == FirebaseAuthUserCollisionException.class) {
                            ToastUtils.makeText(UserRegister.this, getString(R.string.email_already_used_with_facebook_account));
                        }
                    }
                });
    }



}
