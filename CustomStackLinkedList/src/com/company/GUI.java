package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GUI {

    private JPanel panel;
    private JTable table1;
    private JTextField inputFileNameTextField;
    private JLabel inputFileNameText;
    private JButton fillStackButton;
    private JButton reverseStackButton;
    private JButton setFileButton;
    private JButton clearStackButton;

    public GUI() {
        LinkedStack stack = new LinkedStack();
        final String[] fileName = new String[1];
        fileName[0] = "test.txt";
        int[] size = {0};

        setFileButton.addActionListener(new ActionListener() {     // установить имя файла
            @Override
            public void actionPerformed(ActionEvent e) {
                fileName[0] = inputFileNameTextField.getText() + ".txt";
            }
        });

        fillStackButton.addActionListener(new ActionListener() {     // заполнить список и показать его
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Scanner scn = new Scanner(new File(fileName[0]));
                    while (scn.hasNext()) {
                        size[0]++;
                        stack.push(scn.nextLine());
                    }
                    String[] arr = stack.toArray(size[0]);
                    JTableUtils.initJTableForArray(table1, 40, true, true, true, true);
                    JTableUtils.writeArrayToJTable(table1, arr);
                    size[0] = 0;
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        reverseStackButton.addActionListener(new ActionListener() {     // "перевернуть" список, показать его и очистить
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Scanner scn = new Scanner(new File(fileName[0]));
                    while (scn.hasNext()) {
                        size[0]++;
                        stack.push(scn.nextLine());
                    }
                    LinkedStack reversedStack = stack.reverse();
                    String[] arr = reversedStack.toArray(size[0]);
                    JTableUtils.initJTableForArray(table1, 40, true, true, true, true);
                    JTableUtils.writeArrayToJTable(table1, arr);
                    size[0] = 0;
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        clearStackButton.addActionListener(new ActionListener() {     // очистить список
            @Override
            public void actionPerformed(ActionEvent e) {
                stack.clear();
                JTableUtils.initJTableForArray(table1, 40, true, true, true, true);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setContentPane((new GUI().panel));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
