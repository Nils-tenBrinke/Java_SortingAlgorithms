import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.util.Random;

public class InsertionSort extends JPanel{
    private static final long serialVersionUID = 1L;
    private final int WIDTH = 1000, HEIGHT = WIDTH * 9 / 16;
    private final int SIZE = 250;
    private final float BAR_WIDTH = (float)WIDTH / SIZE;
    private float[] BAR_HEIGHT = new float[SIZE];
    private SwingWorker<Void, Void> shuffler, sorter;
    private int current_index, traversing_index;


    private InsertionSort(){
        setBackground(Color.black);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initBarHeight();
        initSorter();
        initShuffler();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.white);
        Rectangle2D.Float bar;
        for (int i = 0; i < SIZE; i++){
            bar = new Rectangle2D.Float(i * BAR_WIDTH, 0, BAR_WIDTH, BAR_HEIGHT[i]);
            g2d.fill(bar);
        }

        g2d.setColor(Color.red);
        bar = new Rectangle2D.Float(current_index * BAR_WIDTH, 0, BAR_WIDTH, BAR_HEIGHT[current_index]);
        g2d.fill(bar);
        
        g2d.setColor(Color.green);
        bar = new Rectangle2D.Float(traversing_index * BAR_WIDTH, 0, BAR_WIDTH, BAR_HEIGHT[traversing_index]);
        g2d.fill(bar);

    }

    
    private void initSorter() {
        sorter = new SwingWorker<>(){
            @Override
            public Void doInBackground() throws InterruptedException {
                for (current_index = 1; current_index < SIZE; current_index++){
                    traversing_index = current_index;
                    while(traversing_index > 0 && BAR_HEIGHT[traversing_index] < BAR_HEIGHT[traversing_index-1]){
                        swap(traversing_index, traversing_index-1);
                        traversing_index--;
                        Thread.sleep(1);
                        repaint();
                    }
                }
                current_index = 0;
                traversing_index = 0;
                
                return null;
            }
        };
    }
    
    private void initShuffler() {
        shuffler = new SwingWorker<>(){
            @Override
            public Void doInBackground() throws InterruptedException {
                int middle = SIZE / 2;
                for (int i = 0, j = middle; i < middle; i++, j++){
                    int random_index = new Random().nextInt(SIZE);
                    swap(i, random_index);
                    
                    random_index = new Random().nextInt(SIZE);
                    swap(j, random_index);
                    
                    Thread.sleep(10);
                    repaint();
                }
                
                return null;
            }
            @Override
            public void done() {
                super.done();
                sorter.execute();
            }
        };
        shuffler.execute();
    }
    
    private void initBarHeight() {
        float interval = (float)HEIGHT / SIZE;
        for (int i = 0; i < SIZE; i++){
            BAR_HEIGHT[i] = i * interval;
        }
    }

    private void swap(int indexA, int indexB) {
        float temp = BAR_HEIGHT[indexA];
        BAR_HEIGHT[indexA] = BAR_HEIGHT[indexB];
        BAR_HEIGHT[indexB] = temp;

    }

    public static void main(String args[]){
        SwingUtilities.invokeLater(()->{
            JFrame frame = new JFrame("Insertion Algorithm");
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new InsertionSort());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
