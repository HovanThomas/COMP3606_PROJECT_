package com.example.javastartupcomponent;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private static final String FILE_NAME = "attempts.dat";

    public static synchronized void saveAttempt(Context context, Person person) {
        List<Person> attempts = loadAttempts(context);
        attempts.add(person);
        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(attempts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static synchronized List<Person> loadAttempts(Context context) {
        List<Person> attempts = new ArrayList<>();
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            return attempts;
        }
        try (FileInputStream fis = context.openFileInput(FILE_NAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                attempts = (List<Person>) obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attempts;
    }

    public static synchronized void clearAttempts(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }
}
