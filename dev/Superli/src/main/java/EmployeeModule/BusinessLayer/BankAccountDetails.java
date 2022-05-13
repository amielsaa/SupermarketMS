package EmployeeModule.BusinessLayer;

public class BankAccountDetails {
        int bankId;
        int branchId;
        int accountId;
        String bankName;
        String branchName;
        String accountOwner;


        public int bankId() {
                return bankId;
        }

        public int branchId() {
                return branchId;
        }

        public int accountId() {
                return accountId;
        }

        public String bankName() {
                return bankName;
        }

        public String branchName() {
                return branchName;
        }

        public String accountOwner() {
                return accountOwner;
        }

        public BankAccountDetails(int bankId, int branchId, int accountId, String bankName, String branchName, String accountOwner) {
                this.bankId = bankId;
                this.branchId = branchId;
                this.accountId = accountId;
                this.bankName = bankName;
                this.branchName = branchName;
                this.accountOwner = accountOwner;
        }
}
