package com.example.radiologi.utils;

public class Event<T> {
    private final T content;
    private boolean hasBeenHandled = false;

    public Event(T content) {
        this.content = content;
    }

    public boolean isHasBeenHandled() {
        return hasBeenHandled;
    }

    public T getContentIfNotHandled(){
        if (hasBeenHandled){
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }

    public T peekContent(){
        return content;
    }
}
