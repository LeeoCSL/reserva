package rafael.reserva.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Usuario {

    protected String id;
    protected String key;
    protected String name;
    protected String phone;
    protected String email;
    protected String password;
    private String image;
    private String sexo;
    public Reserva reservaUser;

    public Usuario() {

    }

    public Usuario(String id, String key, String name, String phone, String email, String password, String image, String sexo, Reserva reservaUser) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;

        this.image = image;
        this.sexo = sexo;
        this.reservaUser = reservaUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Reserva getReservaUser() {
        return reservaUser;
    }

    public void setReservaUser(Reserva reservaUser) {
        this.reservaUser = reservaUser;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
