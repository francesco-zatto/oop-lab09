package it.unibo.oop.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Third experiment with reactive gui.
 */
public final class AnotherConcurrentGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private static final int SLEEP_MSECS = 10;
    private final JLabel display = new JLabel();
    private final JButton down = new JButton("down");
    private final JButton up = new JButton("up");
    private final JButton stop = new JButton("stop");

    /**
     * Builds a new CGUI.
     */
    public AnotherConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        panel.add(display);
        panel.add(down);
        panel.add(up);
        panel.add(stop);
        this.getContentPane().add(panel);
        this.setVisible(true);
        final Counter counter = new Counter();
        final Thread disable = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(SLEEP_MSECS);
                        counter.stopCounting();
                    } catch (InterruptedException e) {
                        e.printStackTrace(); //NOPMD just an exercise
                    }
                }
        }); 
        counter.start();
        disable.start();
        up.addActionListener((e) -> counter.setCountUp());
        down.addActionListener((e) -> counter.setCountDown());
        stop.addActionListener((e) -> counter.stopCounting());
    }

    private class Counter extends Thread {

        private volatile boolean stop;
        private volatile boolean countDown;
        private int count;

        @Override
        public void run() {
            while (!this.stop) {
                try {
                    Thread.sleep(100);
                    final String nextText = Integer.toString(this.count);
                    SwingUtilities.invokeAndWait(() -> AnotherConcurrentGUI.this.display.setText(nextText));
                    this.count = countDown ? this.count - 1 : this.count + 1;
                } catch (InterruptedException | InvocationTargetException e) {
                    e.printStackTrace(); //NOPMD just an exercise
                }
            }
        }

        /**
         * Sets countDown to false and the counter goes up.
         */
        public void setCountUp() {
            this.countDown = false;
        }

        /**
         * Stops the counter, setting the while's condition in run() to false.
         */
        public void stopCounting() {
            this.stop = true;
        }

        /**
         * Sets countDown to true and the counter goes down.
         */
        public void setCountDown() {
            this.countDown = true;
        }

    }

}
