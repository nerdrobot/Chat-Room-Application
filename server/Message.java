/**
 * Hassan and Brandon
 */
public class Message {

    private String command;
    private String source;
    private String destination;
    private String time;
    private String content;

    public Message(String command, String source, String destination, String time, String content) {
        this.command = command;
        this.source = source;
        this.destination = destination;
        this.time = time;
        this.content = content;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return command + '|' +
                source + '|' +
                destination + '|' +
                time + '|' + "\r\n";
    }
}
