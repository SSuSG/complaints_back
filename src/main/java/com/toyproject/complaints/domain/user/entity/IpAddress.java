package com.toyproject.complaints.domain.user.entity;

import lombok.*;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
//@AuditTable("ip_audit")
@AuditOverride(forClass=BaseEntity.class)
public class IpAddress extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "ip_address_id")
    private Long id;

    private String ip;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    //@AuditJoinTable
    private User user;
}
