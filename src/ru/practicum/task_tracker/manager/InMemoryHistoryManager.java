package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    /* Реализуем свой собственный связный список, и его ноды, по аналогии с LinkedList: */

    // Мапа для скорости работы со списком и хранения нод, и ноды крайних элементов списка:
    private final Map<Integer, Node> taskNodes;
    private Node first;
    private Node last;

    public InMemoryHistoryManager() {

        this.taskNodes = new HashMap<>();
    }

    // Добавляем задачу в историю просмотров:
    @Override
    public void addTask(Task task) {

        // Проверим объект типа Task на null:
        if (task == null) {
            System.out.println("task = null");
            return;
        }

        // Удаляем задачу, если она там уже есть:
        remove(task.getId());

        // Ставим задачу в конец двусвязного списка:
        linkLast(task);
        // Добавляем ноду задачи в мапу:
        taskNodes.put(task.getId(), last);
    }

    // Метод возвращает список просмотренных задач:
    @Override
    public List<Task> getTaskHistory() {

        return getTasks();
    }

    // Метод для удаления задачи из просмотра:
    @Override
    public void remove(int id) {

        // Находим ноду в мапе и удаляем её (если она там есть) + удаляем из двусвязного списка:
        Node node = taskNodes.remove(id);
        if (node == null) {

            return;
        }

        removeNode(node);
    }

    // Нода:
    private static class Node {

        // Поля - структура Ноды: сами данные и две ссылки на соседние элементы:
        public Task task;
        public Node prev;
        public Node next;

        public Node(Task task, Node prev, Node next) {

            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    // Внутренний метод для добавления задач в конец созданного двусвязного списка:
    private void linkLast(Task task) {

        // Создаём ноду задачи:
        Node newNode = new Node(task, last, null);

        // Если первого элемента ноды ещё нет, то созданная нода - первая:
        if (first == null) {
            first = newNode;

        // Иначе на созданную ноду будет ссылаться первая нода:
        } else {
            last.next = newNode;
        }

        // Ставим новый элемент в конец списка:
        last = newNode;
    }

    // Внутренний метод для сбора задач в обычный список:
    private List<Task> getTasks() {

        // Создаём список для сбора задач и "находим" первый элемент в списке:
        List<Task> tasksList = new ArrayList<>();
        Node node = first;

        // Начиная с первой (first) добавляем задачи в список, пока следующий элемент(next) не окажется пустым:
        while (node != null) {

            tasksList.add(node.task);
            node = node.next;
        }

        return tasksList;
    }

    // Внутренний метод, для удаления ноды из двусвязного списка / изменения ссылок:
    private void removeNode (Node node) {

        // Случай когда нода первая:
        if (node.prev == null) {
            first = node.next;
            // Нода была единственной:
            if (first == null) {
                last = null;
            } else {
                first.prev = null;
            }

        // Случай, когда нода последняя:
        } else if (node.next == null) {
            last = node.prev;
            last.next = null;

        // Случай, когда нода находится между первой и последней
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }
}