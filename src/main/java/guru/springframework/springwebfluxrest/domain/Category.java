package guru.springframework.springwebfluxrest.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document // MongoDB
@Builder // Pour simplifier la création d'instance
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    private String id;
    private String description;
}
