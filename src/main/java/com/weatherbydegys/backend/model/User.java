package com.weatherbydegys.backend.model;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    private int id;
    @Column(name = "UID", nullable = true)
    private String uid = "";
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD")
    @Size(min = 6, message = "Minimum 6 symbols")
    private String password;

    @Column(name = "PHOTO_URL")
    private String photoURL;
    @Column(name = "LAST_VISIT")
    private LocalDateTime lastVisit;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} , fetch = FetchType.EAGER)
    @JoinTable(name = "USER_FAVCITY",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "FAVCITY_ID"))
    private Set<FavCity> favCities = new HashSet<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public User() {};

    public User(String uid, String name, String email, String photoURL) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.photoURL = photoURL;
    }

    public User(String name, String email, String photoURL) {
        this.uid = "";
        this.name = name;
        this.email = email;
        this.photoURL = photoURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public Set<FavCity> getFavCities() {
        return favCities;
    }

    public void setFavCities(Set<FavCity> favCities) {
        this.favCities = favCities;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getPassword() {return password; }

    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getLastVisit() { return lastVisit; }

    public void setLastVisit(LocalDateTime lastVisit) { this.lastVisit = lastVisit; }

    public Set<Role> getRoles() { return roles; }

    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public void addFavCity(FavCity favCity) {
        this.favCities.add(favCity);
    }

    public void deleteFavCity(FavCity favCity) {
        this.favCities.remove(favCity);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(uid, user.uid) &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, uid);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", favCities=" + favCities +
                '}';
    }
}
