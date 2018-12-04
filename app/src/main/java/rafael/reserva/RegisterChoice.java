package rafael.reserva;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterChoice extends AppCompatActivity {
    @BindView(R.id.tb)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_choice);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

    }

    public void RegisterUser(View v){

        startActivity(new Intent(this, UserRegister.class));
    }
    public void RegisterRestaurant(View v){
        startActivity(new Intent(this, RestaurantRegister.class));
    }

    public void voltar(View v){
        startActivity(new Intent(this, LoginActivity.class));
    }
}
