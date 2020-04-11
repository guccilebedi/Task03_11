package com.company;

public class LinkedStack {
    private StackElement head;     // первый элемент списка
    private StackElement tail;     // последний элемент списка

    public LinkedStack() { }

    public boolean isEmpty() {
        return head == null;
    }

    public void push(String data) {
        StackElement temp = new StackElement(data, null);
        if (isEmpty()) {
            this.head = temp;
            this.tail = head;
        } else {
            temp.setNext(head);
            head = temp;
        }
    }

    public String pop() {
        String temp = head.getData();
        if (head.getNext() == null) {
            head.setData(null);
        } else {
            head = head.getNext();
        }
        return temp;
    }

    public LinkedStack reverse() {
        LinkedStack reversedStack = new LinkedStack();
        while (head.getData() != null) {
            reversedStack.push(pop());
        }
        return reversedStack;
    }

    public String[] toArray(int size) {
        String[] arr = new String[size];
        for (int i = 0; i < size; i++) {
            arr[i] = pop();
        }
        return arr;
    }

    public void clear() {
        head = null;
        tail = null;
    }
}

