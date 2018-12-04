package rafael.reserva.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Reserva {

    public String nomeUser;
    public String diaReserva;
    public String horaReserva;
    public int numeroPessoas;


    public Reserva() {
    }

    public Reserva(String nomeUser, String diaReserva, String horaReserva, int numeroPessoas) {
        this.nomeUser = nomeUser;
        this.diaReserva = diaReserva;
        this.horaReserva = horaReserva;
        this.numeroPessoas = numeroPessoas;

    }

    public String getNomeUser() {
        return nomeUser;
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
    }

    public String getDiaReserva() {
        return diaReserva;
    }

    public void setDiaReserva(String diaReserva) {
        this.diaReserva = diaReserva;
    }

    public String getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(String horaReserva) {
        this.horaReserva = horaReserva;
    }

    public int getNumeroPessoas() {
        return numeroPessoas;
    }

    public void setNumeroPessoas(int numeroPessoas) {
        this.numeroPessoas = numeroPessoas;
    }


}
