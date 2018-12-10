package com.fic.service.utils;

import com.fic.service.constants.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

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

    public Web3jUtil(){
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

    public List<String> getAccountlist(){
        try{
            return admin.personalListAccounts().send().getAccountIds();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String createAccount(String password,String path){
        try {
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            String fileName = WalletUtils.generateNewWalletFile(password,file,true);
            File fileResult = new File(path+fileName);
            Credentials credentials = WalletUtils.loadCredentials(password,fileResult);
            String address = credentials.getAddress();
            return address;
//            NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
//            if(newAccountIdentifier!=null){
//                String accountId = newAccountIdentifier.getAccountId();
//                return  accountId;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public void doTransaction(BigDecimal amount,String password,String keyStore){
//        try{
//            String transactionHash = "";
//            Credentials credentials = WalletUtils.loadCredentials("钱包密码", "钱包文件");
//            String fromAddress = credentials.getAddress();
//            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
//                    fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
//            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
//            Address address = new Address(toAddress);
//            Uint256 value = new Uint256(amount);
//            List<Type> parametersList = new ArrayList<>();
//            parametersList.add(address);
//            parametersList.add(value);
//            List<TypeReference<?>> outList = new ArrayList<>();
//            Function function = new Function("transfer", parametersList, outList);
//            String encodedFunction = FunctionEncoder.encode(function);
//            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, Gas单价,
//                    Gas最大数量, 合约地址, encodedFunction);
//            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//            String hexValue = Numeric.toHexString(signedMessage);
//
//            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
//            transactionHash = ethSendTransaction.getTransactionHash();
//
//        }catch (IOException e) {
//            e.printStackTrace();
//        } catch (CipherException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public BigInteger getBalance(String address){
        try {
            DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(web3j.ethBlockNumber().send().getBlockNumber());
            EthGetBalance ethGetBalance =  web3j.ethGetBalance(address,defaultBlockParameter).send();
            if(ethGetBalance!=null){
                return ethGetBalance.getBalance();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String args[]){
        Web3jUtil web3jUtil = new Web3jUtil();
        String result = web3jUtil.createAccount("123456","F://fic_wallet/");
        System.out.println(result);
        BigInteger balance = web3jUtil.getBalance(result);
        System.out.println("余额: "+ balance);
        web3jUtil.getAccountlist();
    }

}
