import java.util.concurrent.Semaphore;

class Hall{
    Semaphore noJudge = new Semaphore(1);
    int entered = 0;
    int checked = 0;
    int judge = 0;
    Semaphore mutex = new Semaphore(1);
    Semaphore confirmed = new Semaphore(0);
    Semaphore allSignedIn = new Semaphore(0);

}
