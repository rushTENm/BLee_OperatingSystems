package nachos.threads;

/**
 * A <i>communicator</i> allows threads to synchronously exchange 32-bit
 * messages. Multiple threads can be waiting to <i>speak</i>, and multiple
 * threads can be waiting to <i>listen</i>. But there should never be a time
 * when both a speaker and a listener are waiting, because the two threads can
 * be paired off at this point.
 */
public class Communicator {
    /**
     * Allocate a new communicator.
     */
    public Communicator() {
        mutex = ThreadedKernel.mutex;
        speakCondition = ThreadedKernel.speakCon;
        listenCondition = ThreadedKernel.listenCon;
        id = num++;
    }

    /**
     * Wait for a thread to listen through this communicator, and then transfer
     * <i>word</i> to the listener.
     * <p>
     * <p>
     * Does not return until this thread is paired up with a listening thread.
     * Exactly one listener should receive <i>word</i>.
     *
     * @param word the integer to transfer.
     */
    public void speak(int word) {
        mutex.acquire();
        while (alreadySpeake + nowSpeak > 0) {
            waitSpeak++;
            System.out.println("thread " + id + " sleep ");
            speakCondition.sleep();
            waitSpeak--;
        }
        nowSpeak = 1;
        mutex.release();

        buff = word;
        System.out.println("thread " + id + " speaked  (" + buff + ") ");

        mutex.acquire();
        nowSpeak = 0;
        alreadySpeake = 1;
        if (waitListen > 0) {
            listenCondition.wake();
        }
        mutex.release();
    }

    /**
     * Wait for a thread to speak through this communicator, and then return the
     * <i>word</i> that thread passed to <tt>speak()</tt>.
     *
     * @return the integer transferred.
     */
    public int listen() {
        mutex.acquire();
        while (alreadySpeake == 0 || nowListen == 1) {
            waitListen++;
            System.out.println("thread " + id + " sleep ");
            listenCondition.sleep();
            waitListen--;
        }
        nowListen = 1;
        mutex.release();

        System.out.println("thread " + id + " listened (" + buff + ") ");

        mutex.acquire();
        alreadySpeake = 0;
        nowListen = 0;
        if (waitSpeak > 0) {
            speakCondition.wake();
        }
        mutex.release();

        return buff;
    }

    private Condition2 speakCondition = null;
    private Condition2 listenCondition = null;
    private Lock mutex = null;
    private int id;

    private static int buff;
    private static int alreadySpeake;
    private static int nowSpeak;
    private static int waitSpeak;
    private static int waitListen;
    private static int nowListen;
    private static int num = 0;
}
