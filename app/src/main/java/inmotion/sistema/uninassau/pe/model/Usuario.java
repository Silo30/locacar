package inmotion.sistema.uninassau.pe.model;

import java.sql.Timestamp;
import java.util.ArrayList;

import inmotion.sistema.uninassau.pe.model.meiosdetransporte.MeioDeTransporte;

/**
 * Created by mirandagab on 07/07/2018.
 */
public class Usuario {

    private int id;
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private String foto;
    private String facebook;
    private boolean sincronizar;
    private Timestamp lastLogin;
    private ArrayList<MeioDeTransporte> transportes;

    public Usuario(){
        this.transportes = new ArrayList<>(10);
    }
    public Usuario(int id) {
        this.transportes = new ArrayList<>(10);
    }

    public Usuario(int id, String nome, String sobrenome, String email, String senha){
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.senha = senha;
        this.transportes = new ArrayList<>(10);
    }

    private void incluirMeioDeTransporte(MeioDeTransporte transporte) {
        this.transportes.add(transporte);
    }

    private void excluirMeioDeTransporte(MeioDeTransporte transporte) {
        this.transportes.remove(transporte);
    }

    //getters and setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public boolean isSincronizar() {
        return sincronizar;
    }

    public void setSincronizar(boolean sincronizar){
        this.sincronizar = sincronizar;
    }

    public ArrayList<MeioDeTransporte> getTransportes() {
        return transportes;
    }

    public void setLastLogin(){
        this.lastLogin = new Timestamp(System.currentTimeMillis());
    }
    public Timestamp getLastLogin(){
        return this.lastLogin;
    }
}
