package nl.elstarit.crypto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document
public class Transaction {
	Currency currency;
	BigDecimal amount;
	TransactionType type;
}
