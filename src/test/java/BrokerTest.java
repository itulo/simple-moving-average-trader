import CoinbaseObjects.Order;
import conf.BrokerConfiguration;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class BrokerTest {

    private BrokerConfiguration borkerConfMock = mock(BrokerConfiguration.class);
    private CoinbaseClient clientMock = mock(CoinbaseClient.class);

    @Test
    public void checkPendingOrder_BrokerHasNoOrder() {
        Broker broker = new Broker(borkerConfMock, clientMock, "BTC-EUR");

        broker.checkPendingOrder();
        verify(clientMock, times(0)).getOrder(anyString());
    }

    @Test
    public void checkPendingOrder_BrokerHasOrder() {
        Order doneOrder = new Order();
        doneOrder.setStatus("done");
        doReturn(doneOrder).when(clientMock).getOrder(anyString());

        Order pendingOrder = new Order();
        pendingOrder.setId("abc123");
        pendingOrder.setStatus("pending");

        Broker broker = new Broker(borkerConfMock, clientMock, "BTC-EUR");
        broker.setOrder(pendingOrder);

        broker.checkPendingOrder();
        verify(clientMock, times(1)).getOrder("abc123");
    }
}
