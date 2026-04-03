package com.narxoz.rpg.command;

import java.util.ArrayList;
import java.util.List;

public class ActionQueue {
    private final List<ActionCommand> queue = new ArrayList<>();

    public void enqueue(ActionCommand cmd) {
        queue.add(cmd);
    }

    public void undoLast() {
        if (!queue.isEmpty()) {
            queue.remove(queue.size() - 1);
        }
    }

    public void executeAll() {
        for (ActionCommand command : queue) {
            command.execute();
        }
        queue.clear();
    }

    public List<String> getCommandDescriptions() {
        List<String> descriptions = new ArrayList<>();
        for (ActionCommand command : queue) {
            descriptions.add(command.getDescription());
        }
        return descriptions;
    }
}
