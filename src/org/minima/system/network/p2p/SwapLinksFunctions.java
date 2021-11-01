package org.minima.system.network.p2p;

import org.minima.objects.base.MiniData;
import org.minima.system.network.minima.NIOClientInfo;
import org.minima.system.network.p2p.messages.Greeting;
import org.minima.system.network.p2p.params.P2PParams;
import org.minima.utils.MinimaLogger;
import org.minima.utils.json.JSONObject;
import org.minima.utils.messages.Message;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SwapLinksFunctions {

    public static JSONObject wrapP2PMsg(JSONObject data){
        JSONObject msg = new JSONObject();
        msg.put("swap_links_p2p", data);
        return msg;
    }

    public static List<JSONObject> onConnected(P2PState state, Message zMessage) {
        List<JSONObject> msgs = new ArrayList<>();
        //Get the details
        String uid = zMessage.getString("uid");
        boolean incoming = zMessage.getBoolean("incoming");

        NIOClientInfo info = P2PFunctions.getNIOCLientInfo(uid);
        if (incoming) {
            state.getNoneP2PLinks().put(uid, new InetSocketAddress(info.getHost(), 0));
        } else {
            state.getNoneP2PLinks().put(uid, new InetSocketAddress(info.getHost(), info.getPort()));
        }

        Greeting greeting = new Greeting(state);
        msgs.add(wrapP2PMsg(greeting.toJson()));

        if (state.getMyMinimaAddress() == null) {
            JSONObject requestIp = new JSONObject();
            MiniData secret = MiniData.getRandomData(12);
            state.setIpReqSecret(secret);
            requestIp.put("req_ip", secret.toString());
            msgs.add(wrapP2PMsg(requestIp));
        }
        return msgs;
    }

    public static void onDisconnected(P2PState state, Message zMessage){
        //Get the details
        String uid = zMessage.getString("uid");
        boolean incoming = zMessage.getBoolean("incoming");
        boolean reconnect = zMessage.getBoolean("reconnect");

        // Remove uid from current connections
        if (incoming) {
            state.getInLinks().remove(uid);
            state.getNoneP2PLinks().remove(uid);
        } else {
            state.getOutLinks().remove(uid);
        }
    }

    public static void processGreeting(P2PState state, Greeting greeting, String uid) {
        List<InetSocketAddress> newPeers = Stream.of(greeting.getInLinks(), greeting.getOutLinks(), greeting.getKnownPeers())
                .flatMap(Collection::stream)
                .distinct()
                .filter(x -> x.getPort() != 0)
                .filter(x -> !x.equals(state.getMyMinimaAddress()))
                .collect(Collectors.toCollection(ArrayList::new));

        state.getKnownPeers().addAll(newPeers);
        // TODO: Limit Set Size
        NIOClientInfo client = P2PFunctions.getNIOCLientInfo(uid);
        String host = client.getHost();
        int port = greeting.getMyMinimaPort();
        InetSocketAddress minimaAddress = new InetSocketAddress(host, port);
        state.getKnownPeers().add(minimaAddress);

        // Peers are assumed to not be P2P Links until we get a valid P2P Greeting
        state.getNoneP2PLinks().remove(uid);
        if (client.isIncoming()){
            state.getInLinks().put(uid, minimaAddress);
//            if (state.getInLinks().size() > P2PParams.TGT_NUM_LINKS) {
//                P2PFunctions.disconnect(uid);
//                MinimaLogger.log("[-] Too many incoming connections, disconnecting");
//            }
        } else {
            state.getOutLinks().put(uid, minimaAddress);
            if (state.getOutLinks().size() > P2PParams.TGT_NUM_LINKS) {
                P2PFunctions.disconnect(uid);
                MinimaLogger.log("[-] Too many outgoing connections, disconnecting");
            }
        }

        if (state.isDoingDiscoveryConnection()){
            state.setDoingDiscoveryConnection(false);
            P2PFunctions.disconnect(uid);
        }
    }

    public static JSONObject processRequestIPMsg(P2PState state, JSONObject swapLinksMsg, String host){
        MiniData secret = new MiniData((String) swapLinksMsg.get("req_ip"));
        JSONObject responseMsg = new JSONObject();
        JSONObject IpResponse = new JSONObject();
        IpResponse.put("res_ip", host);
        IpResponse.put("secret", secret.toString());
        responseMsg.put("swap_links_p2p", IpResponse);
        return responseMsg;
    }

    public static void processResponseIPMsg(P2PState state, JSONObject swapLinksMsg){
        MiniData secret = new MiniData((String) swapLinksMsg.get("secret"));
        if (state.getIpReqSecret().isEqual(secret)) {
            String hostIP = (String) swapLinksMsg.get("res_ip");
            state.setMyMinimaAddress(hostIP);
            state.getKnownPeers().remove(state.getMyMinimaAddress());
            MinimaLogger.log("[+] Setting My IP: " + hostIP);
        } else {
            MinimaLogger.log("[-] Failed to set my ip. Secrets do not match. MySecret: " + state.getIpReqSecret() + " Received secret: " + secret);
        }
    }

}
