package com.example.chickenapppbl5;

public interface ItemTouchListener {
    void onMove(int oldPosition, int newPosition);

    void swipe(int position, int direction);
}

