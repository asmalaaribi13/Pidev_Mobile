/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/**
 *
 * @author Asma Laaribi
 */
public class User {
    private int id ;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
public User(int id) {
        this.id = id;
      
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
