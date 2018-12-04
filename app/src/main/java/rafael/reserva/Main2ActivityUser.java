package rafael.reserva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import rafael.reserva.modelos.Reserva;
import rafael.reserva.modelos.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2ActivityUser extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button btnReservar, btnVerificar;

    private Toast toast;
    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnReservar = (Button) findViewById(R.id.btnReservar);
        btnVerificar = (Button) findViewById(R.id.btnVerificar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2ActivityUser.this, FazerReserva.class);
                startActivity(intent);
            }
        });
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);


    }

    public void verificarReserva(View view) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (CustomApplication.currentUser.reservaUser != null  ) {
            if(CustomApplication.currentUser.reservaUser.getHoraReserva() != null) {
                Toast.makeText(this, "Voce tem uma reserva dia: " + CustomApplication.currentUser.reservaUser.getDiaReserva() +
                        " as: " + CustomApplication.currentUser.reservaUser.getHoraReserva(), Toast.LENGTH_SHORT).show();
                }else{
                Toast.makeText(this, "Voce não tem uma reserva ativa", Toast.LENGTH_SHORT).show();

            }
            } else {
            Toast.makeText(this, "Voce não tem uma reserva ativa", Toast.LENGTH_SHORT).show();

        }
    }


    public void cancelarReserva(View view) {
        Reserva r = new Reserva();
        CustomApplication.currentUser.setReservaUser(r);

        Call<Usuario> call = new RetrofitInit(Main2ActivityUser.this).getUsuarioService().att(CustomApplication.currentUser.getId(), CustomApplication.currentUser);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                CustomApplication.currentUser = response.body();
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, "Pressione o Botão Voltar novamente para fechar o Aplicativo.", Toast.LENGTH_LONG);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_send) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();


        Intent intent = new Intent(this, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
