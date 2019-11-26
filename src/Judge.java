public class Judge implements Runnable {

    private String name;
    private Hall hall;

    public Judge(Hall hall) {
        this.hall = hall;
        name = String.valueOf(Thread.currentThread().getName());
    }

    public void enter() {
        hall.judge = 1;
        System.out.printf("Judge %s entrou!!%n", name);
    }

    public void confirm() throws InterruptedException {
        Thread.sleep(500);
        System.out.printf("Judge %s confirmou!!%n", name);
    }

    public void leave() throws InterruptedException {
        System.out.printf("Judge %s saiu!!%n", name);
        // Aguarda 1 segundos para entrar dinovo
        Thread.sleep(1_000);
    }


    @Override
    public void run() {

        try {
            while (true) {
                // O juiz usa noJudge para impedir a entrada de imigrantes e espectadores,
                // e mutex para que ele possa acessar as entradas e verificações.
                hall.noJudge.acquire();
                enter();
                hall.mutex.acquire();


                // Se o juiz chegar em um instante em que todos os que entraram também fizeram check-in,
                // ela poderá prosseguir imediatamente. Caso contrário, ela tem que desistir do mutex e esperar.
                if (hall.entered > hall.checked) {
                    hall.mutex.release();

                    // Quando o último imigrante faz check-in e sinaliza allSignedIn,
                    // entende-se que o juiz receberá o mutex de volta.
                    hall.allSignedIn.acquire();
//                hall.mutex.acquire();
                }
                confirm();

                // Depois de invocar a confirmação, o juiz sinaliza confirmado uma vez para cada imigrante
                // que fez check-in e, em seguida, redefine os contadores
                hall.confirmed.release(hall.checked);
                hall.checked = 0;
                hall.entered = 0;

                leave();
                // Então o juiz sai e libera mutex e noJudge.
                hall.judge = 0;

                hall.mutex.release();
                // Depois que os sinais do juiz são confirmados, os imigrantes invocam swear e getCertificate
                // simultaneamente e esperam que a noJudge seja aberta antes de sair.
                hall.noJudge.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
