/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

/**
 *
 * @author Leon Kranjcevic
 */
public class User {

    private int id;
    private String userName;
    private String password;
    private boolean role; //1 admin //0 obican

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    
    public User(String userName, String password, boolean role) {
        this(userName,password);
        this.role = role;
    }

    public User(int id, String userName, String password, boolean role) {
        this(userName, password, role);
        this.id = id;
    }

    public int getId() {
        return id;
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

    public boolean isRole() {
        return role;
    }

}
