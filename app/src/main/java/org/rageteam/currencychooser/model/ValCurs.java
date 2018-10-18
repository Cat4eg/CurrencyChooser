package org.rageteam.currencychooser.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class ValCurs {
    @ElementList(inline = true)
    private List<Valute> valutes;
    @Attribute
    private String name;
    @Attribute(name = "Date")
    private String date;


    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public List<Valute> getValutes() {
        return valutes;
    }

    public void setValutes(List<Valute> valutes) {
        this.valutes = valutes;
    }

    @Override
    public String toString() {
        return "ValCurs{" +
                "valutes=" + valutes +
                ", name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
