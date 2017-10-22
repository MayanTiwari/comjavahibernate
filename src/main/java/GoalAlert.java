/**
 * Created by mayan on 18/10/17.
 */
public class GoalAlert {
    public GoalAlert(){}

    public GoalAlert(String message) {
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
}
