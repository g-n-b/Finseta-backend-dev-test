package gnb.finseta.backend.rest.api.controller;

import gnb.finseta.backend.services.payments.IPaymentsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapitools.model.Account;
import org.openapitools.model.Payment;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PaymentsApiControllerTest {
    IPaymentsService paymentsService = mock(IPaymentsService.class);
    PaymentsApiController subject = new PaymentsApiController(paymentsService);

    @Test
    void paymentsGet() {
        // TODO set up some mock data to retrun
        var result = subject.paymentsGet(BigDecimal.ONE, List.of("NZD", "GBP"));

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.hasBody());
    }

    @Test
    void paymentsPost() {
        // TODO verify service is called
        var result = subject.paymentsPost(new Payment("NZD", BigDecimal.TEN,
                new Account(Account.TypeEnum.SORT_CODE_ACCOUNT_NUMBER,
                        "12345", "sortCode")));

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertFalse(result.hasBody());
    }
}