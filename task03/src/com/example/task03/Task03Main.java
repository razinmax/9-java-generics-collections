package com.example.task03;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class Task03Main {

    public static void main(String[] args) throws IOException {

        List<Set<String>> anagrams = findAnagrams(new FileInputStream("task03/resources/singular.txt"), Charset.forName("windows-1251"));
        for (Set<String> anagram : anagrams) {
            System.out.println(anagram);
        }

    }

    public static List<Set<String>> findAnagrams(InputStream inputStream, Charset charset) {
        // Создаем словарь, где ключ - отсортированные символы слова, значение - список анаграмм
        Map<String, Set<String>> anagramMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Приводим к нижнему регистру
                String word = line.trim().toLowerCase();

                // Проверяем условия
                if (isValidWord(word)) {
                    // Создаем ключ - сортированные буквы слова
                    char[] chars = word.toCharArray();
                    Arrays.sort(chars);
                    String key = new String(chars);

                    // Добавляем слово в соответствующий набор
                    anagramMap.computeIfAbsent(key, k -> new TreeSet<>()).add(word);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Фильтруем наборы, содержащие меньше 2 слов,
        // и сортируем результирующий список по первым словам наборов
        return anagramMap.values().stream()
                .filter(set -> set.size() >= 2)
                .sorted(Comparator.comparing(set -> set.iterator().next()))
                .collect(Collectors.toList());
    }

    // Проверка валидности слова по условиям задачи
    private static boolean isValidWord(String word) {
        // Проверяем длину (должна быть не менее 3 символов)
        if (word.length() < 3) {
            return false;
        }

        // Проверяем, что слово содержит только русские буквы
        return word.chars().allMatch(c -> (c >= 'а' && c <= 'я') || c == 'ё');
    }
}
