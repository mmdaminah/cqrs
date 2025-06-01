package ir.mmdaminah.userservice;

import ir.mmdaminah.core.data.PaymentDetails;
import ir.mmdaminah.core.data.User;
import ir.mmdaminah.core.query.GetUserDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserQueriesHandler {

    @QueryHandler
    public User getUserDetails(GetUserDetailsQuery query) {
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .name("Amin Ahmadi")
                .cardNumber("6219861901322394")
                .validUntilMonth(2)
                .validUntilYear(7)
                .cvv("047")
                .build();
        return User.builder()
                .userId(query.getId())
                .firstName("Amin")
                .lastName("Ahmadi")
                .paymentDetails(paymentDetails)
                .build();
    }

}
