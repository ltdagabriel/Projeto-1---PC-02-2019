public class Main {

    public static void main(String[] args) {
        // write your code here
        Hall hall = new Hall();


        Thread juiz = new Thread(new Judge(hall), "juiz 1"); // 1 juiz
        // juiz esta em laço
        juiz.start();


        ThreadGroup group = new ThreadGroup("hall");
        for (int i = 0; i < 10; i++) { // 10 espectadores
            new Thread(group, new Spectator(hall), String.format("espectador %d", i)).start();
        }
        for (int i = 0; i < 5; i++) { // 5 imigrantes
            new Thread(group, new Immigrant(hall), String.format("imigrante %d", i)).start();
        }
        if (group.activeCount() == 0) juiz.interrupt();

    }
}

