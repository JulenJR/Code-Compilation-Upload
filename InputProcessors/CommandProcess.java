package com.mygdx.safe.InputProcessors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Boris.InspiratGames on 9/10/17.
 */

public class CommandProcess {

    //TAG
    private static final String TAG = CommandProcess.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //COMMAND
    private StringBuilder shellCommand=new StringBuilder();
    private String command;
    private boolean commandExecuted=false;
    private String permitedCommandCharacters="1234567890 abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_#-+*!$·%&/()=?¿"+(char)Input.Keys.BACKSPACE;

    //CHARACTER
    private char character;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public CommandProcess(GenericMethodsInputProcessor g){
        this.g=g;
        System.out.println(TAG+ "  KEYBOARD INTERFACE for COMMAND PROCESS CONSTRUCTED.");
        shellCommand.append(".#");
    }

    public CommandProcess(){
        System.out.println(TAG+"Don't call CommandProcess without argument GenericMethodInputProcessor");
        System.exit(0);
    }

    /*_______________________________________________________________________________________________________________*/

    //COMMAND INPUT
    public void commandInput(){
        if (g.isBlockCommandInput() && !g.isCommandExecutor()){
            character=g.getKeyTypedCode();
            if(g.getKeyDownCode()==Input.Keys.BACKSPACE){
                if(shellCommand.length()>2) {
                    shellCommand.deleteCharAt(shellCommand.length() - 1);
                    System.out.println("\nCOMMAND: " + shellCommand);
                    g.setKeyDownCode(-1);
                }
            }
            if(permitedCommandCharacters.indexOf(character)!=-1) {
                if(shellCommand.toString().equalsIgnoreCase(".#")){
                    System.out.print("COMMAND: .#");
                }
                shellCommand.append(character);
                System.out.print(character + "");
            }
            if(g.getKeyDownCode()==Input.Keys.ENTER) {
                command = shellCommand.toString();

                //SENDING MESSAGE
                Array<String> commandMessage = new Array<String>();
                commandMessage.addAll(command.split("#"));

                System.out.print("\n\n");
                if (commandMessage.size>2) {
                    String message[] = new String[commandMessage.size-2];
                    String commandMessageType = commandMessage.get(1);
                    for (int i = 2; i < commandMessage.size; i++) {
                        message[i-2]=(commandMessage.get(i));
                    }
                    System.out.print("-> :::::: [ "+commandMessageType+"] : " );
                    for(String s:message){
                        System.out.println("[ "+s+" ]");
                    }
                    System.out.println(" :::::: <- ");
                    System.out.println("[ FINISH COMMAND ]");
                    g.message.sendMessage(commandMessageType, message);
                    shellCommand.delete(0, shellCommand.length());
                    shellCommand.append(".#");
                }else{
                    System.out.println("[ COMMAND ERROR SIZE. ]");
                    shellCommand.delete(0, shellCommand.length());
                    shellCommand.append(".#");
                    g.setReturnCommand(false);
                }
            }
        }
    }

    //EXECUTOR
    public void executor(String executorcommand){
        if (g.isCommandExecutor()){

            g.setBlockCommandInput(true);
            System.out.println(" [ INIT COMMAND EXECUTOR ]");
            shellCommand.append(".#");
            shellCommand.append(executorcommand);
            command = shellCommand.toString();

            //SENDING MESSAGE
            Array<String> commandMessage = new Array<String>();
            commandMessage.addAll(command.split("#"));

            System.out.println("\n\n");
            if (commandMessage.size>2) {
                String message[] = new String[commandMessage.size-2];
                String commandMessageType = commandMessage.get(1);
                for (int i = 2; i < commandMessage.size; i++) {
                    message[i-2]=(commandMessage.get(i));
                }

                System.out.println("-> :::::: [ "+commandMessageType+"] : " );
                for(String s:message){
                    System.out.print("[ "+s+" ]");
                }
                System.out.println(" :::::: [ AUTO ] ::::::  ");
                System.out.println(" [ FINISH COMMAND EXECUTOR ]");
                g.message.sendMessage(commandMessageType, message);
                shellCommand.delete(0, shellCommand.length());
                shellCommand.append(".#");
            }else{
                System.out.println("[ COMMAND ERROR SIZE ]");
                shellCommand.delete(0, shellCommand.length());
                shellCommand.append(".#");
                g.setReturnCommand(false);
            }
        }
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public StringBuilder getShellCommand() {
        return shellCommand;
    }

    public String getCommand() {
        return command;
    }

    public boolean isCommandExecuted() {
        return commandExecuted;
    }

    public String getPermitedCommandCharacters() {
        return permitedCommandCharacters;
    }

    public char getCharacter() {
        return character;
    }

    //SETTERS

    public void setShellCommand(StringBuilder shellCommand) {
        this.shellCommand = shellCommand;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setCommandExecuted(boolean commandExecuted) {
        this.commandExecuted = commandExecuted;
    }

    public void setPermitedCommandCharacters(String permitedCommandCharacters) {
        this.permitedCommandCharacters = permitedCommandCharacters;
    }

    public void setCharacter(char character) {
        this.character = character;
    }
}