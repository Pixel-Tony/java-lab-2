package com.peshkov.Matrices;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class GMatrix<T> {
    protected int rows;
    protected int columns;
    protected List<T> matrix;

    public GMatrix() {
        rows = columns = 1;
        matrix = new ArrayList<>(1);
        matrix.add(null);
    }

    private GMatrix(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            throw new RuntimeException();
        }
        this.rows = rows;
        this.columns = columns;
        matrix = new ArrayList<>(rows * columns);
    }

    public GMatrix(int rows, int columns, T[] src) {
        this(rows, columns);
        if (src.length != rows * columns) {
            throw new RuntimeException();
        }
        matrix = new ArrayList<>(List.of(src));
    }

    public GMatrix(int rows, int columns, Iterable<T> src) {
        this(rows, columns);
        if (src == null) throw new RuntimeException();

        var iterator = src.iterator();
        for (int i = 0; i < rows * columns; ++i) {
            if (!iterator.hasNext()) {
                throw new RuntimeException();
            }
            matrix.add(iterator.next());
        }
    }

    public GMatrix(int rows, int columns, Supplier<T> generator) {
        this.rows = rows;
        this.columns = columns;

        matrix = new ArrayList<>(rows * columns);
        for (int i = 0; i < rows * columns; ++i) {
            matrix.add(i, generator.get());
        }
    }

    public T get(int row, int column) {
        checkIndex(row, rows - 1);
        checkIndex(column, columns - 1);
        return matrix.get(row * columns + column);
    }

    public GMatrix<T> getRow(int index) {
        checkIndex(index, rows - 1);
        return new GMatrix<>(1, columns, matrix.subList(index * columns, (index + 1) * columns));
    }

    public GMatrix<T> getColumn(int index) {
        checkIndex(index, columns - 1);
        var m = new GMatrix<T>(rows, 1);
        for (int i = 0; i < rows; ++i) {
            m.matrix.add(i, matrix.get(columns * i + index));
        }
        return m;
    }

    public void set(int row, int column, T value) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) {
            throw new RuntimeException();
        }
        matrix.set(row * columns + column, value);
    }

    private void checkIndex(int index, int max) {
        if (index < 0 || index > max) {
            throw new RuntimeException();
        }
    }

    public void setRow(int index, Iterable<T> src) {
        checkIndex(index, rows - 1);
        var iter = src.iterator();

        for (int i = 0; i < columns; ++i) {
            if (!iter.hasNext()) {
                throw new RuntimeException();
            }
            matrix.set(index * columns + i, iter.next());
        }
    }

    public void setColumn(int index, Iterable<T> src) {
        checkIndex(index, columns - 1);
        var iter = src.iterator();

        for (int i = 0; i < rows; ++i) {
            if (!iter.hasNext()) {
                throw new RuntimeException();
            }
            matrix.set(i * columns + index, iter.next());
        }
    }

    public GMatrix(GMatrix<T> other) {
        if (other == null) throw new RuntimeException();
        rows = other.rows;
        columns = other.columns;
        matrix = new ArrayList<>(rows * columns);
        for (int i = 0; i < rows * columns; ++i) {
            matrix.add(other.matrix.get(i));
        }
    }

    public GMatrix(int rows, int columns, Function<Integer, T> supplier) {
        this(rows, columns);
        if (supplier == null) throw new RuntimeException();

        for (int i = 0; i < rows * columns; ++i) {
            matrix.add(supplier.apply(i));
        }
    }

    public GMatrix<T> filter(Function<T, Boolean> predicate, T _default, Function<T, T> copy) {
        var m = new GMatrix<T>(rows, columns);
        if (predicate == null || copy == null) throw new RuntimeException();
        T element;
        for (int i = 0; i < rows * columns; ++i) {
            m.matrix.add(predicate.apply(element = matrix.get(i)) ? copy.apply(element) : _default);
        }
        return m;
    }

    public GMatrix<T> add(GMatrix<T> right, BinaryOperator<T> addT) {
        if (right == null || addT == null) throw new RuntimeException();
        var m = new GMatrix<>(this);

        for (int i = 0; i < rows * columns; ++i) {
            m.matrix.set(i, addT.apply(matrix.get(i), right.matrix.get(i)));
        }
        return m;
    }

    public GMatrix<T> mul(GMatrix<T> right, BinaryOperator<T> addT, BinaryOperator<T> mulT) {
        if (columns != right.rows) throw new RuntimeException("Row-column count mismatch");
        if (addT == null || mulT == null) throw new RuntimeException();

        var m = new GMatrix<T>(rows, right.columns);
        for (int i = 0; i < rows * right.columns; ++i) {
            m.matrix.add(null);
        }

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                var leftVal = matrix.get(columns * i + j);

                for (int k = 0; k < right.columns; ++k) {
                    T cur;
                    var idx = i * right.columns + k;
                    m.matrix.set(idx, (cur = m.matrix.get(idx)) == null
                            ? mulT.apply(leftVal, right.matrix.get(j * right.columns + k))
                            : addT.apply(cur, mulT.apply(leftVal, right.matrix.get(j * right.columns + k)))
                    );
                }
            }
        }
        return m;
    }

    public String format(Function<T, String> s) {
        if (s == null) throw new RuntimeException();
        var sb = new StringBuilder("GMatrix [\n");
        for (int i = 0; i < rows; ++i) {
            if (i != 0) {
                sb.append(",\n");
            }
            sb.append("\t");
            sb.append("[").append(s.apply(matrix.get(columns * i)));
            for (int j = 1; j < columns; ++j) {
                sb.append(", ").append(s.apply(matrix.get(columns * i + j)));
            }
            sb.append("]");
        }
        return sb.append("\n]").toString();
    }

    @Override
    public int hashCode() {
        return (matrix.hashCode() * 31 + rows) * 31 + columns;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return false;
        if (obj == null) return false;
        if (!(getClass() == obj.getClass())) return false;
        var m = (GMatrix<?>) obj;
        if (!(m.columns == columns && m.rows == rows)) return false;

        for (int i = 0; i < rows * columns; ++i) {
            if (!Objects.equals(matrix.get(i), m.matrix.get(i))) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return format(Objects::toString);
    }

    public interface BinaryOperator<T> {
        T apply(T one, T other);
    }
}
