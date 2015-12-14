package io.xdd.blackscience.socksserver.domain;

public class SSInitPackage {

    private static final byte IPV4=1;
    private static final byte IPV6=4;
    private static final byte HOSTNAME =3;


    private byte type;

    private byte[] destinationAddress;

    private byte[] destinationPort;

    private byte[] data;
}
