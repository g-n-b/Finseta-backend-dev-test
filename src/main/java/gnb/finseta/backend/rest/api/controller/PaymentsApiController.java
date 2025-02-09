package gnb.finseta.backend.rest.api.controller;

import org.apache.commons.lang3.NotImplementedException;
import org.openapitools.api.PaymentsApi;
import org.openapitools.model.Payment;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public class PaymentsApiController implements PaymentsApi {
    @Override
    public ResponseEntity<List<Payment>> paymentsGet(BigDecimal minAmount, List<String> currencies) {
        throw new NotImplementedException("impl this");
    }

    @Override
    public ResponseEntity<Payment> paymentsPost(Payment payment) {
        throw new NotImplementedException("impl this");
    }
}
