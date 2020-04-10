package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class StackUtil {

    public static void fillStack(String fileName, Stack<String> stack) throws FileNotFoundException {     // метод заполнения стека значениями из файла
        Scanner scn = new Scanner(new File(fileName));
        while (scn.hasNext()) {
            stack.push(scn.nextLine());
        }
    }

    public static void reverseStack(Stack<String> stack, Stack<String> reversedStack) {     // метод "переворачивания" стека
        while (!stack.isEmpty()) {
            String element = stack.pop();
            reversedStack.push(element);
        }
    }

    public static void stackToArray(Stack<String> stack, String[] arr) {     // метод преобразования стека в массив
        int i = 0;
        while (!stack.isEmpty()) {
            arr[i] = stack.pop();
            i++;
        }
    }
}
