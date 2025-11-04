package com.hospital.patterns.Command;

public interface Command {
  boolean execute();
  boolean undo();
}
