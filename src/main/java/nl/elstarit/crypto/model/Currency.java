package nl.elstarit.crypto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Currency extends BaseEntity {
	String code;
	String name;
	BigDecimal value;
}
