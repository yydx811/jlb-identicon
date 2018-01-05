package com.bestjlb.identicon.config;

import com.bestjlb.identicon.provider.DefaultSaltProvider;
import com.bestjlb.identicon.provider.SaltProvider;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Created by yydx811 on 2018/1/5.
 */
public class DigestConfig {

    private static final int STREAM_BUFFER_LENGTH = 1024;

    private final String algorithm;

    private final SaltProvider provider;

    /**
     * Returns a <code>MessageDigest</code> for the given <code>algorithm</code>.
     *
     * @param algorithm
     *            the name of the algorithm requested. See <a
     *            href="http://java.sun.com/j2se/1.3/docs/guide/security/CryptoSpec.html#AppA">Appendix A in the Java
     *            Cryptography Architecture API Specification & Reference</a> for information about standard algorithm
     *            names.
     *            <p>{@link org.apache.commons.codec.digest.MessageDigestAlgorithms}
     * @see MessageDigest#getInstance(String)
     * @throws RuntimeException
     *             when a {@link java.security.NoSuchAlgorithmException} is caught.
     */
    public DigestConfig(String algorithm) {
        this(algorithm, 0, null);
    }

    /**
     * Returns a <code>MessageDigest</code> for the given <code>algorithm</code>.
     *
     * @param algorithm
     *            the name of the algorithm requested. See <a
     *            href="http://java.sun.com/j2se/1.3/docs/guide/security/CryptoSpec.html#AppA">Appendix A in the Java
     *            Cryptography Architecture API Specification & Reference</a> for information about standard algorithm
     *            names.
     *            <p>{@link org.apache.commons.codec.digest.MessageDigestAlgorithms}
     * @param saltType
     *            saltType. 0, no slat;1, fixed slat;2, random salt.
     * @param salt
     *            salt
     * @see MessageDigest#getInstance(String)
     * @throws RuntimeException
     *             when a {@link java.security.NoSuchAlgorithmException} is caught.<br>
     *          IllegalArgumentException
     *             when fixed salt is null.
     */
    public DigestConfig(String algorithm, int saltType, String salt) {
        this(algorithm, new DefaultSaltProvider(saltType, salt));
    }

    /**
     * Returns a <code>MessageDigest</code> for the given <code>algorithm</code>.
     *
     * @param algorithm
     *            the name of the algorithm requested. See <a
     *            href="http://java.sun.com/j2se/1.3/docs/guide/security/CryptoSpec.html#AppA">Appendix A in the Java
     *            Cryptography Architecture API Specification & Reference</a> for information about standard algorithm
     *            names.
     *            <p>{@link org.apache.commons.codec.digest.MessageDigestAlgorithms}
     * @param provider
     *            {@link SaltProvider}
     * @see MessageDigest#getInstance(String)
     * @throws RuntimeException
     *             when a {@link java.security.NoSuchAlgorithmException} is caught.<br>
     *          IllegalArgumentException
     *             when fixed salt is null.
     */
    public DigestConfig(String algorithm, SaltProvider provider) {
        DigestUtils.getDigest(algorithm);
        this.algorithm = algorithm;
        if (provider == null) {
            provider = new DefaultSaltProvider(0, null);
        }
        this.provider = provider;
    }

    /**
     * Read through an InputStream and returns the digest for the data
     *
     * @param digest
     *            The MessageDigest to use (e.g. MD5)
     * @param data
     *            Data to digest
     * @return MD5 digest
     * @throws IOException
     *             On error reading from the stream
     */
    public byte[] digest(MessageDigest digest, InputStream data) throws IOException {
        byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
        int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);

        while (read > -1) {
            digest.update(buffer, 0, read);
            read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
        }

        // add salt
        byte[] salt = provider.getByteWithSalt(null);
        if (salt != null) {
            digest.update(salt, 0, salt.length);
        }
        return digest.digest();
    }

    /**
     * Calculates digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return digest
     * @since 1.4
     */
    public byte[] digest(byte[] data) {
        return DigestUtils.getDigest(algorithm).digest(provider.getByteWithSalt(data));
    }

    /**
     * Calculates digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return digest
     * @throws IOException
     *             On error reading from the stream
     * @since 1.4
     */
    public byte[] digest(InputStream data) throws IOException {
        return digest(DigestUtils.getDigest(algorithm), data);
    }

    /**
     * Calculates digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return digest
     * @since 1.4
     */
    public byte[] digest(String data) {
        return digest(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Calculates digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return digest as a hex string
     * @since 1.4
     */
    public String digestHex(byte[] data) {
        return Hex.encodeHexString(digest(data));
    }

    /**
     * Calculates digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return digest as a hex string
     * @throws IOException
     *             On error reading from the stream
     * @since 1.4
     */
    public String digestHex(InputStream data) throws IOException {
        return Hex.encodeHexString(digest(data));
    }

    /**
     * Calculates digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     *
     * @param data
     *            Data to digest
     * @return digest as a hex string
     * @since 1.4
     */
    public String digestHex(String data) {
        return Hex.encodeHexString(digest(data));
    }
}
