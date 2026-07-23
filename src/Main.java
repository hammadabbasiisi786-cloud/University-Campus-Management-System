import com.campus.backend.fileio.DataManager;
import com.campus.frontend.LoginFrame;

public class Main {
    public static void main(String[] args) {
        DataManager dm = new DataManager();
        dm.loadAll();
        dm.startAutoSave();
        new LoginFrame(dm);
    }
}
