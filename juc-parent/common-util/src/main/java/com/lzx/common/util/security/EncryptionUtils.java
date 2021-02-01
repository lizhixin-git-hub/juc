package com.lzx.common.util.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 一、不可逆加密
 * <p>
 * 常见的不可逆加密算法有MD5，HMAC，SHA1、SHA-224、SHA-256、SHA-384，和SHA-512，其中SHA-224、SHA-256、SHA-384，和SHA-512我们可以统称为SHA2加密算法，SHA加密算法的安全性要比MD5更高，而SHA2加密算法比SHA1的要高。其中SHA后面的数字表示的是加密后的字符串长度，SHA1默认会产生一个160位的信息摘要。
 * <p>
 * 不可逆加密算法最大的特点就是密钥，但是HMAC是需要密钥的【手动狗头】。
 * <p>
 * 由于这些加密都是不可逆的，因此比较常用的场景就是用户密码加密，其验证过程就是通过比较两个加密后的字符串是否一样来确认身份的。网上也有很多自称是可以破解MD5密码的网站，其原理也是一样，就是有一个巨大的资源库，存放了许多字符串及对应的MD5加密后的字符串，通过你输入的MD5加密串来进行比较，如果过你的密码复杂度比较低，还是有很大机率验证出来的。
 */
public class EncryptionUtils {

    /**
     * MD5信息摘要算法（英语：MD5 Message-Digest Algorithm），一种被广泛使用的密码散列函数，可以产生出一个128位（16字节）的散列值（hash value），用于确保信息传输完整一致。
     * <p>
     * MD5算法有以下特点：
     * <p>
     * 1、压缩性：无论数据长度是多少，计算出来的MD5值长度相同
     * <p>
     * 2、容易计算性：由原数据容易计算出MD5值
     * <p>
     * 3、抗修改性：即便修改一个字节，计算出来的MD5值也会巨大差异
     * <p>
     * 4、抗碰撞性：知道数据和MD5值，很小概率找到相同MD5值相同的原数据。
     *
     * @param text 待加密字符串
     * @return Md5加密串
     */
    public static String md5(String text) {
        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = messageDigest.digest(text.getBytes());
            return Hex.encodeHexString(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 安全散列算法（英语：Secure Hash Algorithm，缩写为SHA）是一个密码散列函数家族，是FIPS所认证的安全散列算法。能计算出一个数字消息所对应到的，长度固定的字符串（又称消息摘要）的算法。且若输入的消息不同，它们对应到不同字符串的机率很高。
     * 2005年8月17日的CRYPTO会议尾声中王小云、姚期智、姚储枫再度发表更有效率的SHA-1攻击法，能在2的63次方个计算复杂度内找到碰撞。
     * 也就是说SHA-1加密算法有碰撞的可能性，虽然很小。
     *
     * @param text 待加密字符串
     * @return sha256加密串
     */
    public static String sha256(String text) {
        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = messageDigest.digest(text.getBytes());
            return Hex.encodeHexString(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 如果要使用不可逆加密，推荐使用SHA256、SHA384、SHA512以及HMAC-SHA256、HMAC-SHA384、HMAC-SHA512这几种算法。
     * HMAC是密钥相关的哈希运算消息认证码（Hash-based Message Authentication  Code）的缩写，由H.Krawezyk，M.Bellare，R.Canetti于1996年提出的一种基于Hash函数和密钥进行消息认证的方法，并于1997年作为RFC2104被公布，并在IPSec和其他网络协议（如SSL）中得以广泛应用，现在已经成为事实上的Internet安全标准。它可以与任何迭代散列函数捆绑使用。
     * HMAC算法更像是一种加密算法，它引入了密钥，其安全性已经不完全依赖于所使用的Hash算法
     *
     * @param text 待加密字符串
     * @param sk   秘钥SecretKeySpec
     * @return hmacSha256加密串
     */
    public static String hmacSha256(String text, SecretKeySpec sk) {
        Mac mac;

        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(sk);
            byte[] rawHmac = mac.doFinal(text.getBytes());
            return new String(Base64.encodeBase64(rawHmac));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 二、对称加密算法
     * <p>
     * 对称加密算法是应用比较早的算法，在数据加密和解密的时用的都是同一个密钥，这就造成了密钥管理困难的问题。常见的对称加密算法有DES、3DES、AES128、AES192、AES256(默认安装的JDK尚不支持AES256，需要安装对应的jce补丁进行升级jce1.7，jce1.8)。其中AES后面的数字代表的是密钥长度。对称加密算法的安全性相对较低，比较适用的场景就是内网环境中的加解密。
     * DES  加密
     *
     * @param dataSource 数据源
     * @param password   秘钥
     * @return DES加密串
     */
    public static String encrypt(byte[] dataSource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            //正式执行加密操作
            return Base64.encodeBase64String(cipher.doFinal(dataSource));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DES 解密
     *
     * @param src      加密串
     * @param password 秘钥
     * @return 解密串
     */
    public static String decrypt(String src, String password) {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom random = new SecureRandom();
            // 创建一个DESKeySpec对象
            DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());
            // 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // 将DESKeySpec对象转换成SecretKey对象
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
            // 真正开始解密操作
            return new String(cipher.doFinal(Base64.decodeBase64(src)));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 3DES加密
     *
     * @param src 待加密串
     * @param key key
     * @return 3DES加密加密串
     */
    public static String encryptThreeDESECB(String src, String key) {
        try {
            DESedeKeySpec dks = new DESedeKeySpec(key.getBytes(StandardCharsets.UTF_8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey secureKey = keyFactory.generateSecret(dks);

            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secureKey);
            byte[] b = cipher.doFinal(src.getBytes(StandardCharsets.UTF_8));

            String ss = new String(Base64.encodeBase64(b));
            ss = ss.replaceAll("\\+", "-");
            ss = ss.replaceAll("/", "_");
            return ss;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * 3DES解密
     *
     * @param src 待解密串
     * @param key key
     * @return 解密串
     */
    public static String decryptThreeDESECB(String src, String key) {
        try {
            src = src.replaceAll("-", "+");
            src = src.replaceAll("_", "/");
            byte[] byteSrc = Base64.decodeBase64(src.getBytes(StandardCharsets.UTF_8));
            // --解密的key
            DESedeKeySpec dks = new DESedeKeySpec(key.getBytes(StandardCharsets.UTF_8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey secureKey = keyFactory.generateSecret(dks);

            // --Cipher对象解密
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secureKey);
            byte[] retByte = cipher.doFinal(byteSrc);

            return new String(retByte, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
            return src;
        }
    }

}
