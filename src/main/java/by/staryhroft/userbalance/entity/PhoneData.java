package by.staryhroft.userbalance.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "phone_data")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PhoneData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 13)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}