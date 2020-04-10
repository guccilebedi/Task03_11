package com.company;

public class StackElement {
    private String data;     //данные ячейки
    private StackElement next;     // ссылка на следующую ячейку

    StackElement(String data, StackElement next) {     // конструктор для ячейки списка
        this.data = data;
        this.next = next;
    }

    public String getData() {
        return data;
    }

    public StackElement getNext() {
        return next;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setNext(StackElement next) {
        this.next = next;
    }
}
