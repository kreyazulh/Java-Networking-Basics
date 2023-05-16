public class MainThread {
    public static void main(String[] args) throws InterruptedException {
        Thread t = Thread.currentThread();
        System.out.println("Current thread: "+ t);

        t.setName("Hmph Thread");
        System.out.println("New thread name: "+ t);

        for(int i =0; i<10; i++){
            System.out.println(i);
            Thread.sleep(500);
        }

    }
}
