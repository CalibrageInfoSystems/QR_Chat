package com.cis.qrchat.service;

import com.cis.qrchat.Model.AddFundResponse;
import com.cis.qrchat.Model.AddGroupResponse;
import com.cis.qrchat.Model.AddMoneytoUserWalletResponse;
import com.cis.qrchat.Model.AddUserstoGroupResponse;
import com.cis.qrchat.Model.Addcardresponse;
import com.cis.qrchat.Model.Addfriendrequestresp;
import com.cis.qrchat.Model.ConfirmResetPasswordOTPRES;
import com.cis.qrchat.Model.CreateWalletresp;
import com.cis.qrchat.Model.DeleteCard;
import com.cis.qrchat.Model.FindContactResp;
import com.cis.qrchat.Model.GetCards;
import com.cis.qrchat.Model.GetCardtype;
import com.cis.qrchat.Model.GetGender;

import com.cis.qrchat.Model.GetGroupFundTransactions;
import com.cis.qrchat.Model.GetGroupTransactions;
import com.cis.qrchat.Model.GetPassbookDetails;

import com.cis.qrchat.Model.GetGroupMembers;
import com.cis.qrchat.Model.GetGroups;

import com.cis.qrchat.Model.GetRegions;
import com.cis.qrchat.Model.GetTransactions;
import com.cis.qrchat.Model.GetTransactionsByFundId;
import com.cis.qrchat.Model.GetTransactionsDetails;

import com.cis.qrchat.Model.GetUserdetails;
import com.cis.qrchat.Model.GetUserinfo;

import com.cis.qrchat.Model.GetUsers;

import com.cis.qrchat.Model.GetWalletBalanceResponse;
import com.cis.qrchat.Model.Getcontactlist;
import com.cis.qrchat.Model.GetrequestResp;
import com.cis.qrchat.Model.InactiveUserResponse;
import com.cis.qrchat.Model.Invitefriends;
import com.cis.qrchat.Model.LoginResponse;
import com.cis.qrchat.Model.Profileresponse;
import com.cis.qrchat.Model.QrResponse;
import com.cis.qrchat.Model.RegisterResponse;
import com.cis.qrchat.Model.RegistrationOTPresp;
import com.cis.qrchat.Model.RequestmoneyResp;
import com.cis.qrchat.Model.SendMoneytoFundResponse;
import com.cis.qrchat.Model.SendmoneyResponse;
import com.cis.qrchat.Model.UpdateUserres;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;
import rx.Observable;

public interface ApiService {

    @POST(APIConstantURL.Login)
    Observable<LoginResponse> getLoginPage(@Body JsonObject data);

    @GET
    Observable<GetRegions> getregions(@Url String url);

    @POST(APIConstantURL.Register)
    Observable<RegisterResponse> postregister(@Body JsonObject data);
    @POST(APIConstantURL.RegistrationOTP)
    Observable<RegistrationOTPresp> postregisterotp(@Body JsonObject data);
    @POST(APIConstantURL.ResetPasswordSendOTP)
    Observable<RegistrationOTPresp> postResetPassword(@Body JsonObject data);

    @POST(APIConstantURL.ConfirmResetPasswordOTP)
    Observable<ConfirmResetPasswordOTPRES> postConfirmResetPasswordOTP(@Body JsonObject data);

    @POST(APIConstantURL.CreateWallet)
    Observable<CreateWalletresp> postCreateWallet(@Body JsonObject data);

    @GET
    Observable<GetGender> getgender(@Url String url);

    @GET
    Observable<GetCardtype> getcardtype(@Url String url);

    @PUT(APIConstantURL.UpdateUser)
    Observable<UpdateUserres> postupdateuser(@Body JsonObject data);
    @POST(APIConstantURL.Addcard)
    Observable<Addcardresponse> postAdd(@Body JsonObject data);

    @GET
    Observable<GetCards> getcards(@Url String url);

    @DELETE
    Observable<DeleteCard> deletecards(@Url String url);

    @GET
    Observable<FindContactResp> getcontact(@Url String url);

    @POST(APIConstantURL.AddFriend)
    Observable<Addfriendrequestresp> postAddfriend(@Body JsonObject data);
    @GET
    Observable<Getcontactlist> getcontactlist(@Url String url);

    @POST(APIConstantURL.AddMoneytoUserWallet)
    Observable<AddMoneytoUserWalletResponse> postAddMoneytoUserWallet(@Body JsonObject data);

    @GET
    Observable<GetWalletBalanceResponse> getwalletBalance(@Url String url);

    @GET
    Observable<Profileresponse> getprofilerespose(@Url String url);

    @POST(APIConstantURL.SendMoney)
    Observable<SendmoneyResponse> postsendmoney(@Body JsonObject data);

    @POST(APIConstantURL.addGroup)
    Observable<AddGroupResponse> addgrouppage(@Body JsonObject data);

//    @PUT(APIConstantURL.addGroup)
//    Observable<AddGroupResponse> updategrouppage(@Body JsonObject data);

    @POST(APIConstantURL.addFund)
    Observable<AddFundResponse> addfundpage(@Body JsonObject data);

    @GET
    Observable<QrResponse> getqrresponse(@Url String url);


//    @GET
//    Observable<GetTransactions> getTransactions(@Url String url);

//    @GET
//    Observable<GetPassbookDetails> getTransactions(@Url String url);
    @POST(APIConstantURL.GetPassbookDetails)
    Observable<GetPassbookDetails> postpassbookdetails(@Body JsonObject data);
   @GET
    Observable<GetGroups> getGroupspage(@Url String url);

    @GET
    Observable<GetTransactionsByFundId> getTransactionByFundIdPage(@Url String url);

    @GET
    Observable<GetGroupMembers> getGroupMemberspage(@Url String url);

    @GET
    Observable<GetGroupTransactions> getGroupTransactionspage(@Url String url);

    @GET
    Observable<GetGroupFundTransactions> getGroupFundTransactionspage(@Url String url);

    @POST(APIConstantURL.GetUserTransactions)
    Observable<GetTransactionsDetails> posttransactiondetails(@Body JsonObject data);

    @POST(APIConstantURL.AddUsersToGroup)
    Observable<AddUserstoGroupResponse> addUsertoGroupPage(@Body JsonArray data);

    @POST(APIConstantURL.InactiveUser)
    Observable<InactiveUserResponse> inactiveUserPage(@Body JsonObject data);

    @POST(APIConstantURL.sendMoneyToFund)
    Observable<SendMoneytoFundResponse> sendMoneytoFundPage(@Body JsonObject data);

    @POST(APIConstantURL.AddRequest)
    Observable<RequestmoneyResp>Postrequestmoney(@Body JsonObject data);

    @GET
    Observable<GetrequestResp> getrequestresponse(@Url String url);

    @GET

    //Observable<GetUserinfo> getuseinfo(@Url String url);

    Observable<GetUsers> getUserspage(@Url String url);

    @POST(APIConstantURL.getuserinfo)
    Observable<GetUserinfo> getuseinfo(@Body JsonObject data);
    @GET
    Observable<GetUserdetails> getuserdetails(@Url String url);
    @GET
    Observable<Invitefriends> getinvitefriends(@Url String url);


}
