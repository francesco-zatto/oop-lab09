package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix {

    private final int numWorkers;

    public MultiThreadedSumMatrix(final int numWorkers) {
        this.numWorkers = numWorkers;
    }

    private class Worker extends Thread {
        private final List<Double> matrixToList;
        private final int numWorkers;
        private final int startPos;
        private final int stopPos;
        private double partialSum;

        public Worker(final double[][] matrix, final int numWorkers, final int startPos, final int stopPos) {
            this.matrixToList = toList(matrix);
            this.numWorkers = numWorkers;
            this.startPos = startPos;
            this.stopPos = stopPos;
        }

        @Override
        public void run() {
            System.out.println("Starting from " + this.startPos + " to " + this.stopPos);
            for (int i = this.startPos; i < this.stopPos; i++) {
                partialSum = partialSum + this.matrixToList.get(i);
            }
        }

        private List<Double> toList(double[][] matrix) {
            final List<Double> list = new ArrayList<>();
            for (final double[] row : matrix) {
                for (final double elem : row) {
                    list.add(elem);
                }
            }
            return list;
        }

        public double getPartialSum() {
            return this.partialSum;
        } 

    }

    @Override
    public double sum(double[][] matrix) {
        Double sum = 0.0;
        final int nelem = matrix.length * numMatrixCols(matrix) / this.numWorkers;
        //TODO: calcola il resto
        int startPos = 0;
        final List<Worker> workers = new ArrayList<>();
        for (int i = 1; i <= this.numWorkers; i++) {
            workers.add(new Worker(matrix, this.numWorkers, startPos, startPos + nelem));
            workers.get(i).start();
            startPos = i == this.numWorkers ? //TODO ;            
        }
        return sum;
    }

    private int numMatrixCols(double[][] matrix) {
        for (double[] row : matrix) {
            return row.length;
        }
        return 0;
    }
    
}
