public class BlockchainSyncThread implements Runnable {
    private static Blockchain blockchain;
    private static boolean synced;

    public BlockchainSyncThread(Blockchain blockchain) {
        BlockchainSyncThread.blockchain = blockchain;
        // TODO: Dynamically check if blockchain is synced or not
        BlockchainSyncThread.synced = true;
    }

    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Stopping blockchain sync thread...");
                break;
            } else {
                if (BlockchainSyncThread.synced)
                    System.out.print("\n\n* SYNCHRONIZED OK *\n\n");
                else
                    System.out.print("\n\nBlockchain out of date, synchronizing\n\n");
                // TODO: Do actual code instead of sleep
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Stopping blockchain sync thread...");
                    return;
                }
            }
        }
    }
}
