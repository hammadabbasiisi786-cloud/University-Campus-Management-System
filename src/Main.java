import com.campus.FileIO.DataManager;
import com.campus.GUI.LoginFrame;

public class Main {
    public static void main(String[] args) {
        DataManager dm = new DataManager();
        dm.loadAll();
        dm.startAutoSave();
        new LoginFrame(dm);
    }
}