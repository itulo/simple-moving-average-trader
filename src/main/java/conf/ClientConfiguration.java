package conf;

public class ClientConfiguration {

  private boolean useSandbox;
  private String passphrase;
  private String apiKey;
  private String apiSecret;

  public boolean isUseSandbox() {
    return useSandbox;
  }

  public void setUseSandbox(boolean useSandbox) {
    this.useSandbox = useSandbox;
  }

  public void setPassphrase(String passphrase) {
    this.passphrase = passphrase;
  }

  public String getPassphrase() {
    return passphrase;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiSecret(String apiSecret) {
    this.apiSecret = apiSecret;
  }

  public String getApiSecret() {
    return apiSecret;
  }

  @Override
  public String toString() {
    return "Passphrase: " + passphrase +
            " Api Key: " + apiKey +
            "\nApi Secret: " + apiSecret;
  }
}
