package com.fic.service.utils;

import com.fic.service.Vo.GenerateWalletVo;
import com.fic.service.Vo.WalletUnlockVo;
import com.fic.service.constants.ServerProperties;
import com.fic.service.entity.Wallet;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author Xie
 * @Date $date$
 * @Description: common
 **/
@Component
public class Web3jUtil {

    private final Logger log = LoggerFactory.getLogger(Web3jUtil.class);

    @Autowired
    ServerProperties serverProperties;

    private volatile Web3j web3j;
    private volatile Admin admin;

    @PostConstruct
    public void init(){
        try{
            if(web3j==null){
                synchronized (Web3jUtil.class){
                    if(web3j==null){
                        web3j = Web3j.build(new HttpService(serverProperties.getWalletUrl()));
                        web3j.web3ClientVersion().send();
                        log.debug(" Wallet Servet Connected!");
                    }
                }
            }
            if(admin==null){
                synchronized (Web3jUtil.class){
                    if(admin==null){
                        admin = Admin.build(new HttpService(serverProperties.getWalletUrl()));
                        admin.web3ClientVersion().send();
                        log.debug(" Wallet Server Admin Connected!");
                    }
                }
            }
        }catch(IOException e){
            log.error(" Wallet Servet Connected Failed !");
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void dostory(){
        web3j.shutdown();
        admin.shutdown();
        System.out.println("I'm  destory method ");
    }

    public void unLock(List<WalletUnlockVo> toUnLockList){
        try{
            for(WalletUnlockVo lockAccount : toUnLockList){
                PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(lockAccount.getAddress(), lockAccount.getPassword()).sendAsync().get();
                if (personalUnlockAccount.accountUnlocked()) {
                    log.debug(" 解锁成功，账户地址：{}",lockAccount.getAddress());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void unLock(String address,String password){
        try{
            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(address, password,BigInteger.valueOf(5)).sendAsync().get();
            if (personalUnlockAccount.accountUnlocked()) {
                log.debug(" 解锁成功，账户地址：{}",address);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建钱包地址
     * @param password
     * @param path 钱包文件存储路径  /userId/keystore.json
     * @return
     */
    public GenerateWalletVo createAccount(String password,String path){
        try {
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            String fileName = WalletUtils.generateNewWalletFile(password,file,true);
            File fileResult = new File(path+fileName);
            Credentials credentials = WalletUtils.loadCredentials(password,fileResult);
            String address = credentials.getAddress();
            GenerateWalletVo result = new GenerateWalletVo();
            result.setPath(fileName);
            result.setAddress(address);
            return result;
        } catch (Exception e) {
            log.error("创建钱包地址失败 path :{}",path);
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    /**
     * 转出
     * @param amount
     * @param password 密码 转出来源地址密码
     * @param keyStore 钱包文件路径 转出来源地址私钥文件
     * @param toAddress 转出地址
     */
    public void doTransactionOut(BigDecimal amount, String password, String keyStore, String toAddress){
        try{
            unLock(toAddress,password);
            String transactionHash = "";
            Credentials credentials = WalletUtils.loadCredentials(password, keyStore);
            String fromAddress = credentials.getAddress();
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                    fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            Address address = new Address(toAddress);

            List<Type> parametersList = new ArrayList<>();
            parametersList.add(address);
            BigInteger amountValue = Convert.toWei(amount.toString(), Convert.Unit.ETHER).toBigInteger();
            BigInteger gasPrice = Convert.toWei("3",Convert.Unit.GWEI).toBigInteger();
            BigInteger gasLimit = BigInteger.valueOf(8000000l);
            Uint256 value = new Uint256(amountValue);
            Function function = new Function(
                    "transfer",
                    Arrays.asList(address,value),
                    Arrays.asList(new TypeReference<Address>(){},new TypeReference<Uint256>(){})
            );
            String encodedFunction = FunctionEncoder.encode(function);
            log.debug(" gasPrice : {} , gasLimit :{}, amount :{}",gasPrice,gasLimit,amountValue);
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice,
                    gasLimit, serverProperties.getContactAddress(), encodedFunction);
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            if(ethSendTransaction.hasError()){
                Response.Error err = ethSendTransaction.getError();
                if(err.getMessage().equals("insufficient funds for gas * price + value")){

                }
                log.error("Error : " + ethSendTransaction.getError().getMessage());
                return;
            }
            transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println(transactionHash);
        }catch (IOException e) {
            log.error("doTransactionOut e :{} ",e);
            e.printStackTrace();
        } catch (CipherException e) {
            log.error("doTransactionOut e :{} ",e);
            e.printStackTrace();
        } catch (Exception e) {
            log.error("doTransactionOut e :{} ",e);
            e.printStackTrace();
        }
    }

    /**
     * 查询交易状态
     * @param txHash
     * @return (0,失败)(1,成功)(2,交易挂起)
     */
    public int queryTransactionStatus(String txHash){
        int result = 0;
        try {
            EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(txHash).sendAsync().get();
           if(null == ethGetTransactionReceipt.getResult()){
               log.debug(" transaction is pending, wait another query txHash :{}",txHash);
               result = 2;
               return result;
           }
            String status = ethGetTransactionReceipt.getTransactionReceipt().get().getStatus();
            if (StringUtils.isNotEmpty(status)) {
                BigInteger resultInteger  =  Numeric.decodeQuantity(status);
                if(resultInteger.compareTo(BigInteger.ZERO) == 0){
                    log.debug("transaction is failed txHash : {}",txHash);
                    return result;
                }else{
                    result = 1;
                    return result;
                }
            }
        } catch (InterruptedException e) {
            log.error("queryTransactionStatus e :{} ",e);
            e.printStackTrace();
        } catch (ExecutionException e) {
            log.error("queryTransactionStatus e :{} ",e);
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取钱包地址余额
     * @param address
     * @return
     */
    public BigInteger getBalance(String address){
        try {
            /** 查询 ETH */
            DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(web3j.ethBlockNumber().send().getBlockNumber());
            EthGetBalance ethGetBalance =  web3j.ethGetBalance(address,defaultBlockParameter).send();
            if(ethGetBalance!=null){
                return ethGetBalance.getBalance();
            }

            /** 查询 TFC */
            Function function = new Function("balanceOf",
                    Arrays.asList(new Address(address)),
                    Arrays.asList(new TypeReference<Address>() {
                    }));
            String encode = FunctionEncoder.encode(function);
            Transaction ethCallTransaction = Transaction.createEthCallTransaction(address, serverProperties.getContactAddress(), encode);
            EthCall ethCall = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            String value = ethCall.getResult();
            BigInteger result = BigInteger.valueOf(Long.parseLong(value));
            return result;
        }catch (Exception e){
            log.error("getBalance address :{}",address);
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String args[]){
        BigInteger result  = Numeric.decodeQuantity("0x0000000000000000000000000000000000000000000000000000000000002710");
        System.out.println("结果: "+ result.toString());
    }

}
