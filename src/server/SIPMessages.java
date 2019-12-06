package server;

import java.io.IOException;
import java.net.UnknownHostException;


public class SIPMessages {
    static Integer sequence = 2;

    public static String RequestType(String line) {
        String type;
        if (line.matches("INVITE sip:.*@.*")) {
            type = "INVITE";
        } else if (line.matches("SIP/2.0 200 OK")) {
            type = "OK";
        } else if (line.matches("^BYE sip:.* SIP/2.0$")) {
            type = "BYE";
        } else if (line.matches("^ACK sip:.* SIP/2.0$")) {
            type = "ACK";
        } else if (line.contains("CANCEL")) {
            type = "CANCEL";
        } else {
            type = "OTHER";
        }

        return type;
    }

    public static String getSDP(PacketInfo packetInfo) throws UnknownHostException {
        System.out.println("====>" + packetInfo.senderRtpPort);
        System.out.println("====>" + Configuration.sipInterfaceStr());

        String sdp_message = "v=0\r\n" +
                "o=- 3784641311 3784641311 IN IP4 192.168.8.215\r\n" +
                "s=SJphone\r\n" +
                "c=IN IP4 192.168.8.215\r\n" +
                "t=0 0\r\n" +
                "m=audio 8000 RTP/AVP 18 3 97 8 0 101\r\n" +
                "a=rtpmap:8 PCMA/8000\r\n" +
                "a=rtpmap:101 telephone-event/8000\r\n" +
                "a=fmtp:101 0-15\r\n";
        return sdp_message;
    }

    public static void Trying(PacketInfo packetInfo) throws UnknownHostException, IOException {
        String message = "SIP/2.0 100 Trying\r\n"
                + "Via: SIP/2.0/UDP " + packetInfo.senderAddress + ";"
                + "rport=" + Configuration.sipPort() + ";"
                + "received=" + packetInfo.senderAddress + ";"
                + "branch=" + packetInfo.branch + "\r\n"
                + "Content-Length: 0\r\n"
                + "Contact: <sip:" + Configuration.sipAddress() + ">\r\n"
                + "Call-ID: " + packetInfo.callId + "\r\n"
                + "CSeq: 1 INVITE\r\n"
                + "From: \"" + packetInfo.senderUsername + "\"<sip:" + packetInfo.senderAddress + ">;tag=" + packetInfo.tag + "\r\n"
                + "To: \"" + Configuration.sipUser() + "\"<sip:" + Configuration.sipFullAddress() + ">;"
                + "tag=" + Configuration.tag() + "\r\n\r\n";

        SIPUtil.SendPacket(message, packetInfo.senderAddress, packetInfo.senderPort);

    }

    public static void Invite(PacketInfo packetInfo) throws IOException {
        String sdp_message = SIPMessages.getSDP(packetInfo);

        String message = "INVITE sip:84394419265@101.99.18.210:5060 SIP/2.0\r\n" +
                "Via: SIP/2.0/UDP 192.168.8.215:5070;rport;branch=z9hG4bKPj522cd584ae73438db36be1a3f34a840e\r\n" +
                "Max-Forwards: 70\r\n" +
                "From: sip:17999901@101.99.18.210;tag=c3b81c727e8b43c0a841397b931a4f48\r\n" +
                "To: sip:84394419265@101.99.18.210\r\n" +
                "Contact: <sip:17999901@192.168.8.215:5070>\r\n" +
                "Call-ID: f311ee6b15504aa1b2af53cc56169c54\r\n" +
                "CSeq: 19905 INVITE\r\n" +
                "Allow: INFO, PRACK, SUBSCRIBE, NOTIFY, REFER, INVITE, ACK, BYE, CANCEL, UPDATE\r\n" +
                "Content-Type: application/sdp\r\n" +
                "Content-Length:   255\r\n"
                + "\r\n"
                + sdp_message;

        SIPUtil.SendPacket(message, packetInfo.senderAddress, packetInfo.senderPort);
    }

    public static void Ringing(PacketInfo packetInfo) throws UnknownHostException, IOException {

        String message = "SIP/2.0 180 Ringing\r\n"
                + "Via: SIP/2.0/UDP " + packetInfo.senderAddress + ";"
                + "rport=" + Configuration.sipPort() + ";"
                + "received=" + packetInfo.senderAddress + ";"
                + "branch=" + packetInfo.branch + "\r\n"
                + "Content-Length: 0\r\n"
                + "Contact: <sip:" + Configuration.sipAddress() + ">\r\n"
                + "Call-ID: " + packetInfo.callId + "\r\n"
                + "CSeq: 1 INVITE\r\n"
                + "From: \"" + packetInfo.senderUsername + "\"<sip:" + packetInfo.senderAddress + ">;tag=" + packetInfo.tag + "\r\n"
                + "To: \"" + Configuration.sipUser() + "\"<sip:" + Configuration.sipFullAddress() + ">;"
                + "tag=" + Configuration.tag() + "\r\n\r\n";

        SIPUtil.SendPacket(message, packetInfo.senderAddress, packetInfo.senderPort);
    }

