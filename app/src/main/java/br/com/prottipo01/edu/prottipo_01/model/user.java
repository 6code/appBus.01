package br.com.prottipo01.edu.prottipo_01.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import br.com.prottipo01.edu.prottipo_01.configure.configureFirebase;

/**
 * Created by edu on 27/08/17.
 */

public class user {

    private String id;
    private String nome;
    private String email;
    private String senha;


    public user() {
    }

    public void salvar(){

        DatabaseReference databaseReference = configureFirebase.getDatabaseReference();
        databaseReference.child("Usuarios").child(getId()).setValue(this);
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
