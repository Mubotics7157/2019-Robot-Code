package frc.robot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

class Pixy2Handler
{
    boolean lampOn = false;
    //public ArrayList<String> VectorData = new ArrayList<String>();
    public final byte[] CHECKSUM_VERSIONREQUEST = 
    {
        (byte) 0xae,  // first byte of no_checksum_sync (little endian -> least-significant byte first)
        (byte) 0xc1,  // second byte of no_checksum_sync
        (byte) 0x0e,  // this is the version request type
        (byte) 0x00   // data_length is 0
    };

    public final byte[] CHECKSUM_SETLEDCOLOR = {
        (byte) 0xae,
        (byte) 0xc1,
        (byte) 0x14,
        (byte) 0x03,
        (byte) 0x00,
        (byte) 0xFF,
        (byte) 0x00
    };

    public final byte[] CHECKSUM_LAMPON = {
        (byte) 0xae,
        (byte) 0xc1,
        (byte) 0x16,
        (byte) 0x02,
        (byte) 0x01,
        (byte) 0x01
    };

    public final byte[] CHECKSUM_LAMPOFF = {
        (byte) 0xae,
        (byte) 0xc1,
        (byte) 0x16,
        (byte) 0x02,
        (byte) 0x00,
        (byte) 0x00
    };

    public final byte[] CHECKSUM_GETMAINFEATURES = {
        (byte) 0xae,
        (byte) 0xc1,
        (byte) 0x30,
        (byte) 0x02,
        (byte) 0x00,
        (byte) 0x07
    };

    public final byte[] CHECKSUM_SETLINEMODE = {
        (byte) 0xae,
        (byte) 0xc1,
        (byte) 0x02,
        (byte) 0x05,
        (byte) 0x6C,
        (byte) 0x69,
        (byte) 0x6E,
        (byte) 0x65,
        (byte) 0x00
    };

    public final byte[] CHECKSUM_GETBLOCKS = {
        (byte) 0xae,
        (byte) 0xc1,
        (byte) 0x24,
        (byte) 0xFF,
        (byte) 0x01
    };

    public final byte[] CHECKSUM_SETMODE = {
        (byte) 0xae,
        (byte) 0xc1,
        (byte) 0x36,
        (byte) 0x01,
        (byte) 0x01
    };

    I2C pixy = new I2C(Port.kOnboard, 0x54);

    public Pixy2Handler()
    {
        //pixy.writeBulk(CHECKSUM_VERSIONREQUEST);
        
        //byte[] buffer = new byte[32];

        //pixy.readOnly(buffer, 32);
    }

    public void init(){
        pixy.writeBulk(CHECKSUM_SETLINEMODE);
        System.out.println("initializing pixy...");
    }

    public void RequestBytes()
    {
        pixy.writeBulk(CHECKSUM_VERSIONREQUEST);
    }

    public void toggleLamp(){
        if(lampOn){
            pixy.writeBulk(CHECKSUM_LAMPOFF);
            lampOn = false;
        }else{
            pixy.writeBulk(CHECKSUM_LAMPON);
            lampOn = true;
        }
    }

    public void testChecksum(){
        byte[] buffer = new byte[12];
        pixy.writeBulk(CHECKSUM_GETMAINFEATURES);
        //pixy.readOnly(buffer, 12);
    }

    public void sendRequest(byte[] byteArray){
        pixy.writeBulk(byteArray);
        byte[] initBuffer =  new byte[32];
        pixy.readOnly(initBuffer, 12);
        
    }

    /*public int readWord() {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        boolean abortedWhileReading = pixy.read(54, 2, buffer);
        
        if (!abortedWhileReading) {
            int output = getUnsignedInt(buffer.array());
            System.out.println(output);
            return output;
        } else {
            return 0;
        }
    }
    
    public static int getUnsignedInt(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getShort() & 0xffff;
    }*/

    public byte[] HandleInput()
    {
        pixy.writeBulk(CHECKSUM_GETMAINFEATURES);
        
        byte[] initBuffer = new byte[32];
        
        pixy.readOnly(initBuffer, 12);

        int bufferLength = (int) initBuffer[3];
//      int bufferFix = (int) Math.floor(bufferLength/16);
//      bufferLength -= bufferFix*6;

/*      for(int i = 0; i < bufferLength; i++)
        {
            byte[] b = new byte[1];
            pixy.readOnly(b, 1);
            buffer.add(b[0]);
        }*/

        byte[] returnBuffer = new byte[bufferLength];

        //CONTRIBUTION BY DANIEL SHIM :)
        try{
            pixy.readOnly(returnBuffer, bufferLength);
        }catch(Exception e){
            System.out.println("No vectors found.");
            System.out.println(e.toString());
        }

        System.out.println("Pixy Data: ");
        //System.out.println(returnBuffer[6*8]);
        /*for(int i=returnBuffer.length -1; i>=0; i--){
            System.out.println(Byte.toString(returnBuffer[i]));
        }
            /*
            ojiijopsadliuifu p3298qwu sdpoai p8498u 3789u98uiujsjfjoijdlkjsojisa83po

            oaisijffasdfemc]s
            e

            esoidfjoijdflkjsadm 
            admid8e
            2udj394
            4
            e

            sd
            fsadf
            as
            d fsdfjiei3i
            a
            sdekfjasdf9e93mmmmmmmmmmmmmmmmmmmmm
            le
            adf
            eooekfopsdkfa
            asdflkpo
            pldsplkokfoksef


            asodkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkko
            sodkfasdopkfoekldkdoeoeoeoeoooeoeoeoeoe

        }*/
        for(int i = 0; i < Math.ceil(returnBuffer.length/8); i++)
        {
            System.out.println(returnBuffer);
            for(int j = 0; j < 8; j++)
            {
                try
                {
                    System.out.print(Integer.toHexString(returnBuffer[(i*8)+j] & 0xff) + ' ');
                    //VectorData.add(Integer.toHexString(returnBuffer[(i*8)+j] & 0xff) + ' ');
                    //System.out.print(returnBuffer[i*8+j]);
                    //System.out.print(Integer.toHexString(returnBuffer[8]));
                } catch (Exception e)
                {
                    
                }
                
                System.out.println();
            }
        }
        

        return returnBuffer;

    }
}