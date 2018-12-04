package rafael.reserva;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import rafael.reserva.modelos.Reserva;

import java.util.List;

public class listaReservasAdapter extends BaseAdapter {


    private final List<Reserva> reservas;
    private final Context context;


    public listaReservasAdapter(List<Reserva> reservas, Context context) {
        this.reservas = reservas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return reservas.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.layout_reserva, viewGroup, false);

        TextView layout_quantidade = viewCriada.findViewById(R.id.layout_quantidade);
        TextView layout_nome = viewCriada.findViewById(R.id.layout_nome);
        TextView layout_dia = viewCriada.findViewById(R.id.layout_dia);
        TextView layout_hora = viewCriada.findViewById(R.id.layout_hora);


        Reserva reserva = reservas.get(i);

        layout_quantidade.setText(""+reserva.getNumeroPessoas());
        layout_nome.setText(reserva.getNomeUser());
        layout_dia.setText(reserva.getDiaReserva());
        layout_hora.setText(reserva.getHoraReserva());

        return viewCriada;
    }
}
