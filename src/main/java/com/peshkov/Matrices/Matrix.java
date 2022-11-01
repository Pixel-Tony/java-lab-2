package com.peshkov.Matrices;

import org.jetbrains.annotations.NotNull;

public class Matrix extends MatrixBase {
    public Matrix() {
        this(1, 1);
    }

    public Matrix(int height, int width) {
        super(height, width);
    }

    public Matrix(MatrixBase other) {
        super(other);
    }

    public Matrix(int height, int width, float min, float max) {
        super(height, width, min, max);
    }

    public Matrix(float @NotNull ... values) {
        super(values);
    }

    public float get(int row, int column) {
        super.getCheck(row, column);
        return matrix[row * columns + column];
    }

    public Matrix getRow(int index) {
        super.getRowCheck(index);
        var m = new Matrix(1, columns);
        System.arraycopy(matrix, index * columns, m.matrix, 0, columns);
        return m;
    }

    public Matrix getColumn(int index) {
        super.getColumnCheck(index);
        var m = new Matrix(rows, 1);
        for (int i = 0; i < rows; ++i) {
            m.matrix[i] = matrix[i * columns + index];
        }
        return m;
    }

    public void set(int row, int column, float value) {
        super.setCheck(row, column);
        matrix[row * columns + column] = value;
    }

    public void setRow(int index, float[] row) {
        setRow(index, new Matrix(row));
    }

    public void setRow(int index, Matrix row) {
        super.setRowCheck(index, row);
        System.arraycopy(row.matrix, 0, matrix, index * columns, columns);
    }

    public void setColumn(int index, Matrix column) {
        super.setColumnCheck(index, column);
        for (int i = 0; i < rows; ++i) {
            matrix[i * columns + index] = column.matrix[i];
        }
    }

    public static Matrix mul(MatrixBase left, MatrixBase right) {
        baseMulCheck(left, right);
        var m = new Matrix(left);
        m.mul(right);
        return m;
    }

    public static Matrix randomNumbers(int height, int width, float min, float max) {
        if (max < min) {
            throw new RuntimeException();
        }
        var m = new Matrix(height, width);
        for (int i = 0; i < height * width; ++i) {
            m.matrix[i] = (float) Math.random() * (max - min) + min;
        }
        return m;
    }

    public void mul(MatrixBase right) {
        baseMulCheck(this, right);
        var m = new Matrix(rows, right.columns);
        baseMul(this, right, m);
        rows = m.rows;
        columns = m.columns;
        matrix = new float[rows * columns];
        System.arraycopy(m.matrix, 0, matrix, 0, rows * columns);
    }

    @Override
    public String toString() {
        return super.baseToString("Matrix");
    }
}
