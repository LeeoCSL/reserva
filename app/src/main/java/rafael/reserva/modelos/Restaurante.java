package rafael.reserva.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Restaurante {

    private String id;
    private String address;
    private String logo;
    private String email;
    private String name;
    private String key;
    List<Reserva> reservas = new ArrayList<>();

    public Restaurante() {
    }

    public Restaurante(String id, String address, String logo, String name, String key, String email, List<Reserva> reservas) {
        this.id = id;
        this.address = address;
        this.logo = logo;
        this.name = name;
        this.key = key;
        this.email = email;
        this.reservas = reservas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public void addReserva(Reserva reserva){
        reservas.add(reserva);
    }

    @Override
    public String toString() {
        return "Restaurante{" +
                "address=" + address +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}