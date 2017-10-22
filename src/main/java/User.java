import javax.persistence.Entity;
import java.util.*;

/**
 * Created by mayan on 11/10/17.
 */
public class User {

    public Set<GoalAlertOToM> getGoalAlerts() {
        return goalAlerts;
    }

    public void setGoalAlerts(Set<GoalAlertOToM> goalAlerts) {
        this.goalAlerts = goalAlerts;
    }

    private Set<GoalAlertOToM> goalAlerts = new HashSet<GoalAlertOToM>();
    public GoalAlert getGoalAlert() {
        return goalAlert;
    }

    public void setGoalAlert(GoalAlert goalAlert) {
        this.goalAlert = goalAlert;
    }

    private GoalAlert goalAlert;

    private ProteinData proteinData;

    public Collection<UserHistory> getHistoryCollection() {
        return historyCollection;
    }

    public void setHistoryCollection(Collection<UserHistory> historyCollection) {
        this.historyCollection = historyCollection;
    }

    private Collection<UserHistory> historyCollection = new ArrayList<UserHistory>();

    public User() {
        setProteinData(new ProteinData());
    }

    public ProteinData getProteinData() {
        return proteinData;
    }

    public List<UserHistory> getHistory() {
        return history;
    }

    public void setHistory(List<UserHistory> history) {
        this.history = history;
    }

    //We have to have to use interface for collections
    private List<UserHistory> history = new ArrayList<UserHistory>();

    public void setProteinData(ProteinData proteinData) {
        this.proteinData = proteinData;
        proteinData.setUser(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    private int id = 0;
    private  String name;


    @Override
    public String toString() {
        return "User{" +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
    //Should add bi-directional relation
    public void addHistory(UserHistory historyItem){
        historyItem.setUser(this);
        history.add(historyItem);
    }
}
