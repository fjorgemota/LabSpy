package messages;

public class OpenBrowserMessage extends BaseCommandMessage {
    private String url;

    public OpenBrowserMessage(String url) {
        this.url = url;
    }


    public String getUrl() {
        return url;
    }
}
