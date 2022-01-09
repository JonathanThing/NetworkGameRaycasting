import java.awt.Graphics;

import java.awt.*;

public class Player {
    private int width;
    private int height;
    private int x;
    private int y;
    private Color color;
    
    Player(int width, int height, int x, int y, Color color){
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    // Draw the player
    public void drawSprite(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    //--------------------Getters and Setters--------------------

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void changeX(int change){
        x += change;
    }

    public void changeY(int change){
        y += change;
    }
}
