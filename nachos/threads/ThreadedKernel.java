package nachos.threads;

import nachos.machine.*;

/**
 * A multi-threaded OS kernel.
 */
public class ThreadedKernel extends Kernel {
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
				
//				boolean status = Machine.interrupt().disable();
//				System.out.println("Priority currentThread : " + scheduler.getPriority(KThread.currentThread()));
//				Machine.interrupt().restore(status);
				
				KThread.currentThread().yield();
			}
		}

		private int which;
	}

	/* 기존의 selfTest() 부분은 모두 주석으로 처리하고 PingTest Thread 부분 새롭게 추가 */
	public void selfTest() {
		KThread thread2 = new KThread(new PingTest(2)).setName("forked thread");
		KThread thread3 = new KThread(new PingTest(3)).setName("forked thread");

		boolean status = Machine.interrupt().disable();
		scheduler.setPriority(thread2, 2);
		scheduler.setPriority(thread3, 3);
		thread2.fork();
		thread3.fork();
		Machine.interrupt().restore(status);
		
		new PingTest(1).run();

		// KThread.selfTest();
		// Semaphore.selfTest();
		// SynchList.selfTest();
		// if (Machine.bank() != null) {
		// ElevatorBank.selfTest();
		// }
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
}
