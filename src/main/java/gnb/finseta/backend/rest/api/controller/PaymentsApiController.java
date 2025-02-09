package gnb.finseta.backend.rest.api.controller;

import gnb.finseta.backend.services.payments.IPaymentsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.openapitools.api.PaymentsApi;
import org.openapitools.model.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Validated
@Controller
@RequestMapping("/")
public class PaymentsApiController implements PaymentsApi {

	IPaymentsService paymentsService;

	@Override
	public ResponseEntity<List<Payment>> paymentsGet(@Valid BigDecimal minAmount, @Valid List<String> currencies) {

		return ResponseEntity
				.ok()
				.body(paymentsService.getPayments(minAmount, currencies));
	}

	@Override
	public ResponseEntity<Payment> paymentsPost(@Valid Payment payment) {
		return ResponseEntity
				.status(201)
				.body(paymentsService.handlePaymentRequest(payment));
	}
}
