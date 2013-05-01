package de.phoenix.database.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "pw")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "regdate")
    private Timestamp regdate;

    @Column(name = "active", columnDefinition = "bit")
    private boolean active;

    @Column(name = "role_id", nullable = true)
    private int roleId;

    public User() {
    }

    public User(String name, String password, String salt) {
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.regdate = new Timestamp(System.currentTimeMillis());
        this.active = true;
        this.roleId = 1;
    }
}
