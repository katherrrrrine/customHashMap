public class CustomHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;

    // Конструктор
    @SuppressWarnings("unchecked")
    public CustomHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    // Узлы
    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    // Вычисление индекса
    private int getIndex(K key) {
        if (key == null) return 0;
        return Math.abs(key.hashCode()) % table.length;
    }

    // Добавление элемента
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(key);
        Node<K, V> node = table[index];

        // Проверяем, есть ли уже такой ключ в цепочке
        while (node != null) {
            if ((key == null && node.key == null) ||
                    (key != null && key.equals(node.key))) {
                V oldValue = node.value;
                node.value = value;
                return;
            }
            node = node.next;
        }

        // Добавляем новый узел в начало цепочки
        table[index] = new Node<>(key, value, table[index]);
        size++;
    }

    // Получение значения по ключу
    public V get(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];

        while (node != null) {
            if ((key == null && node.key == null) ||
                    (key != null && key.equals(node.key))) {
                return node.value;
            }
            node = node.next;
        }

        return null;
    }

    // Удаление элемента по ключу
    public V remove(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        Node<K, V> prev = null;

        while (node != null) {
            if ((key == null && node.key == null) ||
                    (key != null && key.equals(node.key))) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return node.value;
            }
            prev = node;
            node = node.next;
        }

        return null;
    }

    // Расширение таблицы
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        table = (Node<K, V>[]) new Node[newCapacity];

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> nextNode = node.next; // Сохраняем ссылку на следующий узел
                int newIndex = getIndex(node.key); // Пересчитываем индекс в новой таблице

                // Вставляем узел в начало новой цепочки
                node.next = table[newIndex];
                table[newIndex] = node;

                node = nextNode; // Переходим к следующему узлу в старой цепочке
            }
        }
    }

    // Получение числа элементов
    public int size() {
        return size;
    }

    // Проверка на наличие элементов
    public boolean isEmpty() {
        return size == 0;
    }
}