package com.fic.service.utils;

import com.fic.service.Vo.GenerateWalletVo;
import com.fic.service.Vo.WalletUnlockVo;
import com.fic.service.constants.ServerProperties;
import com.fic.service.entity.Wallet;
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
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
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


    public List<String> getAccountlist(){
        try{
            return admin.personalListAccounts().send().getAccountIds();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
//            NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
//            if(newAccountIdentifier!=null){
//                String accountId = newAccountIdentifier.getAccountId();
//                return  accountId;
//            }
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
//            unLock(toAddress,password);
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
//            parametersList.add(value);
//            List<TypeReference<?>> outList = new ArrayList<>();
            Function function = new Function(
                    "transfer",//交易的方法名称
                    Arrays.asList(address,value),
                    Arrays.asList(new TypeReference<Address>(){},new TypeReference<Uint256>(){})
            );
//            Function function = new Function("transfer", parametersList, outList);
            String encodedFunction = FunctionEncoder.encode(function);
//            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, Gas单价,
//                    Gas最大数量, 合约地址, encodedFunction);
//            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, BigInteger.valueOf(1l),
//                    BigInteger.valueOf(1l), serverProperties.getContactAddress(), encodedFunction);
            log.debug(" gasPrice : {} , gasLimit :{}, amount :{}",gasPrice,gasLimit,amountValue);
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice,
                    gasLimit, serverProperties.getContactAddress(), encodedFunction);
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            if(ethSendTransaction.hasError()){
                log.error("Error : " + ethSendTransaction.getError().getMessage());
                return;
            }
            transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println(transactionHash);
        }catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取钱包地址余额
     * @param address
     * @return
     */
    public BigInteger getBalance(String address){
        try {
            DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(web3j.ethBlockNumber().send().getBlockNumber());
            EthGetBalance ethGetBalance =  web3j.ethGetBalance(address,defaultBlockParameter).send();
//            if(ethGetBalance!=null){
//                return ethGetBalance.getBalance();
//            }

            Function function = new Function("balanceOf",
                    Arrays.asList(new Address(address)),
                    Arrays.asList(new TypeReference<Address>() {
                    }));
            String encode = FunctionEncoder.encode(function);

            Transaction ethCallTransaction = Transaction.createEthCallTransaction(address, serverProperties.getContactAddress(), encode);
            EthCall ethCall = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            String value = ethCall.getResult();



        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String args[]){

        BigInteger result  = Numeric.decodeQuantity("0x0000000000000000000000000000000000000000000000000000000000002710");
        System.out.println("区块: "+ result.toString());
    }

}
