package CoinbaseObjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequest {
  private String side;
  @JsonProperty("product_id")
  private String productId;
  private double size;
  private double price;

  public OrderRequest() {
  }

  public OrderRequest(String side, String productId, double size, double price) {
    this.side = side;
    this.productId = productId;
    this.size = size;
    this.price = price;
  }

  public String getSide() {
    return side;
  }

  public void setSide(String side) {
    this.side = side;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public double getSize() {
    return size;
  }

  public void setSize(double size) {
    this.size = size;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }
}
