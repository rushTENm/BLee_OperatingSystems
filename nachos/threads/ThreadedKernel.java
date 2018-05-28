package nachos.threads;

import nachos.machine.*;

/**
 * A multi-threaded OS kernel.
 */
public class ThreadedKernel extends Kernel {
<<<<<<< HEAD
	/**
	 * Allocate a new multi-threaded kernel.
	 */
	public ThreadedKernel() {
		super();
	}

	/**
	 * Initialize this kernel. Creates a scheduler, the first thread, and an
	 * alarm, and enables interrupts. Creates a file system if necessary.
	 */
	public void initialize(String[] args) {
		// set scheduler
		String schedulerName = Config.getString("ThreadedKernel.scheduler");
		scheduler = (Scheduler) Lib.constructObject(schedulerName);

		// set fileSystem
		String fileSystemName = Config.getString("ThreadedKernel.fileSystem");
		if (fileSystemName != null)
			fileSystem = (FileSystem) Lib.constructObject(fileSystemName);
		else if (Machine.stubFileSystem() != null)
			fileSystem = Machine.stubFileSystem();
		else
			fileSystem = null;

		// start threading
		new KThread(null);

		alarm = new Alarm();

		Machine.interrupt().enable();
	}

	/**
	 * Test this kernel. Test the <tt>KThread</tt>, <tt>Semaphore</tt>,
	 * <tt>SynchList</tt>, and <tt>ElevatorBank</tt> classes. Note that the
	 * autograder never calls this method, so it is safe to put additional tests
	 * here.
	 */
	/* PingTest 클래스는 새롭게 추가한 내용임 */

	private static class PingTest implements Runnable {
		PingTest(int which) {
			this.which = which;
		}

		public void run() {
			for (int i = 0; i < 3; i++) {
				System.out.println("*** thread " + which + " looped " + i + " times");
				KThread.currentThread().yield();
			}
		}

		private int which;
	}

	private static class Communicator_test implements Runnable {
		Communicator_test(Condition2 condi, Condition2 condi2, Lock lock, int id) {
			human = new Communicator();
			which = id;
		}

		public void run() {
			for (int i = 0; i < 3; i++) {
				// 짝수 speak, 홀수 listen
				if (which % 2 == 0)
					human.speak(which);
				else
					human.listen();
			}
		}

		private int which;
		private Communicator human = null;
	}

	/* 기존의 selfTest() 부분은 모두 주석으로 처리하고 PingTest Thread 부분 새롭게 추가 */
	public void selfTest() {

		mutex = new Lock();
		speakCon = new Condition2(mutex);
		listenCon = new Condition2(mutex);

		KThread communicator1, communicator2, communicator3, communicator4;
		communicator1 = new KThread(new Communicator_test(speakCon, listenCon, mutex, 0));
		communicator2 = new KThread(new Communicator_test(speakCon, listenCon, mutex, 1));
		communicator3 = new KThread(new Communicator_test(speakCon, listenCon, mutex, 2));
		communicator4 = new KThread(new Communicator_test(speakCon, listenCon, mutex, 3));
		communicator1.fork();
		communicator2.fork();
		communicator3.fork();
		communicator4.fork();
		communicator1.join();
		communicator2.join();
		communicator3.join();
		communicator4.join();
	}

	public static Condition2 speakCon = null;
	public static Condition2 listenCon = null;
	public static Lock mutex = null;
	public static int communicator_id = 0;

	/**
	 * A threaded kernel does not run user programs, so this method does
	 * nothing.
	 */
	public void run() {
	}

	/**
	 * Terminate this kernel. Never returns.
	 */
	public void terminate() {
		Machine.halt();
	}

	/** Globally accessible reference to the scheduler. */
	public static Scheduler scheduler = null;
	/** Globally accessible reference to the alarm. */
	public static Alarm alarm = null;
	/** Globally accessible reference to the file system. */
	public static FileSystem fileSystem = null;

	// dummy variables to make javac smarter
	private static RoundRobinScheduler dummy1 = null;
	private static PriorityScheduler dummy2 = null;
	private static LotteryScheduler dummy3 = null;
	private static Condition2 dummy4 = null;
	private static Communicator dummy5 = null;
	private static Rider dummy6 = null;
	private static ElevatorController dummy7 = null;
=======
    /**
     * Allocate a new multi-threaded kernel.
     */
    public ThreadedKernel() {
        super();
    }

    /**
     * Initialize this kernel. Creates a scheduler, the first thread, and an
     * alarm, and enables interrupts. Creates a file system if necessary.
     */
    public void initialize(String[] args) {
        // set scheduler
        String schedulerName = Config.getString("ThreadedKernel.scheduler");
        scheduler = (Scheduler) Lib.constructObject(schedulerName);

        // set fileSystem
        String fileSystemName = Config.getString("ThreadedKernel.fileSystem");
        if (fileSystemName != null)
            fileSystem = (FileSystem) Lib.constructObject(fileSystemName);
        else if (Machine.stubFileSystem() != null)
            fileSystem = Machine.stubFileSystem();
        else
            fileSystem = null;

        // start threading
        new KThread(null);

        alarm = new Alarm();

        Machine.interrupt().enable();
    }

    /**
     * Test this kernel. Test the <tt>KThread</tt>, <tt>Semaphore</tt>,
     * <tt>SynchList</tt>, and <tt>ElevatorBank</tt> classes. Note that the
     * autograder never calls this method, so it is safe to put additional tests
     * here.
     */
    /* PingTest 클래스는 새롭게 추가한 내용임 */
    private static class PingTest implements Runnable {
        PingTest(int which) {
            this.which = which;
        }

        public void run() {
            for (int i = 0; i < 3; i++) {
                System.out.println("*** thread " + which + " looped " + i + " times");
                KThread.currentThread().yield();
            }
        }

        private int which;
    }

    public void selfTest() {
        KThread thread2 = new KThread(new PingTest(2)).setName("forked 1");
        KThread thread3 = new KThread(new PingTest(3)).setName("forked 2");
        KThread thread4 = new KThread(new PingTest(4)).setName("forked 3");

        boolean status = Machine.interrupt().disable();
        scheduler.setPriority(thread2, 2);
        scheduler.setPriority(thread3, 3);
        scheduler.setPriority(thread4, 2);
        thread4.fork();
        thread3.fork();
        thread2.fork();
        Machine.interrupt().restore(status);

        new PingTest(1).run();
    }


    /**
     * A threaded kernel does not run user programs, so this method does
     * nothing.
     */
    public void run() {
    }

    /**
     * Terminate this kernel. Never returns.
     */
    public void terminate() {
        Machine.halt();
    }

    /**
     * Globally accessible reference to the scheduler.
     */
    public static Scheduler scheduler = null;
    /**
     * Globally accessible reference to the alarm.
     */
    public static Alarm alarm = null;
    /**
     * Globally accessible reference to the file system.
     */
    public static FileSystem fileSystem = null;

    // dummy variables to make javac smarter
    private static RoundRobinScheduler dummy1 = null;
    private static PriorityScheduler dummy2 = null;
    private static LotteryScheduler dummy3 = null;
    private static Condition2 dummy4 = null;
    private static Communicator dummy5 = null;
    private static Rider dummy6 = null;
    private static ElevatorController dummy7 = null;
>>>>>>> 1ed721e9b2bbeb61a2a1f10b0ee7e67283f15571
}
