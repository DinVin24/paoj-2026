package com.pao.laboratory03.collections;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("==== HashMap ====");
        String[] words = {"java", "python", "java", "c++", "python", "java", "rust", "c++", "go"};
        HashMap<String, Integer> contor = new HashMap<>();
        for (String w : words)
            contor.put(w, contor.getOrDefault(w,0)+1);
        System.out.println("Frecventa: " + contor);

        System.out.print("Contine 'rust'?");
        if (contor.containsKey("rust"))
            System.out.println(" true");
        else
            System.out.println(" false");

        System.out.println("Chei: " + contor.keySet());
        System.out.println("Valori: " + contor.values());

        for(Map.Entry<String, Integer> w : contor.entrySet())
            System.out.println(w.getKey() + " -> " + w.getValue());

        System.out.println("==== TreeMap ====");

        TreeMap<String, Integer> copac = new TreeMap<>(contor);
        System.out.println("Sortat: " + copac);

        System.out.println("Prima cheie: " + copac.firstKey());
        System.out.println("Ultima cheie: " + copac.lastKey());

        System.out.println("==== Map cu obiecte ====");

        HashMap<String, List<String>> cursuri = new HashMap<>();

        cursuri.put("PAOJ", new ArrayList<>(Arrays.asList("Ana", "Mihai", "Ion")));
        cursuri.put("BD", new ArrayList<>(Arrays.asList("Ana", "Elena")));

        System.out.println("Studenti la PAOJ: " + cursuri.get("PAOJ"));

        List<String> studBD = cursuri.get("BD");
        studBD.add("George");
        System.out.println("Studenti la BD (actualizat): " + cursuri.get("BD"));
    }

}

/**
 * Exercițiul 1 — Colecții: HashMap și TreeMap
 *
 * Creează în acest main:
 *
 * PARTEA A — HashMap (frecvența cuvintelor)
 * 1. Declară un array de String-uri:
 *    String[] words = {"java", "python", "java", "c++", "python", "java", "rust", "c++", "go"};
 * 2. Creează un HashMap<String, Integer> care contorizează de câte ori apare fiecare cuvânt.
 *    - Parcurge array-ul și folosește put() + getOrDefault() pentru a incrementa contorul.
 * 3. Afișează map-ul.
 * 4. Verifică dacă există cheia "rust" cu containsKey().
 * 5. Afișează DOAR cheile (keySet()), apoi DOAR valorile (values()).
 * 6. Parcurge map-ul cu entrySet() și afișează "cheia -> valoarea" pentru fiecare intrare.
 *
 * PARTEA B — TreeMap (sortare automată)
 * 7. Creează un TreeMap<String, Integer> din același HashMap (constructor cu argument).
 * 8. Afișează TreeMap-ul — observă ordinea alfabetică a cheilor.
 * 9. Folosește firstKey() și lastKey() pentru a afișa prima și ultima cheie.
 *
 * PARTEA C — Map cu obiecte
 * 10. Creează un HashMap<String, List<String>> care asociază materii cu liste de studenți.
 *     Exemplu: "PAOJ" -> ["Ana", "Mihai", "Ion"], "BD" -> ["Ana", "Elena"]
 * 11. Afișează toți studenții de la materia "PAOJ".
 * 12. Adaugă un student nou la "BD" și afișează lista actualizată.
 *
 * Output așteptat (orientativ — ordinea HashMap poate varia):
 *
 * === PARTEA A: HashMap — frecvența cuvintelor ===
 * Frecvență: {python=2, c++=2, java=3, rust=1, go=1}
 * Conține 'rust'? true
 * Chei: [python, c++, java, rust, go]
 * Valori: [2, 2, 3, 1, 1]
 * python -> 2
 * c++ -> 2
 * java -> 3
 * rust -> 1
 * go -> 1
 *
 * === PARTEA B: TreeMap — sortare automată ===
 * Sortat: {c++=2, go=1, java=3, python=2, rust=1}
 * Prima cheie: c++
 * Ultima cheie: rust
 *
 * === PARTEA C: Map cu obiecte ===
 * Studenți la PAOJ: [Ana, Mihai, Ion]
 * Studenți la BD (actualizat): [Ana, Elena, George]
 */


