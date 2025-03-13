package com.viettravelbk.smartcontract;

import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

public class Fifteen_SmartContract extends Contract{
    private static final String BINARY = "<SMART_CONTRACT_BINARY_HERE>";

    protected Fifteen_SmartContract(String contractAddress, Web3j web3j, TransactionManager credentials, ContractGasProvider gasProvider) {
        super(BINARY, contractAddress, web3j, credentials, gasProvider);
    }

    public static RemoteCall<Fifteen_SmartContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Fifteen_SmartContract.class, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit), BINARY, "");
    }

    public RemoteCall<Uint256> totalRoomRent() {
        return executeRemoteCallSingleValueReturn(new org.web3j.abi.datatypes.Function(
                "totalRoomRent",
                Collections.unmodifiableList(Arrays.asList()),
                Collections.unmodifiableList(Arrays.asList(new org.web3j.abi.TypeReference<Uint256>() {}))));
    }

    public RemoteFunctionCall<String> account1() {
        return executeRemoteCallSingleValueReturn(new org.web3j.abi.datatypes.Function(
                "account1",
                Collections.emptyList(),
                Collections.singletonList(new org.web3j.abi.TypeReference<Address>() {})
        ), String.class);
    }

    public RemoteCall<TransactionReceipt> handleRoomBookingOrReset(BigInteger amount) {
        Function function = new Function(
                "handleRoomBookingOrReset",
                Collections.emptyList(),
                Collections.emptyList()
        );
        return executeRemoteCallTransaction(function, amount);
    }

    public RemoteCall<TransactionReceipt> completePayment() {
        return executeRemoteCallTransaction(new org.web3j.abi.datatypes.Function(
                "completePayment",
                Collections.unmodifiableList(Arrays.asList()),
                Collections.unmodifiableList(Arrays.asList())));
    }

    public RemoteCall<TransactionReceipt> updateAccountAddress(String newAddress) {
        return executeRemoteCallTransaction(new org.web3j.abi.datatypes.Function(
                "updateAccountAddress",
                Collections.unmodifiableList(Arrays.asList(new Address(newAddress))),
                Collections.unmodifiableList(Arrays.asList())));
    }

    /*public static TwentySevenPaymentBookingRoom load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        return new TwentySevenPaymentBookingRoom(contractAddress, web3j, credentials, gasProvider);
    }*/
    public static Fifteen_SmartContract load(
            String contractAddress,
            Web3j web3j,
            TransactionManager web3TransactionManager,
            ContractGasProvider contractGasProvider
    ) {
        return new Fifteen_SmartContract(contractAddress, web3j, web3TransactionManager, contractGasProvider);
    }

    /*public RemoteCall<TransactionReceipt> updateTotalRoomRent(BigInteger newTotalRoomRent) {
        return executeRemoteCallTransaction(new org.web3j.abi.datatypes.Function(
                "updateTotalRoomRent",
                Arrays.asList(new org.web3j.abi.datatypes.generated.Uint256(newTotalRoomRent)),
                Collections.emptyList()
        ));
    }*/
    public RemoteCall<TransactionReceipt> updateTotalRoomRent(BigInteger newTotalRoomRent, String renterAddress) {
        return executeRemoteCallTransaction(new org.web3j.abi.datatypes.Function(
                "updateTotalRoomRent",
                Arrays.asList(
                        new org.web3j.abi.datatypes.generated.Uint256(newTotalRoomRent),
                        new org.web3j.abi.datatypes.Address(renterAddress)
                ),
                Collections.emptyList()
        ));
    }

    public Event ROOM_RENT_RESET_EVENT = new Event("RoomRentReset",
            Arrays.asList(
                    new TypeReference<Address>(true) {},
                    new TypeReference<Uint256>() {}
            )
    );
    public Flowable<Fifteen_SmartContract.RoomRentResetEventResponse> roomRentResetEventFlowable() {
        EthFilter filter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROOM_RENT_RESET_EVENT));

        return web3j.ethLogFlowable(filter).map(log -> {
            Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ROOM_RENT_RESET_EVENT, log);
            Fifteen_SmartContract.RoomRentResetEventResponse response = new Fifteen_SmartContract.RoomRentResetEventResponse();
            response.payer = (String) eventValues.getIndexedValues().get(0).getValue();
            response.time = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            return response;
        });
    }
    public static class RoomRentResetEventResponse {
        public String payer;
        public BigInteger time;
    }

    public Event FIRST_ROOM_RENT_PAID_EVENT = new Event("FirstRoomRentPaid",
            Arrays.asList(
                    new TypeReference<Address>(true) {},
                    new TypeReference<Uint256>() {},
                    new TypeReference<Uint256>() {}
            )
    );

    public Event NOT_PAID_YET_EVENT = new Event("NotPaidYet",
            Arrays.asList(
                    new TypeReference<Address>(true) {},
                    new TypeReference<Uint256>() {}
            )
    );

    public Event SECOND_ROOM_RENT_PAID_EVENT = new Event("SecondRoomRentPaid",
            Arrays.asList(
                    new TypeReference<Address>(true) {},
                    new TypeReference<Uint256>() {},
                    new TypeReference<Uint256>() {}
            )
    );

    // Lắng nghe sự kiện FirstRoomRentPaid
    public Flowable<Fifteen_SmartContract.FirstRoomRentPaidEventResponse> firstRoomRentPaidEventFlowable() {
        EthFilter filter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(FIRST_ROOM_RENT_PAID_EVENT));
        return web3j.ethLogFlowable(filter).map(log -> {
            Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(FIRST_ROOM_RENT_PAID_EVENT, log);
            Fifteen_SmartContract.FirstRoomRentPaidEventResponse response = new Fifteen_SmartContract.FirstRoomRentPaidEventResponse();
            response.payer = (String) eventValues.getIndexedValues().get(0).getValue();
            response.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            response.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            return response;
        });
    }

    // Lắng nghe sự kiện NotPaidYet
    public Flowable<Fifteen_SmartContract.NotPaidYetEventResponse> notPaidYetEventFlowable() {
        EthFilter filter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NOT_PAID_YET_EVENT));
        return web3j.ethLogFlowable(filter).map(log -> {
            Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NOT_PAID_YET_EVENT, log);
            Fifteen_SmartContract.NotPaidYetEventResponse response = new Fifteen_SmartContract.NotPaidYetEventResponse();
            response.payer = (String) eventValues.getIndexedValues().get(0).getValue();
            response.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            return response;
        });
    }

    // Lắng nghe sự kiện SecondRoomRentPaid
    public Flowable<Fifteen_SmartContract.SecondRoomRentPaidEventResponse> secondRoomRentPaidEventFlowable() {
        EthFilter filter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SECOND_ROOM_RENT_PAID_EVENT));
        return web3j.ethLogFlowable(filter).map(log -> {
            //System.out.println("Đã nhận được log sự kiện từ blockchain: " + log);
            Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SECOND_ROOM_RENT_PAID_EVENT, log);
            Fifteen_SmartContract.SecondRoomRentPaidEventResponse response = new Fifteen_SmartContract.SecondRoomRentPaidEventResponse();
            response.payer = (String) eventValues.getIndexedValues().get(0).getValue();
            response.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            response.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            //System.out.println("Dữ liệu sự kiện: payer=" + response.payer + ", amount=" + response.amount + ", timestamp=" + response.timestamp);
            return response;
        });
    }

    // Các class response cho từng sự kiện
    public static class FirstRoomRentPaidEventResponse {
        public String payer;
        public BigInteger amount;
        public BigInteger timestamp;
    }

    public static class NotPaidYetEventResponse {
        public String payer;
        public BigInteger timestamp;
    }

    public static class SecondRoomRentPaidEventResponse {
        public String payer;
        public BigInteger amount;
        public BigInteger timestamp;
    }
}
