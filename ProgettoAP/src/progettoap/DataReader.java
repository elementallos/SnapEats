package progettoap;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class DataReader {
    private String fileName;

    public DataReader(String fileName) {
        this.fileName = fileName;
    }

    public int readIntFromFile() throws IOException {
        DataInputStream dataIn = new DataInputStream(new FileInputStream(fileName));
        int value = dataIn.readInt();
        dataIn.close();
        return value;
    }
    
    public double readDoubleFromFile() throws IOException {
        DataInputStream dataIn = new DataInputStream(new FileInputStream(fileName));
        double value = dataIn.readDouble();
        dataIn.close();
        return value;
    }
}
