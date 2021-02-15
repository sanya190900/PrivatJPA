package org.example;

import javax.persistence.*;

@Entity
@Table(name="Currency")
public class ObjectToDataBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(nullable = false)
    private String date;
    @Column(nullable = false)
    private Double saleRateUSD;

    public ObjectToDataBase(){}

    public ObjectToDataBase(String date, Double saleRateUSD) {
        this.date = date;
        this.saleRateUSD = saleRateUSD;
    }

    public ObjectToDataBase(Long id, String date, Double saleRateUSD) {
        this.id = id;
        this.date = date;
        this.saleRateUSD = saleRateUSD;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getSaleRateUSD() {
        return saleRateUSD;
    }

    public void setSaleRateUSD(Double saleRateUSD) {
        this.saleRateUSD = saleRateUSD;
    }

    @Override
    public String toString() {
        return String.format(id + " \t\t" + date + "\t\t\t" + "%.4f", saleRateUSD);
    }
}

