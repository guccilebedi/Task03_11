package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Stack;

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
        Stack<String> stack = new Stack<>();
        Stack<String> reversedStack = new Stack<>();
        final String[] fileName = new String[1];
        fileName[0] = "test.txt";

        setFileButton.addActionListener(new ActionListener() {     // установить имя файла
            @Override
            public void actionPerformed(ActionEvent e) {
                fileName[0] = inputFileNameTextField.getText() + ".txt";
            }
        });

        fillStackButton.addActionListener(new ActionListener() {     // заполнить стек и показать его
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    StackUtil.fillStack(fileName[0], stack);
                    String[] arr = new String[stack.size()];
                    StackUtil.stackToArray(stack, arr);
                    JTableUtils.initJTableForArray(table1, 40, true, true, true, true);
                    com.company.JTableUtils.writeArrayToJTable(table1, arr);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        reverseStackButton.addActionListener(new ActionListener() {     // "перевернуть" стек и показать его
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    StackUtil.fillStack(fileName[0], stack);
                    StackUtil.reverseStack(stack, reversedStack);
                    String[] arr = new String[reversedStack.size()];
                    StackUtil.stackToArray(reversedStack, arr);
                    JTableUtils.initJTableForArray(table1, 40, true, true, true, true);
                    com.company.JTableUtils.writeArrayToJTable(table1, arr);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        clearStackButton.addActionListener(new ActionListener() {     // очистить интерфейс
            @Override
            public void actionPerformed(ActionEvent e) {
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
