package com.example.magzoo.data;

public class User {

    private int id;
    private String email;
    private String nome;
    private String password;
    private String profilePic;

    public User(int id, String email, String nome, String password) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.password = password;
    }

    public User(int id, String email, String nome, String password, String profilePic) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.password = password;
        this.profilePic = profilePic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
