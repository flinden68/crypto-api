package nl.elstarit.crypto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer extends BaseEntity {

	String name;
	String username;
	String password;
	List<Transaction> transactions;

}
