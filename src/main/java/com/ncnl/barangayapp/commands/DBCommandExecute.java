package com.ncnl.barangayapp.commands;

import com.ncnl.barangayapp.repository.DatabaseCommand;

public class DBCommandExecute {

    private static volatile DBCommandExecute instance;

    private DBCommandExecute() {}

    public static DBCommandExecute getInstance() {
        if (instance == null) {
            synchronized (DBCommandExecute.class) {
                if (instance == null) {
                    instance = new DBCommandExecute();
                }
            }
        }
        return instance;
    }

    public <T> T executeCommand(DatabaseCommand<T> command) {
        return command.execute();
    }
}
