package co.istad.mbanking.utils;

import java.math.BigDecimal;

public class BigDecimalUtil {
    private BigDecimal transferLimitAmount;

    public BigDecimal getTransferLimitAmount() {
        return transferLimitAmount;
    }

    public void setTransferLimitAmount(BigDecimal transferLimitAmount) {
        this.transferLimitAmount = transferLimitAmount;
    }
}
