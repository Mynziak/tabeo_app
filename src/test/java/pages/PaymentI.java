package pages;

import java.util.List;

public interface PaymentI {

    PaymentI checkPrice();

    void payWithCard(List<String> c);
}
