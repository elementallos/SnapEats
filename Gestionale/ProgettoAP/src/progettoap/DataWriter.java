package progettoap;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

public class DataWriter {
    private String fileName;

    public DataWriter(String fileName) {
        this.fileName = fileName;
    }

    public void writeIntToFile(int value) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        DataOutputStream dataOut = new DataOutputStream(new FileOutputStream(fileName));
        dataOut.writeInt(value);
        dataOut.close();
    }
    
    public void writeDoubleToFile(double value, boolean reset) throws IOException {
        File file = new File(fileName);
        if (reset && file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        DataOutputStream dataOut = new DataOutputStream(new FileOutputStream(fileName, !reset));
        dataOut.writeDouble(value);
        dataOut.close();
    }
}
