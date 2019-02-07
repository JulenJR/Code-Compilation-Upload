package com.mygdx.safe.InputProcessors;

/**
 * Created by Boris.InspiratGames on 6/10/17.
 */

public class Message  {

    //TAG
    private static final String TAG = Message.class.getSimpleName();

    //STATIC
    public static String separatorToken="::::";

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //MESSAGE
    private String fullmessage="";
    private String messageType="";

    //BOOLEAN
    private boolean hasMessage=false;
    private boolean dontProcessHasMessage=false;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public Message(GenericMethodsInputProcessor g){
        this.g=g;
    }

    /*_______________________________________________________________________________________________________________*/

    //SEND MESSAGE
    public void sendMessage(String mtype,String ...args){
        hasMessage=true;
        if(dontProcessHasMessage){
            dontProcessHasMessage=false;
            fullmessage="";
            messageType="";
        }
        dontProcessHasMessage=true;
        messageType=mtype;
        System.out.print(" SEND MESSAGE COMMAND [ "+mtype+"] : " );
        for(String s:args){
            fullmessage+=s+separatorToken;
            System.out.print("[ "+s+" ]");
        }
        System.out.println(" ... --> ");
        ProcessDirect(messageType,fullmessage);
    }

    //PROCESS DIRECT
    public boolean ProcessDirect(String mtype,String fmessage){

        boolean proccesed=true;
        System.out.print("\n [ DIRECT PROCESS INIT ]\n\n");
        messageType=mtype;
        fullmessage=fmessage;

        if(hasMessage) {
            String message[] = fmessage.split(separatorToken);

            System.out.println("MTYPE: " + mtype);

            if (message.length >= 1) {

                for(int i=0;i<message.length;i++) {
                    System.out.println("MESSAGE["+i+"]: " + message[i]);
                }
                System.out.println(" ");

                if (mtype.equalsIgnoreCase("sprint")) {

                    if (message[0].equalsIgnoreCase("activate")) {
                        if (!g.isSpecificPrint())
                            System.out.print("\n [ SPECIFIC PRINT ENABLED ]\n");
                        else
                            System.out.print("\n [ SPECIFIC PRINT IS ALREADY ENABLED ]\n");

                        g.setSpecificPrint(true);
                        g.setReturnCommand(false);
                        hasMessage = false;
                        fullmessage = "";
                    }

                    if (message[0].equalsIgnoreCase("deactivate")) {
                        if (g.isSpecificPrint())
                            System.out.print("\n [ SPECIFIC PRINT DISABLED ]\n");
                        else
                            System.out.print("\n [ SPECIFIC PRINT IS ALREADY DISABLED ]\n");
                        g.setSpecificPrint(false);
                        g.setReturnCommand(false);
                        hasMessage = false;
                        fullmessage = "";
                    }
                }

                if (mtype.equalsIgnoreCase("executor")) {
                    if(message[0].equalsIgnoreCase("off")){
                        System.out.println("[ COMMAND EXECUTOR MODE OFF ]");
                        g.setReturnCommand(true);
                        g.setCommandExecutor(false);
                        hasMessage = false;
                        fullmessage = "";
                    }

                }

                if (mtype.equalsIgnoreCase("quit")) {
                    if(message[0].equalsIgnoreCase("command") || message[0].equalsIgnoreCase("com"))
                        System.out.println("[ COMMAND PROCESSOR EXIT ]");
                    g.setReturnCommand(false);
                    g.setCommandExecutor(false);
                    hasMessage = false;
                    fullmessage = "";

                }

                if (mtype.equalsIgnoreCase("dialogGraph") || mtype.equalsIgnoreCase("dgraph") ) {
                    g.getMessageAccessClass().dgMgr.receive(message, null, -1);
                    g.setReturnCommand(true);
                    hasMessage = false;
                    fullmessage = "";
                }

                if (mtype.equalsIgnoreCase("gsys")){

                    String s= "";

                    for(int i=0; i<message.length; i++){
                        s += message[i];
                    }

                    g.getMessageAccessClass().gsys.receive(s, null, -1);
                    g.setReturnCommand(true);
                    hasMessage = false;
                    fullmessage = "";
                }
            }
        }

        if(hasMessage) {
            proccesed=false;
            System.out.print("\n [ DIRECT PROCESS CAN'T PROCESS THIS COMMAND ]\n");
        }
        System.out.print("\n [ DIRECT PROCESS FINISH ]\n\n");

        return proccesed;
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public String getFullmessage() {
        return fullmessage;
    }

    public String getMessageType() {
        return messageType;
    }

    public boolean isHasMessage() {
        return hasMessage;
    }

    public boolean isDontProcessHasMessage() {
        return dontProcessHasMessage;
    }

    //SETTERS
    public void setFullmessage(String fullmessage) {
        this.fullmessage = fullmessage;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setHasMessage(boolean hasMessage) {
        this.hasMessage = hasMessage;
    }

    public void setDontProcessHasMessage(boolean dontProcessHasMessage) {
        this.dontProcessHasMessage = dontProcessHasMessage;
    }

}
