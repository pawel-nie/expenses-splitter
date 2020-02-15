package com.pawelnie.expensessplitter.calc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PartialShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double contribution;
    private double percentUsage;
    private double changeMoney = 0;

    public PartialShare() {}

    public PartialShare(double contribution, double percentUsage) {
        if (contribution >= 0)
            this.contribution = contribution;

        if (isUsageAPercent())
            this.percentUsage = percentUsage;
    }

    public PartialShare(double contribution, double percentUsage,
                        double changeMoney) {
        this(contribution, percentUsage);
        if (changeMoney >= 0)
            this.changeMoney = changeMoney;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getContribution() {
        return contribution;
    }

    public void setContribution(double contribution) {
        this.contribution = contribution;
    }

    public double getPercentUsage() {
        return percentUsage;
    }

    public void setPercentUsage(double percentUsage) {
        this.percentUsage = percentUsage;
    }

    public double getChangeMoney() {
        return changeMoney;
    }

    public void setChangeMoney(double changeMoney) {
        this.changeMoney = changeMoney;
    }

    private boolean isUsageAPercent(){
        return (percentUsage >= 0 && percentUsage <= 100);
    }
}
