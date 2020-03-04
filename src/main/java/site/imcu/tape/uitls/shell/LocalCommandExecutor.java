package site.imcu.tape.uitls.shell;

/**
* LocalCommandExecutor.java
*/
public interface LocalCommandExecutor {
    ExecuteResult executeCommand(String command, long timeout);
}