package nl.elstarit.crypto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction{

	@Id
	protected String id;

	@CreatedDate
	@Builder.Default
	protected LocalDateTime created = LocalDateTime.now();

	@LastModifiedDate
	@Builder.Default
	protected LocalDateTime modified = LocalDateTime.now();

	String customerId;
	String currencyCode;
	BigDecimal amount;
	TransactionType type;
}
