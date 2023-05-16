
class NewThread implements Runnable{
    //global object
    Thread t;
    int threadno;
    //constructor
    NewThread(int threadno){
        t= new Thread(this, "Runnable Thread");
        t.start();
        this.threadno=threadno;
        //after starting, executes run method

    }

    @Override
    public void run() {
        for(int i =0; i<10; i++){
            System.out.println("Child thread "+threadno+": "+i);
            try {
                //for context switching
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Exiting from child thread");
    }
}
public class RunnableThread {
    public static void main(String[] args) {
        System.out.println("Main thread started");
        NewThread thread1= new NewThread(1);
        NewThread thread2= new NewThread(2);
        System.out.println("Thread 1 is alive: "+thread1.t.isAlive());
        System.out.println("Thread 2 is alive: "+thread2.t.isAlive());

        try {
            thread1.t.join();
            thread2.t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Thread 1 is alive: "+thread1.t.isAlive());
        System.out.println("Thread 2 is alive: "+thread2.t.isAlive());
        System.out.println("Main thread exitted");

    }
}

//need for different consoles