    public static void Ok(PacketInfo packetInfo) throws UnknownHostException, IOException {

        String sdp_message = SIPMessages.getSDP(packetInfo);

        String message = "SIP/2.0 200 OK\r\n"
                + "Via: SIP/2.0/UDP " + packetInfo.senderAddress + ";"
                + "rport=" + Configuration.sipPort() + ";received=" + packetInfo.senderAddress + ";"
                + "branch=" + packetInfo.branch + "\r\n"
                + "Content-Length: " + sdp_message.length() + "\r\n"
                + "Contact: <sip:" + Configuration.sipAddress() + ">\r\n"
                + "Call-ID: " + packetInfo.callId + "\r\n"
                + "Content-Type: application/sdp\r\n"
                + "CSeq: 1 INVITE\r\n"
                + "From: \"" + packetInfo.senderUsername + "\"<sip:" + packetInfo.senderAddress + ">;tag=" + packetInfo.tag + "\r\n"
                + "To: \"" + Configuration.sipUser() + "\"<sip:" + Configuration.sipFullAddress() + ">;"
                + "tag=" + Configuration.tag() + "\r\n\r\n"
                + sdp_message;

        SIPUtil.SendPacket(message, packetInfo.senderAddress, packetInfo.senderPort);
    }

    public static void OkForBye(PacketInfo packetInfo) throws UnknownHostException, IOException {

        String message = "SIP/2.0 200 OK\r\n"
                + "Via: SIP/2.0/UDP " + packetInfo.senderAddress + ";\r\n"
                + "branch=" + packetInfo.branch + "\r\n"
                + "Call-ID: " + packetInfo.callId + "\r\n"
                + "CSeq: " + packetInfo.cSeq + " BYE\r\n"
                + "From: \"" + packetInfo.senderUsername + "\"<sip:" + packetInfo.senderAddress + ">;tag=" + packetInfo.tag + "\r\n"
                + "To: \"" + Configuration.sipUser() + "\"<sip:" + Configuration.sipFullAddress() + ">;"
                + "tag=" + Configuration.tag() + "\r\n\r\n";

        SIPUtil.SendPacket(message, packetInfo.senderAddress, packetInfo.senderPort);
    }

    public static void NotFound(PacketInfo packetInfo) throws UnknownHostException, IOException {
        String message = "SIP/2.0 404 Not Found\r\n"
                + "Via: SIP/2.0/UDP " + packetInfo.senderAddress + ";"
                + "rport=" + Configuration.sipPort() + ";received=" + packetInfo.senderAddress + ";"
                + "branch=" + packetInfo.branch + "\r\n"
                + "Content-Length: 0\r\n"
                + "Contact: <sip:" + Configuration.sipAddress() + ">\r\n"
                + "Call-ID: " + packetInfo.callId + "\r\n"
                + "CSeq: 1 INVITE\r\n"
                + "From: \"" + packetInfo.senderUsername + "\"<sip:" + packetInfo.senderAddress + ">;tag=" + packetInfo.tag + "\r\n"
                + "To: \"" + Configuration.sipUser() + "\"<sip:" + Configuration.sipFullAddress() + ">;"
                + "tag=" + Configuration.tag() + "\r\n\r\n";

        SIPUtil.SendPacket(message, packetInfo.senderAddress, packetInfo.senderPort);
    }

    public static void Bye(PacketInfo packetInfo) throws UnknownHostException, IOException {

        String message = "BYE sip:" + packetInfo.senderAddress + " SIP/2.0\r\n"
                + "Via: SIP/2.0/UDP " + packetInfo.sipAddress + ";"
                + "rport;branch=" + packetInfo.branch + "\r\n"
                + "Content-Length: 0\r\n" + //nothing to send additionally
                "Call-ID: " + packetInfo.callId + "\r\n"
                + "CSeq: " + sequence + " BYE\r\n" + //It is the first message sent
                "From: \"" + Configuration.sipUser() + "\"<sip:" + Configuration.sipAddress() + ">;"
                + "tag=" + Configuration.tag() + "\r\n"
                + "Max-Forwards: 70\r\n"
                + "To: <sip:" + packetInfo.senderAddress + ">;tag=" + packetInfo.tag + "\r\n"
                + "User-Agent: SJphone/1.60.299a/L (SJ Labs)\r\n\r\n";

        SIPUtil.SendPacket(message, packetInfo.senderAddress, packetInfo.senderPort);
        System.out.println("Sending Bye to " + packetInfo.senderAddress + ":" + packetInfo.senderPort);
        //removeClient(this);
    }

    public static void Ack(PacketInfo packetInfo) throws IOException {
        String message = "ACK sip:84394419265@101.99.18.210:5060 SIP/2.0\r\n" +
                "Via: SIP/2.0/UDP 192.168.8.215:5070;rport;branch=z9hG4bKPjd65a61f36e8644348686d0ee279e4a67\r\n" +
                "Max-Forwards: 70\r\n" +
                "From: sip:17999901@101.99.18.210;tag=" + packetInfo.tagFrom +"\r\n" +
                "To: sip:84394419265@101.99.18.210;tag=" + packetInfo.tagTo + "\r\n" +
                "Call-ID: " + packetInfo.callId + "\r\n" +
                "CSeq: " + packetInfo.cSeq + " ACK\r\n" +
                "Content-Length:  0\r\n";

        SIPUtil.SendPacket(message, packetInfo.senderAddress, packetInfo.senderPort);


    }

}
