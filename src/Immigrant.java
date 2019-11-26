class Immigrant implements Runnable {


    private Hall hall;
    private String name;


    public Immigrant(Hall hall) {

        this.hall = hall;
        name = String.valueOf(Thread.currentThread().getName());
    }

    public void enter() {
        hall.entered++;
        System.out.printf("Immigrant %s entrou!!%n", name);
    }

    public void checkIn() throws InterruptedException {
        hall.checked++;
        System.out.printf("Immigrant %s fez checkin!!%n", name);
        Thread.sleep(500);


    }

    public void sitDown() throws InterruptedException {
        System.out.printf("Immigrant %s sentou!!%n", name);
        Thread.sleep(100);

    }

    public void swear() throws InterruptedException {
        System.out.printf("Immigrant %s jurou!!%n", name);
        Thread.sleep(400);

    }

    public void getCertificate() throws InterruptedException {
        System.out.printf("Immigrant %s pegou certificado!!%n", name);
        Thread.sleep(1000);

    }

    public void leave() {
        System.out.printf("Immigrant %s saiu!!%n", name);
    }

    @Override
    public void run() {
        try {
            // Os imigrantes aguardam o juiz sair para entrar
            hall.noJudge.acquire();
            enter();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            hall.noJudge.release();
        }

        try {
            // Depois de entrar, os imigrantes precisam obter mutex para fazer o check-in e atualizar
            hall.mutex.acquire();

            checkIn();


            // Se houver um juiz esperando, o último imigrante a fazer o check-in sinaliza allSignedIn
            // e passa o mutex para o juiz
            if (hall.judge == 1 && hall.entered == hall.checked) {
                hall.allSignedIn.release();
            } else {
                hall.mutex.release();
            }
            sitDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            hall.confirmed.acquire();
            swear();
            getCertificate();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            hall.noJudge.acquire();
            leave();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            hall.noJudge.release();
        }

    }
}
