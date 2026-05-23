package by.staryhroft.userbalance.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "email_data")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EmailData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
