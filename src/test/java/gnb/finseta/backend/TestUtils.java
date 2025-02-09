package gnb.finseta.backend;

import org.openapitools.model.Account;
import org.openapitools.model.Payment;

import java.math.BigDecimal;

public class TestUtils {
	public static Payment.PaymentBuilder defaultPaymentBuilder() {
		var counterParty = defaultAccountBuilder().build();

		return Payment.builder()
				.amount(BigDecimal.TEN)
				.currency("NZD")
				.counterparty(counterParty);
	}

	public static Account.AccountBuilder defaultAccountBuilder() {
		return Account.builder()
				.type(Account.TypeEnum.SORT_CODE_ACCOUNT_NUMBER)
				.accountNumber("123")
				.sortCode("123456");
	}
}
