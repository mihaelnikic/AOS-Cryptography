package hr.fer.zemris.nos.utils;

/**
 * Created by mihael on 01.05.17..
 */
public class Pair<X,Y> {

    private X x;
    private Y y;


    public Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public static <X,Y> Pair<X,Y> createPair(X x, Y y) {
        return new Pair<>(x, y);
    }

    public X getX() {
        return x;
    }

    public void setX(X x) {
        this.x = x;
    }

    public Y getY() {
        return y;
    }

    public void setY(Y y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (x != null ? !x.equals(pair.x) : pair.x != null) return false;
        return y != null ? y.equals(pair.y) : pair.y == null;
    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }
}
