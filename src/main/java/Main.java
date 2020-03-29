import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import conf.TraderConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
  private static Logger logger = LogManager.getLogger(Main.class);

  public static void main(String args[]) throws IOException {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    TraderConfiguration traderConf = mapper.readValue(new File("trader.yaml"), TraderConfiguration.class);
    logger.info(traderConf);

    CoinbaseClient client = new CoinbaseClient(traderConf.getClientConf());
    Broker broker = new Broker(traderConf.getBrokerConf(), client,
            traderConf.getWatcherConf().getTradingPair());

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(new CandleWatcher(traderConf.getWatcherConf(), client, broker), 0,
            traderConf.getWatcherConf().getCandlePeriod() / 3, TimeUnit.SECONDS);
  }
}
