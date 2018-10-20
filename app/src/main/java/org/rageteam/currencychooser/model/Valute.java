package org.rageteam.currencychooser.model;

import android.support.annotation.NonNull;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Valute")
public class Valute implements Comparable<Valute> {
    @Attribute(name = "ID")
    private String id;
    @Element(name="NumCode")
    private String numCode;
    @Element(name="CharCode")
    private String charCode;
    @Element(name = "Nominal")
    private int nominal;
    @Element(name = "Name")
    private String name;
    @Element(name = "Value")
    private String value;

    public String getNumCode() {
        return numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        if (value == null) {
            throw new RuntimeException("not enough data for currency convertion. for charCode: " + charCode);
        }
        return Double.valueOf(value.replace(",", "."));
    }

    @Override
    public String toString() {
        return charCode;
    }

    @Override
    public int compareTo(@NonNull Valute o) {
        return charCode.compareTo(o.charCode);
    }
}
