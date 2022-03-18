package main;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
// TODO: 3/18/2022  *Create new files if there is already existing file, add spaces between keys pressed

public class Main implements NativeKeyListener {
    public static FileWriter textWriter;

    public static void main(String[] args) throws IOException {
        int counter = 0;
        File textOutput = new File("output.txt");
        if(textOutput.createNewFile()) {
            System.out.println("Output file created: " + textOutput.getName());
        }
        else {
            counter++;
            textOutput = new File("output" + counter + ".txt");
            System.out.println("File already exists...");
        }
        textWriter = new FileWriter(textOutput.getName());
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        GlobalScreen.addNativeKeyListener(new Main());
        Thread closingHook = new Thread(() -> {
            try {
                textWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Runtime.getRuntime().addShutdownHook(closingHook);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
        System.out.println("Pressed: " + NativeKeyEvent.getKeyText(nativeEvent.getKeyCode()));
        try {
            textWriter.write(NativeKeyEvent.getKeyText(nativeEvent.getKeyCode()) + " ");
        } catch (IOException e) {
            System.out.println("Error occured when writing...");
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
        System.out.println("Released: " + NativeKeyEvent.getKeyText(nativeEvent.getKeyCode()));
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
        NativeKeyListener.super.nativeKeyTyped(nativeEvent);
    }
}
