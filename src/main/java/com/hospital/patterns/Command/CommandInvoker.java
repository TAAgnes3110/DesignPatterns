package com.hospital.patterns.Command;

import java.util.Stack;

public class CommandInvoker {
  private Command command;
  private final Stack<Command> commandHistory;

  public CommandInvoker() {
    this.commandHistory = new Stack<>();
  }

  public void setCommand(Command command) {
    this.command = command;
  }

  public boolean executeCommand() {
    if (command == null) return false;
    boolean result = command.execute();
    if (result) commandHistory.push(command);
    return result;
  }

  public boolean undo() {
    if (commandHistory.isEmpty()) return false;
    return commandHistory.pop().undo();
  }

  public boolean hasHistory() {
    return !commandHistory.isEmpty();
  }

  public void clearHistory() {
    commandHistory.clear();
  }
}

