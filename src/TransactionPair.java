public class TransactionPair {

    private int transactionPairI;
    private int transactionPairJ;
    private int pairOccuredCount;
    private boolean isFrequent;

    public TransactionPair(int transactionPairI, int transactionPairJ) {
        this.transactionPairI = transactionPairI;
        this.transactionPairJ = transactionPairJ;
        this.pairOccuredCount = 0;
        this.isFrequent = false;
    }

    public boolean isFrequent() {
        return isFrequent;
    }

    public void setFrequent(boolean frequent) {
        isFrequent = frequent;
    }

    public int getPairOccuredCount() {
        return pairOccuredCount;
    }

    public void setPairOccuredCount(int pairOccuredCount) {
        this.pairOccuredCount = pairOccuredCount;
    }

    public int getTransactionPairJ() {
        return transactionPairJ;
    }

    public void setTransactionPairJ(int transactionPairJ) {
        this.transactionPairJ = transactionPairJ;
    }

    public int getTransactionPairI() {
        return transactionPairI;
    }

    public void setTransactionPairI(int transactionPairI) {
        this.transactionPairI = transactionPairI;
    }
}
