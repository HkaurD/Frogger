import java.awt.*;

// this class is used to create a button; e.g. play, how to play, next, back etc.
public class Button {
    private String type; // type is the text displayed on the button
    // the x, y coordinates represent the top left corner of the button
    private int x;
    private int y;

    private int width;
    private int height;
    private Color col;

    public Button(String type, int x, int y, int width, int height, Color c){
        // initializing the variables
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        col = c;
    }
    // this method is used to draw the button
    public void drawButton(Graphics g){
        g.setColor(col);
        g.drawRect(x, y, width, height);
        g.drawString(type, x + 15, y + 35); // drawing the text of the button in the middle
    }
    // this method is used to make the button have a hovering effect when the mouse is over the button; changes the colour
    public void hoverButton(Graphics g){
        // re-drawing the button outline and text in a different colour to make a hovering effect
        g.setColor(new Color(177, 119, 143));
        g.drawRect(x, y, width, height);
        g.drawString(type, x + 15, y + 35);
    }
}