public class Spectator implements Runnable {
    private Hall hall;
    private String name;

    public Spectator(Hall hall) {
        this.hall = hall;
        name = String.valueOf(Thread.currentThread().getName());
    }

    public void enter() {
        System.out.printf("Spectator %s entrou!!%n", name);
    }

    public void spectate() throws InterruptedException {
        System.out.printf("Spectator %s assistiu!!%n", name);
        Thread.sleep(5_000);
    }

    public void leave() {
        System.out.printf("Spectator %s saiu!!%n", name);
    }

    @Override
    public void run() {
        try {
            // espectador espera não haver juiz
            hall.noJudge.acquire();
            // entra e assite
            enter();
            spectate();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            hall.noJudge.release();
        }
        leave();

    }
}
