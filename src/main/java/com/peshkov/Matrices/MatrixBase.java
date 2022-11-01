package com.peshkov.Matrices;


import java.util.Arrays;
import java.util.Objects;

abstract class MatrixBase {
    protected int rows;
    protected int columns;
    protected float[] matrix;

    protected MatrixBase() {
        this(1, 1);
    }

    protected MatrixBase(int height, int width) {
        if (height <= 0 || width <= 0) {
            throw new RuntimeException("matrix dimensions should be positive");
        }
        rows = height;
        columns = width;
        matrix = new float[rows * columns];
    }

    protected MatrixBase(MatrixBase other) {
        if (Objects.isNull(other)) throw new RuntimeException("Object can't be null");
        rows = other.rows;
        columns = other.columns;
        matrix = new float[rows * columns];
        System.arraycopy(other.matrix, 0, matrix, 0, rows * columns);
    }

    protected MatrixBase(int height, int width, float min, float max) {
        this(height, width);
        if (max < min) throw new RuntimeException("Max value should be bigger than min");
        for (int i = 0; i < width * height; ++i) {
            matrix[i] = (float) Math.random() * (max - min) + min;
        }
    }

    protected MatrixBase(float... values) {
        this(1, values.length);
        System.arraycopy(values, 0, matrix, 0, values.length);
    }

    protected void getCheck(int row, int column) {
        if (row < 0 || column < 0 || row >= rows || column >= columns) {
            throw new RuntimeException("row/column indexes should be positive");
        }
    }

    protected void getRowCheck(int index) {
        if (index < 0 || index >= rows) {
            throw new RuntimeException("index out of bounds");
        }
    }

    protected void getColumnCheck(int index) {
        if (index < 0 || index >= columns) {
            throw new RuntimeException("index out of bounds");
        }
    }

    protected void setCheck(int row, int column) {
        if (row < 0 || row >= rows) {
            throw new RuntimeException("Row index out of bounds");
        } else if (column < 0 || column >= columns) {
            throw new RuntimeException("Column index out of bounds");
        }
    }

    protected void setRowCheck(int index, MatrixBase row) {
        if (index < 0 || index >= rows) {
            throw new RuntimeException("Row index out of bounds");
        }
        if (row == null) {
            throw new RuntimeException("Row matrix can't be null");
        }
        if (!(row.columns == columns && row.rows == 1)) {
            throw new RuntimeException("Row dimensions mismatch");
        }
    }

    protected void setColumnCheck(int index, MatrixBase column) {
        if (index < 0 || index >= columns) {
            throw new RuntimeException("Column index out of bounds");
        }
        if (column == null) {
            throw new RuntimeException("Row matrix can't be null");
        }
        if (!(column.rows == rows && column.columns == 1)) {
            throw new RuntimeException("Column dimensions mismatch");
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int[] getDimensions() {
        return new int[]{rows, columns};
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj.getClass() != getClass()) return false;
        var m = (MatrixBase) obj;
        return m.columns == columns && m.rows == rows && Arrays.equals(m.matrix, matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(matrix);
    }

    protected static void baseMulCheck(MatrixBase left, MatrixBase right) {
        if (left.columns != right.rows) {
            throw new RuntimeException("Row-column count mismatch");
        }
    }

    protected static void baseMul(MatrixBase left, MatrixBase right, MatrixBase result) {
        for (int i = 0; i < left.rows; ++i) {
            for (int j = 0; j < left.columns; ++j) {
                var leftVal = left.matrix[left.columns * i + j];
                for (int k = 0; k < right.columns; ++k) {
                    result.matrix[i * right.columns + k] += leftVal * right.matrix[j * right.columns + k];
                }
            }
        }
    }

    protected String baseToString(String className) {
        var sb = new StringBuilder(className + " [\n");
        for (int i = 0; i < rows; ++i) {
            if (i != 0) {
                sb.append(",\n");
            }
            sb.append("\t");
            sb.append("[").append(String.format("%.3f", matrix[columns * i]));
            for (int j = 1; j < columns; ++j) {
                sb.append(", ").append(String.format("%.3f", matrix[columns * i + j]));
            }
            sb.append("]");
        }
        return sb.append("\n]").toString();
    }
}


