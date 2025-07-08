package com.ncnl.barangayapp.commands;

import com.ncnl.barangayapp.repository.ModalCommand;

public class ModalCommandExecute {

    private static volatile ModalCommandExecute instance;

    private ModalCommandExecute(){}

    public static ModalCommandExecute getInstance(){
        if(instance == null){
            synchronized (ModalCommandExecute.class){
                if (instance == null){
                    instance = new ModalCommandExecute();
                }
            }
        }
        return instance;
    }


    public <T> T executeCommand(ModalCommand<T> command) { return command.show();}


}
