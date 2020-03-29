package CoinbaseObjects;

public class OrderBookDetails {
  private double price;
  private double size;
  private double numOrders;

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getSize() {
    return size;
  }

  public void setSize(double size) {
    this.size = size;
  }

  public double getNumOrders() {
    return numOrders;
  }

  public void setNumOrders(double numOrders) {
    this.numOrders = numOrders;
  }
}
