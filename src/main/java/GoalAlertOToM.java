/**
 * Created by mayan on 18/10/17.
 */
public class GoalAlertOToM {
    public GoalAlertOToM(){}
    public GoalAlertOToM(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private int id;
    private String message;

    @Override
    public String toString() {
        return "GoalAlertOToM{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
