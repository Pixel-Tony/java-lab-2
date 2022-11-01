package com.peshkov.Matrices;

import java.util.function.Function;
import java.util.function.Supplier;

public class Matrix extends MatrixBase {
    public Matrix() {
        this(1, 1);
    }

    public Matrix(int height, int width) {
        super(height, width);
    }

    public Matrix(int height, int width, Function<Integer, Float> supply) {
        super(height, width, supply);
    }

    public Matrix(int height, int width, Supplier<Float> supply) {
        super(height, width, supply);
    }

    public Matrix(MatrixBase other) {
        super(other);
    }

    public Matrix(int height, int width, float min, float max) {
        super(height, width, min, max);
    }

    public Matrix(float[] values) {
        super(values);
    }

    public Matrix getRow(int index) {
        return (Matrix) super.getRow(index);
    }

    public Matrix getColumn(int index) {
        return (Matrix) super.getColumn(index);
    }

    public Matrix set(int row, int column, float value) {
        return (Matrix) set(this, row, column, value);
    }

    public Matrix setRow(int index, float[] row) {
        return (Matrix) setRow(this, index, row);
    }

    public Matrix setRow(int index, MatrixBase row) {
        return (Matrix) setRow(this, index, row);
    }

    public Matrix setColumn(int index, float[] column) {
        return (Matrix) setColumn(this, index, column);
    }

    public Matrix setColumn(int index, MatrixBase column) {
        return (Matrix) setRow(this, index, column);
    }

    public Matrix mul(MatrixBase right) {
        if (columns != right.rows) throw new RuntimeException("Row-column count mismatch");
        Matrix left = new Matrix(this);
        columns = right.columns;
        matrix = new float[rows * columns];

        mul(left, right, this);
        return this;
    }

    @Override
    public String toString() {
        return super.baseToString("Matrix");
    }

    @Override
    protected MatrixBase createInstance(int rows, int columns) {
        return new Matrix(rows, columns);
    }
}
