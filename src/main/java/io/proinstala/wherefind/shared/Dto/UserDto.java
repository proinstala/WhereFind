package io.proinstala.wherefind.shared.dto;

public class UserDto {
    private int id;
    private String userName;
    private String password;
    private String rol;

    public UserDto() {
    }

    public UserDto(String userName, String password, String rol) {
        this.userName = userName;
        this.password = password;
        this.rol = rol;
    }

    public UserDto(int id, String userName, String password, String rol) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}