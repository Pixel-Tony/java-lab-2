package com.peshkov.Matrices;


import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

abstract class MatrixBase {
    protected int rows;
    protected int columns;
    protected float[] matrix;


    public MatrixBase() {
        this(1, 1);
    }

    public MatrixBase(int height, int width) {
        if (height <= 0 || width <= 0) {
            throw new RuntimeException("matrix dimensions should be positive");
        }
        rows = height;
        columns = width;
        matrix = new float[rows * columns];
    }

    public MatrixBase(MatrixBase other) {
        if (Objects.isNull(other)) throw new RuntimeException("Object can't be null");
        rows = other.rows;
        columns = other.columns;
        matrix = new float[rows * columns];
        System.arraycopy(other.matrix, 0, matrix, 0, rows * columns);
    }

    protected MatrixBase(int height, int width, Function<Integer, Float> supplier) {
        this(height, width);
        if (supplier == null) throw new RuntimeException();

        for (int i = 0; i < rows * columns; ++i) {
            matrix[i] = supplier.apply(i);
        }
    }

    protected MatrixBase(int height, int width, Supplier<Float> supplier) {
        this(height, width);
        if (supplier == null) throw new RuntimeException();

        for (int i = 0; i < rows * columns; ++i) {
            matrix[i] = supplier.get();
        }
    }


    public MatrixBase(int height, int width, float min, float max) {
        this(height, width);
        if (max < min) throw new RuntimeException("Max value should be bigger than min");
        for (int i = 0; i < width * height; ++i) {
            matrix[i] = (float) Math.random() * (max - min) + min;
        }
    }

    public MatrixBase(float[] values) {
        this(1, values.length);
        System.arraycopy(values, 0, matrix, 0, values.length);
    }

    protected void coordinateCheck(int row, int column) {
        if (row < 0 || row >= rows) {
            throw new RuntimeException("row or column out of bounds");
        } else if (column < 0 || column >= columns) {
            throw new RuntimeException("column out of bounds");
        }
    }

    protected void indexCheck(int index, int max) {
        if (index < 0 || index > max) {
            throw new RuntimeException("index out of bounds");
        }
    }

    protected void setRowCheck(int index, MatrixBase row) {
        indexCheck(index, rows - 1);
        if (row == null || row.columns != columns || row.rows != 1) {
            throw new RuntimeException();
        }
    }

    protected void setRowCheck(int index, float[] row) {
        indexCheck(index, rows - 1);
        if (row == null || row.length != columns) {
            throw new RuntimeException();
        }
    }

    protected void setColumnCheck(int index, MatrixBase column) {
        indexCheck(index, columns - 1);
        if (column == null || column.rows != rows || column.columns != 1) {
            throw new RuntimeException();
        }
    }

    protected void setColumnCheck(int index, float[] column) {
        indexCheck(index, columns - 1);
        if (column == null || column.length != rows) {
            throw new RuntimeException();
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

    public float get(int row, int column) {
        coordinateCheck(row, column);
        return matrix[row * columns + column];
    }

    protected abstract MatrixBase createInstance(int rows, int columns);

    protected MatrixBase getColumn(int index) {
        indexCheck(index, columns - 1);
        MatrixBase out = createInstance(rows, 1);
        for (int i = 0; i < rows; ++i) out.matrix[i] = matrix[i * columns + index];
        return out;
    }

    protected MatrixBase getRow(int index) {
        indexCheck(index, rows - 1);
        MatrixBase out = createInstance(1, columns);
        System.arraycopy(this.matrix, index * columns, out.matrix, 0, columns);
        return out;
    }

    protected static MatrixBase set(MatrixBase instance, int row, int column, float value) {
        instance.coordinateCheck(row, column);
        instance.matrix[row * instance.columns + column] = value;
        return instance;
    }

    protected static MatrixBase setRow(MatrixBase instance, int index, float[] row) {
        instance.setRowCheck(index, row);
        System.arraycopy(row, 0, instance.matrix, index * instance.columns, instance.columns);
        return instance;
    }

    protected static MatrixBase setRow(MatrixBase instance, int index, MatrixBase row) {
        instance.setRowCheck(index, row);
        return setRow(instance, index, row.matrix);
    }

    protected static MatrixBase setColumn(MatrixBase instance, int index, float[] column) {
        instance.setColumnCheck(index, column);
        for (int i = 0; i < instance.rows; ++i) instance.matrix[i * instance.columns + index] = column[i];
        return instance;
    }

    protected static MatrixBase setColumn(MatrixBase instance, int index, MatrixBase column) {
        instance.setColumnCheck(index, column);
        return setColumn(instance, index, column.matrix);
    }

    protected static void mul(MatrixBase left, MatrixBase right, MatrixBase result) {
        for (int i = 0; i < left.rows; ++i) {
            for (int j = 0; j < left.columns; ++j) {
                var leftVal = result.matrix[left.columns * i + j];

                for (int k = 0; k < right.columns; ++k) {
                    result.matrix[i * right.columns + k] += leftVal * right.matrix[j * right.columns + k];
                }
            }
        }
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
        return (Arrays.hashCode(matrix) * 31 + rows) * 31 + columns;
    }

    protected String baseToString(String caption) {
        var sb = new StringBuilder(caption + " [\n");
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


