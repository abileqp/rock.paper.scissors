import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;

public class RockPaperScissorsGame {

    private final Web3j web3j;
    private final Credentials credentials;
    private final String contractAddress;

    // Initialize the game with a Web3j instance, Ethereum wallet credentials, and contract address
    public RockPaperScissorsGame(Web3j web3j, Credentials credentials, String contractAddress) {
        this.web3j = web3j;
        this.credentials = credentials;
        this.contractAddress = contractAddress;
    }

    // Play the game by sending a move to the smart contract
    public String playGame(int move) {
        try {
            // Load the smart contract
            RockPaperScissorsContract contract = loadContract();

            // Send the player's move to the smart contract
            contract.play(Uint256.valueOf(BigInteger.valueOf(move)))
                    .send();

            // Check the game outcome and return it
            BigInteger outcome = contract.getGameOutcome().send();
            if (outcome.equals(BigInteger.valueOf(0))) {
                return "Tie";
            } else if (outcome.equals(BigInteger.valueOf(1))) {
                return "You win!";
            } else {
                return "You lose.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    // Load the smart contract using Web3j
    private RockPaperScissorsContract loadContract() {
        TransactionManager transactionManager = new ManagedTransaction(credentials, web3j, 10);
        return RockPaperScissorsContract.load(contractAddress, web3j, transactionManager, Contract.GAS_PRICE, Contract.GAS_LIMIT);
    }

    public static void main(String[] args) {
        // Initialize Web3j, Credentials, and contract address
        String infuraUrl = "https://mainnet.infura.io/v3/YOUR_INFURA_PROJECT_ID";
        Web3j web3j = Web3j.build(new HttpService(infuraUrl));
        Credentials credentials = Credentials.create("YOUR_PRIVATE_KEY");
        String contractAddress = "YOUR_CONTRACT_ADDRESS";

        // Initialize the game
        RockPaperScissorsGame game = new RockPaperScissorsGame(web3j, credentials, contractAddress);

        // Example: Play the game with a move (e.g., 1 for Rock)
        int playerMove = 1;
        String result = game.playGame(playerMove);
        System.out.println("Game result: " + result);
    }
}
