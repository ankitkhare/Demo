package demo.com.demo.network;

public interface OnNetworkComplete {

    void onError();

    void onSuccess(String response);
}
