/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author sange
 */
@Entity
@Table(name = "size")
public class size implements Serializable {
    
    @Id
   @Column(name = "id")
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int id;
    @Column(name = "mili")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

   

    public size() {
    }

    public size(int id, String mili) {
        this.id = id;
        this.name = mili;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
