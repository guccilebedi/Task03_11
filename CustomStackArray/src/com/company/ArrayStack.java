package com.company;

public class ArrayStack {
    private int size;
    private int[] stackArray;     // массив стека
    private int top;     // индекс верхнего элемента

    public ArrayStack() {     // конструктор массива стека
        size = 0;
        stackArray = new int[size];
        top = -1;
    }

    public void push(int element) {     // добавление элемента
        size++;
        int[] temp = new int[size];
        for (int i = 0; i < size - 1; i++) {
            temp[i] = stackArray[i];
        }
        temp[size - 1] = element;
        stackArray = temp;
        top++;
    }

    public int pop() {     // стирание верхнего элемента
        if (isEmpty()) {
            return 0;
        }
        else {
            int temp = stackArray[size - 1];
            size--;
            int[] tempArray = new int[size];
            for (int i = 0; i < size; i++) {
                tempArray[i] = stackArray[i];
            }
            stackArray = tempArray;
            top--;
            return temp;
        }
    }

    public int peek() {     // демонстрация верхнего элемента
        if (isEmpty()) {
            return 0;
        }
        else return stackArray[size - 1];
    }

    public int size() {     // размер стека на данный момент
        return size;
    }

    public boolean isEmpty() {     // проверка пустоты стека
        return top == -1;
    }
}
