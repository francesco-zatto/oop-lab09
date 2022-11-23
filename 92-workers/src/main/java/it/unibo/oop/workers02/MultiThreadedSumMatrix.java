package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;

final public class MultiThreadedSumMatrix implements SumMatrix {

    private final int numWorkers;

    /**
     * @param numWorkers. Initialize an instance with the number of threads.
     */
    public MultiThreadedSumMatrix(final int numWorkers) {
        this.numWorkers = numWorkers;
    }

    private class Worker extends Thread {
        private final List<double[]> rowsList;
        private final int startPos;
        private final int stopPos;
        private double partialSum;

        Worker(final List<double[]> matrixToList, final int startPos, final int stopPos) {
            this.startPos = startPos;
            this.stopPos = stopPos;
            this.rowsList = matrixToList.subList(startPos, stopPos);
        }

        @Override
        public void run() {
            System.out.println("Starting from " + this.startPos + " to " + this.stopPos);
            for (var row : this.rowsList) {
                for (double elem : row) {
                    this.partialSum = partialSum + elem;
                }
            }
        }

        public double getPartialSum() {
            return this.partialSum;
        } 

    }

    /* 
     * A method that takes a matrix and sums all its elements using more than
     * one thread.
     */
    @Override
    public double sum(final double[][] matrix) {
        final List<double[]> rowsList = toRowsList(matrix);
        final List<Worker> workers = new ArrayList<>();
        final int lastWorker = this.numWorkers - 2;
        int rowsPerThreads = rowsList.size() / this.numWorkers;
        final int divRest = rowsList.size() % this.numWorkers;
        Double sum = 0.0;
        int startRow = 0;
        for (int i = 0; i < this.numWorkers; i++) {
            workers.add(new Worker(rowsList, startRow, startRow + rowsPerThreads));
            workers.get(i).start();
            startRow = startRow + rowsPerThreads;
            if (i == lastWorker) {
                rowsPerThreads = rowsPerThreads + divRest;
            }
        }
        for (var worker : workers) {
            try {
                worker.join();
                sum = sum + worker.getPartialSum();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return sum;
    }

    private static List<double[]> toRowsList(final double[][] matrix) {
        final List<double[]> list = new ArrayList<>();
        for (final double[] row : matrix) {
            list.add(row);
        }
        return list;
    }
}
