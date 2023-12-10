package com.example.snakegame;

import android.graphics.Point;
import android.view.MotionEvent;

import java.util.ArrayList;

public class SnakeObserver {
    private Point moveRange;    //The size of the screen

    //Constructor
    public SnakeObserver(Point moveRange)
    {
        //Set the screen size so we can do bounds checking
        this.moveRange = moveRange;
    }

    //Detect if the snake has collided with the borders or itself
    public boolean detectCollision(Snake snake)
    {
        //Check for collision with the borders
        if(snake.getHead().x == -1 || snake.getHead().x > moveRange.x || snake.getHead().y == -1 || snake.getHead().y > moveRange.y)
        {
            //Snake is dead, pass that to game
            return true;
        }

        //Check for collision with itself with enhanced for loop
        int counter = 0;
        for(Point part : snake.getSegmentLocations())
        {
            //Added a counter so that it doesn't include it's head position in this iteration
            if(snake.getHead().x == part.x && snake.getHead().y == part.y && counter != 0)
            {
                return true;
            }
            counter++;
        }
        return false;
    }

    //Detects the collision of a snake object and an item object
    public boolean detectCollision(Snake snake, items item)
    {
        if(snake.getHead().x == item.getLoca().x && snake.getHead().y == item.getLoca().y)
        {
            return true;
        }
        return false;
    }
    public boolean detecttailCollision(Snake snake, items item)
    {
      for(int i = snake.getSegmentLocations().size()-1;i > 0;i--) {
          if(item.getLoca().x == snake.getSegmentLocations().get(i).x && item.getLoca().y == snake.getSegmentLocations().get(i).y){
              return true;
          }
      }
        return false;
    }

    //Spawn the snake in
    public void spawnSnake(Snake snake, int width, int height)
    {
        snake.reset(width, height);
    }

    public void growSnake(Snake snake, int size)
    {
        for(int i = 0; i < size; i++)
        {
            snake.addSegment();
        }
    }
    public void cutSnake(Snake snake, int tail){
        for(int i = 0; i <= tail-1; i++)
        {
            snake.removeSegment(tail);
        }
    }

    public void switchHeading(Snake snake, MotionEvent motionEvent)
    {
        snake.switchHeading(motionEvent);
    }

    //Passes info to the snake that it should move
    public void moveSnake(Snake snake)
    {
        snake.move();
    }

}

