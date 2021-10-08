package IO;

import java.io.File;

public class Filereader {

    public boolean fileEmpty(String path) {
        boolean empty = false;
        File file = new File(path);
        if (file.length() == 0) {
            empty = true;
        } else {
            empty = false;

        }
        return empty;
    }

}
