package BusinessLayer.Objects;

public record BankAccountDetails(
    int bankId,
    int branchId,
    int accountId,
    String bankName,
    String branchName,
    String accountOwner
) { }
