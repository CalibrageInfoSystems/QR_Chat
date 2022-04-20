package com.cis.qrchat.service;

public interface APIConstantURL {

    public static final String LOCAL_URL = "http://183.82.111.111/QRChatAPI/api/";

    String Login = "Account/Login";
    String Register = "Account/Register";
    String getRegion = "Region";
    String RegistrationOTP = "Account/RegistrationOTP";

    String ResetPasswordSendOTP = "Account/ResetPasswordSendOTP";
    String ConfirmResetPasswordOTP = "Account/ConfirmResetPasswordOTP";
    String CreateWallet = "Account/CreateWallet";

    String UpdateUser = "Account/UpdateUser";

    String getGender = "TypeCdDmt?Id=4";
    String getCardtype = "TypeCdDmt?Id=1";

    String Addcard = "Card";
    String getCard = "Card?userId=";

    String DeleteCard = "Card?id=";


    String findcontact = "Contact/FindContact/";

    String AddFriend = "Contact/AddFriend";
    String GetUserContacts = "Contact/GetUserContacts/";

    String AddMoneytoUserWallet = "UserWallet/AddMoneyToUserWallet";

    String GetWalletBalance = "Transaction/GetWalletBallance/";

    String ScanQRCode  = "Transaction/ScanQRCode/";

    String SendMoney  = "UserWallet/SendMoneyToUserWallet";


    String  getprofile  ="Account/users/username/";

    String GetTransactions ="Transaction/GetTransactions/";


    String  addGroup="Group";
    String  getGroups="Group/GetGroups/";
    String  getGroupMembers="Group/GetUsersByGroupId/";
    String  getTransactions="Group/GetFundsByGroupId/";

    String  getFundsTransactions="Group/GetFundsTransactions/";
    String  getTransactionsbyFundId ="Group/GetTransactionsByFundId/";

    String  addFund="Group/CreateFund";

    String GetPassbookDetails ="UserWallet/GetTransactions";
    String GetUserTransactions ="UserWallet/GetUserTransactions";

    String AddRequest ="Transaction/AddRequest";

    String GetRequest ="Transaction/GetRequest/";
    String GetUsers ="Group/GetUsers/";
    String AddUsersToGroup ="Group/AddUsersToGroup";
    String InactiveUser ="Group/InActiveUser";
    String sendMoneyToFund ="Group/SendMoneyToFund";



    String  getuserinfo  ="Account/Search";

    String  getuserdetails  ="Account/users/";

    String InviteFriend = "Contact/InviteFriend/";

}