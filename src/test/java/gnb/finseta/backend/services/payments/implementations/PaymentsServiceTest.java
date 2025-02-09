package gnb.finseta.backend.services.payments.implementations;

import gnb.finseta.backend.services.payments.IPaymentsService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapitools.model.Account;
import org.openapitools.model.Payment;
import org.springframework.core.Constants;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PaymentsServiceTest {
    IPaymentsService subject = new PaymentsService();

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1})
    void thatPaymentWithAmountEqualToOrLessThanZeroThrowsConstrainViolationErr(double invalidAmounts) {

        var paymentWithInvalidAmount = new Payment(
                "NZD",
                BigDecimal.valueOf(invalidAmounts),
                mock(Account.class)
        );

        var res = assertThrows(ConstraintViolationException.class,
                () -> subject.handlePaymentRequest(paymentWithInvalidAmount));
    }

//    currency is empty
//    counterparty.type is empty
//    counterparty.sortCode is not 6 numeric characters
//    counterparty.sortCode is not 8 numeric characters
}