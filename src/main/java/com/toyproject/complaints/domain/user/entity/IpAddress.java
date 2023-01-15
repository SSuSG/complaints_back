package com.toyproject.complaints.domain.user.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IpAddress {

    @Id
    @GeneratedValue
    @Column(name = "ip_address_id")
    private Long id;

    private String ip;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}
