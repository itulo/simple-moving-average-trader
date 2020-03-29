package conf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TraderConfiguration {
  @JsonProperty("client")
  private ClientConfiguration clientConf;
  @JsonProperty("watcher")
  private WatcherConfiguration watcherConf;
  @JsonProperty("broker")
  private BrokerConfiguration brokerConf;

  public ClientConfiguration getClientConf() {
    return clientConf;
  }

  public WatcherConfiguration getWatcherConf() {
    return watcherConf;
  }

  public BrokerConfiguration getBrokerConf() {
    return brokerConf;
  }

  @Override
  public String toString() {
    return "Client configuration: " + clientConf +
            "\nWatcher configuration: " + watcherConf +
            "\nBroker configuration: " + brokerConf;
  }
}
