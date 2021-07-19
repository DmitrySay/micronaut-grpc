package com.solbeg.demo.grpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatEntity {
    private Long id;

    private String channel;

    @EqualsAndHashCode.Exclude
    private Set<UserEntity> people = new LinkedHashSet<>();

    @EqualsAndHashCode.Exclude
    private List<String> messages = new ArrayList<>();
}
