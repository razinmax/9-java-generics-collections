package com.example.task02;

import java.io.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class SavedList<E extends Serializable> extends AbstractList<E> {
    private final File file;
    private final List<E> elements;

    public SavedList(File file) {
        this.file = file;
        this.elements = new ArrayList<>();

        if (file.exists()) {
            loadFromFile();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<E> loadedElements = (List<E>) ois.readObject();
            elements.clear();
            elements.addAll(loadedElements);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading from file: " + e.getMessage());
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new ArrayList<>(elements));
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }

    @Override
    public E get(int index) {
        return elements.get(index);
    }

    @Override
    public E set(int index, E element) {
        E previousElement = elements.set(index, element);
        saveToFile();
        return previousElement;
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public void add(int index, E element) {
        elements.add(index, element);
        saveToFile();
    }

    @Override
    public E remove(int index) {
        E removedElement = elements.remove(index);
        saveToFile();
        return removedElement;
    }
}
