import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * This program presents the user with a JOptionPane to ask them for a count of nodes.
 * The program then builds a binary search tree with that many nodes.
 */
public class Main {

    /**
     * This is the main method that runs on program execution.
     * @param args String[] of commandline args.
     */
    public static void main(String[] args) {

        // Indicate that we are starting.
        System.out.print("Starting... ");

        // Create a new BalancedBST.
        BalancedBST<Integer> tree = new BalancedBST<>();

        // Get the number of nodes the user wants as 'count' using JOptionPane.
        int count;
        while (true){
            // Try to parse an int from the user.
            try {
                // Get the count from the user then break the while loop.
                count = Integer.parseInt(JOptionPane.showInputDialog(null, "How many values do you want?", "Create Balanced BST", JOptionPane.QUESTION_MESSAGE));
                break;
            }
            // On fail, repeat.
            catch (NumberFormatException e){
                // Tell the user to enter a valid integer as an error message.
                JOptionPane.showMessageDialog(null, "Please enter an integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Create a Random and generate integers from 0 through 999, adding each to the tree.
        Random random = new Random();
        for (int i=0;i<count;i++) tree.addNode(random.nextInt(100));

        // Create a matrix to store tree's values for the paint method.
        String[][] matrix = tree.toStringMatrix();

        // Get the screen dimensions.
        int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        // Create a new JFrame and canvas with desired attributes, and add the canvas to the frame.
        JFrame frame = new JFrame("Balanced BST of " + count + " nodes.");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Canvas canvas = new Drawing(matrix);
        canvas.setSize(width, height);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);

        // Indicate that we are done.
        System.out.print("Done");
    }

    /**
     * This class extends canvas to paint a binary tree.
     */
    static class Drawing extends Canvas{

        String[][] matrix; // Matrix representation of BalancedBST.

        /**
         * Constructor to set tree.
         * @param matrix Tree to be painted.
         */
        Drawing(String[][] matrix){
            this.matrix = matrix;
        }

        /**
         * This overridden method is called to draw on the canvas.
         * @param g Graphics object.
         */
        public void paint(Graphics g) {

            // Set unit size, font, and make a FontMetrics to measure strings.
            int sizeUnit = (int)(this.getWidth()/Math.pow(2, matrix.length-1));
            g.setFont(new Font("Serif", Font.PLAIN, (int)(sizeUnit*0.5)));
            FontMetrics fm = g.getFontMetrics();

            // The following double for-loop just draws lines between nodes.
            // Iterate through levels of the tree.
            for (int i=1; i<matrix.length; i++){
                // Iterate through nodes.
                for (int j=0; j<Math.pow(2, i);j++){
                    // Get x any y coordinates based of position in tree and canvas size.
                    int x = (int)(((j+0.5)*(this.getWidth()/Math.pow(2,i)))-(sizeUnit*0.5));
                    int y = (int)((i*(this.getHeight()/matrix.length))+(0.5*(this.getHeight()/matrix.length))-(0.5*sizeUnit));

                    // Get x any y coordinates for parent node based of position in tree and canvas size.
                    int parentX = (int)((((j/2)+0.5)*(this.getWidth()/Math.pow(2,(i-1))))-(sizeUnit*0.5));
                    int parentY = (int)(((i-1)*(this.getHeight()/matrix.length))+(0.5*(this.getHeight()/matrix.length))-(0.5*sizeUnit));

                    // Draw line to parent.
                    g.drawLine((int)(x+(sizeUnit*0.5)), (int)(y+(sizeUnit*0.5)), (int)(parentX+(sizeUnit*0.5)), (int)(parentY+(sizeUnit*0.5)));
                }
            }

            // The following double for-loop draws everything else.
            // Iterate through levels of the tree.
            for (int i=0; i<matrix.length; i++){
                // Iterate through nodes.
                for (int j=0; j<Math.pow(2, i);j++){

                    // Get x any y coordinates based of position in tree and canvas size.
                    int x = (int)(((j+0.5)*(this.getWidth()/Math.pow(2,i)))-(sizeUnit*0.5));
                    int y = (int)((i*(this.getHeight()/matrix.length))+(0.5*(this.getHeight()/matrix.length))-(0.5*sizeUnit));

                    // Draw circle at x,y.
                    g.setColor(Color.PINK);
                    g.fillOval(x, y, sizeUnit, sizeUnit);
                    g.setColor(Color.BLACK);

                    // Get the string to be drawn, and its size using FontMetrics.
                    String nodeVal = matrix[i][j];
                    double textWidth = fm.getStringBounds(nodeVal, g).getWidth();
                    double textHeight = fm.getStringBounds(nodeVal, g).getHeight();

                    // If text is too large, size down the font and reset size values.
                    if (textWidth > sizeUnit*0.9) {
                        g.setFont(new Font("Serif", Font.PLAIN, (int)(g.getFont().getSize()*((sizeUnit*0.9)/textWidth))));
                        fm = g.getFontMetrics();
                        textWidth = fm.getStringBounds(nodeVal, g).getWidth();
                        textHeight = fm.getStringBounds(nodeVal, g).getHeight();
                    }

                    // Draw the string and size the font back up.
                    g.drawString(nodeVal,(int)(x+(0.5*sizeUnit)-(0.5*textWidth)),(int)(y+(0.5*sizeUnit)+(0.5*textHeight)));
                    g.setFont(new Font("Serif", Font.PLAIN, (int)(sizeUnit*0.5)));
                    fm = g.getFontMetrics();
                }
            }
        }
    }
}
