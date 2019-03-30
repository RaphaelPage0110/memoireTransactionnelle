package fr.univ.nantes.carlo;

public class PiMonteCarlo {

    private int nbPoints, goodPoints;

    private double pi;

    private class Point {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public PiMonteCarlo(int nbPoints) {
        this.nbPoints = nbPoints;
        this.goodPoints = 0;
        generateNPoints();
    }

    private void generateNPoints() {
        for (int i = 0; i < this.nbPoints; i++) {
            Point pt = new Point(Math.random(), Math.random());

            if (pt.x * pt.x + pt.y * pt.y < 1.0) {
                this.goodPoints++;
            }
        }
    }

    public double getPi() {
        return (double)this.goodPoints*4.0 / (double)this.nbPoints;
    }
}
