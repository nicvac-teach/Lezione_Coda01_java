# Costruiamo una Pila (Stack) in Java

Costruiamo passo dopo passo una struttura dati dinamica: una pila generica basata su lista concatenata. La pila è una delle strutture più eleganti dell'informatica: con una sola regola — **l'ultimo che entra è il primo che esce (LIFO)** — risolve una vasta gamma di problemi in modo molto elegante.

---

## Cos'è una Pila?

Una **pila** (in inglese **stack**) è una struttura dati in cui gli elementi si aggiungono e si rimuovono sempre dalla stessa estremità, chiamata **cima** (top).

Pensala come una pila di piatti: puoi appoggiare un piatto solo in cima, e puoi togliere solo quello in cima. Non puoi estrarre un piatto dal mezzo senza far crollare tutto.

```
         ┌──────────┐
 top ──▶ │  Piatto  │ ← ultimo arrivato, primo a uscire
         ├──────────┤
         │  Piatto  │
         ├──────────┤
         │  Piatto  │
         ├──────────┤
         │  Piatto  │ ← primo arrivato, ultimo a uscire
         └──────────┘
```

### La regola LIFO

**LIFO** = Last In, First Out (l'ultimo che entra è il primo che esce).

Le operazioni fondamentali sono solo due:
- **push(dato)**: metti un elemento in cima
- **pop()**: togli e restituisci l'elemento in cima

A queste si aggiungono operazioni di supporto:
- **peek()**: guarda l'elemento in cima senza toglierlo
- **isEmpty()**: la pila è vuota?
- **size()**: quanti elementi ci sono?

### Perché è utile?

La pila è la struttura giusta ogni volta che devi **tornare indietro** sui tuoi passi:

| Problema | Perché serve la pila |
|----------|---------------------|
| Annulla/Ripristina (Ctrl+Z) | Ogni azione viene impilata; annullare = pop |
| Valutazione di espressioni matematiche | Gli operandi si impilano, gli operatori li combinano |
| Controllo parentesi bilanciate | Si impila ogni parentesi aperta, si scarica con la chiusa |
| Chiamate di funzione (call stack) | Ogni chiamata si impila, il return fa pop |

### Esempio: il controllore di parentesi

Immagina di dover verificare se una stringa come `{[()]}` ha le parentesi bilanciate, cioè ogni parentesi aperta ha la sua corrispondente chiusa, nel giusto ordine.

**Algoritmo con la pila:**

1. Scorri la stringa carattere per carattere
2. Se trovi una parentesi **aperta** (`(`, `[`, `{`): fai **push** sulla pila
3. Se trovi una parentesi **chiusa** (`)`, `]`, `}`):
   - Se la pila è vuota → **sbilanciata** (chiusa senza aperta)
   - Fai **pop**: se la parentesi aperta estratta non corrisponde → **sbilanciata**
4. Alla fine: se la pila è vuota → **bilanciata**, altrimenti → **sbilanciata**

**Esempio passo-passo** con la stringa `{[()]}`:

```
Carattere    Azione          Stato della pila
─────────    ──────          ────────────────
   {         push '{'        { 
   [         push '['        { [
   (         push '('        { [ (
   )         pop → '(' ✓     { [
   ]         pop → '[' ✓     {
   }         pop → '{' ✓     (vuota)
                              
   Fine stringa, pila vuota → ✅ BILANCIATA
```

**Esempio con stringa sbilanciata** `{[(])}`:

```
Carattere    Azione          Stato della pila
─────────    ──────          ────────────────
   {         push '{'        {
   [         push '['        { [
   (         push '('        { [ (
   ]         pop → '(' ✗     '(' non corrisponde a ']'
                              
   → ❌ SBILANCIATA
```

Questo è esattamente il tipo di problema che senza la pila sarebbe complicato da risolvere, e con la pila diventa lineare e pulito.

---

## Implementazione: la Pila basata su Lista Concatenata

Internamente la nostra pila usa una lista concatenata. La **cima della pila è la testa della lista**: così sia push che pop costano **O(1)**, perché operano sempre e solo sul primo nodo.

```
  top
   │
   ▼
┌───────┬───────┐     ┌───────┬───────┐     ┌───────┬───────┐
│   C   │ NEXT  │────▶│   B   │ NEXT  │────▶│   A   │ NULL  │
└───────┴───────┘     └───────┴───────┘     └───────┴───────┘
   cima                                        fondo
```

Push di D:
```
  top
   │
   ▼
┌───────┬───────┐     ┌───────┬───────┐     ┌───────┬───────┐     ┌───────┬───────┐
│   D   │ NEXT  │────▶│   C   │ NEXT  │────▶│   B   │ NEXT  │────▶│   A   │ NULL  │
└───────┴───────┘     └───────┴───────┘     └───────┴───────┘     └───────┴───────┘
   cima                                                               fondo
```

Pop (restituisce D):
```
  top
   │
   ▼
┌───────┬───────┐     ┌───────┬───────┐     ┌───────┬───────┐
│   C   │ NEXT  │────▶│   B   │ NEXT  │────▶│   A   │ NULL  │
└───────┴───────┘     └───────┴───────┘     └───────┴───────┘
   cima                                        fondo
```

---

## Blocco 0 — La classe Nodo

### Obiettivo

Creare la classe `Nodo<T>` che rappresenta un singolo elemento della pila. È la stessa identica classe usata per la lista concatenata.

### Ingredienti

| Elemento | Descrizione |
|----------|-------------|
| `class Nodo<T>` | Classe generica: `T` è il tipo del dato che conterrà |
| `T dato` | Attributo che memorizza il valore |
| `Nodo<T> next` | Riferimento al nodo successivo (o `null` se è l'ultimo) |
| Costruttore | Inizializza il dato e imposta `next` a `null` |

### Come combinarli

1. Dichiara la classe con il parametro generico `<T>`
2. Dichiara l'attributo `dato` di tipo `T`
3. Dichiara l'attributo `next` di tipo `Nodo<T>`
4. Nel costruttore, ricevi il dato come parametro e assegnalo all'attributo. Imposta `next` a `null`

### Esercizio

Crea la classe `Nodo<T>` nel file `Nodo.java`.

<details>
<summary>Soluzione</summary>

```java
public class Nodo<T> {
    T dato;
    Nodo<T> next;

    public Nodo(T dato) {
        this.dato = dato;
        this.next = null;
    }
}
```

</details>

---

## Blocco 1 — La classe Pila (struttura base)

### Obiettivo

Creare la classe `Pila<T>` con l'attributo `top` che punta al nodo in cima.

### Ingredienti

| Elemento | Descrizione |
|----------|-------------|
| `class Pila<T>` | Classe generica che gestisce la pila |
| `Nodo<T> top` | Riferimento al nodo in cima (o `null` se la pila è vuota) |
| Costruttore | Inizializza `top` a `null` (pila vuota) |

### Come combinarli

1. Dichiara la classe con il parametro generico `<T>`
2. Dichiara l'attributo privato `top` di tipo `Nodo<T>`
3. Nel costruttore, imposta `top` a `null`

### Esercizio

Crea la classe `Pila<T>` nel file `Pila.java` con la struttura base.

<details>
<summary>Soluzione</summary>

```java
public class Pila<T> {
    private Nodo<T> top;

    public Pila() {
        this.top = null;
    }
}
```

</details>

---

## Blocco 2 — isEmpty()

### Obiettivo

Sapere se la pila è vuota.

### Ingredienti

| Elemento | Descrizione |
|----------|-------------|
| `top` | Se è `null`, la pila è vuota |
| `return` | Restituisce `true` o `false` |

### Come combinarli

La pila è vuota quando `top` non punta a nessun nodo. Basta verificare se `top == null`.

### Esercizio

Implementa il metodo `boolean isEmpty()` nella classe `Pila<T>`.

<details>
<summary>Soluzione</summary>

```java
public boolean isEmpty() {
    return top == null;
}
```

</details>

---

## Blocco 3 — push(T dato)

### Obiettivo

Inserire un elemento in cima alla pila.

### Ingredienti

| Elemento | Descrizione |
|----------|-------------|
| `new Nodo<>(dato)` | Creare un nuovo nodo con il dato ricevuto |
| `nuovoNodo.next` | Deve puntare all'attuale cima |
| `top` | Deve essere aggiornato per puntare al nuovo nodo |

### Come combinarli

Push equivale a **inserire in testa** nella lista concatenata:

1. Crea un nuovo nodo con il dato
2. Il `next` del nuovo nodo punta all'attuale `top`
3. `top` diventa il nuovo nodo

Nota: funziona anche se la pila è vuota (in quel caso `top` è `null`, e il nuovo nodo avrà `next = null`, che è corretto).

Prima:
```
  top
   │
   ▼
┌───────┬───────┐     ┌───────┬───────┐     ┌───────┬───────┐
│   C   │ NEXT  │────▶│   B   │ NEXT  │────▶│   A   │ NULL  │
└───────┴───────┘     └───────┴───────┘     └───────┴───────┘
   cima                                        fondo
```

Creo `nuovoNodo = new Nodo<>(D)` e collego:

Dopo:
```
  top
   │
   ▼
┌───────┬───────┐     ┌───────┬───────┐     ┌───────┬───────┐     ┌───────┬───────┐
│   D   │ NEXT  │────▶│   C   │ NEXT  │────▶│   B   │ NEXT  │────▶│   A   │ NULL  │
└───────┴───────┘     └───────┴───────┘     └───────┴───────┘     └───────┴───────┘
   cima                                                               fondo
```

### Esercizio

Implementa il metodo `void push(T dato)` nella classe `Pila<T>`.

<details>
<summary>Soluzione</summary>

```java
public void push(T dato) {
    Nodo<T> nuovoNodo = new Nodo<>(dato);
    nuovoNodo.next = top;
    top = nuovoNodo;
}
```

</details>

---

## Blocco 4 — pop()

### Obiettivo

Rimuovere e restituire l'elemento in cima alla pila.

### Ingredienti

| Elemento | Descrizione |
|----------|-------------|
| `isEmpty()` | Verificare che la pila non sia vuota prima di operare |
| `top.dato` | Il valore da restituire |
| `top = top.next` | Spostare la cima al nodo successivo |
| `throw` | Lanciare un'eccezione se la pila è vuota |

### Come combinarli

1. Controlla se la pila è vuota: se sì, lancia un'eccezione (non puoi fare pop su una pila vuota)
2. Salva il dato del nodo in cima
3. Sposta `top` al nodo successivo (il vecchio nodo in cima verrà rimosso dal garbage collector)
4. Restituisci il dato salvato

Prima:
```
  top
   │
   ▼
┌───────┬───────┐     ┌───────┬───────┐     ┌───────┬───────┐     ┌───────┬───────┐
│   D   │ NEXT  │────▶│   C   │ NEXT  │────▶│   B   │ NEXT  │────▶│   A   │ NULL  │
└───────┴───────┘     └───────┴───────┘     └───────┴───────┘     └───────┴───────┘
   cima                                                               fondo
```

Salvo `dato = D`, poi `top = top.next`:

Dopo (restituisce D):
```
  top
   │
   ▼
┌───────┬───────┐     ┌───────┬───────┐     ┌───────┬───────┐
│   C   │ NEXT  │────▶│   B   │ NEXT  │────▶│   A   │ NULL  │
└───────┴───────┘     └───────┴───────┘     └───────┴───────┘
   cima                                        fondo
```

### Esercizio

Implementa il metodo `T pop()` nella classe `Pila<T>`. Se la pila è vuota, lancia una `RuntimeException` con messaggio `"Pila vuota"`.

<details>
<summary>Soluzione</summary>

```java
public T pop() {
    if (isEmpty()) {
        throw new RuntimeException("Pila vuota");
    }

    T dato = top.dato;
    top = top.next;
    return dato;
}
```

</details>

---

## Blocco 5 — peek()

### Obiettivo

Guardare l'elemento in cima alla pila **senza rimuoverlo**.

### Ingredienti

| Elemento | Descrizione |
|----------|-------------|
| `isEmpty()` | Verificare che la pila non sia vuota |
| `top.dato` | Il valore da restituire |
| `throw` | Lanciare un'eccezione se la pila è vuota |

### Come combinarli

È come `pop()`, ma **senza spostare** `top`. Restituisci il dato del nodo in cima e basta.

1. Controlla se la pila è vuota: se sì, lancia un'eccezione
2. Restituisci `top.dato`

```
  top
   │
   ▼
┌───────┬───────┐     ┌───────┬───────┐     ┌───────┬───────┐
│   C   │ NEXT  │────▶│   B   │ NEXT  │────▶│   A   │ NULL  │
└───────┴───────┘     └───────┴───────┘     └───────┴───────┘
   cima                                        fondo
   ▲
   │
 peek() restituisce C, la pila resta invariata
```

### Esercizio

Implementa il metodo `T peek()` nella classe `Pila<T>`. Se la pila è vuota, lancia una `RuntimeException` con messaggio `"Pila vuota"`.

<details>
<summary>Soluzione</summary>

```java
public T peek() {
    if (isEmpty()) {
        throw new RuntimeException("Pila vuota");
    }

    return top.dato;
}
```

</details>

---

## Blocco 6 — size()

### Obiettivo

Contare quanti elementi ci sono nella pila.

### Ingredienti

| Elemento | Descrizione |
|----------|-------------|
| `int contatore` | Una variabile che parte da 0 |
| `Nodo<T> corrente` | Un riferimento che parte da `top` |
| `while (corrente != null)` | Ciclo che scorre tutti i nodi |
| `corrente = corrente.next` | Avanza al nodo successivo |

### Come combinarli

Devi scorrere tutta la catena di nodi dalla cima al fondo, contando ogni nodo incontrato:

1. Inizializza un contatore a 0
2. Parti da `top` con un riferimento `corrente`
3. Finché `corrente` non è `null`, incrementa il contatore e avanza al nodo successivo
4. Restituisci il contatore

### Esercizio

Implementa il metodo `int size()` nella classe `Pila<T>`.

<details>
<summary>Soluzione</summary>

```java
public int size() {
    int contatore = 0;
    Nodo<T> corrente = top;

    while (corrente != null) {
        contatore++;
        corrente = corrente.next;
    }

    return contatore;
}
```

</details>

---

## Blocco 7 — toString()

### Obiettivo

Rappresentare la pila come stringa leggibile, mostrando gli elementi dalla cima al fondo.

### Ingredienti

| Elemento | Descrizione |
|----------|-------------|
| `StringBuilder` | Per costruire la stringa in modo efficiente |
| `Nodo<T> corrente` | Riferimento che parte da `top` |
| `while (corrente != null)` | Ciclo che scorre tutti i nodi |
| `@Override` | Sovrascrive il metodo `toString()` di `Object` |

### Come combinarli

1. Crea un `StringBuilder`
2. Aggiungi un'intestazione `"[CIMA] "` per rendere chiaro dove inizia la pila
3. Scorri tutti i nodi da `top` in avanti, aggiungendo per ciascuno il dato seguito da `" → "`
4. Alla fine della catena aggiungi `"[FONDO]"`
5. Restituisci la stringa

Formato atteso: `[CIMA] D → C → B → A → [FONDO]`

### Esercizio

Implementa il metodo `toString()` nella classe `Pila<T>`.

<details>
<summary>Soluzione</summary>

```java
@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[CIMA] ");

    Nodo<T> corrente = top;
    while (corrente != null) {
        sb.append(corrente.dato);
        sb.append(" → ");
        corrente = corrente.next;
    }

    sb.append("[FONDO]");
    return sb.toString();
}
```

</details>

---

## Codice Completo

Prima dell'esercizio finale, ecco il codice completo delle due classi.

### Nodo.java

```java
public class Nodo<T> {
    T dato;
    Nodo<T> next;

    public Nodo(T dato) {
        this.dato = dato;
        this.next = null;
    }
}
```

### Pila.java

```java
public class Pila<T> {
    private Nodo<T> top;

    public Pila() {
        this.top = null;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public void push(T dato) {
        Nodo<T> nuovoNodo = new Nodo<>(dato);
        nuovoNodo.next = top;
        top = nuovoNodo;
    }

    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Pila vuota");
        }

        T dato = top.dato;
        top = top.next;
        return dato;
    }

    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Pila vuota");
        }

        return top.dato;
    }

    public int size() {
        int contatore = 0;
        Nodo<T> corrente = top;

        while (corrente != null) {
            contatore++;
            corrente = corrente.next;
        }

        return contatore;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[CIMA] ");

        Nodo<T> corrente = top;
        while (corrente != null) {
            sb.append(corrente.dato);
            sb.append(" → ");
            corrente = corrente.next;
        }

        sb.append("[FONDO]");
        return sb.toString();
    }
}
```

---

## Esercizio Finale — Usa la tua Pila!

### Il controllore di parentesi

Ora metti alla prova la pila che hai costruito. Implementa un programma che verifica se una stringa ha le parentesi bilanciate. Deve gestire tre tipi di parentesi: tonde `()`, quadre `[]` e graffe `{}`.

### Specifiche

1. Crea un metodo `static boolean parentesiBilanciate(String espressione)` in una classe `TestPila`
2. Usa una `Pila<Character>` per gestire il controllo
3. Scorri la stringa carattere per carattere:
   - Se è una parentesi aperta (`(`, `[`, `{`): fai push
   - Se è una parentesi chiusa (`)`, `]`, `}`): fai pop e verifica che la parentesi aperta corrisponda
   - Se la pila è vuota quando serve fare pop → sbilanciata
4. Alla fine, la pila deve essere vuota
5. Nel `main`, testa con queste stringhe:
   - `{[()]}` → bilanciata
   - `{[(])}` → sbilanciata
   - `((()))` → bilanciata
   - `(]` → sbilanciata
   - `{[}` → sbilanciata
   - stringa vuota `""` → bilanciata

<details>
<summary>Soluzione</summary>

```java
public class TestPila {

    public static boolean parentesiBilanciate(String espressione) {
        Pila<Character> pila = new Pila<>();

        for (int i = 0; i < espressione.length(); i++) {
            char c = espressione.charAt(i);

            if (c == '(' || c == '[' || c == '{') {
                pila.push(c);
            } else if (c == ')' || c == ']' || c == '}') {
                if (pila.isEmpty()) {
                    return false;
                }

                char aperta = pila.pop();

                if (!corrispondono(aperta, c)) {
                    return false;
                }
            }
        }

        return pila.isEmpty();
    }

    private static boolean corrispondono(char aperta, char chiusa) {
        return (aperta == '(' && chiusa == ')')
            || (aperta == '[' && chiusa == ']')
            || (aperta == '{' && chiusa == '}');
    }

    public static void main(String[] args) {
        String[] test = {"{[()]}", "{[(])}", "((()))", "(]", "{[}", ""};
        boolean[] atteso = {true, false, true, false, false, true};

        for (int i = 0; i < test.length; i++) {
            boolean risultato = parentesiBilanciate(test[i]);
            String esito = (risultato == atteso[i]) ? "✓" : "✗";
            System.out.println(esito + " \"" + test[i] + "\" → " + risultato);
        }
    }
}
```

</details>