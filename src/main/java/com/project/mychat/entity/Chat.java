package com.project.mychat.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String chat_name;
    private String chat_image;

    @Column(name = "is_group")
    private boolean isGroup;
    @JoinColumn(name = "create_by")
    @ManyToOne
    private User createBy;
    /*@ManyToMany
    private User admin;
    admin mở ra phai chu y neu khong se bi loi
    */
    @ManyToMany
    private Set<User> admins = new HashSet<>();
    @ManyToMany
    private Set<User> users = new HashSet<>();
    @OneToMany
    private List<Message> messages = new ArrayList<>();
}
