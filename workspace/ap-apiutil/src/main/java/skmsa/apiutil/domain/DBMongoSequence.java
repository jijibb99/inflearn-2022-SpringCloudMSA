package skmsa.apiutil.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "db_sequences")
@Data
public class DBMongoSequence {

    @Id
    private String id;

    private long seq;
}    