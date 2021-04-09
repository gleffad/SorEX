package blockchain;

import java.io.Serializable;

public class Transaction implements Serializable {
    private String senderIP;

    private String receiverIP;

    private double value;

    private String control = ""; // Control value (ACK or ERR)

    public Transaction(String senderIP, String receiverIP, double value) {
        this.senderIP = senderIP;
        this.receiverIP = receiverIP;
        this.value = value;
    }

    public String toString(){
        return "From " + senderIP + " to " + receiverIP + ", value: " + value + " SorEx";
    }

    public String getSenderIP() {
        return senderIP;
    }

    public void setSenderIP(String senderIP) {
        this.senderIP = senderIP;
    }

    public String getReceiverIP() {
        return receiverIP;
    }

    public void setReceiverIP(String receiverIP) {
        this.receiverIP = receiverIP;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getControlValue() {
        return control;
    }

    public void setControlValue(String control) {
        this.control = control;
    }
}
