package rafael.reserva;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import rafael.reserva.modelos.Reserva;
import rafael.reserva.modelos.Restaurante;
import rafael.reserva.modelos.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FazerReserva extends AppCompatActivity {

    int numeroPessoas = 1;
    TextView tvPessoas;
    String horaReserva = "";
    String diaReserva = "";
    TextView tvHora;
    TextView tvDia;
    Button btnSalvar;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Restaurante r = new Restaurante();
    Reserva reserva = new Reserva();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fazer_reseva);

        loadUI();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String currentTime = sdf.format(Calendar.getInstance().getTime());
        String currentDate = df.format(Calendar.getInstance().getTime());
        tvHora.setText(currentTime);
        tvDia.setText(currentDate);


        tvHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog();
            }
        });

        mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        String data = String.valueOf(dayOfMonth) + " /"
                                + String.valueOf(monthOfYear+1) + " /" + String.valueOf(year);
//                        Toast.makeText(FazerReserva.this,
//                                "DATA = " + data, Toast.LENGTH_SHORT)
//                                .show();
                        diaReserva = data;
                        tvDia.setText(diaReserva);
                    }
                };

        tvDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int ano = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog date = new DatePickerDialog(FazerReserva.this, mDateSetListener, ano, mes,
                        dia);

                date.show();

            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reserva.setDiaReserva(tvDia.getText().toString().trim());
                reserva.setHoraReserva(tvHora.getText().toString().trim());
                reserva.setNomeUser(CustomApplication.currentUser.getName());
                reserva.setNumeroPessoas(numeroPessoas);

                CustomApplication.currentUser.setReservaUser(reserva);


                r.setId(CustomApplication.getIdRestaurante());

                Call<Restaurante> call = new RetrofitInit(FazerReserva.this).getRestauranteService().getRes(r.getId());
                call.enqueue(new Callback<Restaurante>() {
                    @Override
                    public void onResponse(Call<Restaurante> call, Response<Restaurante> response) {
                            r = response.body();
                        r.addReserva(reserva);

                        patchRes();
                    }
                    @Override
                    public void onFailure(Call<Restaurante> call, Throwable t) {
                    }
                });







            }
        });
    }

    private void patchRes() {
        Call<Restaurante> call2 = new RetrofitInit(FazerReserva.this).getRestauranteService().att(r.getId(), r);
        call2.enqueue(new Callback<Restaurante>() {
            @Override
            public void onResponse(Call<Restaurante> call, Response<Restaurante> response) {
                patchUser();
            }

            @Override
            public void onFailure(Call<Restaurante> call, Throwable t) {

            }
        });
    }

    private void patchUser() {
        Call<Usuario> call3 = new RetrofitInit(FazerReserva.this).getUsuarioService().att(CustomApplication.currentUser.getId(), CustomApplication.currentUser);
        call3.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Toast.makeText(FazerReserva.this, "eh isso", Toast.LENGTH_LONG);
                startActivity(new Intent(FazerReserva.this, Main2ActivityUser.class));
                finish();
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }

    private void loadUI() {

        tvPessoas = findViewById(R.id.tvPessoas);
        tvPessoas.setText("" + numeroPessoas);
        tvHora = findViewById(R.id.tvHora);
        tvDia = findViewById(R.id.tvDia);
        btnSalvar = findViewById(R.id.btnSalvar);
    }


    public void btnMenos(View v) {
        if (numeroPessoas > 1) {
            numeroPessoas--;
            tvPessoas.setText("" + numeroPessoas);
        }
    }

    public void btnMais(View v) {
        if (numeroPessoas < 10) {
            numeroPessoas++;
            tvPessoas.setText("" + numeroPessoas);
        }
    }


    private void showTimeDialog() {

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        MyCustomTimePicker.OnTimeSetListener onTimeSetListener = new MyCustomTimePicker.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                    String hora = String.valueOf(hourOfDay);
                    String minuto = String.valueOf(minute);
                    if (hourOfDay < 10) {
                        hora = "0" + hourOfDay;
                    }
                    if (minute < 10) {
                        minuto = "0" + minute;
                    }
                horaReserva = hora + ":" + minuto;
                   tvHora.setText(horaReserva);

                }
            }

            ;

            final MyCustomTimePicker dialog = new MyCustomTimePicker(
                    this,
                    3,
                    onTimeSetListener,
                    hour,
                    minute,
                    true);

        if(dialog !=null)

            {
                dialog.show();
            }
        }

}