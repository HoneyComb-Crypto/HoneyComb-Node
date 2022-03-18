import com.martiansoftware.jsap.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Entry point for HoneyComb Node
 */
public class Main {
    public static final String version = "0.0.0";
    public static final int buildId = 0;
    private static final BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
    private static final JSAP argParser = new JSAP();
    private static final Blockchain blockchain = new Blockchain();
    private static Thread blockchainSyncThread;

    public static void main(String @NotNull [] args) throws IOException {

        // Set java parameter structure
        FlaggedOption logFile = new FlaggedOption("log-file")
                .setStringParser(JSAP.STRING_PARSER)
                .setRequired(false)
                .setShortFlag(JSAP.NO_SHORTFLAG)
                .setLongFlag("log-file");

        FlaggedOption logLevel = new FlaggedOption("log-level")
                .setStringParser(JSAP.INTEGER_PARSER)
                .setRequired(false)
                .setShortFlag(JSAP.NO_SHORTFLAG)
                .setLongFlag("log-level")
                .setDefault("0");

        FlaggedOption nodeHost = new FlaggedOption("node-host")
                .setStringParser(JSAP.STRING_PARSER)
                .setRequired(true)
                .setShortFlag(JSAP.NO_SHORTFLAG)
                .setLongFlag("node-host")
                .setDefault("localhost");

        FlaggedOption daemonPort = new FlaggedOption("daemon-port")
                .setStringParser(JSAP.STRING_PARSER)
                .setRequired(true)
                .setShortFlag(JSAP.NO_SHORTFLAG)
                .setLongFlag("daemon-port")
                .setDefault("17777");

        Switch publicNode = new Switch("public-node")
                .setLongFlag("public-node")
                .setDefault("false");

        try {
            argParser.registerParameter(logFile);
            argParser.registerParameter(logLevel);
            argParser.registerParameter(nodeHost);
            argParser.registerParameter(daemonPort);
            argParser.registerParameter(publicNode);
        } catch (JSAPException e) {
            System.out.println("FATAL ERROR: Failed to register command line arguments.");
            System.exit(1);
        }
        JSAPResult config = argParser.parse(args);

        Main.printWelcome();

        Main.blockchainSyncThread = new Thread(new BlockchainSyncThread(Main.blockchain));
        Main.blockchainSyncThread.start();

        inputLoop:
        while (true) {
            System.out.print("> ");
            switch (inputReader.readLine()) {
                case "help":
                    Main.showHelp();
                    break;
                case "status":
                    Main.showStatus();
                    break;
                case "version":
                    Main.showVersion();
                    break;
                case "peers":
                    Main.showPeers();
                    break;
                case "exit":
                    break inputLoop;
                default:
                    System.out.print("Unknown command. Please refer to the help menu below:\n\n");
                    Main.showHelp();
                    break;
            }
        }
        inputReader.close();
        Main.blockchainSyncThread.interrupt();
        System.out.println("Goodbye!");
    }

    private static void showHelp() {
        System.out.println("""
                Startup Options:
                ----------------
                    --log-file <arg> | Set location of log file (default: none)
                    --log-level <arg> | Set log level (0-4, with 4 being the most verbose, default 0)
                        * WARNING: log level `4` *may* show sensitive information in the console and in the logs
                    --node-host <arg> | Instance of HoneyComb Node at the specified host (default: "localhost")
                    --daemon-port <arg> | Instance of HoneyComb Node at the specified port (default: 17777)
                    --public-node | Advertise to other nodes that this node can be used to connect to a wallet (default: false)
                Commands:
                ---------
                    status | Show node status
                    version | Show version information
                    help | Show this help menu
                    peers | Print connected peers
                    exit | Shutdown this node
                """);
    }

    private static void showStatus() {
        System.out.println("Status...");
    }

    private static void showVersion() {
        System.out.printf("HoneyComb Node v%s_b%s\n", Main.version, Main.buildId);
    }

    private static void showPeers() {
        System.out.println("""
                Connected peers:
                ----------------
                """);
    }

    private static void printWelcome() {
        System.out.println("""
                HoneyComb Node v0.0.0
                Welcome!
                Type `help` to see all available options
                """);
    }
}
