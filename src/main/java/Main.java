import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Frame fr = new Frame();
        Database db = Database.getInstance();
        db.initialize();

    }
}
