package frc.robot;

import edu.wpi.first.wpilibj.I2C;

class VersionCheck{
 
    byte[] versionRequest = 
    {
        (byte) 0xae,  // first byte of no_checksum_sync (little endian -> least-significant byte first)
        (byte) 0xc1,  // second byte of no_checksum_sync
        (byte) 0x0e,  // this is the version request type
        (byte) 0x00   // data_length is 0
    };

    byte[] recBuffer = new byte[32];

    I2C pixy;

    public void send(byte[] b){
        pixy.writeBulk(b);
    }

    public String receive(byte[] buffer){
        pixy.readOnly(buffer, 16 + 6);
        return("%hhu: 0x%hhx\n");
    }

    public void checkVersion(){
        send(versionRequest);
        System.out.println(receive(recBuffer));
    }
}