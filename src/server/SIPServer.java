package server;

//fix imports

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;

public class SIPServer {

    public static void main(String[] args) throws UnknownHostException, SocketException, IOException, InterruptedException {

        try {
            // parse the command line arguments
            Configuration.sipUser("84394419265");
            Configuration.sipInterface("192.168.8.215");
            Configuration.sipPort("5060");
        } catch (Exception exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }

        System.out.println(Configuration.sipFullAddress());

        SIPClient sipClient = new SIPClient();

        PacketInfo packetInfo = new PacketInfo();
        packetInfo.senderAddress = "101.99.18.210";
        packetInfo.senderPort = 5060;

//        VoIPWorker voipWorker = new VoIPWorker(packetInfo);
//        voipWorker.start();

        sipClient.sendInvite(packetInfo);

        sipClient.start();




//    System.out.println("Sip Server " + Configuration.sipUser()
//    		+ " listening to :" + Configuration.sipInterface() + "- on port :" + Configuration.sipPort());}

    }

}
