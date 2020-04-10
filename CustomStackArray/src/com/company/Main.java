package com.company;

public class Main {

    public static void main(String[] args) {
        ArrayStack stack = new ArrayStack();
        boolean checker;
        int top;
        int pop;
        int size;
        checker = stack.isEmpty();
        System.out.println(checker);     // проверяет пустой ли стек, выведет true
        for (int i = 1; i <= 5; i++) {     // добавляет в стек элементы от 1 до 5
            stack.push(i);
        }
        checker = stack.isEmpty();
        size = stack.size();
        System.out.println(checker);
        System.out.println(size);
        top = stack.peek();
        System.out.println(top);     // показывает верхний элемент
        pop = stack.pop();
        System.out.println(pop);     //убирает из стека верхний элемент и показывает его
        top = stack.peek();
        System.out.println(top);
        size = stack.size();
        System.out.println(size);     // показывает размер стека на данный момент
    }
}
