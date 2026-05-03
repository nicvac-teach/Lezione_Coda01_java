# Costruiamo una Coda (Queue) in Java

Costruiamo passo dopo passo una struttura dati dinamica: una coda generica basata su lista concatenata. La coda è una delle strutture più intuitive dell'informatica: con una sola regola — **il primo che entra è il primo che esce (FIFO)** — modella in modo elegante tutte le situazioni in cui bisogna rispettare un ordine di arrivo.

---

## Cos'è una Coda?

Una **coda** (in inglese **queue**) è una struttura dati in cui gli elementi si aggiungono da un'estremità (la **fine**) e si rimuovono dall'estremità opposta (la **testa**).

Pensala come la fila alla cassa del supermercato: chi arriva si mette in fondo, chi è in testa viene servito per primo. Nessuno può "passare avanti" e nessuno può uscire dal mezzo.

```
       esce                                                     entra
        ▲                                                         │
        │                                                         ▼
     ┌──────┐    ┌──────┐    ┌──────┐    ┌──────┐    ┌──────┐
     │ Anna │    │ Bea  │    │ Carlo│    │ Dario│    │ Elena│
     └──────┘    └──────┘    └──────┘    └──────┘    └──────┘
       testa                                            fine
   (prima ad uscire)                              (ultima ad entrare)
```

### La regola FIFO

**FIFO** = First In, First Out (il primo che entra è il primo che esce).

Le operazioni fondamentali sono solo due:
- **enqueue(dato)**: aggiungi un elemento alla fine della coda
- **dequeue()**: togli e restituisci l'elemento in testa

A queste si aggiungono operazioni di supporto:
- **peek()**: guarda l'elemento in testa senza toglierlo
- **isEmpty()**: la coda è vuota?
- **size()**: quanti elementi ci sono?

> **Differenza chiave con la Pila:** nella pila si entra ed esce dalla stessa estremità (LIFO), nella coda si entra da una parte ed esce dall'altra (FIFO).

### Perché è utile?

La coda è la struttura giusta ogni volta che bisogna **rispettare l'ordine di arrivo**:

| Problema | Perché serve la coda |
|----------|---------------------|
| Stampante condivisa | I documenti vengono stampati nell'ordine in cui sono stati inviati |
| Scheduler dei processi | Il sistema operativo serve i processi in ordine di arrivo |
| Messaggistica (WhatsApp, email) | I messaggi vengono consegnati nell'ordine di invio |
| Visita in ampiezza di un grafo (BFS) | Si esplorano i nodi nell'ordine in cui vengono scoperti |
| Buffer dati (rete, audio, video) | I pacchetti vengono consumati nell'ordine in cui arrivano |

### Esempio: la coda di stampa

Immagina una stampante d'ufficio con tre persone che inviano un documento:

```
Tempo  Azione                          Stato della coda (testa → fine)
─────  ──────                          ───────────────────────────────
 t1    Anna invia "report.pdf"         report.pdf
 t2    Bea invia "fattura.pdf"         report.pdf, fattura.pdf
 t3    Stampante prende il prossimo    fattura.pdf            → stampa report.pdf
 t4    Carlo invia "contratto.pdf"     fattura.pdf, contratto.pdf
 t5    Stampante prende il prossimo    contratto.pdf          → stampa fattura.pdf
 t6    Stampante prende il prossimo    (vuota)                → stampa contratto.pdf
```

Anche se Carlo ha inviato il suo documento **prima** che la stampante finisse il primo lavoro, il suo file viene stampato per ultimo: **rispetto rigoroso dell'ordine di arrivo**. Questo è esattamente ciò che fa una coda.

---

## Implementazione: la Coda basata su Lista Concatenata

Internamente la nostra coda usa una lista concatenata. A differenza della pila, abbiamo bisogno di **due puntatori**:

