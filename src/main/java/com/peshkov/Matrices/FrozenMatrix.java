package com.peshkov.Matrices;

import java.util.function.Function;
import java.util.function.Supplier;

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

    public FrozenMatrix(int height, int width, Function<Integer, Float> supplier) {
        super(height, width, supplier);
    }

    public FrozenMatrix(int height, int width, Supplier<Float> supplier) {
        super(height, width, supplier);
    }

    @Override
    protected MatrixBase createInstance(int rows, int columns) {
        return new FrozenMatrix(rows, columns);
    }

    public FrozenMatrix(int height, int width, float min, float max) {
        super(height, width, min, max);
    }

    public FrozenMatrix(float [] values) {
        super(values);
    }

    public float get(int row, int column) {
        return super.get(row, column);
    }

    public FrozenMatrix getRow(int index) {
        return (FrozenMatrix) super.getRow(index);
    }

    public FrozenMatrix getColumn(int index) {
        return (FrozenMatrix) super.getColumn(index);
    }

    public FrozenMatrix set(int row, int column, float value) {
        return (FrozenMatrix) set(new FrozenMatrix(this), row, column, value);
    }

    public FrozenMatrix setRow(int index, MatrixBase row) {
        return (FrozenMatrix) setRow(new FrozenMatrix(this), index, row);
    }

    public FrozenMatrix setRow(int index, float[] row) {
        return (FrozenMatrix) setRow(new FrozenMatrix(this), index, row);
    }

    public FrozenMatrix setColumn(int index, MatrixBase column) {
        return (FrozenMatrix) setColumn(new FrozenMatrix(this), index, column);
    }

    public FrozenMatrix setColumn(int index, float[] column) {
        return (FrozenMatrix) setColumn(new FrozenMatrix(this), index, column);
    }

    public FrozenMatrix mul(MatrixBase right) {
        if (columns != right.rows) {
            throw new RuntimeException("Row-column count mismatch");
        }
        FrozenMatrix result = new FrozenMatrix(rows, right.columns);

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                var leftVal = matrix[columns * i + j];
                for (int k = 0; k < right.columns; ++k) {
                    result.matrix[i * right.columns + k] += leftVal * right.matrix[j * right.columns + k];
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return super.baseToString("Frozen Matrix");
    }
}
