package be.kdg.FastradaMobile.config;

/**
 * Created by philip on 5/02/14.
 */
public class Parameter {
    private int startBit;
    private int length;
    private String byteOrder;
    private String valueType;
    private double factor;
    private int offset;
    private double minimum;
    private double maximum;
    private String unit;

    public Parameter(int startBit, int length, String byteOrder, String valueType, double factor, int offset, double minimum, double maximum, String unit) {
        this.startBit = startBit;
        this.length = length;
        this.byteOrder = byteOrder;
        this.valueType = valueType;
        this.factor = factor;
        this.offset = offset;
        this.minimum = minimum;
        this.maximum = maximum;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        Parameter p = (Parameter) o;
        return
                this.startBit == p.startBit &&
                        this.length == p.length &&
                        this.byteOrder.equals(p.byteOrder) &&
                        this.valueType.equals(p.valueType) &&
                        this.factor == p.factor &&
                        this.offset == p.offset &&
                        this.maximum == p.maximum &&
                        this.minimum == p.minimum &&
                        this.unit.equals(p.unit);
    }

    public int getStartBit() {
        return startBit;
    }

    public int getLength() {
        return length;
    }

    public double getFactor() {
        return factor;
    }

    public int getOffset() {
        return offset;
    }

    public String getUnit() {
        return unit;
    }

    public double getMaximum() {
        return maximum;
    }

    public double getMinimum() {
        return minimum;
    }

    public String getValueType() {
        return valueType;
    }

    public String getByteOrder() {
        return byteOrder;
    }
}
