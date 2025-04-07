package com.shiba.baseproject.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tbl_user")
@Slf4j(topic = "USER-ENTITY")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends AbstractEntity<Long> {
    String username;
    String password;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UserHasRole> roles = new HashSet<>();
}
