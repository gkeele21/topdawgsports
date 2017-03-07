package bglib.ecommerce;

public interface CCTransaction
{
    public CCTransactionResponse sendTransaction(CCTransactionType ct);
}
