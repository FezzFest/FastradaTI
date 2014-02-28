package dataInterpreter;

/**
 * Created by Carlo on 28/02/14.
 */
public class Parameter {
    private String name;
    private int startBit;
    private int length;
    private String byteOrder;
    private double factor;
    private int offset;
    private double minimum;
    private double maximum;
    private String unit;

    public Parameter(String name, int startBit, int length, String byteOrder, double factor, int offset, double minimum, double maximum, String unit) {
        this.name = name;
        this.startBit = startBit;
        this.length = length;
        this.byteOrder = byteOrder;
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
                        this.factor == p.factor &&
                        this.offset == p.offset &&
                        this.maximum == p.maximum &&
                        this.minimum == p.minimum &&
                        this.unit.equals(p.unit) &&
                        this.name.equals(p.name);
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

    public String getName() {
        return name;
    }

    public int getOffset() {
        return offset;
    }

}
