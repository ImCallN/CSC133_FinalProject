package com.example.snakegame;
import android.content.Context;
import android.graphics.Point;

import java.util.Random;

//Class that will create a random item at a random location
public class itemFactory {
    public itemFactory() {

    }

    private int randomSeed()
    {
        Random random = new Random();
        int x = random.nextInt(2);
        return x;
    }
    public items createItem(Context context, Point x, int y)
    {
        int seed = randomSeed();
        switch(seed)
        {
            case 0:
                return new GoldenApple(context, x, y);
            case 1:
                return new Apple(context, x, y);
            case 2:
                return new Blocker(context, x, y);
        }
        return null;
    }
}
