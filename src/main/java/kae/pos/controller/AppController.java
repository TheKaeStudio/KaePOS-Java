package kae.pos.controller;

import kae.pos.view.MainFrame;

import javax.swing.*;

public class AppController {

    private final MainFrame frame;

    public AppController(MainFrame frame) {
        this.frame = frame;
        frame.getMiQuit().addActionListener(e -> quit());
    }

    private void quit() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Quit the application?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            frame.dispose();
            System.exit(0);
        }
    }
}