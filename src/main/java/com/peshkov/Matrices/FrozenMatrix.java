package com.peshkov.Matrices;

public class FrozenMatrix extends MatrixBase {
    public FrozenMatrix() {
        this(1, 1);
    }

    public FrozenMatrix(int height, int width) {
        super(height, width);
    }

    public FrozenMatrix(MatrixBase other) {
        super(other);
    }

    public FrozenMatrix(int height, int width, float min, float max) {
        this(height, width);
        if (max < min) throw new RuntimeException("Max value should be bigger than min");
        for (int i = 0; i < width * height; ++i) {
            matrix[i] = (float) Math.random() * (max - min) + min;
        }
    }

    public FrozenMatrix(float... values) {
        this(1, values.length);
        System.arraycopy(values, 0, matrix, 0, values.length);
    }

    public float get(int row, int column) {
        super.getCheck(row, column);
        return matrix[row * columns + column];
    }

    public FrozenMatrix getRow(int index) {
        super.getRowCheck(index);
        var m = new FrozenMatrix(1, columns);
        System.arraycopy(matrix, index * columns, m.matrix, 0, columns);
        return m;
    }

    public FrozenMatrix getColumn(int index) {
        super.getColumnCheck(index);
        var m = new FrozenMatrix(rows, 1);
        for (int i = 0; i < rows; ++i) {
            m.matrix[i] = matrix[i * columns + index];
        }
        return m;
    }

    public FrozenMatrix set(int row, int column, float value) {
        super.setCheck(row, column);
        var m = new FrozenMatrix(this);
        m.matrix[row * columns + column] = value;
        return m;
    }

    public static FrozenMatrix randomNumbers(int height, int width, float min, float max) {
        if (max < min) {
            throw new RuntimeException();
        }
        var m = new FrozenMatrix(height, width);
        for (int i = 0; i < height * width; ++i) {
            m.matrix[i] = (float) Math.random() * (max - min) + min;
        }
        return m;
    }

    public FrozenMatrix setRow(int index, Matrix row) {
        super.setRowCheck(index, row);
        var m = new FrozenMatrix(this);
        System.arraycopy(row.matrix, 0, m.matrix, index * columns, columns);
        return m;
    }

    public FrozenMatrix setRow(int index, float[] row) {
        return setRow(index, new Matrix(row));
    }

    public FrozenMatrix setColumn(int index, Matrix column) {
        super.setColumnCheck(index, column);
        var m = new FrozenMatrix(this);
        for (int i = 0; i < rows; ++i) {
            m.matrix[i * columns + index] = column.matrix[i];
        }
        return m;
    }

    public FrozenMatrix mul(MatrixBase right) {
        baseMulCheck(this, right);
        var m = new FrozenMatrix(this.rows, right.columns);
        baseMul(this, right, m);
        return m;
    }

    @Override
    public String toString() {
        return super.baseToString("Frozen Matrix");
    }
}