- **`head`** punta alla **testa** della coda (il primo elemento, quello che uscirà)
- **`tail`** punta alla **fine** della coda (l'ultimo elemento inserito)

Perché due puntatori? Se avessimo solo `head`, per inserire in fondo dovremmo scorrere tutta la lista (operazione O(n)). Con `tail` che punta sempre all'ultimo nodo, possiamo inserire in fondo in tempo costante **O(1)**.

I nodi della lista puntano sempre **dalla testa verso la fine**: questo è importante perché vogliamo che `dequeue` (rimozione dalla testa) sia immediato.

```
      head                                                tail
       │                                                   │
       ▼                                                   ▼
 ┌───────┬───────┐    ┌───────┬───────┐    ┌───────┬───────┐
 │   A   │ NEXT  │───▶│   B   │ NEXT  │───▶│   C   │ NULL  │
 └───────┴───────┘    └───────┴───────┘    └───────┴───────┘
    TESTA                                       FINE
   (esce)                                      (entra)
```

Enqueue di D (si attacca dopo `tail`, e `tail` si sposta sul nuovo nodo):
```
      head                                                                   tail
       │                                                                      │
       ▼                                                                      ▼
 ┌───────┬───────┐    ┌───────┬───────┐    ┌───────┬───────┐    ┌───────┬───────┐
 │   A   │ NEXT  │───▶│   B   │ NEXT  │───▶│   C   │ NEXT  │───▶│   D   │ NULL  │
 └───────┴───────┘    └───────┴───────┘    └───────┴───────┘    └───────┴───────┘
    TESTA                                                            FINE
```

Dequeue (restituisce A, e `head` avanza al nodo successivo):
```
      head                                                tail
       │                                                   │
       ▼                                                   ▼
 ┌───────┬───────┐    ┌───────┬───────┐    ┌───────┬───────┐
 │   B   │ NEXT  │───▶│   C   │ NEXT  │───▶│   D   │ NULL  │
 └───────┴───────┘    └───────┴───────┘    └───────┴───────┘
    TESTA                                       FINE
```

> **Attenzione ai casi limite!** Quando la coda è vuota, **sia `head` che `tail` sono `null`**. Quando inseriamo un nuovo elemento in una coda vuota, dobbiamo far puntare entrambi all'unico elemento ora presente. Quando rimuoviamo l'unico elemento presente nella coda, dobbiamo riportare entrambi a `null`. Vedremo questi dettagli nei blocchi 3 e 4.

---

## Blocco 0 — La classe Nodo

### Obiettivo

Creare la classe `Nodo<T>` che rappresenta un singolo elemento della coda. È la stessa identica classe usata per la lista concatenata e per la pila.

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

## Blocco 1 — La classe Coda (struttura base)

### Obiettivo

Creare la classe `Coda<T>` con i due attributi `head` e `tail` che puntano rispettivamente alla testa e alla fine della coda.

### Ingredienti

| Elemento | Descrizione |
|----------|-------------|
| `class Coda<T>` | Classe generica che gestisce la coda |
| `Nodo<T> head` | Riferimento al nodo in testa, da cui si esce (o `null` se la coda è vuota) |
| `Nodo<T> tail` | Riferimento al nodo in fondo, dove si entra (o `null` se la coda è vuota) |
| Costruttore | Inizializza `head` e `tail` a `null` (coda vuota) |

### Come combinarli

1. Dichiara la classe con il parametro generico `<T>`
2. Dichiara gli attributi privati `head` e `tail`, entrambi di tipo `Nodo<T>`
3. Nel costruttore, imposta sia `head` che `tail` a `null`

### Esercizio

Crea la classe `Coda<T>` nel file `Coda.java` con la struttura base.

<details>
<summary>Soluzione</summary>

```java
public class Coda<T> {
    private Nodo<T> head;
    private Nodo<T> tail;

    public Coda() {
        this.head = null;
        this.tail = null;
    }
}
```

</details>

---

## Blocco 2 — isEmpty()

### Obiettivo

Sapere se la coda è vuota.

### Ingredienti

| Elemento | Descrizione |
|----------|-------------|
| `head` | Se è `null`, la coda è vuota |
| `return` | Restituisce `true` o `false` |

### Come combinarli

La coda è vuota quando non c'è nessun nodo. Basta controllare se `head == null` (quando `head` è `null`, anche `tail` lo è — manterremo questo invariante in tutte le operazioni).

### Esercizio

Implementa il metodo `boolean isEmpty()` nella classe `Coda<T>`.

<details>
<summary>Soluzione</summary>

```java
public boolean isEmpty() {
    return head == null;
}
```

</details>

---

## Blocco 3 — enqueue(T dato)

### Obiettivo

Inserire un elemento alla **fine** della coda.

### Ingredienti

| Elemento | Descrizione |
|----------|-------------|
| `new Nodo<>(dato)` | Creare un nuovo nodo con il dato ricevuto |
| `isEmpty()` | Distinguere il caso "coda vuota" dal caso "coda con elementi" |
| `tail.next` | Collegare il nuovo nodo dopo l'attuale ultimo nodo |
| `tail` | Aggiornarlo per puntare al nuovo nodo (che è il nuovo ultimo) |
| `head` | Se la coda era vuota, anche `head` deve puntare al nuovo nodo |

### Come combinarli

Enqueue equivale a **inserire in fondo** alla lista concatenata. Bisogna distinguere due casi:

**Caso 1 — Coda vuota:** non c'è nessun nodo. Il nuovo nodo diventa contemporaneamente testa e fine.

**Caso 2 — Coda non vuota:** il nuovo nodo va attaccato dopo l'attuale `tail`, e poi `tail` deve essere aggiornato per puntare al nuovo nodo.

Pseudocodice:
1. Crea un nuovo nodo con il dato
2. Se `isEmpty()`: imposta `head = nuovoNodo` e `tail = nuovoNodo`
3. Altrimenti: imposta `tail.next = nuovoNodo`, poi `tail = nuovoNodo`

Prima:
```
      head                                                tail
       │                                                   │
       ▼                                                   ▼
 ┌───────┬───────┐    ┌───────┬───────┐    ┌───────┬───────┐
 │   A   │ NEXT  │───▶│   B   │ NEXT  │───▶│   C   │ NULL  │
 └───────┴───────┘    └───────┴───────┘    └───────┴───────┘
```

Creo `nuovoNodo = new Nodo<>(D)`, poi `tail.next = nuovoNodo` e `tail = nuovoNodo`:

Dopo:
```
      head                                                                   tail
       │                                                                      │
       ▼                                                                      ▼
 ┌───────┬───────┐    ┌───────┬───────┐    ┌───────┬───────┐    ┌───────┬───────┐
 │   A   │ NEXT  │───▶│   B   │ NEXT  │───▶│   C   │ NEXT  │───▶│   D   │ NULL  │
 └───────┴───────┘    └───────┴───────┘    └───────┴───────┘    └───────┴───────┘
```

> **Errore tipico:** dimenticare di aggiornare `head` quando la coda era vuota. Senza quell'aggiornamento, dopo il primo `enqueue` la coda risulterebbe ancora vuota a chiunque guardi `head`!

### Esercizio

Implementa il metodo `void enqueue(T dato)` nella classe `Coda<T>`.

<details>
<summary>Soluzione</summary>

```java
public void enqueue(T dato) {
    Nodo<T> nuovoNodo = new Nodo<>(dato);

    if (isEmpty()) {
        head = nuovoNodo;
        tail = nuovoNodo;
    } else {
        tail.next = nuovoNodo;
        tail = nuovoNodo;
    }
}
```

</details>

---

## Blocco 4 — dequeue()

### Obiettivo

Rimuovere e restituire l'elemento in **testa** alla coda.

### Ingredienti

| Elemento | Descrizione |
|----------|-------------|
| `isEmpty()` | Verificare che la coda non sia vuota prima di operare |
| `head.dato` | Il valore da restituire |
| `head = head.next` | Spostare la testa al nodo successivo |
| `tail = null` | Se dopo lo spostamento la coda è vuota, azzerare anche `tail` |
| `throw` | Lanciare un'eccezione se la coda è vuota |

### Come combinarli

1. Controlla se la coda è vuota: se sì, lancia un'eccezione (non puoi fare dequeue su una coda vuota)
2. Salva il dato del nodo in testa
3. Sposta `head` al nodo successivo (il vecchio nodo in testa verrà rimosso dal garbage collector)
4. **Caso limite**: se ora `head` è `null` (avevamo un solo elemento e l'abbiamo tolto), azzera anche `tail`. Senza questa riga, `tail` continuerebbe a puntare a un nodo "fantasma" che non fa più parte della coda!
5. Restituisci il dato salvato

Prima:
```
      head                                                                   tail
       │                                                                      │
       ▼                                                                      ▼
 ┌───────┬───────┐    ┌───────┬───────┐    ┌───────┬───────┐    ┌───────┬───────┐
 │   A   │ NEXT  │───▶│   B   │ NEXT  │───▶│   C   │ NEXT  │───▶│   D   │ NULL  │
 └───────┴───────┘    └───────┴───────┘    └───────┴───────┘    └───────┴───────┘
```

Salvo `dato = A`, poi `head = head.next`:

Dopo (restituisce A):
```
      head                                                tail
       │                                                   │
       ▼                                                   ▼
 ┌───────┬───────┐    ┌───────┬───────┐    ┌───────┬───────┐
 │   B   │ NEXT  │───▶│   C   │ NEXT  │───▶│   D   │ NULL  │
 └───────┴───────┘    └───────┴───────┘    └───────┴───────┘
```

Caso limite — coda con un solo elemento:
```
   head, tail
       │
       ▼
 ┌───────┬───────┐
 │   X   │ NULL  │
 └───────┴───────┘
```

Dopo `head = head.next` → `head` è `null`, ma `tail` punta ancora al vecchio nodo X! Per questo serve l'`if`:

```
   head = null
   tail = null    ✓ ora la coda è davvero vuota
```

### Esercizio

Implementa il metodo `T dequeue()` nella classe `Coda<T>`. Se la coda è vuota, lancia una `RuntimeException` con messaggio `"Coda vuota"`.

<details>
<summary>Soluzione</summary>

```java
public T dequeue() {
    if (isEmpty()) {
        throw new RuntimeException("Coda vuota");
    }

    T dato = head.dato;
    head = head.next;

    if (head == null) {
        tail = null;
    }

    return dato;
}
```

</details>

---

## Blocco 5 — peek()

### Obiettivo

Guardare l'elemento in testa alla coda **senza rimuoverlo**.

### Ingredienti

| Elemento | Descrizione |
|----------|-------------|
| `isEmpty()` | Verificare che la coda non sia vuota |
| `head.dato` | Il valore da restituire |
| `throw` | Lanciare un'eccezione se la coda è vuota |

### Come combinarli

È come `dequeue()`, ma **senza spostare** `head`. Restituisci il dato del nodo in testa e basta.

1. Controlla se la coda è vuota: se sì, lancia un'eccezione
2. Restituisci `head.dato`

```
      head                                                tail
       │                                                   │
       ▼                                                   ▼
 ┌───────┬───────┐    ┌───────┬───────┐    ┌───────┬───────┐
 │   B   │ NEXT  │───▶│   C   │ NEXT  │───▶│   D   │ NULL  │
 └───────┴───────┘    └───────┴───────┘    └───────┴───────┘
       ▲
       │
   peek() restituisce B, la coda resta invariata
```

### Esercizio

Implementa il metodo `T peek()` nella classe `Coda<T>`. Se la coda è vuota, lancia una `RuntimeException` con messaggio `"Coda vuota"`.

<details>
<summary>Soluzione</summary>

```java
public T peek() {
    if (isEmpty()) {
        throw new RuntimeException("Coda vuota");
    }

    return head.dato;
}
```

</details>

---

## Blocco 6 — size()

### Obiettivo

Contare quanti elementi ci sono nella coda.

### Ingredienti

| Elemento | Descrizione |
|----------|-------------|
| `int contatore` | Una variabile che parte da 0 |
| `Nodo<T> corrente` | Un riferimento che parte da `head` |
| `while (corrente != null)` | Ciclo che scorre tutti i nodi |
| `corrente = corrente.next` | Avanza al nodo successivo |

### Come combinarli

Devi scorrere tutta la catena di nodi dalla testa alla fine, contando ogni nodo incontrato:

1. Inizializza un contatore a 0
2. Parti da `head` con un riferimento `corrente`
3. Finché `corrente` non è `null`, incrementa il contatore e avanza al nodo successivo
4. Restituisci il contatore

### Esercizio

Implementa il metodo `int size()` nella classe `Coda<T>`.

<details>
<summary>Soluzione</summary>

```java
public int size() {
    int contatore = 0;
    Nodo<T> corrente = head;

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

Rappresentare la coda come stringa leggibile, mostrando gli elementi dalla testa alla fine.

### Ingredienti

| Elemento | Descrizione |
|----------|-------------|
| `StringBuilder` | Per costruire la stringa in modo efficiente |
| `Nodo<T> corrente` | Riferimento che parte da `head` |
| `while (corrente != null)` | Ciclo che scorre tutti i nodi |
| `@Override` | Sovrascrive il metodo `toString()` di `Object` |

### Come combinarli

1. Crea uno `StringBuilder`
2. Aggiungi un'intestazione `"[TESTA] "` per rendere chiaro da dove si esce
3. Scorri tutti i nodi da `head` in avanti, aggiungendo per ciascuno il dato seguito da `" → "`
4. Alla fine della catena aggiungi `"[FINE]"`
5. Restituisci la stringa

Formato atteso: `[TESTA] A → B → C → D → [FINE]`

### Esercizio

Implementa il metodo `toString()` nella classe `Coda<T>`.

<details>
<summary>Soluzione</summary>

```java
@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[TESTA] ");

    Nodo<T> corrente = head;
    while (corrente != null) {
        sb.append(corrente.dato);
        sb.append(" → ");
        corrente = corrente.next;
    }

    sb.append("[FINE]");
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

### Coda.java

```java
public class Coda<T> {
    private Nodo<T> head;
    private Nodo<T> tail;

    public Coda() {
        this.head = null;
        this.tail = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void enqueue(T dato) {
        Nodo<T> nuovoNodo = new Nodo<>(dato);

        if (isEmpty()) {
            head = nuovoNodo;
            tail = nuovoNodo;
        } else {
            tail.next = nuovoNodo;
            tail = nuovoNodo;
        }
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Coda vuota");
        }

        T dato = head.dato;
        head = head.next;

        if (head == null) {
            tail = null;
        }

        return dato;
    }

    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Coda vuota");
        }

        return head.dato;
    }

    public int size() {
        int contatore = 0;
        Nodo<T> corrente = head;

        while (corrente != null) {
            contatore++;
            corrente = corrente.next;
        }

        return contatore;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[TESTA] ");

        Nodo<T> corrente = head;
        while (corrente != null) {
            sb.append(corrente.dato);
            sb.append(" → ");
            corrente = corrente.next;
        }

        sb.append("[FINE]");
        return sb.toString();
    }
}
```

---

## Esercizio Finale — Usa la tua Coda!

### Il gioco della Patata Bollente

Ora metti alla prova la coda che hai costruito. Implementa una simulazione del classico gioco della **patata bollente**:

> Un gruppo di persone è disposto in cerchio. Si passano una patata bollente di mano in mano. Dopo `K` passaggi, la persona che ha la patata in mano viene **eliminata** ed esce dal cerchio. Il gioco continua finché resta un solo giocatore, che è il **vincitore**.

### Perché serve una coda?

L'idea è semplice ma elegante: rappresentiamo il cerchio di giocatori con una coda.

- **Passare la patata** = togliere il giocatore dalla testa e rimetterlo in fondo (`dequeue` + `enqueue`)
- **Eliminare un giocatore** = togliere il giocatore dalla testa e **non** rimetterlo in coda (`dequeue` e basta)

Esempio con 5 giocatori `[Anna, Bea, Carlo, Dario, Elena]` e `K = 3`:

```
Stato iniziale:    [TESTA] Anna → Bea → Carlo → Dario → Elena → [FINE]

Passaggio 1: Anna passa la patata a Bea
             [TESTA] Bea → Carlo → Dario → Elena → Anna → [FINE]

Passaggio 2: Bea passa la patata a Carlo
             [TESTA] Carlo → Dario → Elena → Anna → Bea → [FINE]

3° passaggio: Carlo ha la patata → ELIMINATO!
             [TESTA] Dario → Elena → Anna → Bea → [FINE]

...e così via, finché resta un solo giocatore.
```

### Specifiche

1. Crea un metodo `static String patataBollente(String[] giocatori, int k)` in una classe `TestCoda`
2. Crea una `Coda<String>` e inserisci tutti i giocatori con `enqueue`
3. Finché nella coda c'è più di un giocatore:
   - Per **k - 1** volte: fai `dequeue` e subito `enqueue` dello stesso elemento (la patata passa di mano)
   - Alla `k`-esima volta: fai `dequeue` e basta (il giocatore con la patata è eliminato)
   - Stampa chi è stato eliminato per seguire la simulazione
4. Restituisci l'unico giocatore rimasto (il vincitore)
5. Nel `main`, prova con:
   - `["Anna", "Bea", "Carlo", "Dario", "Elena"]` con `k = 3`
   - `["Marco", "Luigi", "Sofia", "Giulia"]` con `k = 2`
   - `["Solo"]` con `k = 5` (un solo giocatore vince a tavolino)

### Suggerimento

Il caso "un solo giocatore" è importante: il `while (coda.size() > 1)` non entra mai nel ciclo, e il programma restituisce subito quel giocatore.

<details>
<summary>Soluzione</summary>

```java
public class TestCoda {

    public static String patataBollente(String[] giocatori, int k) {
        Coda<String> coda = new Coda<>();

        for (int i = 0; i < giocatori.length; i++) {
            coda.enqueue(giocatori[i]);
        }

        System.out.println("Inizio gioco: " + coda);

        while (coda.size() > 1) {
            for (int i = 0; i < k - 1; i++) {
                String giocatore = coda.dequeue();
                coda.enqueue(giocatore);
            }

            String eliminato = coda.dequeue();
            System.out.println("Eliminato: " + eliminato + "  →  " + coda);
        }

        return coda.peek();
    }

    public static void main(String[] args) {
        String[] gruppo1 = {"Anna", "Bea", "Carlo", "Dario", "Elena"};
        String vincitore1 = patataBollente(gruppo1, 3);
        System.out.println("Vincitore: " + vincitore1);
        System.out.println();

        String[] gruppo2 = {"Marco", "Luigi", "Sofia", "Giulia"};
        String vincitore2 = patataBollente(gruppo2, 2);
        System.out.println("Vincitore: " + vincitore2);
        System.out.println();

        String[] gruppo3 = {"Solo"};
        String vincitore3 = patataBollente(gruppo3, 5);
        System.out.println("Vincitore: " + vincitore3);
    }
}
```

</details>

### Esercizi extra

- **Coda inversa**: scrivi un metodo che restituisce una nuova coda con gli elementi in ordine inverso. Suggerimento: ti servirà... una pila!
- **Schedulatore round-robin**: simula un sistema operativo che dà 1 secondo di CPU a ogni processo a turno, finché tutti hanno terminato il lavoro richiesto.
