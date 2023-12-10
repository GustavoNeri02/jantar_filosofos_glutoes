import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Filosofo implements Runnable {
    private final int id;
    private final Lock garfoEsquerdo;
    private final Lock garfoDireito;

    public Filosofo(int id, Lock garfoEsquerdo, Lock garfoDireito) {
        this.id = id;
        this.garfoEsquerdo = garfoEsquerdo;
        this.garfoDireito = garfoDireito;
    }

    private void pensar() throws InterruptedException {
        System.out.println("Filósofo " + (id + 1) + " está pensando.");
        Thread.sleep(1500); // Simula o tempo do pensamento
    }

    private void comer() throws InterruptedException {
        System.out.println("Filósofo " + (id + 1) + " está comendo.");
        Thread.sleep(1500); // Simula o tempo da refeição
    }

    @Override
    public void run() {
        try {
            while (true) {
                pensar();

                garfoEsquerdo.lock(); 
                System.out.println("Filósofo " + (id + 1) + " pegou o garfo à esquerdo. ( " + id + ")");

                garfoDireito.lock(); 
                System.out.println("Filósofo " + (id + 1) + " pegou o garfo à direito. ( " + (id + 1) % 5 + ")");
                
                comer();

                System.out.println("Filósofo " + (id + 1) + " liberou o garfo à esquerdo. ( " + id + ")");
                System.out.println("Filósofo " + (id + 1) + " liberou o garfo à direito. ( " + (id + 1) % 5 + ")");
                Thread.sleep(100);
                garfoEsquerdo.unlock();
                garfoDireito.unlock();

            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

public class App {

    public static void main(String[] args) {

        Lock[] garfos = new ReentrantLock[5];

        for (int i = 0; i < 5; i++) {
            garfos[i] = new ReentrantLock();
        }

        Thread[] filosofos = new Thread[5];

        for (int j = 0; j < 5; j++) {
            filosofos[j] = new Thread(new Filosofo(j, garfos[j], garfos[(j + 1) % 5]));
            filosofos[j].start();
            
            prevenirDeadLock(j);
        }
    }

    private static void prevenirDeadLock(int j) {
        if (j == 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
