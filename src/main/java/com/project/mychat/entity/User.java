package com.project.mychat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Integer id;
    private String email;
    private String full_name;
    private String profile_picture;
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(full_name, user.full_name) && Objects.equals(profile_picture, user.profile_picture) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, full_name, profile_picture, password);
    }
    //    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
//    private List<Notification> notifications = new ArrayList<>();
}
